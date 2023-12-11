package bank.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;

import bank.Branch;
import bank.model.ExchangeRate;

public interface HeadQuartersDAO extends Remote {
	ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException;
	BranchDTO createBranch(int regNumber, String address) throws RemoteException;
	BranchDTO readBranch(int regNumber) throws RemoteException;
	void updateBranch(Branch branch) throws RemoteException;
}
