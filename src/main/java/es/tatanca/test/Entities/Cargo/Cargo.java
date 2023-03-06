package es.tatanca.test.Entities.Cargo;

import es.tatanca.test.Entities.City.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;


@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
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
    private CargoStatus status;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_upcity", nullable = false, updatable = true)
    private City upcity;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_uncity", nullable = false, updatable = true)
    private City uncity;

    @Column(name = "address0")
    private String address0;

    @Column(name = "address1")
    private String address1;

    @Column(name = "upload")
    private Long upload;

    @Column(name = "unload")
    private Long unload;

}

