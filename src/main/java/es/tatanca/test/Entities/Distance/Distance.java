package es.tatanca.test.Entities.Distance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
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
    @Column(name = "city0")
    private Long city0;
    @Column(name = "city1")
    private Long city1;

}
