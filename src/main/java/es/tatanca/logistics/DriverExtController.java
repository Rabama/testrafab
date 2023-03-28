package es.tatanca.logistics;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoServiceImpl;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.CargoOrder;
import es.tatanca.logistics.entities.Credentials.Credentials;
import es.tatanca.logistics.entities.Credentials.CredentialsServiceImpl;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Driver.DriverServiceImpl;
import es.tatanca.logistics.entities.Driver.DriverStatus;
import es.tatanca.logistics.entities.Order.Order;
import es.tatanca.logistics.entities.Order.OrderCompleted;
import es.tatanca.logistics.entities.Order.OrderServiceImpl;
import es.tatanca.logistics.entities.Truck.Truck;
import es.tatanca.logistics.entities.Waypoint.Waypoint;
import es.tatanca.logistics.entities.Waypoint.WaypointServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class DriverExtController {

    private final CargoServiceImpl cargoServiceImpl;
    private final DriverServiceImpl driverServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final CredentialsServiceImpl userServiceImpl;
    private final WaypointServiceImpl waypointServiceImpl;


    // *****************************************************************************************************************
    @GetMapping("/drivers/load/")
    public String getDriverExtPage(Model model) {
        log.debug("getDriverExtPage: In.");

        Credentials user;
        try {
            user = userServiceImpl.getCurrentUser();
            if (user == null) {
                log.error("Can't find user.");
                throw new RuntimeException("Error: Can't find user.");
            }
        } catch (Exception e) {
            log.error("Can't find user.");
            throw new RuntimeException("Error: Can't find user.");
        }
        log.debug("getDriverExtPage: User " + user.getUsername());

        model.addAttribute("user", user);

        Driver driver = driverServiceImpl.getEqualByUsername(user.getUsername());
        if (driver == null) {
            model.addAttribute("message", "Can't find user");
            log.error("Actual user is not a driver.");
            throw new RuntimeException("Error: Actual user is not a driver");
        }
        log.debug("getDriverExtPage: Driver " + driver.getName());

        model.addAttribute("driver", driver);
        model.addAttribute("statusValues", DriverStatus.values());

        log.debug("getDriverExtPage: Out.");
        return "drivers/driver";
    }

    // *****************************************************************************************************************
    @PostMapping("/drivers/save/")
    @ResponseBody
    public ResponseEntity<String> saveDriverExt(@RequestBody Map<String, String> reqParam) {
        log.debug("saveDriverExt: In.");

        String id       = reqParam.get("id");
        String name     = reqParam.get("name");
        String surname  = reqParam.get("surname");
        String workedh  = reqParam.get("workedhours");
        String status   = reqParam.get("status");
        String password = reqParam.get("password");

        log.debug("Id       : " + id);
        log.debug("Name     : " + name);
        log.debug("Surname  : " + surname);
        log.debug("WorkedH  : " + workedh);
        log.debug("Status   : " + status);
        log.debug("Password : " + password);

        // Tenemos que hacer las comprobaciones necesarias para asegurarnos de que el usuario tiene permiso para grabar este conductor.

        if (id.equals("")) {
            return new ResponseEntity<>("A empty id is not valid.", HttpStatus.BAD_REQUEST);
        }

        Credentials user;
        try {
            user = userServiceImpl.getCurrentUser();
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to get current user.", HttpStatus.BAD_REQUEST);
        }

        Driver driverq;
        try {
            driverq = driverServiceImpl.getEqualByUsername(user.getUsername());
        } catch (Exception e) {
            return new ResponseEntity<>("Driver not found for user "+user.getUsername()+".", HttpStatus.BAD_REQUEST);
        }

        if (!id.equals(driverq.getId().toString())) {
            return new ResponseEntity<>("You haven't permission to save this driver.", HttpStatus.BAD_REQUEST);
        }

        // El usuario es este conductor y el id coincide.

        Order order = orderServiceImpl.findOrderDriverExt(driverq);
        Boolean working = (order != null);

        Driver driver = new Driver();

        // El id no se puede cambiar.
        driver.setId(driverq.getId());

        // EL username no se puede cambiar.
        driver.setUsername(user.getUsername());

        driver.setName(name);

        driver.setSurname(surname);

        // Esto debería venir bien desde javascript, pero...
        try {
            driver.setWorkedHours(Double.valueOf(workedh));
        } catch (Exception e) {
            return new ResponseEntity<>("The worked hours are not correct.", HttpStatus.BAD_REQUEST);
        }

        if ( (driverq.getStatus() == DriverStatus.WORK) && working) {
            if (DriverStatus.fromLongName(status) == DriverStatus.REST) {
                return new ResponseEntity<>("You have an assigned order and can't change your status.", HttpStatus.BAD_REQUEST);
            }
        }
        driver.setStatus(DriverStatus.fromLongName(status));

        String savedId;
        try {
            savedId = driverServiceImpl.saveDriver(driver).toString();
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        Credentials userq = userServiceImpl.findById(user.getId());
        if (password.equals("")) {
            log.debug("userSave: password encripted (" + userq.getPassword() + ").");
            user.setPasswordEncripted(true);
            user.setPassword(userq.getPassword());
        } else {
            user.setPasswordEncripted(false);
            user.setPassword(password);
        }
        userServiceImpl.saveUser(user);

        log.debug("saveDriverExt: Out. Driver.id = "+driver.getId().toString()+" / Saved id = "+savedId);
//        return new ResponseEntity<>(savedId, HttpStatus.OK);
        return ResponseEntity.ok().body(savedId);
    } // saveDriver

    /*@PostMapping("/drivers/cargo/order/save/")
    @ResponseBody
    public ResponseEntity<String> saveCargoOrder(@RequestBody Map<String, String> reqParam, Model model) {
        log.debug("saveCargo");

        return ResponseEntity.ok();
    }*/


    // *****************************************************************************************************************
    @GetMapping("/drivers/order/load/")
    // queries.loadOrder
    public String getOrderDriverExtPageById(Model model) {
        log.debug("getOrderDriverExtPageById");

        // Las listas desplegables.
        model.addAttribute("completedValues", OrderCompleted.values());

        // ¿Hay orden activa para este driver?
        Credentials user;
        try {
            user = userServiceImpl.getCurrentUser();
            if (user == null) {
                log.error("Can't find user.");
                throw new RuntimeException("Error: Can't find user.");
            }
        } catch (Exception e) {
            log.error("Can't find user.");
            throw new RuntimeException("Error: Can't find user.");
        }

        Driver driver = driverServiceImpl.getEqualByUsername(user.getUsername());
        if (driver == null) {
            throw new RuntimeException("No driver");
        }

        Order order = orderServiceImpl.findOrderDriverExt(driver);
        if (order != null) {
            log.debug("Found order " + order.getId());

            model.addAttribute("orderid", order.getId());

            model.addAttribute("ordercompleted", order.getCompleted());

            // Y ahora los campos que son objetos o múltiples.
            Truck truck = order.getTruck();
            if (truck != null) {
                model.addAttribute("truckid", truck.getId());
                model.addAttribute("trucknumber", truck.getNumber());
            }

            // Drivers.
            Driver driver1 = order.getDriver1();
            if (driver1 != null) {
                model.addAttribute("driver1id", driver1.getId());
                model.addAttribute("driver1name", driver1.getName());
                model.addAttribute("driver1hours", driver1.getWorkedHours());
            }

            Driver driver2 = order.getDriver2();
            if (driver2 != null) {
                model.addAttribute("driver2id", driver2.getId());
                model.addAttribute("driver2name", driver2.getName());
                model.addAttribute("driver2hours", driver2.getWorkedHours());
            }

            Driver driver3 = order.getDriver3();
            if (driver3 != null) {
                model.addAttribute("driver3id", driver3.getId());
                model.addAttribute("driver3name", driver3.getName());
                model.addAttribute("driver3hours", driver3.getWorkedHours());
            }

            // Cargos.
            List<Waypoint> waypointList = waypointServiceImpl.findByOrder(order);

            int index = 1;
            List<CargoOrder> cargoOrderList = new ArrayList<>();
            for (Waypoint waypoint : waypointList) {
                CargoOrder cargoOrder = new CargoOrder(waypoint.getCargo(), waypoint);

//                cargoOrder.setId(String.valueOf(waypoint.getId()));
//
//                cargoOrder.setName(waypoint.getCargo().getName());
//                cargoOrder.setUpCity(waypoint.getCargo().getUpcity().getName());
//                cargoOrder.setUnCity(waypoint.getCargo().getUncity().getName());
//                cargoOrder.setWeight(String.valueOf(waypoint.getCargo().getWeight()));

                if (waypoint.isUpload()) {
                    cargoOrder.setUpload("UPLOAD");
                    cargoOrder.setUnload("");
                } else {
                    cargoOrder.setUpload("");
                    cargoOrder.setUnload("UNLOAD");
                }

//                cargoOrder.setRowsNum(String.valueOf(index));
                cargoOrder.setWayId(waypoint.getId().toString());
//                    System.out.println("cargoOrder: " + cargoOrder.name);
                cargoOrderList.add(cargoOrder);
                index++;
            }

            model.addAttribute("cargoOrderList", cargoOrderList);





        } // if (order != null) {

        return "drivers/order";
    } // getOrderDriverExtPageById

    // *****************************************************************************************************************
    @GetMapping("/drivers/waypoint/load/{id}")
    // queries.loadOrder
    public String getWaypointDriverExtPageById(@PathVariable(value = "id") long waypointId, Model model) {

        log.debug("getOrderDriverExtPageById: Waypoint Id " + waypointId);

        Waypoint waypoint = waypointServiceImpl.findById(waypointId);
        if (waypoint == null) {
            throw new RuntimeException("Error: No waypoint.");
        }
        model.addAttribute("waypoint", waypoint);

        boolean upload = waypoint.isUpload();

        Cargo cargo = waypoint.getCargo();
        if (cargo == null) {
            throw new RuntimeException("Error: No cargo assigned to that waypoint.");
        }

        Order order = waypoint.getOrder();
        if (order == null) {
            throw new RuntimeException("Error: No order assigned to that waypoint.");
        }

//        log.debug("upload" + upload);

        boolean driversFromOrder = ( ((upload)  && (cargo.getStatus() == CargoStatus.ASSIGNED)) ||
                                     ((!upload) && (cargo.getStatus() == CargoStatus.SHIPPED)) );
//        log.debug("driverFromOrder");
        List<String> driverNameList = new ArrayList<>();
        driverNameList.add(null);
        if (driversFromOrder) {
//            log.debug("driverFromOrder true");
            if (order.getDriver1() != null) {
                driverNameList.add(order.getDriver1().getId() + ": " + order.getDriver1().getName());
                model.addAttribute("driver1selected", order.getDriver1().getId() + ": " + order.getDriver1().getName());
            }
            if (order.getDriver2() != null) {
                driverNameList.add(order.getDriver2().getId() + ": " + order.getDriver2().getName());
                model.addAttribute("driver2selected", order.getDriver2().getId() + ": " + order.getDriver2().getName());
            }
            if (order.getDriver3() != null) {
                driverNameList.add(order.getDriver3().getId() + ": " + order.getDriver3().getName());
                model.addAttribute("driver3selected", order.getDriver3().getId() + ": " + order.getDriver3().getName());
            }

            model.addAttribute("workedhours", 0);
            model.addAttribute("write", true);

        } else {
//            log.debug("driverFromOrder false" + (!upload) + (cargo.getStatus() == CargoStatus.SHIPPED));
            if (waypoint.getDriver1() != null) {
                driverNameList.add(waypoint.getDriver1().getId() + ": " + waypoint.getDriver1().getName());
                model.addAttribute("driver1selected", waypoint.getDriver1().getId() + ": " + waypoint.getDriver1().getName());
            }
            if (waypoint.getDriver2() != null) {
                driverNameList.add(waypoint.getDriver2().getId() + ": " + waypoint.getDriver2().getName());
                model.addAttribute("driver2selected", waypoint.getDriver2().getId() + ": " + waypoint.getDriver2().getName());
            }
            if (waypoint.getDriver3() != null) {
                driverNameList.add(waypoint.getDriver3().getId() + ": " + waypoint.getDriver3().getName());
                model.addAttribute("driver3selected", waypoint.getDriver3().getId() + ": " + waypoint.getDriver3().getName());
            }

            model.addAttribute("workedHours", waypoint.getHours());
            model.addAttribute("write", false);

        }
        model.addAttribute("driverNameList", driverNameList);


        return "drivers/cargo";
    } // getCargoDriverExtPageById

    // *****************************************************************************************************************
    @PostMapping("/drivers/waypoint/order/save/")
    @ResponseBody
    public ResponseEntity<String> saveDriversWaypointExt(@RequestBody Map<String, String> reqParam) {
        log.debug("saveDriversWaypointExt: In.");

        String waypointStr   = reqParam.get("waypointId");
        String driver1IdStr = reqParam.get("driver1Id");
        String driver2IdStr = reqParam.get("driver2Id");
        String driver3IdStr = reqParam.get("driver3Id");
        String hoursStr     = reqParam.get("hours");

        log.debug("waypointId : " + waypointStr);
        log.debug("driver1id : " + driver1IdStr);
        log.debug("driver2id : " + driver2IdStr);
        log.debug("driver3id : " + driver3IdStr);
        log.debug("Hours     : " + hoursStr);

        Long waypointId = Long.parseLong(waypointStr);
        Waypoint waypoint = waypointServiceImpl.findById(waypointId);
        if (waypoint == null) {
            throw new RuntimeException("Error: No waypoint.");
        }

        Cargo cargo = waypoint.getCargo();
        if (cargo == null) {
            throw new RuntimeException("Error: No cargo assigned to that waypoint.");
        }

        if (driver1IdStr.equals("")) {
            log.error("First driver can't be empty.");
            return new ResponseEntity<>("Error: First driver can't be empty.", HttpStatus.BAD_REQUEST);
        }
        Long driver1Id = Long.parseLong(driver1IdStr);
        if (driverServiceImpl.findById(driver1Id).getCity() != waypoint.getOrder().getTruck().getCity()) {
            log.error("Driver 1 isn't located in the same city than the truck.");
            return new ResponseEntity<>("Error: Driver 1 isn't located in the same city than the truck.", HttpStatus.BAD_REQUEST);
        }


        Long driver2Id;
        if (driver2IdStr.equals("")) {
            driver2Id = null;
        } else {
            driver2Id = Long.parseLong(driver2IdStr);
            if (driverServiceImpl.findById(driver2Id).getCity() != waypoint.getOrder().getTruck().getCity()) {
                log.error("Driver 2 isn't located in the same city than the truck.");
                return new ResponseEntity<>("Error: Driver 2 isn't located in the same city than the truck.", HttpStatus.BAD_REQUEST);
            }
        }

        Long driver3Id;
        if (driver3IdStr.equals("")) {
            driver3Id = null;
        } else {
            driver3Id = Long.parseLong(driver3IdStr);
            if (driverServiceImpl.findById(driver3Id).getCity() != waypoint.getOrder().getTruck().getCity()) {
                log.error("Driver 3 isn't located in the same city than the truck.");
                return new ResponseEntity<>("Error: Driver 3 isn't located in the same city than the truck.", HttpStatus.BAD_REQUEST);
            }
        }

        if (hoursStr == null) {
            log.error("Empty value at hours.");
            return new ResponseEntity<>("Error: Empty value at hours.", HttpStatus.BAD_REQUEST);
        }
        float hours;
        try {
            hours = Float.parseFloat(hoursStr);
        } catch (Exception e) {
            log.error("Invalid value at hours.");
            return new ResponseEntity<>("Error: Invalid value at hours.", HttpStatus.BAD_REQUEST);
        }

        // Checking conditions of previous waypoints for saving this one.
        int zorder = waypoint.getZorder();
        if (zorder > 0) {
            zorder -= 2;
            Waypoint waypointPrev = waypointServiceImpl.getByZOrder(waypoint.getOrder(), zorder);
            if (waypointPrev == null) {
                return new ResponseEntity<>("Error: There is no previous waypoint.", HttpStatus.BAD_REQUEST);
            }

            if (waypointPrev.isUpload()) {
                if (waypointPrev.getCargo().getStatus() != CargoStatus.SHIPPED) {
                    return new ResponseEntity<>("Error: Previous cargo isn't shipped.", HttpStatus.BAD_REQUEST);
                }
            } else {
                if (waypointPrev.getCargo().getStatus() != CargoStatus.DELIVERED) {
                    return new ResponseEntity<>("Error: Previous cargo isn't delivered.", HttpStatus.BAD_REQUEST);
                }
            }
        }



        if (    (waypoint.isUpload() && cargo.getStatus() == CargoStatus.ASSIGNED) ||
                (!waypoint.isUpload() && cargo.getStatus() == CargoStatus.SHIPPED) ) {
            try {
                waypointStr = waypointServiceImpl.saveWaypoint(waypoint, driver1Id, driver2Id, driver3Id, hours, cargo).toString();
            } catch (Exception e) {
                log.error(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
                log.info(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            zorder = waypoint.getZorder();
            if (zorder > 0) {
                zorder += 2;
                Waypoint waypointNext = waypointServiceImpl.getByZOrder(waypoint.getOrder(), zorder);
                if (waypointNext == null) {
                    orderServiceImpl.saveOrderFinal(waypoint.getOrder());
                }
            }

        } else {
            return new ResponseEntity<>("Error: Invalid conditions to save values.", HttpStatus.BAD_REQUEST);
        }

        log.debug("saveDriversWaypointExt: Out.");
        return new ResponseEntity<>(waypointStr, HttpStatus.OK);
    }

}
