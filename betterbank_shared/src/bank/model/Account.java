package bank.model;

import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	private AccountNumber accountNumber;
	private String currency;
	private Money balance;

	public Account(AccountNumber accountNumber, String currency) {
		this(accountNumber, Money.zero(currency));
	}

	public Account(AccountNumber accountNumber, Money balance) {
		this.accountNumber = accountNumber;
		this.currency = balance.getCurrency();
		this.balance = balance;
	}

	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	public Money getBalance() {
		return balance;
	}

	public String getSettledCurrency() {
		return currency;
	}

	public synchronized void deposit(Money amount) {
		this.balance = balance.add(amount);
	}

	public synchronized void withdraw(Money amount) {
		this.balance = balance.subtract(amount);
	}
}
