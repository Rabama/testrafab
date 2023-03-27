package es.tatanca.logistics.entities.Distance;

import es.tatanca.logistics.entities.City.City;
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
@Table(name = "PODistance")
public class Distance {

    // ¿Condiciones para borrar?

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "distance")
    private float distance;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_city", nullable = true, updatable = true)
    private City city0;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_city1", nullable = true, updatable = true)
    private City city1;

}
