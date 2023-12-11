package bank;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface Transaction extends Remote {
	LocalDateTime getTransactionTime() throws RemoteException;
	Money getAmount() throws RemoteException;
	Account getDirectParticipant() throws RemoteException;
	void carryOut() throws RemoteException;
}
