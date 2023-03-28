package es.tatanca.logistics.entities.Credentials;

public interface CredentialsService {

    long count();

    Credentials findById(Long id);

    // ADMIN
    Long saveUser(Credentials user);
}
