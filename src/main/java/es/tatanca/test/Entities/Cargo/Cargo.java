package es.tatanca.test.Entities.Cargo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "POCargo")
public class Cargo {

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

