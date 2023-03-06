package es.tatanca.test.Entities.Truck;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.test.Entities.Truck.Truck;
import es.tatanca.test.Entities.Truck.TruckRepository;
import es.tatanca.test.Entities.Truck.TruckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TruckServiceImpl implements TruckService {


//      @Autowired
    private final TruckRepository truckRepo;

//    TruckServiceImpl(TruckRepository truckRepo) {
//        this.truckRepo = truckRepo;
//    }

        //    @Override
//    @Transactional
//    public Truck update(final Truck truck) {
//        final Truck updatableTruck = truckRepo.findById(truck.getId())
//                .orElseThrow(NullPointerException::new);
//        updatableTruck.setNumber(truck.getNumber());
//        updatableTruck.setCapacity(truck.getCapacity());
//        updatableTruck.setStatus(truck.getStatus());
//        updatableTruck.setCity(truck.getCity());
//        return updatableTruck;
//    };
        @Override
        @Transactional
        public Truck update(final Truck truck) {
            System.out.println("TruckServiceImpl.update1: truck.getId() = " + truck.getId());
            final Truck updatableTruck = truckRepo.findById(truck.getId())
                    .orElseThrow(NullPointerException::new);
            updatableTruck.setNumber(truck.getNumber());
            updatableTruck.setCapacity(truck.getCapacity());
            updatableTruck.setStatus(truck.getStatus());
            updatableTruck.setCity(truck.getCity());
            System.out.println("TruckServiceImpl.update2: truck.getId() = " + truck.getId());
            return updatableTruck;
        };

    public Long saveTruck(Truck truck) {
        System.out.println("TruckServiceImpl.saveTruck: truck.getNumber() = " + truck.getNumber());
        truck = truckRepo.save(truck);
        return truck.getId();
    };

    @Override
    public List<Truck> getLikeNumber(String valor) {
        System.out.println("getLikeNumber " + valor);
        List<Truck> truckList = truckRepo.getLikeNumber(valor);
        return truckList;
    }

    //    public List<Truck> getEqualStatus(Truck.EnumStatus valor) {
    public List<Truck> getEqualStatus(TruckStatus valor) {
        System.out.println("getEqualStatus " + valor);
        List<Truck> truckList = truckRepo.getEqualStatus(valor);
        return truckList;
    }

    //    public List<Truck> getLikeNumberStatus(String valor1, Truck.EnumStatus valor2) {
    public List<Truck> getLikeNumberStatus(String valor1, TruckStatus valor2) {
        System.out.println("getLikeNumberStatus  number: " + valor1 + " and status: "+ valor2.toString());
        List<Truck> truckList = truckRepo.getLikeNumberStatus(valor1, valor2);
        return truckList;
    }


    public Truck findById(Long id) {
        System.out.println("findById " + id);
        Truck truck = null;
        Optional<Truck> opt = truckRepo.findById(id);
        if (opt.isPresent()) { truck = opt.get(); }
        return truck;
    };

    @Override
    public List<Truck> findAll() {
        List<Truck> truckList = truckRepo.findAll();
        return truckList;
    }

    @Override
    public Truck getEqualByCampo(String campo, String valor) {
        Truck truck = truckRepo.getEqualBy(campo, valor);
        return truck;
    }

    @Override
    public List<Truck> getLikeByCampo(String campo, String valor) {
        System.out.println("getLikeByCampo campo/valor " + campo + "/" + valor);
        List<Truck> truckList = null;
        switch(campo) {
            case "number" -> { truckList = truckRepo.getLikeNumber(valor); }
            default -> { }
        }
        return truckList;
    }

}




