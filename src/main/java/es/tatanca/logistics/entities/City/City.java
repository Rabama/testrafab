package es.tatanca.logistics.entities.City;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "POCity")
public class City {

    // No se puede borrar si tiene algún "Truck", "Driver", "Cargo" en esa ciudad.
    // En caso de borrarse, hay que borrar en cascada todas las distancias que tenga esa ciudad.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

}
