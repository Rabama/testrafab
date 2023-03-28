package es.tatanca.logistics.entities.City;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findById(Long id);

    @Query("FROM City WHERE name LIKE CONCAT('%', :valor, '%')")
    List<City> getLikeName(@Param("valor") String valor);

}
