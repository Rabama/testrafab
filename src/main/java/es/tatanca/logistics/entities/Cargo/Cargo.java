package es.tatanca.logistics.entities.Cargo;

import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.Driver.Driver;
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
@Table(name = "POCargo")
public class Cargo {

    // No se puede borrar si está en alguna "Order" que no haya finalizado.
    // No se puede borrar si su status NO es "DELIVERED".

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

    @Column(name = "upaddress")
    private String upaddress;

    @Column(name = "unaddress")
    private String unaddress;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_upcity", nullable = false, updatable = true)
    private City upcity;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_uncity", nullable = false, updatable = true)
    private City uncity;


/*
    @Column(name = "upload")
    private Long upload;

    @Column(name = "unload")
    private Long unload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver1", nullable = true)
    private Driver driver1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver2", nullable = true)
    private Driver driver2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver3", nullable = true)
    private Driver driver3;

    private Float hours;
*/

}

