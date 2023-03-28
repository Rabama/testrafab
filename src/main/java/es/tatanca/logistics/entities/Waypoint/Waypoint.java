package es.tatanca.logistics.entities.Waypoint;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Order.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "POWaypoint")
public class Waypoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order", nullable = true, updatable = true)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cargo", nullable = true, updatable = true)
    private Cargo cargo;

    private int zorder;

    private boolean upload;

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

}
