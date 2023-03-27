package es.tatanca.logistics.entities.Cargo;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.logistics.entities.City.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class CargoServiceImpl implements CargoService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CargoRepository cargoRepo;


    @Override
    public Long saveCargo(Cargo cargo) {
        log.debug("CargoServiceImpl.saveCargo: cargo.getName() = " + cargo.getName());
        cargo = cargoRepo.save(cargo);
        return cargo.getId();
    }

    public void delCargo(Cargo cargo) {
        if ((cargo.getStatus() != CargoStatus.READY) && (cargo.getStatus() != CargoStatus.ASSIGNED)) {
            throw new RuntimeException("Error: Cargo has already been shipped or is delivered");
        }

        cargoRepo.delete(cargo);
    }

    @Override
    public Cargo findById(Long id) {
        log.debug("findById " + id);
        Cargo cargo = null;
        Optional<Cargo> opt = cargoRepo.findById(id);
        if (opt.isPresent()) { cargo = opt.get(); }
//        log.debug("findById = " + cargo);
        return cargo;
    }

    @Override
    public List<Cargo> findAll() {
        log.debug("CargoServiceImpl.findAll");
        return cargoRepo.findAll();
    }


    @Override
    public List<Cargo> getLikeByCampo(String campo, String valor) {
        log.debug("CargoServiceImpl.getLikeByCampo campo/valor " + campo + "/" + valor);
        return switch(campo) {
            case "name" -> cargoRepo.getLikeName(valor);
            default -> null;
        };
    }
    @Override
    public List<Cargo> getLikeName(String valor) {
        log.debug("CargoServiceImpl.getLikeName " + valor);
        return cargoRepo.getLikeName(valor);
    }


    @Override
    public List<Cargo> getEqualStatus(CargoStatus valor) {
        log.debug("getEqualStatus " + valor);
        return cargoRepo.getEqualStatus(valor);
    }

    public List<Cargo> getEqualStatusCity(CargoStatus status, City city) {
        log.debug("getEqualStatusCity " + status + " / " + city);
        return cargoRepo.getEqualStatusCity(status, city);
    }

    @Override
    public List<Cargo> getLikeNameStatus(String valor1, CargoStatus valor2) {
        log.debug("getLikeNameStatus  name: " + valor1 + " and status: "+ valor2.toString());
        return cargoRepo.getLikeNameStatus(valor1, valor2);
    }

    public List<Cargo> getMultiple(HashMap<String, Object> conditions) {

        CriteriaBuilder cb         = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cargo> query = cb.createQuery(Cargo.class);
        Root<Cargo> root           = query.from(Cargo.class);

        List<Predicate> predicates = new ArrayList<>();

        if (conditions.size() > 0) {
            conditions.forEach((field, value) -> {
                switch (field) {
                    case "name" -> predicates.add(cb.like(root.get(field), "%" + (String) value + "%"));
                    case "status" -> predicates.add(cb.equal(root.get(field), (CargoStatus) value));
                    case "upcity" -> predicates.add(cb.equal(root.get(field), (City) value));
                    case "uncity" -> predicates.add(cb.equal(root.get(field), (City) value));
                    case "weight" -> predicates.add(cb.lessThanOrEqualTo(root.get(field), (Float) value));
                } // switch
            });

            query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        } else {
            query.select(root);
        }

        return entityManager.createQuery(query).getResultList();
    } // getMultiple

    // *****************************************************************************************************************
    public void setStatus(CargoStatus status, Long cargoId) {
        log.debug("setStatus: " + cargoId + " / " + status);
        cargoRepo.setStatus(status, cargoId);
    }

}