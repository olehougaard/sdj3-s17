package bank.transaction;

import java.rmi.RemoteException;

import bank.model.AccountNumber;
import bank.model.Money;
import bank.model.TransactionDetails;

public class TransferTransaction implements Transaction {
	private static final long serialVersionUID = 1L;
	private WithdrawTransaction withdrawTransaction;
	private DepositTransaction depositTransaction;
	
	public TransferTransaction(Money amount, AccountNumber account, AccountNumber recipientNumber) {
		this.withdrawTransaction = new WithdrawTransaction(amount, account, "Transfered " + amount + " to " + recipientNumber);
		this.depositTransaction = new DepositTransaction(amount, recipientNumber, "Transfered" + amount + "from " + recipientNumber);
	}

	public Money getAmount() {
		return withdrawTransaction.getAmount();
	}

	public AccountNumber getAccountNumber() {
		return withdrawTransaction.getAccountNumber();
	}
	
	public AccountNumber getRecipientNumber() {
		return depositTransaction.getAccountNumber();
	}
	
	public WithdrawTransaction getWithdrawTransaction() {
		return withdrawTransaction;
	}

	public DepositTransaction getDepositTransaction() {
		return depositTransaction;
	}

	@Override
	public TransactionDetails accept(TransactionVisitor visitor) throws RemoteException {
		return visitor.transfer(this);
	}

	@Override
	public String getText() {
		return withdrawTransaction.getText();
	}
}
