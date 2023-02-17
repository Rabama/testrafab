package es.tatanca.test.Entities.Distance;


import java.util.List;

public interface DistanceService {
    void saveDistance(Distance distance);

    List<Distance> getAllDistance();

    Distance getEqualByCampo(String campo, String valor);
}
