package es.tatanca.test.Entities.Cargo;

import java.util.List;

public interface CargoService {


    void saveCargo(Cargo cargo);

    List<Cargo> getAllCargo();

    Cargo getEqualByCampo(String campo, String valor);

    List<Cargo> getLikeByCampo(String campo, String valor);


}
