package bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeadQuarters extends Remote {
	Money exchange(Money amount, String currency) throws RemoteException;
	void register(Branch branch) throws RemoteException;
	Branch findBranch(String regNumber) throws RemoteException;
}
