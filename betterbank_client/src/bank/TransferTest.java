package bank;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.model.Customer;
import bank.model.Money;

public class TransferTest {
	private HeadQuarters hq;
	private AccountProxy primaryAccount;
	private AccountProxy secondaryAccount;
	private Branch primaryBranch;
	private Branch secondaryBranch;

	@Before
	public void setUp() throws Exception {
		Registry registry = LocateRegistry.getRegistry(1099);
		hq = (HeadQuarters) registry.lookup("HQ");
		primaryBranch = hq.findBranch(7255);
		Customer customer = primaryBranch.getCustomer("1234567890");
		primaryAccount = primaryBranch.createAccount(customer, "DKK");
		secondaryBranch = hq.findBranch(1234);
		Customer other = secondaryBranch.getCustomer("1122334455");
		secondaryAccount = secondaryBranch.createAccount(other, "EUR");
	}
	
	@After
	public void tearDown() throws Exception {
		primaryBranch.cancelAccount(primaryAccount.getAccountNumber());
		secondaryBranch.cancelAccount(secondaryAccount.getAccountNumber());
	}
	
	@Test
	public void test() throws RemoteException {
		Money startingAmount = new Money(new BigDecimal(10000), "DKK");
		Money transferAmount = new Money(new BigDecimal(1000), "DKK");
		Money remainingAmount = new Money(new BigDecimal(9000), "DKK");
		primaryAccount.deposit(startingAmount);
		assertEquals(startingAmount, primaryAccount.getBalance());
		primaryAccount.transferTo(secondaryAccount.getAccountNumber(), transferAmount);
		secondaryAccount = secondaryBranch.getAccount(secondaryAccount.getAccountNumber());
		assertEquals(remainingAmount, primaryAccount.getBalance());
		assertEquals(hq.exchange(transferAmount, "EUR"), secondaryAccount.getBalance());
	}
}
