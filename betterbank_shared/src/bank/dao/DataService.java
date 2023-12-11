package bank.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataService extends Remote {
	HeadQuartersDAO getHQDAO() throws RemoteException;
	AccountDAO getAccountDAO() throws RemoteException;
	CustomerDAO getCustomerDAO() throws RemoteException;
	TransactionDAO getTransactionDAO() throws RemoteException;
}
