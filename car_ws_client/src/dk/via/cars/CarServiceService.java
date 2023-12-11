/**
 * CarServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dk.via.cars;

public interface CarServiceService extends javax.xml.rpc.Service {
    public java.lang.String getCarServiceAddress();

    public dk.via.cars.CarService getCarService() throws javax.xml.rpc.ServiceException;

    public dk.via.cars.CarService getCarService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
