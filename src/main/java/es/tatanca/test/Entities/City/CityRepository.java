package es.tatanca.test.Entities.City;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("FROM City WHERE id = :valor")
    City getEqualById(@Param("valor") String valor);

    @Query("FROM City WHERE name = :valor")
    City getEqualByCampo(@Param("valor") String valor);

    @Query("FROM City WHERE name LIKE CONCAT('%', :valor, '%')")
    List<City> getLikeByCampo(@Param("valor") String valor);

}
