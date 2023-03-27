package es.tatanca.logistics.entities.Truck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    Optional<Truck> findById(Long id);

    List<Truck> findAll();

    @Query("SELECT a FROM Truck a WHERE :campo = :valor")
    Truck getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Truck WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Truck> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Truck WHERE number LIKE CONCAT('%', :valor, '%')")
    List<Truck> getLikeNumber(@Param("valor") String valor);

    @Query("FROM Truck WHERE status = :valor")
    List<Truck> getEqualStatus(@Param("valor") TruckStatus valor);

    @Query("FROM Truck WHERE number LIKE CONCAT('%', :valor1, '%') AND status = :valor2")
    List<Truck> getLikeNumberStatus(@Param("valor1") String valor1, @Param("valor2") TruckStatus valor2);


}
