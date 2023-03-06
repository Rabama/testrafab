package es.tatanca.test.Entities.Distance;


import java.util.List;

public interface DistanceService {
    Long saveDistance(Distance distance) throws Exception;

    List<Distance> findAll();

    Distance getEqualByCampo(String campo, String valor);

    List<Distance> getLikeByCampo(String campo, String valor);
}
