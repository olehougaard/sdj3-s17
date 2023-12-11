package bank.dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DataServiceImpl extends UnicastRemoteObject implements DataService {
	private static final long serialVersionUID = 1L;
	private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=bank";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "password";
	private HeadQuartersDAOService hqDao;
	private AccountDAOService accountDAO;
	private CustomerDAOService customerDAO;
	private TransactionDAO transactionDAOService;

	public DataServiceImpl() throws RemoteException {
		hqDao = new HeadQuartersDAOService(JDBC_URL, USERNAME, PASSWORD);
		transactionDAOService = new TransactionDAOService(JDBC_URL, USERNAME, PASSWORD);
		accountDAO = new AccountDAOService(JDBC_URL, USERNAME, PASSWORD, transactionDAOService);
		customerDAO = new CustomerDAOService(JDBC_URL, USERNAME, PASSWORD, accountDAO);
	}

	@Override
	public HeadQuartersDAO getHQDAO() throws RemoteException {
		return hqDao;
	}

	@Override
	public AccountDAO getAccountDAO() throws RemoteException {
		return accountDAO;
	}

	@Override
	public CustomerDAO getCustomerDAO() throws RemoteException {
		return customerDAO;
	}

	@Override
	public TransactionDAO getTransactionDAO() throws RemoteException {
		return transactionDAOService;
	}

}
