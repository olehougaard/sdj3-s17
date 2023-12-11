package dk.via.cars;

public class CarServiceProxy implements dk.via.cars.CarService {
  private String _endpoint = null;
  private dk.via.cars.CarService carService = null;
  
  public CarServiceProxy() {
    _initCarServiceProxy();
  }
  
  public CarServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCarServiceProxy();
  }
  
  private void _initCarServiceProxy() {
    try {
      carService = (new dk.via.cars.CarServiceServiceLocator()).getCarService();
      if (carService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)carService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)carService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (carService != null)
      ((javax.xml.rpc.Stub)carService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public dk.via.cars.CarService getCarService() {
    if (carService == null)
      _initCarServiceProxy();
    return carService;
  }
  
  public void delete(java.lang.String license_number) throws java.rmi.RemoteException{
    if (carService == null)
      _initCarServiceProxy();
    carService.delete(license_number);
  }
  
  public dk.via.cars.CarDTO create(java.lang.String licenseNo, java.lang.String model, int year, dk.via.cars.MoneyDTO price) throws java.rmi.RemoteException{
    if (carService == null)
      _initCarServiceProxy();
    return carService.create(licenseNo, model, year, price);
  }
  
  public dk.via.cars.CarDTO[] readAll() throws java.rmi.RemoteException{
    if (carService == null)
      _initCarServiceProxy();
    return carService.readAll();
  }
  
  
}