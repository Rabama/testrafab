package es.tatanca.logistics.entities.Order;

import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Truck.Truck;
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
@Table(name = "POOrder")
public class Order {

    // No se puede eliminar hasta que haya sido completada.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "completed")
    private OrderCompleted completed;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_truck", nullable = true, updatable = true)
    private Truck truck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver1", nullable = true)
    private Driver driver1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver2", nullable = true)
    private Driver driver2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver3", nullable = true)
    private Driver driver3;



/*

    @Column(name = "currentOrder")
    private Long currentOrder;

    @Transient
    private List<Cargo> cargoList;

    */

}
