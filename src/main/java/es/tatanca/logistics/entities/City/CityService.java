package es.tatanca.logistics.entities.City;

import java.util.List;

public interface CityService {


    Long saveCity(City city);

    City findById(Long id);

    List<City> findAll();

    List<City> getLikeByCampo(String campo, String valor);

    List<City> getLikeName(String valor);


}
