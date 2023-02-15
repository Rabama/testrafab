package es.tatanca.test.Entities.Truck;

import es.tatanca.test.Entities.City.City;

import java.util.List;

public interface TruckService {


    void saveTruck(Truck truck);

    List<Truck> getAllTruck();

    Truck getEqualByCampo(String campo, String valor);

    List<Truck> getLikeByCampo(String campo, String valor);


}
