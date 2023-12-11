package bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import bank.dao.AccountDAO;
import bank.dao.CustomerDAO;
import bank.dao.DataService;
import bank.dao.TransactionDAO;
import bank.model.Account;
import bank.model.AccountNumber;
import bank.model.Customer;
import bank.model.Money;
import bank.model.TransactionDetails;
import bank.transaction.DepositTransaction;
import bank.transaction.Transaction;
import bank.transaction.TransactionVisitor;
import bank.transaction.TransferTransaction;
import bank.transaction.WithdrawTransaction;

public class RemoteBranch extends UnicastRemoteObject implements Branch, TransactionVisitor {
	private static final long serialVersionUID = 1;
	private int regNumber;
	private String name;
	private String address;
	private long nextAccount = 1;
	private AccountDAO accountDAO;
	private CustomerDAO customerDAO;
	private TransactionDAO transactionDAO;
	private HeadQuarters hq;
	
	public RemoteBranch(int regNumber, String name, String address, DataService service, HeadQuarters hq) throws RemoteException {
		this.regNumber = regNumber;
		this.name = name;
		this.address = address;
		this.hq = hq;
		this.accountDAO = service.getAccountDAO();
		this.customerDAO = service.getCustomerDAO();
		this.transactionDAO = service.getTransactionDAO();
	}

	@Override
	public int getRegNumber() throws RemoteException {
		return regNumber;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public String getAddress() throws RemoteException {
		return address;
	}

	@Override
	public Customer createCustomer(String cpr, String name, String address) throws RemoteException {
		return customerDAO.create(cpr, name, address);
	}

	@Override
	public Customer getCustomer(String cpr) throws RemoteException {
		return customerDAO.read(cpr);
	}

	@Override
	public AccountProxy createAccount(Customer customer, String currency) throws RemoteException {
		AccountNumber accountNumber;
		synchronized(RemoteBranch.class) {
			accountNumber = new AccountNumber(regNumber, nextAccount++);
		}
		return new AccountProxy(accountDAO.create(accountNumber, customer, currency), this);
	}

	@Override
	public AccountProxy getAccount(AccountNumber accountNumber) throws RemoteException {
		return new AccountProxy(accountDAO.read(accountNumber), this);
	}
	
	@Override
	public void cancelAccount(AccountNumber accountNumber) throws RemoteException {
		accountDAO.delete(accountDAO.read(accountNumber));
	}

	@Override
	public Collection<Account> getAccounts(Customer customer) throws RemoteException {
		return accountDAO.readAccountsFor(customer);
	}
	
	@Override
	public TransactionDetails execute(Transaction t) throws RemoteException {
		return t.accept(this);
	}
	
	private Money translateToSettledCurrency(Money amount, Account account) throws RemoteException {
		if (!amount.getCurrency().equals(account.getSettledCurrency())) {
			amount = hq.exchange(amount, account.getSettledCurrency());
		}
		return amount;
	}

	@Override
	public TransactionDetails deposit(DepositTransaction transaction) throws RemoteException {
		Account account = accountDAO.read(transaction.getAccountNumber());
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		account.deposit(amount);
		accountDAO.update(account);
		return transactionDAO.create(amount, transaction.getText(), transaction.getAccountNumber());
	}
	
	@Override
	public TransactionDetails withdraw(WithdrawTransaction transaction) throws RemoteException {
		Account account = accountDAO.read(transaction.getAccountNumber());
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		account.withdraw(amount);
		accountDAO.update(account);
		return transactionDAO.create(amount, transaction.getText(), transaction.getAccountNumber());
	}
	
	private Branch findBranch(AccountNumber accountNumber) throws RemoteException {
		if (accountNumber.getRegNumber() == regNumber) {
			return this;
		} else {
			return hq.findBranch(accountNumber.getRegNumber());
		}
	}
	
	@Override
	public TransactionDetails transfer(TransferTransaction transaction) throws RemoteException {
		Branch recepientBranch = findBranch(transaction.getRecipientNumber());
		recepientBranch.execute(transaction.getDepositTransaction());
		return this.execute(transaction.getWithdrawTransaction());
	}
}
