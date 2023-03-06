package es.tatanca.test.Entities.Order;

import es.tatanca.test.Entities.Cargo.CargoServiceImpl;
import es.tatanca.test.Entities.CargoOrder;
import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Driver.DriverServiceImpl;
import es.tatanca.test.Entities.DriverHasOrder.DriverHasOrder;
import es.tatanca.test.Entities.DriverHasOrder.DriverHasOrderServiceImpl;
import es.tatanca.test.Entities.Truck.Truck;
import es.tatanca.test.Entities.Waypoint.Waypoint;
import es.tatanca.test.Entities.Waypoint.WaypointServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class OrderController {

    //    private final TruckServiceImpl truckServiceImpl;
    private final DriverServiceImpl driverServiceImpl;
    private final DriverHasOrderServiceImpl driverHasOrderServiceImpl;
    private final CargoServiceImpl cargoServiceImpl;
    private final OrderServiceImpl orderServiceImpl;
    private final WaypointServiceImpl waypointServiceImpl;

    @GetMapping("/entities/order")
    public String getOrderPage(Model model) {
        System.out.println("getOrderPage");
        return getOrderPageById(0L, model);
    }

    @GetMapping("/entities/order/{id}")
    // queries.loadOrder
    public String getOrderPageById(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getOrderPageById");

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
                }

                // Drivers.
                List<DriverHasOrder> driverList = driverHasOrderServiceImpl.getDriverHasOrderByOrder(order);
                driverList.forEach(d -> {
                    System.out.println("Driver "+d.getDriver());
                    Driver driver = driverServiceImpl.findById(d.getDriver().getId());
                    if (driver != null) {
                        switch (d.getStatus()) {
                            case DRIVER -> {
                                model.addAttribute("driver1id", d.getDriver().getId());
                                model.addAttribute("driver1name", d.getDriver().getName());
                            }
                            case SECOND -> {
                                model.addAttribute("driver2id", d.getDriver().getId());
                                model.addAttribute("driver2name", d.getDriver().getName());
                            }
                            case LOAD -> {
                                model.addAttribute("driver3id", d.getDriver().getId());
                                model.addAttribute("driver3name", d.getDriver().getName());
                            }
                            default -> {
                                System.out.println("OrderController.getOrderPageById.ERROR: Estado del conductor.");
                            }
                        }
                    } else {
                        // ERROR
                    }
                });

                // Cargos.
                List<Waypoint> waypointList = waypointServiceImpl.findByOrder(order);

                int index = 1;
                List<CargoOrder> cargoOrderList = new ArrayList<>();
                for (Waypoint waypoint : waypointList) {
                    CargoOrder cargoOrder = new CargoOrder();
                    cargoOrder.setId(String.valueOf(waypoint.getCargo().getId()));
                    cargoOrder.setName(waypoint.getCargo().getName());
                    cargoOrder.setUpCity(waypoint.getCargo().getUpcity().getName());
                    cargoOrder.setUnCity(waypoint.getCargo().getUncity().getName());
                    cargoOrder.setWeight(String.valueOf(waypoint.getCargo().getWeight()));
                    if (waypoint.isUpload()) {
                        cargoOrder.setUpload("UPLOAD");
                        cargoOrder.setUnload("");
                    } else {
                        cargoOrder.setUpload("");
                        cargoOrder.setUnload("UNLOAD");
                    }
                    cargoOrder.setRowsNum(String.valueOf(index));
//                    System.out.println("cargoOrder: " + cargoOrder.name);
                    cargoOrderList.add(cargoOrder);
                    index++;
                }

                model.addAttribute("cargoOrderList", cargoOrderList);

            } // order != null
        }
        return "entities/order/order";
    }

    @GetMapping("/order/{id}")
    @ResponseBody
    public Order getOrder(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getOrder " + id);
        return orderServiceImpl.findById(id);
    }

    @PostMapping("/saveOrder")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> saveOrder(@RequestBody Map<String, String> reqParam) {

        String id        = reqParam.get("id");
        String completed = reqParam.get("completed");
        String truckId   = reqParam.get("truckId");
        String driver1Id = reqParam.get("driver1Id");
        String driver2Id = reqParam.get("driver2Id");
        String driver3Id = reqParam.get("driver3Id");
        String rowsNum   = reqParam.get("rowsNum");

        System.out.println("OrderControllar.saveOrder");
        System.out.println("Id        : " + id);
        System.out.println("Completed : " + completed);
        System.out.println("TruckId   : " + truckId);
        System.out.println("Driver1Id : " + driver1Id);
        System.out.println("Driver2Id : " + driver2Id);
        System.out.println("Driver3Id : " + driver3Id);
        System.out.println("RowsNum   : " + rowsNum);


        long lId = 0;
        if (!id.equals("")) {
            try {
                lId = Long.parseLong(id);
            } catch (Exception e) {
                return new ResponseEntity<>("The order id is not correct.", HttpStatus.BAD_REQUEST);
            }
        }

        try {
//            (new AppConfig.OrderCompletedConverter()).convert(completed);
            OrderCompleted.fromLongName(completed);
        } catch (Exception e) {
            return new ResponseEntity<>("The order status is not correct.", HttpStatus.BAD_REQUEST);
        }

        if (!truckId.equals("")) {
            try {
                Long.parseLong(truckId);
            } catch (Exception e) {
                return new ResponseEntity<>("The truck id is not correct.", HttpStatus.BAD_REQUEST);
            }
        }

        if (!driver1Id.equals("")) {
            try {
                Long.parseLong(driver1Id);
            } catch (Exception e) {
                return new ResponseEntity<>("The driver 1 id is not correct.", HttpStatus.BAD_REQUEST);
            }
        }

        if (!driver2Id.equals("")) {
            try {
                Long.parseLong(driver2Id);
            } catch (Exception e) {
                return new ResponseEntity<>("The driver 2 id is not correct.", HttpStatus.BAD_REQUEST);
            }
        }

        if (!driver3Id.equals("")) {
            try {
                Long.parseLong(driver3Id);
            } catch (Exception e) {
                return new ResponseEntity<>("The driver 3 id is not correct.", HttpStatus.BAD_REQUEST);
            }
        }



        // Waypoints
// {"0":"3","1":"UPLOAD",
//  "2":"3","3":"",
//  "4":"4","5":"UPLOAD",
//  "6":"4","7":"",
//  "8":"5","9":"UPLOAD",
//  "10":"5","11":"",
//  "id":"1","completed":"NO","truckId":"2","driver1Id":"2","driver2Id":"4","driver3Id":"6","rowsNum":7}

        // Borramos los registros actuales antes de insertar los nuevos.
        waypointServiceImpl.deleteByOrder(lId);

        //
        long rows = 0;
        if (!rowsNum.equals("")) {
            try {
                rows = Long.parseLong(rowsNum);
            } catch (Exception e) {
                return new ResponseEntity<>("The rowsNum is not correct.", HttpStatus.BAD_REQUEST);
            }
        }
        rows--;

        for (int i=0; i<rows*2; i+=2) {
            Long lCargo = Long.parseLong(reqParam.get(String.valueOf(i)));
            System.out.println("Order: " + lId + " / Cargo: " + lCargo);
            Waypoint waypoint = new Waypoint();
            waypoint.setOrder(orderServiceImpl.findById(lId));
            waypoint.setCargo(cargoServiceImpl.findById(lCargo));
            waypoint.setZorder(i/2+1);
            waypoint.setUpload((reqParam.get(String.valueOf(i+1)).equals("UPLOAD")));
            waypointServiceImpl.save(waypoint);
        }








        Long orderId = 0L;
        try {
            orderId = orderServiceImpl.saveOrder(reqParam);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderId.toString(), HttpStatus.OK);
    } // saveOrder






    @PostMapping("/findOrder/{mode}")
    // Desde queries.findOrder
    public String findOrder(@RequestBody Order order, @PathVariable(value = "mode") int mode, Model model) {
        System.out.println("findOrder Id: "+order.getId()+" and completed "+order.getCompleted());

        List<Order> orderList = null;

//        AppConfig.OrderCompletedConverter tsc = new AppConfig.OrderCompletedConverter();
//        int completed = 0;
//        if (order.getCompleted() == Order.EnumCompleted.YES) completed = 1;

        if (order.getId() == null) {
            orderList = orderServiceImpl.getEqualCompleted(order.getCompleted());
        } else {
            orderList = orderServiceImpl.getEqualId(order.getId());
        }

        // ANTERIOR        List<Order> orderList = orderServiceImpl.getLikeNumber(order.getNumber());

        model.addAttribute("mode", mode);
        model.addAttribute("orderList", orderList);
        return "entities/order/orderquerybody";
    } // findOrder



    // ***************************************************************************************************************** CITY

    @GetMapping({"/order/query/head/{mode}"})
    public String queryOrderHead(@PathVariable(value = "mode") int mode, Model model) throws Exception {
        System.out.println("queryOrderHead");
        model.addAttribute("mode", mode);
        return "entities/order/orderqueryhead";
    }

    @GetMapping({"/order/query/body"})
    public String queryOrderBody() throws Exception {
        System.out.println("queryHeadOrder");
        return "entities/order/orderquerybody";
    }
}
