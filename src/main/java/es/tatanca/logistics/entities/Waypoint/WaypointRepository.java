package es.tatanca.logistics.entities.Waypoint;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaypointRepository extends JpaRepository<Waypoint, Long> {

    List<Waypoint> findAll();

    @Query("FROM Waypoint WHERE order = :valor ORDER BY zorder")
    List<Waypoint> findByOrder(@Param("valor") Order valor);

    @Query("FROM Waypoint WHERE cargo = :valor ORDER BY zorder")
    List<Waypoint> findByCargo(@Param("valor") Cargo valor);

    @Query("FROM Waypoint WHERE cargo = :cargo AND order = :order AND upload = :upload ORDER BY zorder")
    Waypoint findByOrderCargoUpload(@Param("order") Order order, @Param("cargo") Cargo cargo, @Param("upload") Boolean upload);

    @Query("FROM Waypoint WHERE (driver1 = :driver OR driver2 = :driver OR driver3 =: driver) AND upload = :upload")
    List<Waypoint> findByDriverUpload(@Param("driver") Driver driver, @Param("upload") Boolean upload);

    @Query("SELECT a FROM Waypoint a WHERE :campo = :valor")
    Waypoint getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Waypoint WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Waypoint> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

    @Modifying
    @Query("DELETE FROM Waypoint WHERE order.id = :valor")
    void deleteByOrder(@Param("valor") Long valor);

    @Modifying // (clearAutomatically = true)
    @Query("DELETE FROM Waypoint WHERE cargo = :valor")
    void deleteByCargo(@Param("valor") Cargo valor);

    @Query("FROM Waypoint WHERE ( (order = :order) AND (zorder = :zorder) )")
    Waypoint getByZOrder(@Param("order") Order order, @Param("zorder") int zorder);


}

