package es.tatanca.test.Entities.City;

import java.util.List;

public interface CityService {


    Long saveCity(City city) throws Exception;

    public List<City> getLikeName(String valor);

    List<City> findAll();

    City getEqualByCampo(String campo, String valor);

    List<City> getLikeByCampo(String campo, String valor);


}
