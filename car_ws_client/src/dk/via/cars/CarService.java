/**
 * CarService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dk.via.cars;

public interface CarService extends java.rmi.Remote {
    public void delete(java.lang.String license_number) throws java.rmi.RemoteException;
    public dk.via.cars.CarDTO create(java.lang.String licenseNo, java.lang.String model, int year, dk.via.cars.MoneyDTO price) throws java.rmi.RemoteException;
    public dk.via.cars.CarDTO[] readAll() throws java.rmi.RemoteException;
}
