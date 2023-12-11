package betterbank_main;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import bank.HeadQuarters;
import bank.RemoteBranch;
import bank.RemoteHQ;
import bank.dao.DataService;
import bank.dao.DataServiceImpl;

public class Main {
	@SuppressWarnings("unchecked")
	private static<T extends Remote> T bind(Registry registry, String URI, T remoteObject) throws RemoteException {
		registry.rebind(URI, remoteObject);
		try {
			return (T) registry.lookup(URI);
		} catch (NotBoundException e) {
			// How could this happen?
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("dataservice", new DataServiceImpl());
		DataService dataService = bind(registry, "dataservice", new DataServiceImpl());
		HeadQuarters hq = bind(registry, "HQ", new RemoteHQ(dataService));
		RemoteBranch branch1 = new RemoteBranch(1234, "branch1", "address1", dataService, hq);
		registry.rebind(branch1.getName(), branch1);
		hq.register(branch1);
		RemoteBranch branch2 = new RemoteBranch(7255, "Østergade", "Østergade", dataService, hq);
		registry.rebind(branch2.getName(), branch1);
		hq.register(branch2);
	}
}
