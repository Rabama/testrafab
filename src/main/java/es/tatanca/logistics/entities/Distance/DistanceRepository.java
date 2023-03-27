package es.tatanca.logistics.entities.Distance;

import es.tatanca.logistics.entities.City.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
    @Query("FROM Distance WHERE id = :valor")
    Distance getEqualById(@Param("valor") String valor);

    List<Distance> findAll();

    Optional<Distance> findById(Long id);

    @Query("FROM Distance WHERE city0 = :city0 AND city1 = :city1")
    Distance getDistance(@Param("city0") City city0, @Param("city1") City city1);

    @Query("FROM Distance WHERE distance <= :target ORDER BY distance")
    List<Distance> getLessOrEqualByDistance(@Param("target") float target);

    /*@Query("FROM Distance WHERE distance <= :valor")
    List<Distance> getLessOrEqualByDistance(@Param("valor") float valor);*/

}
