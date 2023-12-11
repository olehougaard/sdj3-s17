package bank.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import bank.model.Account;
import bank.model.AccountNumber;
import bank.model.Customer;

public interface AccountDAO extends Remote {
	public Account create(AccountNumber accountNumber, Customer customer, String currency) throws RemoteException;
	public Account read(AccountNumber accountNumber) throws RemoteException;
	public Collection<Account> readAccountsFor(Customer customer) throws RemoteException;
	public void update(Account account) throws RemoteException;
	public void delete(Account account) throws RemoteException;
}
