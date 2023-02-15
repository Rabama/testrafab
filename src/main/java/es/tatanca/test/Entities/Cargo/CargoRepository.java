package es.tatanca.test.Entities.Cargo;

import es.tatanca.test.Entities.Truck.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    @Query("FROM Cargo WHERE id = :valor")
    Cargo getEqualById(@Param("valor") String valor);

    @Query("FROM Cargo WHERE name = :valor")
    Cargo getEqualByName(@Param("valor") String valor);
    @Query("FROM Cargo WHERE name LIKE CONCAT('%', :valor, '%')")
    List<Cargo> getLikeByName(@Param("valor") String valor);

    @Query("FROM Cargo WHERE weight = :valor")
    Cargo getEqualByWeight(@Param("valor") String valor);

    @Query("FROM Cargo WHERE status = :valor")
    Cargo getEqualByStatus(@Param("valor") String valor);
    @Query("FROM Cargo WHERE status LIKE CONCAT('%', :valor, '%')")
    List<Cargo> getLikeByStatus(@Param("valor") String valor);

    @Query("FROM Cargo WHERE CityId0 = :valor")
    Cargo getEqualByCityId0(@Param("valor") String valor);

    @Query("FROM Cargo WHERE CityId1 = :valor")
    Cargo getEqualByCityId1(@Param("valor") String valor);

    @Query("FROM Cargo WHERE Address0 = :valor")
    Cargo getEqualByAddress0(@Param("valor") String valor);

    @Query("FROM Cargo WHERE Address1 = :valor")
    Cargo getEqualByAddress1(@Param("valor") String valor);

    @Query("FROM Cargo WHERE upload = :valor")
    Cargo getEqualByUpload(@Param("valor") String valor);

    @Query("FROM Cargo WHERE unload = :valor")
    Cargo getEqualByUnload(@Param("valor") String valor);


}
