package es.tatanca.test.Entities.Truck;

import es.tatanca.test.Entities.City.City;
import es.tatanca.test.Entities.City.CityServiceImpl;
import es.tatanca.test.TestConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class TruckController {

    private final CityServiceImpl cityServiceImpl;
    private final TruckServiceImpl truckServiceImpl;

    @GetMapping("/entities/truck")
    public String getTruckPage(Model model) {
        System.out.println("getTruckPage");
        return getTruckPageById(0L, model);
    }

    @GetMapping("/entities/truck/{id}")
    // queries.loadTruck
    public String getTruckPageById(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getTruckPageById");

        // Las listas desplegables.
        model.addAttribute("statusValues", TruckStatus.values());

        if (id > 0) {
            Truck truck = truckServiceImpl.findById(id);
            if (truck != null) {
                model.addAttribute("truck", truck);

                // Y ahora los campos que son objetos o múltiples.
                City city = truck.getCity();
                if (city != null) {
                    model.addAttribute("cityid", city.getId());
                    model.addAttribute("cityname", city.getName());
                }


            }
        }
        return "entities/truck/truck";
    }

    @GetMapping("/truck/{id}")
    @ResponseBody
    public Truck getTruck(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getTruck " + id);
        Truck truck = truckServiceImpl.findById(id);
        System.out.println("getTruck " + id + "/ truck: " + truck);
        return truck;
    }

    @PostMapping("/saveTruck")
    @ResponseBody
//    public String saveTruck(@RequestBody Truck truck, Model model) {
    public String saveTruck(@RequestBody Map<String, String> reqParam, Model model) {
        String id       = reqParam.get("id");
        String number   = reqParam.get("number");
        String capacity = reqParam.get("capacity");
        String status   = reqParam.get("status");
        String cityId   = reqParam.get("cityId");

        System.out.println("Id      : " + id);
        System.out.println("Number  : " + number);
        System.out.println("Capacity: " + capacity);
        System.out.println("Status  : " + status);
        System.out.println("CityId  : " + cityId);

        Truck truck = new Truck();

        try {
            truck.setId(Long.parseLong(id));
        } catch (Exception e) {

        }

        truck.setNumber(number);

        try {
            truck.setCapacity(Double.parseDouble(capacity));
        } catch (Exception e) {

        }

//        AppConfig.TruckStatusConverter tsc = new AppConfig.TruckStatusConverter();
//        truck.setStatus(tsc.convert(status));
        truck.setStatus(TruckStatus.fromLongName(status));

        long longCityId = 0L;
        try {
            longCityId = Long.parseLong(cityId);
        } catch (Exception e) {

        }
        if (longCityId > 0) {
            System.out.println("longCityId1 : " + longCityId);
            City city = cityServiceImpl.findById(longCityId);
            System.out.println("longCityId2 : " + city.getId());
            truck.setCity(city);
        }

        Long truckId = truckServiceImpl.saveTruck(truck);

        return truckId.toString();
    } // saveTruck

    @PostMapping("/findTruck/{mode}")
    // Desde queries.findTruck
    public String findTruck(@RequestBody Truck truck, @PathVariable(value = "mode") int mode, Model model) {
        System.out.println("findTruck number: "+truck.getNumber()+" and status "+truck.getStatus());

        List<Truck> truckList = null;

//        AppConfig.TruckStatusConverter tsc = new AppConfig.TruckStatusConverter();
//        int status = 0;
//        if (truck.getStatus() == Truck.EnumStatus.NOK) status = 1;

        if (Objects.equals(truck.getNumber(), "")) {
            truckList = truckServiceImpl.getEqualStatus(truck.getStatus());
        } else {
            truckList = truckServiceImpl.getLikeNumberStatus(truck.getNumber(), truck.getStatus());
        }

        // ANTERIOR        List<Truck> truckList = truckServiceImpl.getLikeNumber(truck.getNumber());

        model.addAttribute("mode", mode);
        model.addAttribute("truckList", truckList);
        return "entities/truck/truckquerybody";
    } // findTruck



    // ***************************************************************************************************************** CITY

    @GetMapping({"/truck/query/head/{mode}"})
    public String queryTruckHead(@PathVariable(value = "mode") int mode, Model model) throws Exception {
        System.out.println("queryTruckHead");
        model.addAttribute("mode", mode);
        return "entities/truck/truckqueryhead";
    }

    @GetMapping({"/truck/query/body"})
    public String queryTruckBody() throws Exception {
        System.out.println("queryHeadTruck");
        return "entities/truck/truckquerybody";
    }



}

