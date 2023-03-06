package es.tatanca.test.Entities.Truck;

import es.tatanca.test.Entities.City.City;

import java.util.List;

public interface TruckService {


    public Truck update(final Truck truck);

    Long saveTruck(Truck truck);
    public List<Truck> getLikeNumber(String valor);

    List<Truck> findAll();
    Truck getEqualByCampo(String campo, String valor);
    List<Truck> getLikeByCampo(String campo, String valor);


}
