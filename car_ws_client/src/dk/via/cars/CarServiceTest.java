package dk.via.cars;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CarServiceTest {

	private static CarService carService;
	private static CarDTO car;

	@BeforeClass
	public static void setUp() throws Exception {
		CarServiceServiceLocator locator = new CarServiceServiceLocator();
		carService = locator.getCarService();
		car = carService.create("CD 12 345", "Ford", 2011, new MoneyDTO(new BigDecimal(10000), "DKK"));
	}

	@AfterClass
	public static void tearDown() throws Exception {
		carService.delete("CD 12 345");
	}

	@Test
	public void testLicenseNumber() {
		Assert.assertEquals("CD 12 345", car.getLicenseNumber());
	}

	@Test
	public void testReadAll() throws RemoteException {
		CarDTO[] all = carService.readAll();
		boolean found = false;
		for(CarDTO car: all) {
			if (car.getLicenseNumber().equals("CD 12 345")) {
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
	}
}
