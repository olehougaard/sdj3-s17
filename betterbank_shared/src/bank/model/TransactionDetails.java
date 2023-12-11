package bank.model;

import java.io.Serializable;

public class TransactionDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private Money amount;
	private String text;
	private AccountNumber primaryAccount;
	private AccountNumber secondaryAccount;

	public TransactionDetails(int id, Money amount, String text, AccountNumber primaryAccount) {
		this(id, amount, text, primaryAccount, null);
	}

	public TransactionDetails(int id, Money amount, String text, AccountNumber primaryAccount,
			AccountNumber secondaryAccount) {
				this.id = id;
				this.amount = amount;
				this.text = text;
				this.primaryAccount = primaryAccount;
				this.secondaryAccount = secondaryAccount;
	}

	public int getId() {
		return id;
	}

	public Money getAmount() {
		return amount;
	}

	public String getText() {
		return text;
	}

	public AccountNumber getPrimaryAccount() {
		return primaryAccount;
	}

	public AccountNumber getSecondaryAccount() {
		return secondaryAccount;
	}

}
