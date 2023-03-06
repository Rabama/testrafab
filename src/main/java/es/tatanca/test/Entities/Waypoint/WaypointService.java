package es.tatanca.test.Entities.Waypoint;

import java.util.List;

public interface WaypointService {

    Long save(Waypoint waypoint) throws Exception;

//    public List<Waypoint> getLikeName(String valor);

    List<Waypoint> findAll();

    Waypoint getEqualByCampo(String campo, String valor);

//    List<Waypoint> getLikeByCampo(String campo, String valor);

}
