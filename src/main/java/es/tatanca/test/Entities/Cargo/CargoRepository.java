package es.tatanca.test.Entities.Cargo;

import es.tatanca.test.Entities.Truck.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findById(Long id);

    Optional<Cargo> findByName(String name);


    @Modifying
    @Query(value = "update cargos t set t.name='xxx' where t.id=:cargoId", nativeQuery = true)
    void updateCargo(@Param("cargoId") Long cargoId);



    @Query("FROM Cargo WHERE name LIKE CONCAT('%', :valor, '%')")
    List<Cargo> getLikeName(@Param("valor") String valor);

    @Query("FROM Cargo WHERE status = :valor")
    List<Cargo> getEqualStatus(@Param("valor") CargoStatus valor);

    @Query("FROM Cargo WHERE name LIKE CONCAT('%', :valor1, '%') AND status = :valor2")
    List<Cargo> getLikeNameStatus(@Param("valor1") String valor1, @Param("valor2") CargoStatus valor2);


    @Query("FROM Cargo WHERE address0 = :valor")
    Cargo getEqualByAddress0(@Param("valor") String valor);

    @Query("FROM Cargo WHERE address1 = :valor")
    Cargo getEqualByAddress1(@Param("valor") String valor);

    @Query("FROM Cargo WHERE upload = :valor")
    Cargo getEqualByUpload(@Param("valor") String valor);

    @Query("FROM Cargo WHERE unload = :valor")
    Cargo getEqualByUnload(@Param("valor") String valor);


}
