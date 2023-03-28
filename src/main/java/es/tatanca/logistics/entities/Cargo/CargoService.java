package es.tatanca.logistics.entities.Cargo;

import java.util.List;

public interface CargoService {

    Long saveCargo(Cargo cargo);

    Cargo findById(Long id);

    List<Cargo> findAll();

    List<Cargo> getLikeByCampo(String campo, String valor);

    List<Cargo> getLikeName(String valor);

    List<Cargo> getEqualStatus(CargoStatus valor);

    List<Cargo> getLikeNameStatus(String valor1, CargoStatus valor2);

}
