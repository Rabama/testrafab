package es.tatanca.test.Entities.City;

import es.tatanca.test.Entities.Distance.Distance;
import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Truck.Truck;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "POCity")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

}
