package es.tatanca.test.Entities.Waypoint;

import es.tatanca.test.Entities.Cargo.Cargo;
import es.tatanca.test.Entities.Order.Order;
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


}
