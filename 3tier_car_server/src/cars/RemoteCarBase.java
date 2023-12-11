package cars;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoteCarBase implements CarBase {
	private Map<String, RemoteCar> cars = new HashMap<>();

	@Override
	public Car registerCar(String licenseNumber, String model, int year, Money price) throws RemoteException {
		CarDTO carDTO = DAOLocator.getDAO().create(licenseNumber, model, year, price);
		RemoteCar car = new RemoteCar(carDTO);
		cars.put(licenseNumber, car);
		return car;
	}
	

	@Override
	public Car getCar(String licenseNumber) throws RemoteException {
		if (!cars.containsKey(licenseNumber)) {
			cars.put(licenseNumber, new RemoteCar(DAOLocator.getDAO().read(licenseNumber)));
		}
		return cars.get(licenseNumber);
	}

	@Override
	public List<Car> getAllCars() throws RemoteException {
		Collection<CarDTO> allCars = DAOLocator.getDAO().readAll();
		LinkedList<Car> list = new LinkedList<Car>();
		for(CarDTO car: allCars) {
			if (!cars.containsKey(car.getLicenseNumber())) {
				cars.put(car.getLicenseNumber(), new RemoteCar(car));
			}
			list.add(cars.get(car.getLicenseNumber()));
		}
		return list;
	}

	@Override
	public void removeCar(Car car) throws RemoteException {
		DAOLocator.getDAO().delete(new CarDTO(car));
		cars.remove(car.getLicenseNumber());
	}
}
