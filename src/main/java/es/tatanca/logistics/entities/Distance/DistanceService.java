package es.tatanca.logistics.entities.Distance;


import es.tatanca.logistics.entities.City.City;

import java.util.List;

public interface DistanceService {

    Long saveDistance(Distance distance) throws Exception;

    List<Distance> findAll();

    Float getDistance(City city0, City city1);

    List<Distance> getLessOrEqualByDistance(float distance);
}
