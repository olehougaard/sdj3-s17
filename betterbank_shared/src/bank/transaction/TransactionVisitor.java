package bank.transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;

import bank.model.TransactionDetails;

public interface TransactionVisitor extends Remote {
	TransactionDetails deposit(DepositTransaction transaction) throws RemoteException;
	TransactionDetails withdraw(WithdrawTransaction transaction) throws RemoteException;
	TransactionDetails transfer(TransferTransaction transaction) throws RemoteException;
}
