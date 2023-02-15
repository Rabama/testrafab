package es.tatanca.test.Entities.City;

import java.util.List;

public interface CityService {


    void saveCity(City city);

    List<City> getAllCity();

    City getEqualByCampo(String campo, String valor);

    List<City> getLikeByCampo(String campo, String valor);


}
