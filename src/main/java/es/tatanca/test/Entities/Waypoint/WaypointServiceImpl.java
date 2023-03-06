package es.tatanca.test.Entities.Waypoint;

import es.tatanca.test.Entities.Order.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WaypointServiceImpl implements WaypointService {

    private final WaypointRepository waypointRepo;

    @Transactional
    public Long save(Waypoint waypoint) {
        waypoint = waypointRepo.save(waypoint);
        return waypoint.getId();
    }

//    @Override
//    public List<Waypoint> getLikeName(String valor) {
//        System.out.println("getLikeName " + valor);
//        List<Waypoint> waypointList = waypointRepo.getLikeName(valor);
//        return waypointList;
//    }

    public Waypoint findById(Long id) {
        Waypoint waypoint = null;
        Optional<Waypoint> opt = waypointRepo.findById(id);
        if (opt.isPresent()) { waypoint = opt.get(); }
        return waypoint;
    }

    @Override
    public List<Waypoint> findAll() {
        List<Waypoint> waypointList = waypointRepo.findAll();
        return waypointList;
    }

    public List<Waypoint> findByOrder(Order order) {
        List<Waypoint> waypointList = waypointRepo.findByOrder(order);
        return waypointList;
    }

    @Override
    public Waypoint getEqualByCampo(String campo, String valor) {
        Waypoint waypoint = waypointRepo.getEqualBy(campo, valor);
        return waypoint;
    }

//    @Override
//    public List<Waypoint> getLikeByCampo(String campo, String valor) {
//        System.out.println("getLikeByCampo campo/valor " + campo + "/" + valor);
//
//        List<Waypoint> waypointList = null;
//        switch(campo) {
//            case "name" -> { waypointList = waypointRepo.getLikeName(valor); }
//            default -> { }
//        }
//        return waypointList;
//    }


    public void deleteByOrder(Long id) {
        waypointRepo.deleteByOrder(id);
    }

}
