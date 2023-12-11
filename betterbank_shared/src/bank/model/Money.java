package bank.model;

import java.io.Serializable;
import java.math.BigDecimal;

public final class Money implements Serializable {
	private static final long serialVersionUID = 1;
	private BigDecimal amount;
	private String currency;

	public Money(BigDecimal amount, String currency) {
		assert amount != null && currency != null;
		this.amount = amount.setScale(2);
		this.currency = currency;
	}

	public static Money zero(String currency) {
		return new Money(new BigDecimal(0), currency);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public Money add(Money that) {
		assert currency.equals(that.getCurrency());
		return new Money(this.amount.add(that.amount), currency);
	}

	public Money subtract(Money that) {
		assert currency.equals(that.getCurrency());
		return new Money(this.amount.subtract(that.amount), currency);
	}

	public Money multiply(BigDecimal factor) {
		return new Money(amount.multiply(factor), currency);
	}

	public Money exchange(ExchangeRate rate) {
		if (!rate.getFromCurrency().equals(currency)) {
			throw new IllegalArgumentException("Wrong currency");
		}
		return new Money(amount.multiply(rate.getExchangeRate()), rate.getToCurrency());
	}

	@Override
	public int hashCode() {
		return amount.hashCode() ^ currency.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Money))
			return false;
		Money that = (Money) obj;
		return this.amount.equals(that.amount) && this.currency.equals(that.currency);
	}

	@Override
	public String toString() {
		return String.format("%s %s", amount.toPlainString(), currency);
	}
}
