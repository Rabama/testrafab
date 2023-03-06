package es.tatanca.test.Entities.City;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findAll();

    @Query("SELECT a FROM City a WHERE :campo = :valor")
    City getEqualBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM City WHERE :campo LIKE CONCAT('%', :valor, '%')")
    List<City> getLikeBy(@Param("campo") String campo, @Param("valor") String valor);

    @Query("FROM City WHERE name LIKE CONCAT('%', :valor, '%')")
    List<City> getLikeName(@Param("valor") String valor);

}
