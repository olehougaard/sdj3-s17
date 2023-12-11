package bank.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import bank.model.Account;
import bank.model.AccountNumber;
import bank.model.Money;
import bank.model.TransactionDetails;

public interface TransactionDAO extends Remote {
	TransactionDetails read(int transactionId) throws RemoteException;
	List<TransactionDetails> readAllFor(AccountNumber account) throws RemoteException;
	TransactionDetails create(Money amount, String text, AccountNumber primaryAccount, AccountNumber secondaryAccount) throws RemoteException;
	TransactionDetails create(Money amount, String text, AccountNumber primaryAccount) throws RemoteException;
	void deleteFor(Account account) throws RemoteException;
}
