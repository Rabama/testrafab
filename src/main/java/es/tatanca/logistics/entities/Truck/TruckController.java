package es.tatanca.logistics.entities.Truck;

import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityServiceImpl;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Order.OrderServiceImpl;
import es.tatanca.logistics.entities.TruckSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@AllArgsConstructor
@Slf4j
public class TruckController {

    private final CityServiceImpl cityServiceImpl;
    private final TruckServiceImpl truckServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    // *****************************************************************************************************************
    @GetMapping("/entities/truck/load/")
    public String getTruckPage(Model model) {
        log.debug("getTruckPage");
        return getTruckPageById(0L, model);
    }

    // *****************************************************************************************************************
    @GetMapping("/entities/truck/load/{id}")
    // queries.loadTruck
    public String getTruckPageById(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getTruckPageById");

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
        } else if (id == -1) {
            Truck truck = new Truck();
            model.addAttribute("truck", truck);
        }
        return "entities/truck/truck";
    }

    // *****************************************************************************************************************
    @PostMapping("/entities/truck/save/")
    @ResponseBody
//    public String saveTruck(@RequestBody Truck truck, Model model) {
    public ResponseEntity<String> saveTruck(@RequestBody Map<String, String> reqParam) {
        log.debug("savetruck");

        String id       = reqParam.get("id");
        String number   = reqParam.get("number");
        String capacity = reqParam.get("capacity");
        String status   = reqParam.get("status");
        String cityId   = reqParam.get("cityId");

        log.debug("Id      : " + id);
        log.debug("Number  : " + number);
        log.debug("Capacity: " + capacity);
        log.debug("Status  : " + status);
        log.debug("CityId  : " + cityId);

        Truck truck = new Truck();

        try { truck.setId(Long.parseLong(id)); } catch (Exception ignored) { }

        truck.setNumber(number);

        try {
            truck.setCapacity(Double.parseDouble(capacity));
        } catch (Exception e) {
            return new ResponseEntity<>("The capacity is not correct.", HttpStatus.BAD_REQUEST);
        }

        truck.setStatus(TruckStatus.fromLongName(status));


        long longCityId;
        try {
            longCityId = Long.parseLong(cityId);
        } catch (Exception e) {
            return new ResponseEntity<>("The city id is not correct.", HttpStatus.BAD_REQUEST);
        }
        if (longCityId > 0) {
            log.debug("longCityId1 : " + longCityId);
            City city = cityServiceImpl.findById(longCityId);
            log.debug("longCityId2 : " + city.getId());
            truck.setCity(city);
        }

        String truckId;
        try {
            truckId = truckServiceImpl.saveTruck(truck).toString();
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(truckId, HttpStatus.OK);
    } // saveTruck

    // *****************************************************************************************************************
    @PostMapping("/entities/truck/delete/")
    @ResponseBody
    public ResponseEntity<String> delTruck(@RequestBody Map<String, String> reqParam) {
        log.debug("delTruck");

        String id       = reqParam.get("id");
        log.debug("Id      : " + id);

        Truck truck = new Truck();

        try {
            truck = truckServiceImpl.findById(Long.parseLong(id));
        } catch (Exception e) {
            log.debug("Truck ID not found.");
            return new ResponseEntity<>("Truck ID not found.", HttpStatus.BAD_REQUEST);
        }

        try {
            truckServiceImpl.delTruck(truck);
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Truck deleted successfully.", HttpStatus.OK);
    } // saveTruck


    // *****************************************************************************************************************
    @PostMapping("/entities/truck/find/{mode}")
    // Desde queries.findTruck
    public String findTruck(@RequestBody Map<String,String> bundle, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("findTruck: In.");
        log.debug("number:   " + bundle.get("number"));
        log.debug("status:   " + bundle.get("status"));
        log.debug("cityid:   " + bundle.get("cityid"));
        log.debug("capacity: " + bundle.get("capacity"));

        String number = bundle.get("number");
        TruckStatus status = TruckStatus.fromLongName(bundle.get("status"));

        City city = null;
        try { city = cityServiceImpl.findById(Long.parseLong(bundle.get("cityid"))); } catch (Exception ignored) { }

        HashMap<String, Object> searchMap = new HashMap<>();
        if (!number.equals("")) { searchMap.put("number", number); }
        searchMap.put("status", status);
        if (city != null)       { searchMap.put("city",   city); }

        float floatCapacity = 0.0F;
        try { floatCapacity = Float.parseFloat(bundle.get("capacity")); } catch (Exception ignored) { }
        if (floatCapacity > 0) { searchMap.put("capacity", floatCapacity); }

        List<Truck> truckList = truckServiceImpl.getMultiple(searchMap);

        List<TruckSearch> truckSearchList = new ArrayList<>();
        for (Truck tl: truckList) {
            truckSearchList.add(new TruckSearch() {{
                setId(tl.getId().toString());
                setNumber(tl.getNumber());
                setStatus(tl.getStatus().toString());
                setCity(tl.getCity().getName());
                setCapacity(tl.getCapacity().toString());
            }});
        }

        model.addAttribute("mode", mode);
        model.addAttribute("truckList", truckSearchList);

        log.debug("findTruck: Out,");
        return "entities/truck/truckquerybody";
    }



    // *****************************************************************************************************************
    //                  QUERIES
    // *****************************************************************************************************************
    @GetMapping({"/queries/truck/head/{mode}"})
    public String queryTruckHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("queryTruckHead");
        model.addAttribute("mode", mode);
        model.addAttribute("statusValues", TruckStatus.values());
        return "entities/truck/truckqueryhead";
    }

    @GetMapping({"/queries/truck/body"})
    public String queryTruckBody() {
        log.debug("queryHeadTruck");
        return "entities/truck/truckquerybody";
    }

    @GetMapping({"/queries/truck/load/{id}"})
    @ResponseBody
    public Truck queryTruckLoad(@PathVariable(value = "id") Long id) {
        log.debug("queryTruckLoad " + id);
        return truckServiceImpl.findById(id);
    }


}

