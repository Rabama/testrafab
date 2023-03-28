package es.tatanca.logistics.entities.Waypoint;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoServiceImpl;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Driver.DriverServiceImpl;
import es.tatanca.logistics.entities.Order.Order;
import es.tatanca.logistics.entities.Order.OrderServiceImpl;
import es.tatanca.logistics.entities.Truck.TruckServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static es.tatanca.logistics.entities.Cargo.CargoStatus.DELIVERED;
import static es.tatanca.logistics.entities.Cargo.CargoStatus.SHIPPED;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class WaypointServiceImpl implements WaypointService {

    private final WaypointRepository waypointRepo;
    private final DriverServiceImpl driverServiceImpl;
    private final TruckServiceImpl truckServiceImpl;

    private final double maxHours = 176D;

    // *****************************************************************************************************************
    @Transactional
    public Long save(Waypoint waypoint) {
        waypoint = waypointRepo.save(waypoint);
        return waypoint.getId();
    }

    // *****************************************************************************************************************
    @Transactional
    public Long saveWaypoint(Waypoint waypoint, Long driver1Id, Long driver2Id, Long driver3Id, Float hours, Cargo cargo) {

        waypoint.setDriver1(driverServiceImpl.findById(driver1Id));

        if (driver2Id == null) {
            waypoint.setDriver2(null);
        } else {
            waypoint.setDriver2(driverServiceImpl.findById(driver2Id));
        }

        if (driver3Id == null) {
            waypoint.setDriver3(driverServiceImpl.findById(driver1Id));
        } else {
            waypoint.setDriver3(driverServiceImpl.findById(driver3Id));
        }


        waypoint.setHours(hours);

        // Driver's worked hours.
        Double prevWorkedHours = driverServiceImpl.findById(driver1Id).getWorkedHours();
        if (driverServiceImpl.findById(driver1Id).getWorkedHours() + hours > maxHours) {
            throw new RuntimeException("First driver's worked hours exceed the limit after this order.");
        }
        driverServiceImpl.findById(driver1Id).setWorkedHours(prevWorkedHours + hours);

        if (driver2Id != null) {
            prevWorkedHours = driverServiceImpl.findById(driver2Id).getWorkedHours();
            if (driverServiceImpl.findById(driver2Id).getWorkedHours() + hours > maxHours) {
                throw new RuntimeException("Second driver's worked hours exceed the limit after this order.");
            }
            driverServiceImpl.findById(driver2Id).setWorkedHours(prevWorkedHours + hours);
        }

        if ( (driver3Id != null) && (driver3Id != driver1Id) && (driver3Id != driver2Id) ) {
            prevWorkedHours = driverServiceImpl.findById(driver3Id).getWorkedHours();
            if (driverServiceImpl.findById(driver3Id).getWorkedHours() + hours > maxHours) {
                throw new RuntimeException("Load and unload worker's worked hours exceed the limit after this order.");
            }
            driverServiceImpl.findById(driver3Id).setWorkedHours(prevWorkedHours + hours);
        }

        // Driver's city change.
        if (waypoint.isUpload()) {
            driverServiceImpl.findById(driver1Id).setCity(waypoint.getCargo().getUpcity());
            if (driver2Id != null) {
                driverServiceImpl.findById(driver2Id).setCity(waypoint.getCargo().getUpcity());
            }
            if (driver3Id != null) {
                driverServiceImpl.findById(driver3Id).setCity(waypoint.getCargo().getUpcity());
            }
        } else {
            driverServiceImpl.findById(driver1Id).setCity(waypoint.getCargo().getUncity());
            if (driver2Id != null) {
                driverServiceImpl.findById(driver2Id).setCity(waypoint.getCargo().getUncity());
            }
            if (driver3Id != null) {
                driverServiceImpl.findById(driver3Id).setCity(waypoint.getCargo().getUncity());
            }
        }

        // Truck's city change.
        if (waypoint.isUpload()) {
          waypoint.getOrder().getTruck().setCity(waypoint.getCargo().getUpcity());
        } else {
            waypoint.getOrder().getTruck().setCity(waypoint.getCargo().getUncity());
        }
        // Cargo status modifier.
        if (waypoint.isUpload()) {
            cargo.setStatus(CargoStatus.SHIPPED);
        } else {
            cargo.setStatus(CargoStatus.DELIVERED);
        }

 //       Order order = waypoint.getOrder();


        waypoint = waypointRepo.save(waypoint);
        return waypoint.getId();
    }

    // *****************************************************************************************************************
    public Waypoint findById(Long id) {
        Waypoint waypoint = null;
        Optional<Waypoint> opt = waypointRepo.findById(id);
        if (opt.isPresent()) { waypoint = opt.get(); }
        return waypoint;
    }

    // *****************************************************************************************************************
    @Override
    public List<Waypoint> findAll() {
        List<Waypoint> waypointList = waypointRepo.findAll();
        return waypointList;
    }

    // *****************************************************************************************************************
    public List<Waypoint> findByOrder(Order order) {
        List<Waypoint> waypointList = waypointRepo.findByOrder(order);
        return waypointList;
    }

    // *****************************************************************************************************************
    public List<Waypoint> findByCargo(Cargo cargo) {
        List<Waypoint> waypointList = waypointRepo.findByCargo(cargo);
        return waypointList;
    }

    // *****************************************************************************************************************
    public Waypoint findByOrderCargoUpload(Order order, Cargo cargo, Boolean upload) {
        return waypointRepo.findByOrderCargoUpload(order, cargo, upload);
    }

    // *****************************************************************************************************************
    @Override
    public Waypoint getEqualByCampo(String campo, String valor) {
        Waypoint waypoint = waypointRepo.getEqualBy(campo, valor);
        return waypoint;
    }

    // *****************************************************************************************************************
    public Waypoint getByZOrder(Order order, int zorder) {
        return waypointRepo.getByZOrder(order, zorder);
    }

    // *****************************************************************************************************************
    public void deleteByOrder(Long id) {
        waypointRepo.deleteByOrder(id);
    }

    // *****************************************************************************************************************
    public void deleteByCargo(Cargo cargo) { waypointRepo.deleteByCargo(cargo); }



}
