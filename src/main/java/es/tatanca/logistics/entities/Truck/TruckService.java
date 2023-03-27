package es.tatanca.logistics.entities.Truck;

import java.util.List;

public interface TruckService {

    Long saveTruck(Truck truck);

    Truck findById(Long id);

    List<Truck> findAll();

    List<Truck> getLikeByCampo(String campo, String valor);

    List<Truck> getEqualStatus(TruckStatus valor);

    List<Truck> getLikeNumberStatus(String valor1, TruckStatus valor2);


}
