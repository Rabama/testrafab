package es.tatanca.logistics.entities.Driver;

import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.Order.Order;
import es.tatanca.logistics.entities.Order.OrderRepository;
import es.tatanca.logistics.entities.Waypoint.Waypoint;
import es.tatanca.logistics.entities.Waypoint.WaypointRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class DriverServiceImpl implements DriverService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final DriverRepository driverRepo;
    private final OrderRepository orderRepo;
    private final WaypointRepository waypointRepo;

    public Long saveDriver(Driver driver) {
        log.debug("saveDriver: In (" + driver.getName() + ").");

        if (driver.getId() != null) {
            log.debug("saveDriver: Is not a new driver (" + driver.getId() + ").");

            Optional<Driver> opt = driverRepo.findById(driver.getId());
            if (opt.isEmpty()) {
                throw new RuntimeException("Error: There isn't a saved driver with id " + driver.getId()+".");
            }
            Driver pre = opt.get();

            if ( (pre.getStatus() != driver.getStatus()) || (pre.getCity() != driver.getCity()) ) {
                log.debug("saveDriver: Status or City changed.");

                List<Order> orderList = orderRepo.getOrderDriverNotCompleted(driver);
                log.debug("saveDriver: orderList.size() = " + orderList.size());
                if (orderList.size() > 0) {
                    throw new RuntimeException("Error: The driver is in non completed order.");
                }
            }
        }

        driver = driverRepo.save(driver);

        log.debug("saveDriver: Out (" + driver.getId() + ").");
        return driver.getId();
    }

    public void delDriver(Driver driver) {
        log.debug("driverServiceImpl.delDriver: " + driver.getId() );

        List<Waypoint> waypointUploadList;
        waypointUploadList = waypointRepo.findByDriverUpload(driver, Boolean.TRUE);
        if (waypointUploadList.size() > 0) {
            for (Waypoint waypoint : waypointUploadList) {
                if ((waypoint.getCargo().getStatus() != CargoStatus.SHIPPED) ||  (waypoint.getCargo().getStatus() != CargoStatus.DELIVERED)) {
                    throw new RuntimeException("Error: Driver is assigned to an unfinished upload.");
                }
            }
        }

        List<Waypoint> waypointUnloadList;
        waypointUnloadList = waypointRepo.findByDriverUpload(driver, Boolean.FALSE);
        if (waypointUnloadList.size() > 0) {
            for (Waypoint waypoint : waypointUnloadList) {
                if (waypoint.getCargo().getStatus() != CargoStatus.DELIVERED) {
                    throw new RuntimeException("Error: Driver is assigned to an unfinished unload.");
                }
            }
        }

        driverRepo.delete(driver);
    }


    @Override
    public List<Driver> getLikeName(String valor) {
        log.debug("getLikeName " + valor);
        List<Driver> driverList = driverRepo.getLikeName(valor);
        return driverList;
    }

    public List<Driver> getEqualStatus(DriverStatus valor) {
        log.debug("getEqualStatus " + valor);
        List<Driver> driverList = driverRepo.getEqualStatus(valor);
        return driverList;
    }

    public List<Driver> getLikeNumberStatus(String valor1, DriverStatus valor2) {
        log.debug("getLikeNumberStatus  number: " + valor1 + " and status: "+ valor2.toString());
        List<Driver> driverList = driverRepo.getLikeNumberStatus(valor1, valor2);
        return driverList;
    }

    public Driver findById(Long id) {
        log.debug("findById " + id);
        Driver driver = null;
        Optional<Driver> opt = driverRepo.findById(id);
        if (opt.isPresent()) { driver = opt.get(); }
        return driver;
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> driverList = driverRepo.findAll();
        return driverList;
    }

    @Override
    public Driver getEqualByCampo(String campo, String valor) {
        Driver driver = driverRepo.getEqualBy(campo, valor);
        return driver;
    }

    public Driver getEqualByUsername(String valor) {
        return driverRepo.getEqualByUsername(valor);
    }

    @Override
    public List<Driver> getLikeByCampo(String campo, String valor) {
        log.debug("getLikeByCampo campo/valor " + campo + "/" + valor);
        List<Driver> driverList = null;
        switch(campo) {
            case "name" -> driverList = driverRepo.getLikeName(valor);
            default -> { }
        }
        return driverList;
    }

    public List<Driver> getMultiple(HashMap<String, Object> conditions) {
        log.debug("getMultiple: In.");

        CriteriaBuilder cb          = entityManager.getCriteriaBuilder();
        CriteriaQuery<Driver> query = cb.createQuery(Driver.class);
        Root<Driver> root           = query.from(Driver.class);

        List<Predicate> predicates = new ArrayList<>();

        if (conditions.size() > 0) {
            conditions.forEach((field, value) -> {
                log.debug("getMultiple: field " + field + " / value " + value);
                switch (field) {
                    case "name"   -> predicates.add(cb.like(root.get(field), "%" + (String) value + "%"));
                    case "status" -> predicates.add(cb.equal(root.get(field), (DriverStatus) value));
                    case "city"   -> predicates.add(cb.equal(root.get(field), (City) value));
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