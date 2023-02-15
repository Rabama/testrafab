package es.tatanca.test.Entities.WaypointSort;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "POWaypointSort")
public class WaypointSort {

    @Column(name = "order0")
    private int order0;
    @Column(name = "order1")
    private int order1;
    @Id
    @Column(name = "CargoId")
    private Long cargoId;
    @Column(name = "OrderId")
    private Long OrderId;

}
