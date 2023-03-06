package es.tatanca.test.Entities.Cargo;

import java.util.List;

public interface CargoService {


    Cargo create(final Cargo cargo);

    Cargo update(final Cargo cargo);

//    public Long saveTruck(Truck truck);

    public List<Cargo> findAll();

    public List<Cargo> getLikeByCampo(String campo, String valor);


}
