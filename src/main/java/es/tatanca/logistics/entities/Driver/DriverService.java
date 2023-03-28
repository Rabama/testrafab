package es.tatanca.logistics.entities.Driver;

import java.util.List;

public interface DriverService {

    Long saveDriver(Driver driver);

    public List<Driver> getLikeName(String valor);

    List<Driver> findAll();

    Driver getEqualByCampo(String campo, String valor);

    List<Driver> getLikeByCampo(String campo, String valor);
}
