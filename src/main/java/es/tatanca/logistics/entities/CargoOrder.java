package es.tatanca.logistics.entities;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Waypoint.Waypoint;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class CargoOrder {

        String id;
        String name;

        String upCityId;
        String unCityId;
        String upCityName;
        String unCityName;

        String upload;
        String unload;

        String weight;
//        String total;

        String status;

        String driver1;
        String driver2;
        String driver3;

        String hours;

        // String options;

        String wayId;

        // *************************************************************************************************************
        public CargoOrder(Cargo cargo) {
                id   = cargo.getId().toString();
                name = cargo.getName();

                upCityId   = cargo.getUpcity().getId().toString();
                unCityId   = cargo.getUncity().getId().toString();
                upCityName = cargo.getUpcity().getName();
                unCityName = cargo.getUncity().getName();

                weight     = String.valueOf(cargo.getWeight());

                status     = cargo.getStatus().toString();

                //               hours      = String.valueOf(cargo.getHours());

        }

        // *************************************************************************************************************
        public CargoOrder(Cargo cargo, Waypoint waypoint) {
                id   = cargo.getId().toString();
                name = cargo.getName();

                upCityId   = cargo.getUpcity().getId().toString();
                unCityId   = cargo.getUncity().getId().toString();
                upCityName = cargo.getUpcity().getName();
                unCityName = cargo.getUncity().getName();

                weight     = String.valueOf(cargo.getWeight());

                status     = cargo.getStatus().toString();
                if (waypoint.getDriver1() != null) {
                        driver1 = waypoint.getDriver1().getName();
                }
                if (waypoint.getDriver2() != null) {
                        driver2    = waypoint.getDriver2().getName();
                }
                if (waypoint.getDriver3() != null) {
                        driver3    = waypoint.getDriver3().getName();
                }


 //               hours      = String.valueOf(cargo.getHours());

        }

        // *************************************************************************************************************
        public Map<String,String> toMap() {
                Map<String, String> bundle = new HashMap<String, String>();

                bundle.put("id",         id);
                bundle.put("name",       name);
                bundle.put("upCityId",   upCityId);
                bundle.put("unCityId",   unCityId);
                bundle.put("upCityName", upCityName);
                bundle.put("unCityName", unCityName);
                bundle.put("weight",     weight);
                bundle.put("status",     status);
                bundle.put("hours",      hours);

                return bundle;
        }
}
