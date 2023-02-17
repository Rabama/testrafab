package es.tatanca.test.Entities.Cargo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "POCargo")
public class Cargo {


    /*   public Cargo() {
        System.out.println("Cargo creado 1.");
    }

    public Cargo(String name, Double weight, String status, Long CityId0, Long CityId1, String Address0, String Address1, Long upload, Long unload) {
        System.out.println("Cargo creado 2.");
    }
*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "status")
    private String status;

    @Column(name = "CityId0")
    private Long CityId0;

    @Column(name = "CityId1")
    private Long CityId1;
    @Column(name = "Address0")
    private String Address0;
    @Column(name = "Address1")
    private String Address1;
    @Column(name = "upload")
    private Long upload;
    @Column(name = "unload")
    private Long unload;

}

