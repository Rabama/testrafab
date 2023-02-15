package es.tatanca.test.Entities.Truck;

import es.tatanca.test.Entities.City.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    @Query("FROM Truck WHERE id = :valor")
    Truck getEqualById(@Param("valor") String valor);

    @Query("FROM Truck WHERE number = :valor")
    Truck getEqualByNumber(@Param("valor") String valor);

    @Query("FROM Truck WHERE number LIKE CONCAT('%', :valor, '%')")
    List<Truck> getLikeByNumber(@Param("valor") String valor);

    @Query("FROM Truck WHERE capacity = :valor")
    Truck getEqualByCapacity(@Param("valor") String valor);

    @Query("FROM Truck WHERE status = :valor")
    Truck getEqualByStatus(@Param("valor") String valor);
    @Query("FROM Truck WHERE status LIKE CONCAT('%', :valor, '%')")
    List<Truck> getLikeByStatus(@Param("valor") String valor);

    @Query("FROM Truck WHERE CityId = :valor")
    Truck getEqualByCityId(@Param("valor") String valor);

}
