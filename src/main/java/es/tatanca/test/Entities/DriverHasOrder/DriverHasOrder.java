package es.tatanca.test.Entities.DriverHasOrder;

import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Order.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "driverhasorder", uniqueConstraints={@UniqueConstraint(columnNames = {"fk_driver", "fk_order", "status"})} )
public class DriverHasOrder {

    public static enum EnumStatus {DRIVER, SECOND, LOAD}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_driver", nullable = true, updatable = true)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order", nullable = true, updatable = true)
    private Order order;

    private DriverHasOrder.EnumStatus status;

}
