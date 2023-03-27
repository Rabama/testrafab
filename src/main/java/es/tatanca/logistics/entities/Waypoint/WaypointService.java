package es.tatanca.logistics.entities.Waypoint;

import es.tatanca.logistics.entities.Cargo.Cargo;

import java.util.List;

public interface WaypointService {

    Long save(Waypoint waypoint) throws Exception;

    Long saveWaypoint(Waypoint waypoint, Long driver1Id, Long driver2Id, Long driver3Id, Float hours, Cargo cargo) throws Exception;

    List<Waypoint> findAll();

    Waypoint getEqualByCampo(String campo, String valor);

}
