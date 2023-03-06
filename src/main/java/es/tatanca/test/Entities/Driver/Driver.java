package es.tatanca.test.Entities.Driver;

import es.tatanca.test.Entities.City.City;
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

    public enum EnumStatus {WORK, REST}

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
    private Driver.EnumStatus status;

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
