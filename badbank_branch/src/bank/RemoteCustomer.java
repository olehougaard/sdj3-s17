package bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

public class RemoteCustomer extends UnicastRemoteObject implements Customer {
	private static final long serialVersionUID = 1L;
	private String cpr;
	private String name;
	private String address;
	private Branch branch;

	public RemoteCustomer(String cpr, String name, String address, Branch branch) throws RemoteException {
		this.cpr = cpr;
		this.name = name;
		this.address = address;
		this.branch = branch;
	}

	@Override
	public String getCpr() throws RemoteException {
		return cpr;
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
	public void move(String address) throws RemoteException {
		this.address = address;
	}

	@Override
	public Collection<Account> getAccounts() throws RemoteException {
		return branch.getAccounts(this);
	}
}
