package es.tatanca.logistics.entities.Distance;

import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class DistanceController {

    private final DistanceServiceImpl distanceServiceImpl;
    private final CityServiceImpl cityServiceImpl;

    @GetMapping("/entities/distance/load/")
    public String getDistancePage(Model model) {
        log.debug("getDistancePage");
        return getDistancePageById(0L, model);
    }

    @GetMapping("/entities/distance/load/{id}")
    public String getDistancePageById(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getDistancePageById");
        if (id > 0) {
            Distance distance = distanceServiceImpl.findById(id);
            model.addAttribute("distance", distance);
        } else if (id == -1) {
            Distance distance = new Distance();
            model.addAttribute("distance", distance);
        }
        return "entities/distance/distance";
    }

    @GetMapping("/entities/distance/{id}")
    @ResponseBody
    public Distance getDistance(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getDistance " + id);
        return distanceServiceImpl.findById(id);
    }

    @PostMapping("/entities/distance/save/")
    @ResponseBody
    public ResponseEntity<String> saveDistance(@RequestBody Map<String, String> reqParam, Model  model) {
        log.debug("saveDistance " + reqParam.get("id"));

        String id       = reqParam.get("id");
        String distanc   = reqParam.get("distance");
        String city0   = reqParam.get("city0");
        String city1   = reqParam.get("city1");

        log.debug("Id       : " + id);
        log.debug("Distance : " + distanc);
        log.debug("City     : " + city0);
        log.debug("City     : " + city1);


        Distance distance = new Distance();
        try { distance.setId(Long.parseLong(id)); } catch (Exception ignored) { }

        try {
            distance.setDistance(Float.parseFloat(distanc));
        } catch (Exception e) {
            return new ResponseEntity<>("Distance is not valid.", HttpStatus.BAD_REQUEST);
        }

        // Set first city.
        City city = null;
        long longCityId = 0L;
        try {
            longCityId = Long.parseLong(city0);
            city = cityServiceImpl.findById(longCityId);
        } catch (Exception e) {
            return new ResponseEntity<>("First city is not valid.", HttpStatus.BAD_REQUEST);
        }
        if (city == null) {
            return new ResponseEntity<>("The city id is not valid.", HttpStatus.BAD_REQUEST);
        }
        distance.setCity0(city);

        // Set second city.
        try {
            longCityId = Long.parseLong(city1);
            city = cityServiceImpl.findById(longCityId);
        } catch (Exception e) {
            return new ResponseEntity<>("First city is not valid.", HttpStatus.BAD_REQUEST);
        }
        if (city == null) {
            return new ResponseEntity<>("The city id is not valid.", HttpStatus.BAD_REQUEST);
        }
        distance.setCity1(city);


        String distanceId = "";
        try {
            distanceId = distanceServiceImpl.saveDistance(distance).toString();
        } catch (Exception e) {
            log.error(e.toString() + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(distanceId, HttpStatus.OK);
    }
    @PostMapping("/entities/distance/delete/")
    @ResponseBody
    public ResponseEntity<String> delDistance(@RequestBody Map<String, String> reqParam, Model  model) {
        log.debug("saveDistance " + reqParam.get("id"));

        String id       = reqParam.get("id");
        log.debug("Id       : " + id);

        Distance distance = new Distance();
        try {
            distance = distanceServiceImpl.findById(Long.parseLong(id));
        } catch (Exception e) {
            log.error("Distance ID not found.");
            return new ResponseEntity<>("Distance ID not found.", HttpStatus.BAD_REQUEST);
        }

        try {
            distanceServiceImpl.delDistance(distance);
        } catch (Exception e) {
            log.error("Unable to delete distance.");
            return new ResponseEntity<>("Unable to delete distance.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Distance deleted successfully.", HttpStatus.OK);
    } //delDistance

    @PostMapping("/entities/distance/find/{mode}")
    // Desde queries.findDistance
    public String findDistance(@RequestBody Map<String,String> bundle, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("findDistance ");
        log.debug("findDistance " + bundle.get("distance"));

        float distance = 0.0F;
        try { distance = Float.parseFloat(bundle.get("distance")); } catch (Exception ignored) { }

        List<Distance> distanceList = distanceServiceImpl.getLessOrEqualByDistance(distance);

        model.addAttribute("mode", mode);
        model.addAttribute("distanceList", distanceList);

        return "entities/distance/distancequerybody";
    }



    // *****************************************************************************************************************

    @GetMapping({"/queries/distance/head/{mode}"})
    public String queryDistanceHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("queryDistanceHead " + mode);
        model.addAttribute("mode", mode);
        return "entities/distance/distancequeryhead";
    }

    @GetMapping({"/queries/distance/body"})
    public String queryDistanceBody() {
        log.debug("queryDistanceBody");
        return "entities/distance/distancequerybody";
    }

    @GetMapping({"/queries/distance/load/{id}"})
    @ResponseBody
    public Distance queryDistanceLoad(@PathVariable(value = "id") Long id) {
        log.debug("queryDistanceLoad " + id);
        return distanceServiceImpl.findById(id);
    }




    // *****************************************************************************************************************
    @GetMapping({"/queries/hours/{id1}/{id2}"})
    @ResponseBody
    public String querHoursCityCity(@PathVariable(value = "id1") Long id1, @PathVariable(value = "id2") Long id2) {
        log.debug("queryHoursCityCity " + id1 + " / " + id2);
        Float hours = distanceServiceImpl.getHours(id1, id2);
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(hours);
    }

    @PostMapping({"/queries/orderhours/"})
    @ResponseBody
    public Map<String,String> queryOrderHoursCityCity(@RequestBody Map<String,String> bundle) {
        log.debug("queryOrderHoursCityCity " + bundle.get("city1Id") + " / " + bundle.get("city2Id"));

        long longCity1Id;
        try {
            longCity1Id = Long.parseLong(bundle.get("city1Id"));
        } catch (Exception e) {
            return null;
        }

        long longCity2Id;
        try {
            longCity2Id = Long.parseLong(bundle.get("city2Id"));
        } catch (Exception e) {
            return null;
        }

        Float hours = distanceServiceImpl.getHours(longCity1Id, longCity2Id);

        DecimalFormat df = new DecimalFormat("###.##");

        Map<String,String> salida = new HashMap<>();
        salida.put("City1Id", bundle.get("city1Id"));
        salida.put("City2Id", bundle.get("city2Id"));
        salida.put ("hours", df.format(hours));

        log.debug("queryOrderHoursCityCity " + bundle.get("city1Id") + " / " + bundle.get("city2Id") + " : " + df.format(hours));
        return salida;
    }



}
