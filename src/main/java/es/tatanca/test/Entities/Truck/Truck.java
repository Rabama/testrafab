package es.tatanca.test.Entities.Truck;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import es.tatanca.test.Entities.City.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "POTruck")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Truck {

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