package es.tatanca.test.Entities.Order;

import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Driver.DriverServiceImpl;
import es.tatanca.test.Entities.DriverHasOrder.DriverHasOrder;
import es.tatanca.test.Entities.DriverHasOrder.DriverHasOrderServiceImpl;
import es.tatanca.test.Entities.Truck.Truck;
import es.tatanca.test.Entities.Truck.TruckServiceImpl;
import es.tatanca.test.Entities.Truck.TruckStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
//@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;

    private final TruckServiceImpl truckServiceImpl;
    private final DriverServiceImpl driverServiceImpl;
    private final DriverHasOrderServiceImpl driverHasOrderServiceImpl;

//    @Lazy
//    private final OrderServiceImpl orderServiceImpl;


    public Order findById(Long id) {
        System.out.println("findById " + id);
        Order order = null;
        Optional<Order> opt = orderRepo.findById(id);
        if (opt.isPresent()) { order = opt.get(); }
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orderList = orderRepo.findAll();
        return orderList;
    }

    public List<Order> getEqualId(Long valor) {
        List<Order> orderList = orderRepo.getEqualId(valor);
        return orderList;
    }

    //    public List<Order> getEqualCompleted(Order.EnumCompleted valor) {
    public List<Order> getEqualCompleted(OrderCompleted valor) {
        System.out.println("getEqualCompleted " + valor);
        List<Order> orderList = orderRepo.getEqualCompleted(valor);
        return orderList;
    }








    // https://www.baeldung.com/transaction-configuration-with-jpa-and-spring
    // https://www.arquitecturajava.com/jpa-transaction-un-concepto-importante/
    // https://thorben-janssen.com/transactions-spring-data-jpa/
    // https://medium.com/javarevisited/spring-transactional-mistakes-everyone-did-31418e5a6d6b
    // https://wesome.org/spring-data-jpa-transactionality
    // https://vladmihalcea.com/spring-transaction-best-practices/
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
//    @Transactional(rollbackFor=Exception.class)
    // Spring only rolled back on unchecked exceptions by default.
    // https://medium.com/geekculture/spring-transactional-rollback-handling-741fcad043c6
    /*
        Aquí llegamos tras comprobar que todos los valores son válidos.
        En este función aAtendemos a la lógica de negocio.

        id
        completed
        truckId
        driver1Id
        driver2Id
        driver3Id

     */
    public Long saveOrder(Map<String,String> bundle) throws Exception {
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        String id        = bundle.get("id");
        String completed = bundle.get("completed");
        String truckId   = bundle.get("truckId");
        String driver1Id = bundle.get("driver1Id");
        String driver2Id = bundle.get("driver2Id");
        String driver3Id = bundle.get("driver3Id");

        System.out.println("OrderServiceImpl.saveOrder");
        System.out.println("id        : " + id);
        System.out.println("completed : " + completed);
        System.out.println("truckId   : " + truckId);
        System.out.println("driver1Id : " + driver1Id);
        System.out.println("driver2Id : " + driver2Id);
        System.out.println("driver3Id : " + driver3Id);

        Order order = new Order();

        // status. Ya hemos comprobado que es válido.
//        AppConfig.OrderCompletedConverter tsc = new AppConfig.OrderCompletedConverter();
//        order.setCompleted(tsc.convert(completed));
        order.setCompleted(OrderCompleted.fromLongName(completed));

        // id.
        if (id.equals("")) {
            // NUEVA ORDEN.

            // Una nueva orden no puede estar completada.
//            if (order.getCompleted() == Order.EnumCompleted.YES) {
            if (order.getCompleted() == OrderCompleted.YES) {
                System.out.println("saveOrder: Una nueva orden no puede estar completada.");
                throw new RuntimeException("A new order can't be completed.");
            }

        } else {
            // ORDEN EXISTENTE.

            // id. Ya hemos comprobado que es válido.
            order.setId(Long.parseLong(id));

            Order oldOrder = findById(order.getId());

            // La orden YA esta completada.
//            if (oldOrder.getCompleted() == Order.EnumCompleted.YES) {
            if (oldOrder.getCompleted() == OrderCompleted.YES) {
                System.out.println("saveOrder: La orden ya estaba completada.");
                throw new RuntimeException("The order was already completed.");
            }

            // La orden existe con otro camión.
            if (oldOrder.getTruck() != null) {
                if (!oldOrder.getTruck().getId().toString().equals(truckId)) {
                    System.out.println("saveOrder: Orden existente y camión distinto.");
                    throw new RuntimeException("You can't change the truck in a order.");
                }
            }

            // TODO: La orden ya existe con otros conductores.


        }

        // truck.
        if (!truckId.equals("")) {

            // El camión tiene que existir.
            Truck truck = truckServiceImpl.findById(Long.parseLong(truckId));
            if (truck == null) {
                System.out.println("saveOrder: El camión no existe..");
                throw new RuntimeException("The truck not exist.");
            }

            // El camión no puede estar en una orden no completada.
//            List<Order> orderNC = orderRepo.getOrderByTruckAndCompleted(truck, Order.EnumCompleted.NO);
            List<Order> orderNC = orderRepo.getOrderByTruckAndCompleted(truck, OrderCompleted.NO);
            int max = 0; if (!id.equals("")) { max++; }
            if (orderNC.size() > max) {
                System.out.println("saveOrder: El camión está asignado a una orden no completada.");
                throw new RuntimeException("The truck is assigned to not completed order.");
            }

            // El camión tiene que estar OK.
//            if (truck.getStatus() == Truck.EnumStatus.NOK) {
            if (truck.getStatus() == TruckStatus.NOK) {
                System.out.println("saveOrder: El camión no está disponible.");
                throw new RuntimeException("The truck is not OK.");
            }

            order.setTruck(truck);
        }



        // driver1.
        if (!driver1Id.equals("")) {

            // El conductor tiene que existir.
            Driver driver1 = driverServiceImpl.findById(Long.parseLong(driver1Id));
            if (driver1 == null) {
                System.out.println("saveOrder: El conductor1 no existe..");
                throw new RuntimeException("The driver1 not exist.");
            }

            // El conductor no puede estar en una orden no completada.
            List<Order> orderNC = orderRepo.getOrderByDriverAndNotCompleted(driver1);
            int max = 0; if (!id.equals("")) { max++; }
            if (orderNC.size() > max) {
                System.out.println("saveOrder: El conductor1 está asignado a una orden no completada.");
                throw new RuntimeException("The driver1 is assigned to not completed order.");
            }

            // El conductor tiene que estar trabajando.
            if (driver1.getStatus() == Driver.EnumStatus.REST) {
                System.out.println("saveOrder: El conductor1 no está disponible.");
                throw new RuntimeException("The driver1 is resting.");
            }

            driverHasOrderServiceImpl.save(order, driver1, DriverHasOrder.EnumStatus.DRIVER);
        }

        // driver2.
        if (!driver2Id.equals("")) {

            // El conductor tiene que existir.
            Driver driver2 = driverServiceImpl.findById(Long.parseLong(driver2Id));
            if (driver2 == null) {
                System.out.println("saveOrder: El conductor2 no existe..");
                throw new RuntimeException("The driver2 not exist.");
            }

            // El conductor no puede estar en una orden no completada.
            List<Order> orderNC = orderRepo.getOrderByDriverAndNotCompleted(driver2);
            int max = 0; if (!id.equals("")) { max++; }
            if (orderNC.size() > max) {
                System.out.println("saveOrder: El conductor2 está asignado a una orden no completada.");
                throw new RuntimeException("The driver2 is assigned to not completed order.");
            }

            // El conductor tiene que estar trabajando.
            if (driver2.getStatus() == Driver.EnumStatus.REST) {
                System.out.println("saveOrder: El conductor2 no está disponible.");
                throw new RuntimeException("The driver2 is resting.");
            }

            driverHasOrderServiceImpl.save(order, driver2, DriverHasOrder.EnumStatus.SECOND);
        }

        // driver3.
        if (!driver3Id.equals("")) {

            // El conductor tiene que existir.
            Driver driver3 = driverServiceImpl.findById(Long.parseLong(driver3Id));
            if (driver3 == null) {
                System.out.println("saveOrder: El conductor3 no existe..");
                throw new RuntimeException("The driver3 not exist.");
            }

            // El conductor no puede estar en una orden no completada.
            List<Order> orderNC = orderRepo.getOrderByDriverAndNotCompleted(driver3);
            int max = 0; if (!id.equals("")) { max++; }
            if (orderNC.size() > max) {
                System.out.println("saveOrder: El conductor3 está asignado a una orden no completada.");
                throw new RuntimeException("The driver3 is assigned to not completed order.");
            }

            // El conductor tiene que estar trabajando.
            if (driver3.getStatus() == Driver.EnumStatus.REST) {
                System.out.println("saveOrder: El conductor3 no está disponible.");
                throw new RuntimeException("The driver3 is resting.");
            }

            driverHasOrderServiceImpl.save(order, driver3, DriverHasOrder.EnumStatus.LOAD);
        }




        // TODO: El cargo existe.
        // TODO: El cargo no está asignado a otra orden.
        // TODO: No se puede modificar un cargo ya completado.



        // Finalizamos.
        order = orderRepo.save(order);
        return order.getId();
    } // saveOrder
}
