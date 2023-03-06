package es.tatanca.test.Entities.Truck;

import es.tatanca.test.Entities.City.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    List<Truck> findAll();

    @Query("SELECT a FROM Truck a WHERE :campo = :valor")
    Truck getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Truck WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Truck> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Truck WHERE number LIKE CONCAT('%', :valor, '%')")
    List<Truck> getLikeNumber(@Param("valor") String valor);

    @Query("FROM Truck WHERE status = :valor")
//    List<Truck> getEqualStatus(@Param("valor") Truck.EnumStatus valor);
    List<Truck> getEqualStatus(@Param("valor") TruckStatus valor);

    @Query("FROM Truck WHERE number LIKE CONCAT('%', :valor1, '%') AND status = :valor2")
//    List<Truck> getLikeNumberStatus(@Param("valor1") String valor1, @Param("valor2") Truck.EnumStatus valor2);
    List<Truck> getLikeNumberStatus(@Param("valor1") String valor1, @Param("valor2") TruckStatus valor2);
/*
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

 */

}
