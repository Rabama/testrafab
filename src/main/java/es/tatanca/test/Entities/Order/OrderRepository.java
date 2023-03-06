package es.tatanca.test.Entities.Order;

import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Truck.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("FROM Order WHERE id = :valor")
    List<Order> getEqualId(@Param("valor") Long valor);

    @Query("FROM Order WHERE completed = :valor")
//    List<Order> getEqualCompleted(@Param("valor") Order.EnumCompleted valor);
    List<Order> getEqualCompleted(@Param("valor") OrderCompleted valor);

    @Query("FROM Order WHERE truck = :valor1 AND completed = :valor2")
//    List<Order> getOrderByTruckAndCompleted(@Param("valor1") Truck valor1, @Param("valor2") Order.EnumCompleted valor2);
    List<Order> getOrderByTruckAndCompleted(@Param("valor1") Truck valor1, @Param("valor2") OrderCompleted valor2);

    @Query("FROM Order o INNER JOIN DriverHasOrder d ON o.id = d.order WHERE o.completed = 0 AND d.driver = :valor1")
    List<Order> getOrderByDriverAndNotCompleted(@Param("valor1") Driver valor1);
}
