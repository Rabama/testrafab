package es.tatanca.logistics.entities.Truck;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityRepository;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Order.Order;
import es.tatanca.logistics.entities.Order.OrderRepository;
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
public class TruckServiceImpl implements TruckService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CityRepository  cityRepo;
    private final TruckRepository truckRepo;
    private final OrderRepository orderRepo;


    // *****************************************************************************************************************
    @Override
    public Long saveTruck(Truck truck) {
        log.debug("TruckServiceImpl.saveTruck: truck.getNumber() = " + truck.getNumber());

        if (truck.getId() != null) {
            log.debug("saveTruck: Is not a new truck (" + truck.getId() + ").");

            Optional<Truck> opt = truckRepo.findById(truck.getId());
            if (opt.isEmpty()) {
                throw new RuntimeException("Error: There isn't a saved truck with id " + truck.getId()+".");
            }
            Truck pre = opt.get();

            if (!orderRepo.getOrderTruckNotCompleted(pre).equals(Collections.emptyList())) {
                throw new RuntimeException("Error: You can't modify status of a truck assigned to a non completed order.");
            }

            if ( (pre.getStatus() != truck.getStatus()) || (pre.getCity() != truck.getCity()) ) {
                log.debug("saveTruck: Status or City changed.");

                List<Order> orderList = orderRepo.getOrderTruckNotCompleted(truck);
                log.debug("saveTruck: orderList.size() = " + orderList.size());
                if (orderList.size() > 0) {
                    throw new RuntimeException("Error: The truck is in non completed order.");
                }
            }
        }

        if (truck.getNumber() == null) {
            throw new RuntimeException("Error: The truck number is null.");
        }
        if (truck.getNumber().equals("")) {
            throw new RuntimeException("Error: The truck number is empty.");
        }

        if (truck.getCapacity() == null) {
            throw new RuntimeException("Error: The truck capacity is null.");
        }
        if (truck.getCapacity() <= 0) {
            throw new RuntimeException("Error: The truck capacity is zero or lower.");
        }

        if (truck.getStatus() == null) {
            throw new RuntimeException("Error: The truck status is null.");
        }

        if (truck.getCity() != null) {

            Optional<City> opt = cityRepo.findById(truck.getCity().getId());
            if (opt.isEmpty()) {
                throw new RuntimeException("Error: The truck city is not valid.");

            }


        }

        truck = truckRepo.save(truck);
        return truck.getId();
    }

    // *****************************************************************************************************************
    public void delTruck(Truck truck) {
        log.debug("TruckServiceImpl.delTruck: " + truck.getNumber());

        if (!orderRepo.getOrderTruckNotCompleted(truck).equals(Collections.emptyList())) {
            throw new RuntimeException("Error: You can't modify status of a truck assigned to a non completed order.");
        }

        truckRepo.delete(truck);
    }


    // *****************************************************************************************************************
    @Override
    public Truck findById(Long id) {
        log.debug("TruckServiceImpl.findById " + id);
        Truck truck = null;
        Optional<Truck> opt = truckRepo.findById(id);
        if (opt.isPresent()) { truck = opt.get(); }
        return truck;
    }

    // *****************************************************************************************************************
    @Override
    public List<Truck> findAll() {
        log.debug("TruckServiceImpl.findAll");
        return truckRepo.findAll();
    }

    // *****************************************************************************************************************
    @Override
    public List<Truck> getLikeByCampo(String campo, String valor) {
        log.debug("TruckServiceImpl.getLikeByCampo campo/valor " + campo + "/" + valor);
        return switch(campo) {
            case "number" -> truckRepo.getLikeNumber(valor);
            default -> null;
        };
    }

    // *****************************************************************************************************************
    @Override
    public List<Truck> getEqualStatus(TruckStatus valor) {
        log.debug("TruckServiceImpl.getEqualStatus " + valor);
        return truckRepo.getEqualStatus(valor);
    }


    // *****************************************************************************************************************
    @Override
    public List<Truck> getLikeNumberStatus(String valor1, TruckStatus valor2) {
        log.debug("getLikeNumberStatus  number: " + valor1 + " and status: "+ valor2.toString());
        return truckRepo.getLikeNumberStatus(valor1, valor2);
    }


    // *****************************************************************************************************************
    public List<Truck> getMultiple(HashMap<String, Object> conditions) {
        log.debug("getMultiple: In.");

        CriteriaBuilder cb         = entityManager.getCriteriaBuilder();
        CriteriaQuery<Truck> query = cb.createQuery(Truck.class);
        Root<Truck> root           = query.from(Truck.class);

        List<Predicate> predicates = new ArrayList<>();

        if (conditions.size() > 0) {
            conditions.forEach((field, value) -> {
                log.debug("getMultiple: field " + field + " / value " + value);
                switch (field) {
                    case "number"   -> predicates.add(cb.like(root.get(field), "%" + (String) value + "%"));
                    case "status"   -> predicates.add(cb.equal(root.get(field), (TruckStatus) value));
                    case "city"     -> predicates.add(cb.equal(root.get(field), (City) value));
                    case "capacity" -> predicates.add(cb.greaterThanOrEqualTo(root.get(field), (Float) value));
                } // switch
            });

            query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        } else {
            query.select(root);
        }

        log.debug("getMultiple: Out.");
        return entityManager.createQuery(query).getResultList();
    } // getMultiple

}




