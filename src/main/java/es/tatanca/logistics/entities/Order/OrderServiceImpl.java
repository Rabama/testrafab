package es.tatanca.logistics.entities.Order;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoServiceImpl;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.Credentials.Credentials;
import es.tatanca.logistics.entities.Credentials.CredentialsServiceImpl;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Driver.DriverServiceImpl;
import es.tatanca.logistics.entities.Driver.DriverStatus;
import es.tatanca.logistics.entities.Truck.Truck;
import es.tatanca.logistics.entities.Truck.TruckStatus;
import es.tatanca.logistics.entities.Waypoint.Waypoint;
import es.tatanca.logistics.entities.Waypoint.WaypointRepository;
import es.tatanca.logistics.entities.Waypoint.WaypointServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
//@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final DriverServiceImpl driverServiceImpl;
    private final OrderRepository orderRepo;
//    private final TruckServiceImpl truckServiceImpl;
    private final CredentialsServiceImpl userServiceImpl;
    private final WaypointServiceImpl waypointServiceImpl;
    private final CargoServiceImpl cargoServiceImpl;

    private final WaypointRepository waypointRepo;

    // *****************************************************************************************************************
    public Credentials getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Credentials user;
        try {
            user = userServiceImpl.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Username is not unique");
        }
        return user;
    } // getCurrentUser


    // *****************************************************************************************************************
    public Order findById(Long id) {
        log.debug("findById " + id);
        Order order = null;
        Optional<Order> opt = orderRepo.findById(id);
        if (opt.isPresent()) { order = opt.get(); }
        return order;
    }

    // *****************************************************************************************************************
    @Override
    public List<Order> findAll() {
        List<Order> orderList = orderRepo.findAll();
        return orderList;
    }
/*
    // *****************************************************************************************************************
    public List<Order> getEqualId(Order order) {
        log.debug("getEqualId. Order " + order.getId());

        List<Order> orderList = Collections.emptyList();

        Credentials user = getCurrentUser();
        if (user != null) {
            log.debug("user != null");
            Driver driver = driverServiceImpl.getEqualByCampo("username", user.getUsername());
            if (driver != null) {
                log.debug("driver != null");
                if (user.isAdmin() || user.isEmployee()) {
                    log.debug("User is not a driver");
                    orderList = new ArrayList<>();
                    orderList.add(orderRepo.getEqualId(valor);
                } else { // user.isDriver
                    log.debug("User is a driver");
                    orderList = orderRepo.getEqualIdDriver(valor, driver);
                }
            } else {
                log.debug("driver == null");
                orderList = orderRepo.getEqualId(order);
            }
        }
        return orderList;
    }
*/
    // *****************************************************************************************************************
    //    public List<Order> getEqualCompleted(Order.EnumCompleted valor) {
    public List<Order> getEqualCompleted(OrderCompleted valor) {
        log.debug("getEqualCompleted " + valor);

        List<Order> orderList = Collections.emptyList();
        Credentials user = getCurrentUser();
        if (user != null) {
            log.debug("getEqualCompleted. User: " + user.getUsername());
            Driver driver = driverServiceImpl.getEqualByUsername(user.getUsername());
            if (driver != null) {
                log.debug("getEqualCompleted. Driver: " + driver.getName());
                if (user.isAdmin() || user.isEmployee()) {
                    log.debug("getEqualCompleted. Admin or employee");
                    orderList = orderRepo.getEqualCompleted(valor);
                } else { // user.isDriver
                    log.debug("getEqualCompleted. Driver");
                    if (valor == OrderCompleted.YES) {
                        orderList = orderRepo.getEqualYesCompletedDriver(driver);
                    } else {
                        orderList = orderRepo.getEqualNotCompletedDriver(driver);

                    }
                }
            } else {
                log.debug("getEqualCompleted. Admin or employee");
                orderList = orderRepo.getEqualCompleted(valor);
            }
        }

        return orderList;
    }

    // *****************************************************************************************************************
    public Long saveOrderFinal(Order order) {

        if (order.getCompleted() == OrderCompleted.YES) {
            throw new RuntimeException("Error: The order is just completed.");
        }

        List<Cargo> cargoList = orderRepo.getCargoNotCompleted(order);
        if (cargoList.size() > 0) {
            throw new RuntimeException("There ara cargos not delivered.");
        }

        order.setCompleted(OrderCompleted.YES);

        Order savedOrder = orderRepo.save(order);

        return savedOrder.getId();
    } // saveOrderSimple

    // *****************************************************************************************************************
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long saveOrder(Order order, Truck truck, Driver driver1, Driver driver2, Driver driver3, List<Waypoint> waypointList) {


        if (order.getId() != null) {
            Optional<Order> opt = orderRepo.findById(order.getId());
            if (opt.isEmpty()) {
                throw new RuntimeException("Error: Can't find the order");
            }
            Order oldOrder = opt.get();

            if (oldOrder.getCompleted() == OrderCompleted.YES) {
                throw new RuntimeException("Error: The order was just completed");
            }
        }

        // The order can't be completed.
        if (order.getCompleted() == OrderCompleted.YES) {
            throw new RuntimeException("Error: The order can't be completed.");
        }

        order.setCompleted(OrderCompleted.NO);

        // Truck
        if (truck == null) {
            throw new RuntimeException("Error: The truck can't be null.");
        }

        List<Order> orderNC = orderRepo.getOrderByTruckAndCompleted(truck, OrderCompleted.NO);
        int max = 0; if (order.getId() != null) { max++; }
        if (orderNC.size() > max) {
            log.debug("saveOrder: The truck is assigned to not completed order.");
            throw new RuntimeException("Error: The truck is assigned to not completed order.");
        }

        // El camión tiene que estar OK.
        if (truck.getStatus() == TruckStatus.NOK) {
            log.debug("saveOrder: The truck is not OK.");
            throw new RuntimeException("Error: The truck is not OK.");
        }
        order.setTruck(truck);


        //
        if (driver1 == null) {
            throw new RuntimeException("Error: The driver1 can't be null.");
        }

//            List<Order> orderNC = orderRepo.getOrderByDriverAndNotCompleted(driver1);
        orderNC = orderRepo.getEqualNotCompletedDriver(driver1);
        max = 0; if (order.getId() != null) { max++; }
        if (orderNC.size() > max) {
            log.debug("saveOrder: The driver1 is assigned to not completed order.");
            throw new RuntimeException("Error: The driver1 is assigned to not completed order.");
        }
        if (driver1.getStatus() == DriverStatus.REST) {
            log.debug("saveOrder: The driver1 is resting.");
            throw new RuntimeException("Error: The driver1 is resting.");
        }
        order.setDriver1(driver1);

        if (driver2 != null) {
//            List<Order> orderNC = orderRepo.getOrderByDriverAndNotCompleted(driver2);
            orderNC = orderRepo.getEqualNotCompletedDriver(driver2);
            max = 0; if (order.getId() != null) { max++; }
            if (orderNC.size() > max) {
                log.debug("saveOrder: The driver2 is assigned to not completed order.");
                throw new RuntimeException("Error: The driver2 is assigned to not completed order.");
            }
            if (driver2.getStatus() == DriverStatus.REST) {
                log.debug("saveOrder: The driver2 is resting.");
                throw new RuntimeException("Error: The driver2 is resting.");
            }
            order.setDriver2(driver2);
        }

        if (driver3 != null) {
//            List<Order> orderNC = orderRepo.getOrderByDriverAndNotCompleted(driver3);
            orderNC = orderRepo.getEqualNotCompletedDriver(driver3);
            max = 0; if (order.getId() != null) { max++; }
            if (orderNC.size() > max) {
                log.debug("saveOrder: The driver3 is assigned to not completed order.");
                throw new RuntimeException("Error: The driver3 is assigned to not completed order.");
            }
            if (driver3.getStatus() == DriverStatus.REST) {
                log.debug("saveOrder: The driver3 is resting.");
                throw new RuntimeException("Error: The driver3 is resting.");
            }
            order.setDriver3(driver3);
        }


        /*if (waypointList == null) {
            throw new RuntimeException("Error: The waypoint list can't be null.");
        }
        if (waypointList.size() == 0) {
            throw new RuntimeException("Error: The waypoint list can't be empty.");
        }*/



        // Saving...
        orderRepo.save(order);

        //
        if (waypointList != null) {
            waypointList.forEach(w -> {
                Cargo cargo = w.getCargo();
                if (cargo.getStatus() == CargoStatus.READY) {
                    cargo.setStatus(CargoStatus.ASSIGNED);
                }
                cargoServiceImpl.saveCargo(cargo);
            });

            //
//        waypointList.forEach(w -> waypointServiceImpl.save(w); );
            waypointList.forEach(waypointServiceImpl::save);
        }

        //
        return order.getId();

    } // saveOrder

    // *****************************************************************************************************************
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delOrder(Order order, List<Waypoint> waypointList) {

        if (order.getId() != null) {
            Optional<Order> opt = orderRepo.findById(order.getId());
            if (opt.isEmpty()) {
                throw new RuntimeException("Error: Can't find the order");
            }
        }

        // The order can't be completed.
        if (order.getCompleted() == OrderCompleted.YES) {
            throw new RuntimeException("Error: You can't delete a completed order.");
        }

    // Delete waypoints.
        if (waypointList != null) {
            waypointList.forEach(w -> {
                Cargo cargo = w.getCargo();
                if ((cargo.getStatus() != CargoStatus.READY) && (cargo.getStatus() != CargoStatus.ASSIGNED)) {
                    throw new RuntimeException("Error: You can't delete an order that has started.");
                }
                waypointRepo.delete(w);
            });
        }

        // Delete order.
        orderRepo.delete(order);

    } // delOrder


    // *****************************************************************************************************************
    // DriverExt
    public Order findOrderDriverExt(Driver driver) {
        log.debug("findOrderDriverExt " + driver.getName());

//        List<Order> orderList = orderRepo.getOrderByDriverAndNotCompleted(driver);
        List<Order> orderList = orderRepo.getEqualNotCompletedDriver(driver);
        if (orderList.size() > 1) {
            log.debug("findOrderDriverExt: The driver is in multiple orders not completed.");
            throw new RuntimeException("The driver is in multiple orders not completed.");
        }
        log.debug("findOrderDriverExt " + orderList.size() + " records found.");
        if (orderList.size() == 0) {
            return null;
        } else {
            return orderList.get(0);
        }

    } // findOrderDriverExt

    // *****************************************************************************************************************
    public void  deleteCargo(Cargo cargo) {

        cargo.setStatus(CargoStatus.READY);
        cargoServiceImpl.saveCargo(cargo);

        waypointServiceImpl.deleteByCargo(cargo);
    }




}