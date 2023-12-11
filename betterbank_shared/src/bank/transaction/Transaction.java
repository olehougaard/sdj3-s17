package bank.transaction;

import java.io.Serializable;
import java.rmi.RemoteException;

import bank.model.TransactionDetails;

public interface Transaction extends Serializable {
	String getText();
	TransactionDetails accept(TransactionVisitor visitor) throws RemoteException;
}
