package bank;

import java.io.Serializable;
import java.rmi.RemoteException;

import bank.model.Account;
import bank.model.AccountNumber;
import bank.model.Money;
import bank.model.TransactionDetails;
import bank.transaction.DepositTransaction;
import bank.transaction.TransferTransaction;
import bank.transaction.WithdrawTransaction;

public class AccountProxy implements Serializable {
	private static final long serialVersionUID = 1L;
	private Account account;
	private Branch branch;
	
	public AccountProxy(Account account, Branch branch) {
		this.account = account;
		this.branch = branch;
	}

	public Money getBalance() {
		return account.getBalance();
	}

	public AccountNumber getAccountNumber() {
		return account.getAccountNumber();
	}

	public String getSettledCurrency() {
		return account.getSettledCurrency();
	}
	
	public TransactionDetails deposit(Money amount) throws RemoteException {
		TransactionDetails transaction = branch.execute(new DepositTransaction(amount, account.getAccountNumber()));
		account.deposit(transaction.getAmount());
		return transaction;
	}
	
	public TransactionDetails withdraw(Money amount) throws RemoteException {
		TransactionDetails transaction = branch.execute(new WithdrawTransaction(amount, account.getAccountNumber()));
		account.withdraw(transaction.getAmount());
		return transaction;
	}
	
	public TransactionDetails transferTo(AccountNumber recipient, Money amount) throws RemoteException {
		TransactionDetails transaction = branch.execute(new TransferTransaction(amount, account.getAccountNumber(), recipient));
		account.withdraw(transaction.getAmount());
		return transaction;
	}
}
