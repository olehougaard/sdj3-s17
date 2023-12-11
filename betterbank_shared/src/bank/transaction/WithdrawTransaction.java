package bank.transaction;

import java.rmi.RemoteException;

import bank.model.AccountNumber;
import bank.model.Money;
import bank.model.TransactionDetails;

public class WithdrawTransaction extends AbstractTransaction {
	private static final long serialVersionUID = 1L;

	public WithdrawTransaction(Money amount, AccountNumber account) {
		this(amount, account, "Withdrew " + amount);
	}

	WithdrawTransaction(Money amount, AccountNumber accountNumber, String text) {
		super(amount, accountNumber, text);
	}


	@Override
	public TransactionDetails accept(TransactionVisitor visitor) throws RemoteException {
		return visitor.withdraw(this);
	}
}
