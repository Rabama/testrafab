package es.tatanca.logistics.entities.Cargo;

import es.tatanca.logistics.entities.City.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findById(Long id);

    @Query("FROM Cargo WHERE name LIKE CONCAT('%', :valor, '%')")
    List<Cargo> getLikeName(@Param("valor") String valor);

    //    @Query("FROM Cargo WHERE status = :valor")
    @Query("FROM Cargo WHERE status = :valor")
    List<Cargo> getEqualStatus(@Param("valor") CargoStatus valor);

    @Query("FROM Cargo Where upcity = :upcity AND uncity = :uncity")
    List<Cargo> getEqualCities(@Param("upcity") City upcity, @Param("uncity") City uncity);

    @Query("FROM Cargo WHERE status = :status AND upcity = :city")
    List<Cargo> getEqualStatusCity(@Param("status") CargoStatus status, @Param("city") City city);

    @Query("FROM Cargo WHERE status <> :status AND upcity = :city")
    List<Cargo> getDiffStatusEqUpcity(@Param("status") CargoStatus status, @Param("city") City city);

    @Query("FROM Cargo WHERE status <> :status AND uncity = :city")
    List<Cargo> getDiffStatusEqUncity(@Param("status") CargoStatus status, @Param("city") City city);

    @Query("FROM Cargo WHERE name LIKE CONCAT('%', :valor1, '%') AND status = :valor2")
    List<Cargo> getLikeNameStatus(@Param("valor1") String valor1, @Param("valor2") CargoStatus valor2);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Cargo SET status = :status WHERE id = :cargoId")
    void setStatus(@Param("status") CargoStatus status, @Param("cargoId") Long cargoId);

}
