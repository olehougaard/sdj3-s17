package bank.dao;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import bank.model.Account;
import bank.model.AccountNumber;
import bank.model.Customer;
import bank.model.Money;

public class AccountDAOService extends UnicastRemoteObject implements AccountDAO {
	private static final long serialVersionUID = 1L;
	private DatabaseHelper<Account> helper;
	private TransactionDAO transactionDAO;
	
	protected AccountDAOService(String jdbcURL, String username, String password, TransactionDAO transactionDAO) throws RemoteException {
		this.transactionDAO = transactionDAO;
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	@Override
	public Account create(AccountNumber accountNumber, Customer customer, String currency)
			throws RemoteException {
		helper.executeUpdate("INSERT INTO Account(reg_number, account_number, customer, currency) VALUES (?, ?, ?, ?)", 
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(), customer.getCpr(), currency);
		return read(accountNumber);
	}
	
	public static class AccountMapper implements DataMapper<Account>{
		@Override
		public Account create(ResultSet rs) throws SQLException {
			AccountNumber accountNumber = new AccountNumber(rs.getInt("reg_number"), rs.getLong("account_number"));
			BigDecimal balance = rs.getBigDecimal("balance");
			String currency = rs.getString("currency");
			return new Account(accountNumber, new Money(balance, currency));
		}
		
	}

	@Override
	public Account read(AccountNumber accountNumber) throws RemoteException {
		return helper.mapSingle(new AccountMapper(), "SELECT * FROM Account WHERE reg_number = ? AND account_number = ?", 
				accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	@Override
	public Collection<Account> readAccountsFor(Customer customer) throws RemoteException {
		return helper.map(new AccountMapper(), "SELECT * FROM Account WHERE customer = ?", customer.getCpr()) ;
	}

	@Override
	public void update(Account account) throws RemoteException {
		helper.executeUpdate("UPDATE ACCOUNT SET balance = ?, currency = ? WHERE reg_number = ? AND account_number = ?", 
				account.getBalance().getAmount(), account.getSettledCurrency(), account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}

	@Override
	public void delete(Account account) throws RemoteException {
		transactionDAO.deleteFor(account);
		helper.executeUpdate("DELETE FROM ACCOUNT WHERE reg_number = ? AND account_number = ?", 
				account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}
}
