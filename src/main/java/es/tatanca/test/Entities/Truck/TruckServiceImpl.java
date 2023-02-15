package es.tatanca.test.Entities.Truck;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.test.Entities.Truck.Truck;
import es.tatanca.test.Entities.Truck.TruckRepository;
import es.tatanca.test.Entities.Truck.TruckService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

//    @Autowired
    private final TruckRepository truckRepo;

    TruckServiceImpl(TruckRepository truckRepo) {
        this.truckRepo = truckRepo;
    }

    @Override
    public void saveTruck(Truck truck) {
        truckRepo.save(truck);
    }

    @Override
    public List<Truck> getAllTruck() {
        return truckRepo.findAll();
    }

    @Override
    public Truck getEqualByCampo(String campo, String valor) {
        Truck truck = null;
        switch (campo) {
            case "id" -> truck = truckRepo.getEqualById(valor);
            case "number" -> truck = truckRepo.getEqualByNumber(valor);
            case "capacity" -> truck = truckRepo.getEqualByCapacity(valor);
            case "status" -> truck = truckRepo.getEqualByStatus(valor);
            case "CityId" -> truck = truckRepo.getEqualByCityId(valor);
            default -> {
            }
        }
        return truck;
    }

    @Override
    public List<Truck> getLikeByCampo(String campo, String valor) {
        List<Truck> truckList = null;
        switch (campo) {
            case "number" -> truckList = truckRepo.getLikeByNumber(valor);
            case "status" -> truckList = truckRepo.getLikeByStatus(valor);
            default -> {
            }
        }
        return truckList;
    }



}
