package car_client;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cars.Car;
import cars.CarBase;
import cars.Money;

public class CarBaseTest {
	private CarBase carBase;
	
	@Before
	public void setUp() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(1099);
		carBase = (CarBase) registry.lookup("CarBase");
	}

	@Test
	public void test() throws RemoteException {
		Money dkk = new Money(new BigDecimal(100000), "DKK");
		Money eur = new Money(new BigDecimal("4999.99"), "EUR");
		Car car = carBase.registerCar("Ford", 2014, dkk);
		car.setPrice(eur);
		List<Car> allCars = carBase.getAllCars();
		assertEquals(eur, allCars.get(0).getPrice());
		assertEquals("Ford", allCars.get(0).getModel());
		assertEquals(2014, allCars.get(0).getYear());
	}
}
