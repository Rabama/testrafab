package es.tatanca.test.Entities.Distance;

import es.tatanca.test.Entities.Cargo.Cargo;
import es.tatanca.test.Entities.City.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
    @Query("FROM Distance WHERE id = :valor")
    Distance getEqualById(@Param("valor") String valor);

    List<Distance> findAll();

    @Query("SELECT a FROM Distance a WHERE :campo = :valor")
    Distance getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM Distance WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<Distance> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);
}
