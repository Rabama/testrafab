package es.tatanca.logistics.entities.Credentials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    @Query("SELECT COUNT(u) FROM Credentials u")
    long count();

    Optional<Credentials> findUserByUsername(String username);

    @Query("FROM Credentials WHERE username LIKE CONCAT('%', :valor, '%')")
    List<Credentials> getLikeUsername(@Param("valor") String valor);
}
