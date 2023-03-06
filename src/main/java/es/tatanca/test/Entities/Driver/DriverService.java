package es.tatanca.test.Entities.Driver;

import es.tatanca.test.Entities.City.City;

import java.util.List;

public interface DriverService {

    public Driver update(final Driver driver);

    Long saveDriver(Driver driver);

    public List<Driver> getLikeName(String valor);

    List<Driver> findAll();

    Driver getEqualByCampo(String campo, String valor);

    List<Driver> getLikeByCampo(String campo, String valor);

}
