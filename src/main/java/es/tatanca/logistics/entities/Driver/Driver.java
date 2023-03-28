package es.tatanca.logistics.entities.Driver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.tatanca.logistics.entities.City.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "PODriver")
public class Driver {

    // No se puede borrar si tiene un "Truck" u "Order" asignada.
    // Si se borra se ha de eliminar su usuario correspondiente.


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "workedhours")
    private Double workedHours;

    @Column(name = "status")
    private DriverStatus status;

    @Column(nullable = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_city", nullable = true, updatable = true)
    @JsonIgnore
    private City city;

    public Driver(String sId) {
        try {
            id = Long.parseLong(sId);
        } catch (Exception e) {
            throw new RuntimeException("Driver id not valid");
        }
    }

    public Driver(String sId, Long defValue) {
        if (sId.equals("")) { sId = defValue.toString(); }
        try {
            id = Long.parseLong(sId);
        } catch (Exception e) {
            throw new RuntimeException("Driver id not valid");
        }
    }

}
