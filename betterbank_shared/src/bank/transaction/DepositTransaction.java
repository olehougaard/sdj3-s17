package bank.transaction;

import java.rmi.RemoteException;

import bank.model.AccountNumber;
import bank.model.Money;
import bank.model.TransactionDetails;

public class DepositTransaction extends AbstractTransaction {
	private static final long serialVersionUID = 1L;

	public DepositTransaction(Money amount, AccountNumber accountNumber) {
		this(amount, accountNumber, "Deposited " + amount);
	}
	
	DepositTransaction(Money amount, AccountNumber accountNumber, String text) {
		super(amount, accountNumber, text);
	}

	@Override
	public TransactionDetails accept(TransactionVisitor visitor) throws RemoteException {
		return visitor.deposit(this);
	}
}
