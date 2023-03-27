package es.tatanca.logistics.entities.Driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findAll();

    @Query("FROM Driver WHERE :campo = :valor")
    Driver getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Driver WHERE username = :valor")
    Driver getEqualByUsername(@Param("valor") String valor);

    @Query("FROM Driver WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Driver WHERE name LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeName(@Param("valor") String valor);

    @Query("FROM Driver WHERE status = :valor")
    List<Driver> getEqualStatus(@Param("valor") DriverStatus valor);

    @Query("FROM Driver WHERE name LIKE CONCAT('%', :valor1, '%') AND status = :valor2")
    List<Driver> getLikeNumberStatus(@Param("valor1") String valor1, @Param("valor2") DriverStatus valor2);

}
