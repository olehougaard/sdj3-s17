package bank.dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import bank.model.Account;
import bank.model.AccountNumber;
import bank.model.Money;
import bank.model.TransactionDetails;

public class TransactionDAOService extends UnicastRemoteObject implements TransactionDAO {
	private static final long serialVersionUID = 1L;
	private DatabaseHelper<TransactionDetails> helper;
	
	public TransactionDAOService(String jdbcURL, String username, String password) throws RemoteException {
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}
	
	private static class TransactionMapper implements DataMapper<TransactionDetails> {
		@Override
		public TransactionDetails create(ResultSet rs) throws SQLException {
			int id = rs.getInt("transaction_id");
			Money amount = new Money(rs.getBigDecimal("amount"), rs.getString("currency"));
			String text = rs.getString("transaction_text");
			AccountNumber primaryAccount = new AccountNumber(rs.getInt("primary_reg_number"), rs.getLong("primary_account_number"));
			if (rs.getObject("secondary_reg_number") == null) {
				return new TransactionDetails(id, amount, text, primaryAccount);
			} else {
				AccountNumber secondaryAccount = new AccountNumber(rs.getInt("secondary_reg_number"), rs.getLong("secondary_account_number"));
				return new TransactionDetails(id, amount, text, primaryAccount, secondaryAccount);
			}
		}
	}

	@Override
	public TransactionDetails create(Money amount, String text, AccountNumber primaryAccount) throws RemoteException {
		List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Transaction(amount, currency, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?)", 
				amount.getAmount(), amount.getCurrency(), text, primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
		return new TransactionDetails(keys.get(0), amount, text, primaryAccount);
	}

	@Override
	public TransactionDetails create(Money amount, String text, AccountNumber primaryAccount,
			AccountNumber secondaryAccount) throws RemoteException {
		List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Transaction(amount, currency, transaction_text, primary_reg_number, primary_account_number, secondary_reg_number, secondary_account_number) VALUES (?, ?, ?, ?, ?, ?, ?)", 
				amount.getAmount(), amount.getCurrency(), text, primaryAccount.getRegNumber(), primaryAccount.getAccountNumber(), secondaryAccount.getRegNumber(), secondaryAccount.getAccountNumber());
		return new TransactionDetails(keys.get(0), amount, text, primaryAccount, secondaryAccount);
	}

	@Override
	public TransactionDetails read(int transactionId) throws RemoteException {
		return helper.mapSingle(new TransactionMapper(), "SELECT * FROM Transaction WHERE transaction_id = ?", transactionId);
	}

	@Override
	public List<TransactionDetails> readAllFor(AccountNumber accountNumber) throws RemoteException {
		return helper.map(new TransactionMapper(), 
				"SELECT * FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	@Override
	public void deleteFor(Account account) throws RemoteException {
		AccountNumber accountNumber = account.getAccountNumber();
		helper.executeUpdate("DELETE FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
		
	}

}
