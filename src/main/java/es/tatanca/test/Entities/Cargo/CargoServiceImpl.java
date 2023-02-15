package es.tatanca.test.Entities.Cargo;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.test.Entities.Cargo.Cargo;
import es.tatanca.test.Entities.Cargo.CargoRepository;
import es.tatanca.test.Entities.Cargo.CargoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoServiceImpl implements CargoService {

//    @Autowired
    private final CargoRepository cargoRepo;

    CargoServiceImpl(CargoRepository cargoRepo) {
        this.cargoRepo = cargoRepo;
    }

    @Override
    public void saveCargo(Cargo cargo) {
        cargoRepo.save(cargo);
    }

    @Override
    public List<Cargo> getAllCargo() {
        return cargoRepo.findAll();
    }

    @Override
    public Cargo getEqualByCampo(String campo, String valor) {
        Cargo cargo = null;
        switch (campo) {
            case "id" -> cargo = cargoRepo.getEqualById(valor);
            case "name" -> cargo = cargoRepo.getEqualByName(valor);
            case "weight" -> cargo = cargoRepo.getEqualByWeight(valor);
            case "status" -> cargo = cargoRepo.getEqualByStatus(valor);
            case "CityId0" -> cargo = cargoRepo.getEqualByCityId0(valor);
            case "CityId1" -> cargo = cargoRepo.getEqualByCityId1(valor);
            case "Adress0" -> cargo = cargoRepo.getEqualByAddress0(valor);
            case "Adress1" -> cargo = cargoRepo.getEqualByAddress1(valor);
            case "upload" -> cargo = cargoRepo.getEqualByUpload(valor);
            case "unload" -> cargo = cargoRepo.getEqualByUnload(valor);
            default -> {
            }
        }
        return cargo;
    }

    @Override
    public List<Cargo> getLikeByCampo(String campo, String valor) {
        List<Cargo> cargoList = null;
        switch (campo) {
            case "name" -> cargoList = cargoRepo.getLikeByName(valor);
            case "status" -> cargoList = cargoRepo.getLikeByStatus(valor);
            default -> {
            }
        }
        return cargoList;
    }



}
