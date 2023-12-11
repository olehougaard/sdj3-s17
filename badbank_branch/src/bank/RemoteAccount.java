package bank;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class RemoteAccount extends UnicastRemoteObject implements Account {
	private static final long serialVersionUID = 1L;
	private Customer customer;
	private String accountNumber;
	private String currency;
	private Money balance;

	public RemoteAccount(Customer customer, String accountNumber, String currency) throws RemoteException {
		this.customer = customer;
		this.accountNumber = accountNumber;
		this.currency = currency;
		this.balance = new Money(new BigDecimal(0), currency);
	}

	@Override
	public String getAccountNumber() throws RemoteException {
		return accountNumber;
	}

	@Override
	public Customer getCustomer() throws RemoteException {
		return customer;
	}

	@Override
	public Money getBalance() throws RemoteException {
		return balance;
	}

	@Override
	public String getSettledCurrency() throws RemoteException {
		return currency;
	}

	@Override
	// TODO: Review this
	public synchronized void deposit(Money amount) throws RemoteException {
		assert amount.getCurrency().equals(currency);
		this.balance = new Money(balance.getAmount().add(amount.getAmount()), currency);
	}

	@Override
	// TODO: Review this
	public synchronized void withdraw(Money amount) throws RemoteException {
		assert amount.getCurrency().equals(currency);
		this.balance = new Money(balance.getAmount().subtract(amount.getAmount()), currency);
	}

	private class DepositTransaction extends UnicastRemoteObject implements Transaction {
		private static final long serialVersionUID = 1L;
		private LocalDateTime transactionTime;
		private Money amount;
		private RemoteAccount account;
		
		protected DepositTransaction(Money amount, RemoteAccount account) throws RemoteException {
			assert(amount.getCurrency().equals(account.getSettledCurrency()));
			this.amount = amount;
			this.account = account;
		}

		@Override
		public LocalDateTime getTransactionTime() throws RemoteException {
			return transactionTime;
		}

		@Override
		public Money getAmount() throws RemoteException {
			return amount;
		}

		@Override
		public Account getDirectParticipant() throws RemoteException {
			return account;
		}

		@Override
		public void carryOut() throws RemoteException {
			transactionTime = LocalDateTime.now();
			account.deposit(amount);
		}
	}

	private class WithdrawTransaction extends UnicastRemoteObject implements Transaction {
		private static final long serialVersionUID = 1L;
		private LocalDateTime transactionTime;
		private Money amount;
		private RemoteAccount account;
		
		protected WithdrawTransaction(Money amount, RemoteAccount account) throws RemoteException {
			assert(amount.getCurrency().equals(account.getSettledCurrency()));
			this.amount = amount;
			this.account = account;
		}

		@Override
		public LocalDateTime getTransactionTime() throws RemoteException {
			return transactionTime;
		}

		@Override
		public Money getAmount() throws RemoteException {
			return amount;
		}

		@Override
		public Account getDirectParticipant() throws RemoteException {
			return account;
		}

		@Override
		public void carryOut() throws RemoteException {
			transactionTime = LocalDateTime.now();
			account.withdraw(amount);
		}
	}

	private class TransferTransaction extends UnicastRemoteObject implements bank.TransferTransaction {
		private static final long serialVersionUID = 1L;
		private LocalDateTime transactionTime;
		private Money amount;
		private RemoteAccount fromAccount;
		private Account toAccount;
		
		protected TransferTransaction(Money amount, RemoteAccount fromAccount, Account toAccount) throws RemoteException {
			assert(amount.getCurrency().equals(fromAccount.getSettledCurrency()));
			this.amount = amount;
			this.fromAccount = fromAccount;
			this.toAccount = toAccount;
		}

		@Override
		public LocalDateTime getTransactionTime() throws RemoteException {
			return transactionTime;
		}

		@Override
		public Money getAmount() throws RemoteException {
			return amount;
		}

		@Override
		public Account getDirectParticipant() throws RemoteException {
			return fromAccount;
		}

		@Override
		// TODO: does synchronized work here?
		public void carryOut() throws RemoteException {
			synchronized(getClass()) {
				transactionTime = LocalDateTime.now();
				fromAccount.withdraw(amount);
				toAccount.deposit(getRecipientAmount());
			}
		}

		@Override
		public Account getRecipient() throws RemoteException {
			return toAccount;
		}

		@Override
		public Money getRecipientAmount() throws RemoteException {
			try {
				Registry registry = LocateRegistry.getRegistry(1099);
				HeadQuarters hq = (HeadQuarters) registry.lookup("HQ");
				assert hq != null;
				return hq.exchange(amount, toAccount.getSettledCurrency());
			} catch (NotBoundException e) {
				throw new RemoteException("HQ not found", e);
			}
		}
	}

	@Override
	public Transaction createDeposit(Money amount) throws RemoteException {
		return new DepositTransaction(amount, this);
	}

	@Override
	public Transaction createWithdraw(Money amount) throws RemoteException {
		return new WithdrawTransaction(amount, this);
	}

	@Override
	public TransferTransaction createTransfer(Account receiver, Money amount) throws RemoteException {
		return new TransferTransaction(amount, this, receiver);
	}
}
