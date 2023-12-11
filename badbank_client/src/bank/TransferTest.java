package bank;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Before;
import org.junit.Test;

public class TransferTest {
	private HeadQuarters hq;
	private Account primaryAccount;
	private Account secondaryAccount;

	@Before
	public void setUp() throws Exception {
		Registry registry = LocateRegistry.getRegistry(1099);
		hq = (HeadQuarters) registry.lookup("HQ");
		Branch branch1 = hq.findBranch("1234");
		Customer other = branch1.createCustomer("1122334455", "Other guy", "Other address");
		secondaryAccount = branch1.createAccount(other, "EUR");
		Branch primaryBranch = hq.findBranch("7255");
		Customer customer = primaryBranch.createCustomer("1234567890", "Me", "My address");
		primaryAccount = primaryBranch.createAccount(customer, "DKK");
	}
	
	// TODO: Review this
	private void transfer(String recipientAccountNumber, Money amount) throws RemoteException {
		Branch recipientBranch = hq.findBranch(recipientAccountNumber.substring(0, 4));
		Account recipientAccount = recipientBranch.getAccount(recipientAccountNumber);
		TransferTransaction transfer = primaryAccount.createTransfer(recipientAccount, amount);
		transfer.carryOut();
	}
	
	@Test
	public void test() throws RemoteException {
		Money startingAmount = new Money(new BigDecimal(10000), "DKK");
		Money transferAmount = new Money(new BigDecimal(1000), "DKK");
		Money remainingAmount = new Money(new BigDecimal(9000), "DKK");
		primaryAccount.createDeposit(startingAmount).carryOut();
		assertEquals(startingAmount, primaryAccount.getBalance());
		transfer("1234 0000000001", transferAmount);
		assertEquals(remainingAmount, primaryAccount.getBalance());
		assertEquals(hq.exchange(transferAmount, "EUR"), secondaryAccount.getBalance());
	}

}
