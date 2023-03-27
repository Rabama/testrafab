package es.tatanca.logistics.entities.Order;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Truck.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("FROM Order WHERE completed = :valor")
    List<Order> getEqualCompleted(@Param("valor") OrderCompleted valor);

//    @Query("FROM Order o INNER JOIN Waypoint w ON o = w.order WHERE (o.completed = :completed) AND ( (w.driver1 = :driver) OR (w.driver2 = :driver) OR (w.driver3 = :driver) )")
//    List<Order> getEqualCompletedDriver(@Param("completed") OrderCompleted completed, @Param("driver") Driver driver);

    @Query("FROM Order WHERE ( (completed = 0) AND ( (driver1 = :driver) OR (driver2 = :driver) OR (driver3 = :driver) ) )")
    List<Order> getEqualNotCompletedDriver(@Param("driver") Driver driver);

    @Query("FROM Order o INNER JOIN Waypoint w ON o = w.order WHERE ( (o.completed = 1) AND ( (w.driver1 = :driver) OR (w.driver2 = :driver) OR (w.driver3 = :driver) ) )")
    List<Order> getEqualYesCompletedDriver(@Param("driver") Driver driver);

    @Query("FROM Order WHERE truck = :valor1 AND completed = :valor2")
    List<Order> getOrderByTruckAndCompleted(@Param("valor1") Truck valor1, @Param("valor2") OrderCompleted valor2);

    @Query("FROM Order WHERE ( (completed = OrderCompleted.NO) AND ((driver1 = :driver) OR (driver2 = :driver) OR (driver3 = :driver)) )")
    List<Order> getOrderDriverNotCompleted(@Param("driver") Driver driver);

    @Query("FROM Order WHERE ( (completed = OrderCompleted.NO) AND (truck = :truck) )")
    List<Order> getOrderTruckNotCompleted(@Param("truck") Truck truck);

    @Query("FROM Cargo c INNER JOIN Waypoint w ON c = w.cargo WHERE ( (w.order = :order) AND (c.status != CargoStatus.DELIVERED) )")
    List<Cargo> getCargoNotCompleted(@Param("order") Order order);

}
