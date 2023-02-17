package es.tatanca.test.Entities.Driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("FROM Driver WHERE id = :valor")
    Driver getEqualById(@Param("valor") String valor);


    @Query("FROM Driver WHERE name = :valor")
    Driver getEqualByName(@Param("valor") String valor);
    @Query("FROM Driver WHERE name LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeByName(@Param("valor") String valor);

    @Query("FROM Driver WHERE surname = :valor")
    Driver getEqualBySurname(@Param("valor") String valor);
    @Query("FROM Driver WHERE surname LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeBySurname(@Param("valor") String valor);

    @Query("FROM Driver WHERE workedHours = :valor")
    Driver getEqualByWorkedHours(@Param("valor") String valor);

    @Query("FROM Driver WHERE status = :valor")
    Driver getEqualByStatus(@Param("valor") String valor);
    @Query("FROM Driver WHERE status LIKE CONCAT('%', :valor, '%')")
    List<Driver> getLikeByStatus(@Param("valor") String valor);

    @Query("FROM Driver WHERE orderId = :valor")
    Driver getEqualByOrderId(@Param("valor") String valor);

    @Query("FROM Driver WHERE cityId = :valor")
    Driver getEqualByCityId(@Param("valor") String valor);
}
