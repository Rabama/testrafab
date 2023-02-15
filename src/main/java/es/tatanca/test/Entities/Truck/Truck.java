package es.tatanca.test.Entities.Truck;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "POTruck")
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
    private String status;
    @Column(name = "CityId")
    private Long CityId;

    @Transient
    public static List<String> camposconsulta = new ArrayList<>();

    @Transient
    private String City;

}