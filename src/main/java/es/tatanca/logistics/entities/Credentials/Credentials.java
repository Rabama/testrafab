package es.tatanca.logistics.entities.Credentials;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "POCredentials")
public class Credentials {

    // Si queremos borrar un usuario "Admin" asegurarse que existe otro.
    // Si borramos el usuario de un driver significa que ese driver debe ser eliminado, y
    // hay que asegurarse de que no tiene ningún "Truck" ni "Order" asignada.

    public static String roleAdmin    = "Admin";
    public static String roleEmployee = "Employee";
    public static String roleDriver   = "Driver";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public Credentials (String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles    = roles;
    }

    public boolean isAdmin() { return (roles.stream().anyMatch(u -> u.equals(roleAdmin))); }

    public boolean isEmployee() { return (roles.stream().anyMatch(u -> u.equals(roleEmployee))); }

    public boolean isDriver() {  return (roles.stream().anyMatch(u -> u.equals(roleDriver))); }


    @Transient
    private boolean passwordEncripted;


}