package es.tatanca.logistics.entities.Truck;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import es.tatanca.logistics.entities.City.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "POTruck")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Truck {

    // No se puede eliminar si tiene asociado un "Driver", si está en movimiento,
    // si tiene asignado un "Order".

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "number")
    public String number;

    @Column(name = "capacity")
    private Double capacity;

    @Column(name = "status")
    @Convert(converter = TruckStatusConverter.class)
    private TruckStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_city", nullable = true, updatable = true)
    @JsonIgnore
    @JsonProperty(value = "city")
    private City city;

}