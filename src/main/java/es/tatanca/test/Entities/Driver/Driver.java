package es.tatanca.test.Entities.Driver;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "PODriver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "workedHours")
    private Double workedHours;
    @Column(name = "status")
    private String status;
    @Column(name = "OrderId")
    private String orderId;
    @Column(name = "CityId")
    private String cityId;

}
