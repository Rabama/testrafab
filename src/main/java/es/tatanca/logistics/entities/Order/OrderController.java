package es.tatanca.logistics.entities.Order;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoServiceImpl;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.CargoOrder;
import es.tatanca.logistics.entities.Credentials.Credentials;
import es.tatanca.logistics.entities.Distance.DistanceServiceImpl;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Driver.DriverServiceImpl;
import es.tatanca.logistics.entities.Truck.Truck;
import es.tatanca.logistics.entities.Truck.TruckServiceImpl;
import es.tatanca.logistics.entities.Waypoint.Waypoint;
import es.tatanca.logistics.entities.Waypoint.WaypointRepository;
import es.tatanca.logistics.entities.Waypoint.WaypointServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final TruckServiceImpl truckServiceImpl;
    private final DistanceServiceImpl distanceServiceImpl;
    private final DriverServiceImpl driverServiceImpl;
    private final CargoServiceImpl cargoServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final WaypointServiceImpl waypointServiceImpl;

    private final WaypointRepository waypointRepo;

    // *****************************************************************************************************************
    @GetMapping("/entities/order")
    public String getOrderPage(Model model) {
        log.debug("getOrderPage");
        return getOrderPageById(0L, model);
    }

    // *****************************************************************************************************************
    @GetMapping("/entities/order/{id}")
    // queries.loadOrder
    public String getOrderPageById(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getOrderPageById " + id + " entrada");

        // Las listas desplegables.
        model.addAttribute("completedValues", OrderCompleted.values());

        if (id > 0) {
            Order order = orderServiceImpl.findById(id);
            if (order != null) {
                model.addAttribute("order", order);

                // Y ahora los campos que son objetos o múltiples.
                Truck truck = order.getTruck();
                if (truck != null) {
                    model.addAttribute("truckid", truck.getId());
                    model.addAttribute("trucknumber", truck.getNumber());
                    model.addAttribute("truckcapacity", truck.getCapacity());
                }

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

                    Cargo cargo = waypoint.getCargo();

                    CargoOrder cargoOrder = new CargoOrder(cargo);

                    DecimalFormat df = new DecimalFormat("###.##");
                    float hours = 0;
                    if ((cargo.getStatus() == CargoStatus.ASSIGNED) ||
                            (cargo.getStatus() == CargoStatus.SHIPPED) && (!waypoint.isUpload())) {

                        try { hours = waypoint.getHours(); } catch (Exception ignored) { }
                        if (hours == 0) {
                            Float newHours = distanceServiceImpl.getHours(waypoint.getCargo().getUpcity().getId(), waypoint.getCargo().getUncity().getId());
                            cargoOrder.setHours(df.format(newHours));
                        }

                    } else {
                        try {
                            cargoOrder.setHours(df.format(waypoint.getHours()));
                        } catch (Exception e) {
                            cargoOrder.setHours(df.format(0.0F));
                        }
                    }

                    if (waypoint.isUpload()) {
                        cargoOrder.setUpload("UPLOAD");
                        cargoOrder.setUnload("");
                    } else {
                        cargoOrder.setUpload("");
                        cargoOrder.setUnload("UNLOAD");
                    }

                    cargoOrder.setWayId(waypoint.getId().toString());

                    cargoOrderList.add(cargoOrder);
                    index++;
                }


                model.addAttribute("cargoOrderList", cargoOrderList);

            } // order != null
        } else if (id == -1) {
            Order order = new Order();
            model.addAttribute("order", order);
        }

        Credentials user = orderServiceImpl.getCurrentUser();
        model.addAttribute("employee", (user.isAdmin() || user.isEmployee()) );

        log.debug("getOrderPageById " + id + " salida");
        return "entities/order/order";
    }

    // *****************************************************************************************************************
    @GetMapping("/order/{id}")
    @ResponseBody
    public Order getOrder(@PathVariable(value = "id") Long id) {
       log.debug("getOrder " + id);
        return orderServiceImpl.findById(id);
    }

    // *****************************************************************************************************************
    @PostMapping("/entities/order/save/")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> saveOrder(@RequestBody Map<String, String> reqParam) {

        String orderIdStr = reqParam.get("id");
        String completedStr = reqParam.get("completed");
        String truckIdStr = reqParam.get("truckId");
        String driver1IdStr = reqParam.get("driver1Id");
        String driver2IdStr = reqParam.get("driver2Id");
        String driver3IdStr = reqParam.get("driver3Id");
        String rowsNumStr = reqParam.get("rowsNum");

        log.debug("OrderControllar.saveOrder");
        log.debug("Id        : " + orderIdStr);
        log.debug("Completed : " + completedStr);
        log.debug("TruckId   : " + truckIdStr);
        log.debug("Driver1Id : " + driver1IdStr);
        log.debug("Driver2Id : " + driver2IdStr);
        log.debug("Driver3Id : " + driver3IdStr);
        log.debug("RowsNum   : " + rowsNumStr);


        Order order;
        Truck truck = null;
        Driver driver1 = null;
        Driver driver2 = null;
        Driver driver3 = null;
        List<Waypoint> waypointList = new ArrayList<>();

        if (!orderIdStr.equals("")) {

            long orderId;
            try {
                orderId = Long.parseLong(orderIdStr);
            } catch (Exception e) {
                return new ResponseEntity<>("The order id is not correct.", HttpStatus.BAD_REQUEST);
            }

            order = orderServiceImpl.findById(orderId);
            if (order == null) {
                return new ResponseEntity<>("Can't find order " + orderIdStr + ".", HttpStatus.BAD_REQUEST);
            }

        } else {
            order = new Order();
            // id = null
        }

        order.setCompleted(OrderCompleted.fromLongName(completedStr));

        if (!truckIdStr.equals("")) {

            long truckId;
            try {
                truckId = Long.parseLong(truckIdStr);
            } catch (Exception e) {
                return new ResponseEntity<>("The truck id is not correct.", HttpStatus.BAD_REQUEST);
            }

            truck = truckServiceImpl.findById(truckId);
            if (truck == null) {
                return new ResponseEntity<>("Can't find truck " + truckIdStr + ".", HttpStatus.BAD_REQUEST);
            }

            order.setTruck(truck);
        }

        if (!driver1IdStr.equals("")) {

            long driver1Id;
            try {
                driver1Id = Long.parseLong(driver1IdStr);
            } catch (Exception e) {
                return new ResponseEntity<>("The driver 1 id is not correct.", HttpStatus.BAD_REQUEST);
            }

            driver1 = driverServiceImpl.findById(driver1Id);
            if (truck == null) {
                return new ResponseEntity<>("Can't find driver " + driver1IdStr + ".", HttpStatus.BAD_REQUEST);
            }

        }

        if (!driver2IdStr.equals("")) {

            long driver2Id;
            try {
                driver2Id = Long.parseLong(driver2IdStr);
            } catch (Exception e) {
                return new ResponseEntity<>("The driver 1 id is not correct.", HttpStatus.BAD_REQUEST);
            }

            driver2 = driverServiceImpl.findById(driver2Id);
            if (truck == null) {
                return new ResponseEntity<>("Can't find driver " + driver2IdStr + ".", HttpStatus.BAD_REQUEST);
            }

        }

        if (!driver3IdStr.equals("")) {

            long driver3Id;
            try {
                driver3Id = Long.parseLong(driver3IdStr);
            } catch (Exception e) {
                return new ResponseEntity<>("The driver 1 id is not correct.", HttpStatus.BAD_REQUEST);
            }

            driver3 = driverServiceImpl.findById(driver3Id);
            if (truck == null) {
                return new ResponseEntity<>("Can't find driver " + driver3IdStr + ".", HttpStatus.BAD_REQUEST);
            }

        }

        long rowsNum = 0L;
        if (!rowsNumStr.equals("")) {

            try {
                rowsNum = Long.parseLong(rowsNumStr);
            } catch (Exception e) {
                return new ResponseEntity<>("The rowsNum is not correct.", HttpStatus.BAD_REQUEST);
            }

            rowsNum--;
        }

        // (field, value)
        for (int i = 0; i < rowsNum * 2; i += 2) {

            long cargoIdLong;
            try {
                cargoIdLong = Long.parseLong(reqParam.get(String.valueOf(i)));
            } catch (Exception e) {
                return new ResponseEntity<>("The cargo id" + reqParam.get(String.valueOf(i)) + " is not valid.", HttpStatus.BAD_REQUEST);
            }

            boolean upload = reqParam.get(String.valueOf(i + 1)).equals("UPLOAD");

            Cargo cargo = cargoServiceImpl.findById(cargoIdLong);
            if (cargo == null) {
                return new ResponseEntity<>("saveOrder: Cargo " + cargoIdLong + " not exist.", HttpStatus.BAD_REQUEST);
            }

            Long orderId = orderServiceImpl.saveOrder(order, truck, driver1, driver2, driver3, null);
            log.debug("saveOrder. Order: " + order.getId() + " / Cargo: " + cargo.getId() + " / " + upload);

            Waypoint waypoint = waypointServiceImpl.findByOrderCargoUpload(order, cargo, upload);
            if (waypoint == null) {
                waypoint = new Waypoint();
                waypoint.setOrder(order);
                waypoint.setCargo(cargo);
            }
            waypoint.setZorder(i);
            waypoint.setUpload(upload);

            waypointList.add(waypoint);
        }


        Long orderId = orderServiceImpl.saveOrder(order, truck, driver1, driver2, driver3, waypointList);

        return new ResponseEntity<>(orderId.toString(), HttpStatus.OK);

    }

    // *****************************************************************************************************************
    @PostMapping("/entities/order/delete/")
    @ResponseBody
    public ResponseEntity<String> delOrder(@RequestBody Map<String, String> reqParam, Model model) {
        log.debug("delOrder");

        String id     = reqParam.get("id");

        log.debug("Id    : " + id);

        Order order = new Order();
        List<Waypoint> waypointList = new ArrayList<>();

        try {
            order = orderServiceImpl.findById(Long.parseLong(id));
        } catch (Exception e) {
            log.debug("Order ID not found.");
            return new ResponseEntity<>("OrderID not found.", HttpStatus.BAD_REQUEST);
        }

        waypointList = waypointRepo.findByOrder(order);

        try {
            orderServiceImpl.delOrder(order, waypointList);
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            log.info(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Order deleted successfully.", HttpStatus.OK);
    } // delCargo


    @PostMapping("/entities/order/find/{mode}")
    // Desde queries.findOrder
    // *****************************************************************************************************************
    public String findOrder(@RequestBody Map<String,String> bundle, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("findOrder: In.");
        log.debug("id: "        + bundle.get("id"));
        log.debug("completed: " + bundle.get("completed"));

        long id = -1;
        try { id = Long.parseLong(bundle.get("id")); } catch (Exception ignored) { }

        OrderCompleted completed = OrderCompleted.fromLongName(bundle.get("completed"));

        Order order = null;
        if (id >= 0) { order = orderServiceImpl.findById(id); }

        List<Order> orderList;
        if (order == null) {
            orderList = orderServiceImpl.getEqualCompleted(completed);
        } else {
            orderList = new ArrayList<>();
            orderList.add(order);
        }

        model.addAttribute("mode", mode);
        model.addAttribute("orderList", orderList);

        log.debug("findOrder: Out.");
        return "entities/order/orderquerybody";
    } // findOrder



    // *****************************************************************************************************************

    @GetMapping({"/queries/order/head/{mode}"})
    public String queryOrderHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("queryOrderHead");
        model.addAttribute("mode", mode);
        model.addAttribute("completedValues", OrderCompleted.values());
        return "entities/order/orderqueryhead";
    }

    @GetMapping({"/queries/order/body"})
    public String queryOrderBody() {
        log.debug("queryHeadOrder");
        return "entities/order/orderquerybody";
    }
    @GetMapping("/queries/order/cargo/append/")
    @ResponseBody
    // *****************************************************************************************************************
    public ResponseEntity<String> orderCargoAppend() {
        log.debug("orderCargoAppend");
        return new ResponseEntity<>("orderCargoAppend", HttpStatus.OK);
    }

    @GetMapping("/queries/order/cargo/ready/{id}")
    @ResponseBody
    // *****************************************************************************************************************
    public ResponseEntity<String> orderCargoReady(@PathVariable(value = "id") Long id) {
        log.debug("orderCargoReady");

//        cargoServiceImpl.setStatus(CargoStatus.READY, id);
//        waypointServiceImpl.deleteByCargo(id);

        Cargo cargo = cargoServiceImpl.findById(id);
        if (cargo == null) {
            return new ResponseEntity<>("Can't find cargo " + id, HttpStatus.BAD_REQUEST);
        }

//        List<Waypoint> waypointList = waypointServiceImpl.findByCargo(cargo);
//        if (waypointList.size() == 0) {
//            return new ResponseEntity<>("Can't find waypoints for cargo " + id, HttpStatus.BAD_REQUEST);
//        }
//
//        orderServiceImpl.deleteCargo(cargo, waypointList);

        orderServiceImpl.deleteCargo(cargo);

        return new ResponseEntity<>(id.toString(), HttpStatus.OK);

    }

}


