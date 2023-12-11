package bank.dao;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;

import bank.Branch;
import bank.model.ExchangeRate;

public class HeadQuartersDAOService extends UnicastRemoteObject implements HeadQuartersDAO {
	private static final long serialVersionUID = 1L;
	private String jdbcURL;
	private String username;
	private String password;

	public HeadQuartersDAOService(String jdbcURL, String username, String password) throws RemoteException {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
	}

	@Override
	public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException {
		DatabaseHelper<BigDecimal> helper = new DatabaseHelper<>(jdbcURL, username, password);
		BigDecimal rate = helper.mapSingle((rs)->rs.getBigDecimal(1), "SELECT rate FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		return new ExchangeRate(fromCurrency, toCurrency, rate);
	}

	@Override
	public BranchDTO readBranch(int regNumber) throws RemoteException {
		DatabaseHelper<BranchDTO> helper = new DatabaseHelper<>(jdbcURL, username, password);
		DataMapper<BranchDTO> mapper = new DataMapper<BranchDTO>() {
			@Override
			public BranchDTO create(ResultSet rs) throws SQLException {
				int regNumber = rs.getInt("reg_number");
				String address = rs.getString("address");
				return new BranchDTO(regNumber, address);
			}
		};
		return helper.mapSingle(mapper, "SELECT * FROM Branch WHERE reg_number = ?", regNumber);
	}

	@Override
	public BranchDTO createBranch(int regNumber, String address) throws RemoteException {
		DatabaseHelper<BranchDTO> helper = new DatabaseHelper<>(jdbcURL, username, password);
		helper.executeUpdate("INSERT INTO Branch VALUES (?, ?)", regNumber, address);
		return new BranchDTO(regNumber, address);
	}
	
	@Override
	public void updateBranch(Branch branch) throws RemoteException {
		DatabaseHelper<BranchDTO> helper = new DatabaseHelper<>(jdbcURL, username, password);
		helper.executeUpdate("UPDATE Branch SET address = ? WHERE reg_number = ?", branch.getAddress(), branch.getRegNumber());
	}
}
