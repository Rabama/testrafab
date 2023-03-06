package es.tatanca.test.Entities.Cargo;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.test.Entities.Cargo.Cargo;
import es.tatanca.test.Entities.Cargo.CargoRepository;
import es.tatanca.test.Entities.Cargo.CargoService;
import es.tatanca.test.Entities.City.City;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CargoServiceImpl implements CargoService {

//    @Autowired
    private final CargoRepository cargoRepo;

    @Override
    public Cargo create(final Cargo cargo) {
        return cargoRepo.save(cargo);
    };

    @Override
    @Transactional
    public Cargo update(final Cargo cargo) {
        final Cargo updatableCargo = cargoRepo.findById(cargo.getId())
                .orElseThrow(NullPointerException::new);
        updatableCargo.setName(cargo.getName());
        return updatableCargo;
    };

    public Long saveCargo(Cargo cargo) {
        System.out.println("CargoServiceImpl.saveCargo: cargo.getName() = " + cargo.getName());
        cargo = cargoRepo.save(cargo);
        return cargo.getId();
    };

    public Cargo findById(Long id) {
        Cargo cargo = null;
        Optional<Cargo> opt = cargoRepo.findById(id);
        if (opt.isPresent()) { cargo = opt.get(); }
        return cargo;
    };

    @Override
    public List<Cargo> findAll() {
        List<Cargo> cargoList = cargoRepo.findAll();
        return cargoList;
    }


    public void updateCity(Cargo cargo, City city) {

    }

//    public void updateCity(Cargo cargo) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        em.persist(cargo);
//        em.getTransaction().commit();
//    }


    @Override
    public List<Cargo> getLikeByCampo(String campo, String valor) {
        System.out.println("getLikeByCampo campo/valor " + campo + "/" + valor);
        List<Cargo> cargoList = null;
        switch(campo) {
            case "name" -> { cargoList = cargoRepo.getLikeName(valor); }
            default -> { }
        }
        return cargoList;
    }


    public List<Cargo> getLikeName(String valor) {
        System.out.println("getLikeName " + valor);
        List<Cargo> cargoList = cargoRepo.getLikeName(valor);
        return cargoList;
    }

    public List<Cargo> getEqualStatus(CargoStatus valor) {
        System.out.println("getEqualStatus " + valor);
        List<Cargo> cargoList = cargoRepo.getEqualStatus(valor);
        return cargoList;
    }

    public List<Cargo> getLikeNameStatus(String valor1, CargoStatus valor2) {
        System.out.println("getLikeNameStatus  name: " + valor1 + " and status: "+ valor2.toString());
        List<Cargo> cargoList = cargoRepo.getLikeNameStatus(valor1, valor2);
        return cargoList;
    }


}