package es.tatanca.test.Entities.Waypoint;

import es.tatanca.test.Entities.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaypointRepository extends JpaRepository<Waypoint, Long> {
    List<Waypoint> findAll();

    @Query("FROM Waypoint WHERE order = :valor ORDER BY zorder")
    List<Waypoint> findByOrder(@Param("valor") Order valor);

    @Query("SELECT a FROM Waypoint a WHERE :campo = :valor")
    Waypoint getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Waypoint WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Waypoint> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

//    @Query("FROM Waypoint WHERE name LIKE CONCAT('%', :valor, '%')")
//    List<Waypoint> getLikeName(@Param("valor") String valor);

    @Modifying
    @Query("DELETE FROM Waypoint WHERE order.id = :valor")
    void deleteByOrder(@Param("valor") Long valor);
}
