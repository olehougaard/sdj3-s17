package bank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RemoteBranch extends UnicastRemoteObject implements Branch {
	private static final long serialVersionUID = 1;
	private String regNumber;
	private String name;
	private String address;
	private Map<String, Customer> customers = new HashMap<>();
	private Map<String, Account> accounts = new HashMap<>();
	private long nextAccount = 1;
	
	public RemoteBranch(String regNumber, String name, String address) throws RemoteException {
		this.regNumber = regNumber;
		this.name = name;
		this.address = address;
	}

	@Override
	public String getRegNumber() throws RemoteException {
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
		Customer customer = new RemoteCustomer(cpr, name, address, this);
		customers.put(customer.getCpr(), customer);
		return customer;
	}

	@Override
	public Customer getCustomer(String cpr) throws RemoteException {
		return customers.get(cpr);
	}

	@Override
	public Account createAccount(Customer customer, String currency) throws RemoteException {
		String accountNumber;
		// TODO: Why synchronize like this?
		// TODO: Is this a good way of making account numbers?
		synchronized(RemoteBranch.class) {
			long theNumber = nextAccount;
			nextAccount++;
			accountNumber = String.format("%s %010d", regNumber, theNumber);
		}
		Account account = new RemoteAccount(customer, accountNumber, currency);
		accounts.put(accountNumber, account);
		return account;
	}

	@Override
	public Account getAccount(String accountNumber) throws RemoteException {
		return accounts.get(accountNumber);
	}

	@Override
	public Collection<Account> getAccounts(Customer customer) throws RemoteException {
		LinkedList<Account> customerAccounts = new LinkedList<>();
		for(Account account: accounts.values()) {
			if (account.getCustomer().equals(customer)) {
				customerAccounts.add(account);
			}
		}
		return customerAccounts;
	}
	
	public void run() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(1099);
		registry.rebind(this.getName(), this);
		HeadQuarters hq = (HeadQuarters) registry.lookup("HQ");
		hq.register(this);
	}
	
	public static void main(String[] args) throws Exception {
		RemoteBranch branch1 = new RemoteBranch("1234", "branch1", "address1");
		RemoteBranch branch2 = new RemoteBranch("7255", "Østergade", "Østergade");
		branch1.run();
		branch2.run();
	}
}
