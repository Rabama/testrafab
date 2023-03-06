package es.tatanca.test.Entities.DriverHasOrder;

import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverHasOrderRepository extends JpaRepository<DriverHasOrder, Long> {
    @Query("FROM DriverHasOrder d INNER JOIN Order o ON d.order = o.id WHERE o.id = :valor1")
    List<DriverHasOrder> getDriverHasOrderByOrder(@Param("valor1") Long valor1);

    @Modifying
    @Query("DELETE DriverHasOrder d WHERE d.order = :valor1 AND d.driver = :valor2")
    void deleteDriverHasOrderByOrderDriver(@Param("valor1") Order valor1, @Param("valor2") Driver valor2);

}
