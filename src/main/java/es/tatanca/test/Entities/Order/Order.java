package es.tatanca.test.Entities.Order;

import es.tatanca.test.Entities.Cargo.Cargo;
import es.tatanca.test.Entities.Truck.Truck;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "POOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "completed")
    private OrderCompleted completed;

    @Column(name = "currentOrder")
    private Long currentOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_truck", nullable = true, updatable = true)
    private Truck truck;

    @Transient
    private List<Cargo> cargoList;

}
