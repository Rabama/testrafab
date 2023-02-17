package es.tatanca.test.Entities.Distance;

import es.tatanca.test.Entities.Cargo.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
    @Query("FROM Distance WHERE id = :valor")
    Distance getEqualById(@Param("valor") String valor);
    @Query("FROM Distance WHERE distance = :valor")
    Distance getEqualByDistance(@Param("valor") String valor);
    @Query("FROM Distance WHERE time = :valor")
    Distance getEqualByTime(@Param("valor") String valor);
    @Query("FROM Distance WHERE city0 = :valor")
    Distance getEqualByCity0(@Param("valor") String valor);
    @Query("FROM Distance WHERE city1 = :valor")
    Distance getEqualByCity1(@Param("valor") String valor);
}
