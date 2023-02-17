package es.tatanca.test.Entities.Driver;

import java.util.List;

public interface DriverService {

    void saveDriver(Driver driver);

    List<Driver> getAllDriver();

    Driver getEqualByCampo(String campo, String valor);

    List<Driver> getLikeByCampo(String campo, String valor);

}
