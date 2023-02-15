package es.tatanca.test.Entities.Order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "POOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "completed")
    private String completed;
    @Column(name = "currentOrder")
    private Long currentOrder;
    @Column(name = "TruckId")
    private Long TruckId;

}
