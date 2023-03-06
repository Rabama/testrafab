package es.tatanca.test.Entities.Distance;

import es.tatanca.test.Entities.City.City;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "time")
    private Double time;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_city", nullable = true, updatable = true)
    private City city;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_city1", nullable = true, updatable = true)
    private City city1;

}
