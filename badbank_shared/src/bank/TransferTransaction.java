package bank;

import java.rmi.RemoteException;

public interface TransferTransaction extends Transaction {
	Account getRecipient() throws RemoteException;
	Money getRecipientAmount() throws RemoteException;
}
