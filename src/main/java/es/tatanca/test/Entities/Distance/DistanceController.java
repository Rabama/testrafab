package es.tatanca.test.Entities.Distance;

import es.tatanca.test.Entities.City.City;
import es.tatanca.test.Entities.City.CityServiceImpl;
import es.tatanca.test.Entities.Distance.DistanceServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class DistanceController {

    private DistanceServiceImpl distanceServiceImpl;
    private CityServiceImpl cityServiceImpl;

    @GetMapping("/entities/distance")
    public String getDistancePage(Model model) {
        System.out.println("getDistancePage");
        return getDistancePageById(0L, model);
    }

    @GetMapping("/entities/distance/{id}")
    public String getDistancePageById(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getDistancePageById");
        if (id > 0) {
            Distance distance = distanceServiceImpl.findById(id);
            model.addAttribute("distance", distance);
        }
        return "entities/distance/distance";
    }

    @GetMapping("/distance/{id}")
    @ResponseBody
    public Distance getDistance(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getDistance " + id);
        return distanceServiceImpl.findById(id);
    }

    @PostMapping("/saveDistance")
    @ResponseBody
    public ResponseEntity<String> saveDistance(@RequestBody Map<String, String> reqParam, Model  model) {
        System.out.println("saveDistance " + reqParam.get("id"));

        String id       = reqParam.get("id");
        String distanc   = reqParam.get("distance");
        String time = reqParam.get("time");
        String city0   = reqParam.get("city");
        String city1   = reqParam.get("city1");

        System.out.println("Id       : " + id);
        System.out.println("Distance : " + distanc);
        System.out.println("Time     : " + time);
        System.out.println("City     : " + city0);
        System.out.println("City     : " + city1);


        Distance distance = new Distance();
        try {
            distance.setId(Long.parseLong(id));
        } catch (Exception e) {

        }

        try {
            distance.setTime(Double.parseDouble(time));
        } catch (Exception e) {

        }

        try {
            distance.setDistance(Double.parseDouble(distanc));
        } catch (Exception e) {

        }


        long longCityId = 0L;
        try {
            longCityId = Long.parseLong(city0);
        } catch (Exception e) {

        }
        if (longCityId > 0) {
            System.out.println("longCityId1 : " + longCityId);
            City city = cityServiceImpl.findById(longCityId);
            System.out.println("longCityId2 : " + city.getId());
            distance.setCity(city);
        }

        try {
            longCityId = Long.parseLong(city1);
        } catch (Exception e) {

        }
        if (longCityId > 0) {
            System.out.println("longCityId1 : " + longCityId);
            City city = cityServiceImpl.findById(longCityId);
            System.out.println("longCityId2 : " + city.getId());
            distance.setCity1(city);
        }

        String distanceId = "";
        try {
            distanceId = distanceServiceImpl.saveDistance(distance).toString();
        } catch (Exception e) {
            System.out.println(e.toString() + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(distanceId, HttpStatus.OK);
    }

 /*   @PostMapping("/findDistance/{mode}")
    public String findDistanceExt(@RequestBody Distance distance, @PathVariable(value = "mode") int mode, Model model) {
        System.out.println("findDistance" + distance.getCity());
        List<Distance> distanceList = distanceServiceImpl.getLikeByCampo()
    }*/
}
