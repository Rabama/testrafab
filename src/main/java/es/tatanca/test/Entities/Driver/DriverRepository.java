package es.tatanca.test.Entities.Driver;

import es.tatanca.test.Entities.City.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findAll();

    @Query("SELECT a FROM Driver a WHERE :campo = :valor")
    Driver getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Driver WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Driver WHERE name LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeName(@Param("valor") String valor);

    @Query("FROM Driver WHERE status = :valor")
    List<Driver> getEqualStatus(@Param("valor") Driver.EnumStatus valor);

    @Query("FROM Driver WHERE name LIKE CONCAT('%', :valor1, '%') AND status = :valor2")
    List<Driver> getLikeNumberStatus(@Param("valor1") String valor1, @Param("valor2") Driver.EnumStatus valor2);

}
