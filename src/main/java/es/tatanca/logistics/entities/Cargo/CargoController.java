package es.tatanca.logistics.entities.Cargo;

import es.tatanca.logistics.entities.CargoOrder;
import es.tatanca.logistics.entities.CargoSearch;
import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityServiceImpl;
import es.tatanca.logistics.entities.Distance.DistanceServiceImpl;
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
public class CargoController {

    private final CityServiceImpl cityServiceImpl;
    private final CargoServiceImpl cargoServiceImpl;
    private final DistanceServiceImpl distanceServiceImpl;


    @GetMapping("/entities/cargo/load/")
    public String getCargoPage(Model model) {
        log.debug("getCargoPage");
        return getCargoPageById(0L, model);
    }

    @GetMapping("/entities/cargo/load/{id}")
    // queries.loadCargo
    public String getCargoPageById(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getCargoPageById");

        // Las listas desplegables.
        model.addAttribute("statusValues", CargoStatus.values());

        if (id > 0) {
            Cargo cargo = cargoServiceImpl.findById(id);
            model.addAttribute("cargo", cargo);

/*            if (cargo.getDriver1() != null) {
                model.addAttribute("driver1id", cargo.getDriver1().getId());
                model.addAttribute("driver1name", cargo.getDriver1().getName());
            }
            if (cargo.getDriver2() != null) {
                model.addAttribute("driver2id", cargo.getDriver2().getId());
                model.addAttribute("driver2name", cargo.getDriver2().getName());
            }
            if (cargo.getDriver3() != null) {
                model.addAttribute("driver3id", cargo.getDriver3().getId());
                model.addAttribute("driver3name", cargo.getDriver3().getName());
            }*/

            // Y ahora los campos que son objetos o múltiples.
            City upcity = cargo.getUpcity();
            if (upcity != null) {
                model.addAttribute("upcity", upcity.getId());
                model.addAttribute("upcityname", upcity.getName());
            }
            City uncity = cargo.getUncity();
            if (uncity != null) {
                model.addAttribute("uncity", uncity.getId());
                model.addAttribute("uncityname", uncity.getName());
            }

        } else if (id == -1) {
            Cargo cargo = new Cargo();
            model.addAttribute("cargo", cargo);
        }
        return "entities/cargo/cargo";
    }

    @PostMapping("/entities/cargo/save/")
    @ResponseBody
    public ResponseEntity<String> saveCargo(@RequestBody Map<String, String> reqParam, Model model) {
        log.debug("saveCargo");

        String id     = reqParam.get("id");
        String name   = reqParam.get("name");
        String weight = reqParam.get("weight");
        String status = reqParam.get("status");
        String upcity = reqParam.get("upcity");
        String uncity = reqParam.get("uncity");
        String address0 = reqParam.get("address0");
        String address1 = reqParam.get("address1");

        log.debug("Id    : " + id);
        log.debug("Name  : " + name);
        log.debug("Weight: " + weight);
        log.debug("Status: " + status);
        log.debug("UpCity: " + upcity);
        log.debug("UnCity: " + uncity);
        log.debug("Address0: " + address0);
        log.debug("Address1: " + address1);

        Cargo cargo = new Cargo();

        if (!id.equals("")) {
            try {
                cargo.setId(Long.parseLong(id));
            } catch (Exception e) {
                return new ResponseEntity<>("Cargo id is not valid.", HttpStatus.BAD_REQUEST);
            }
        }

        cargo.setName(name);

        try {
            cargo.setWeight(Double.parseDouble(weight));
        } catch (Exception e) {
            return new ResponseEntity<>("Weight is not valid.", HttpStatus.BAD_REQUEST);
        }

        cargo.setStatus(CargoStatus.fromLongName(status));

        long longUpCityId;
        try {
            longUpCityId = Long.parseLong(upcity);
        } catch (Exception e) {
            return new ResponseEntity<>("Upload city is not valid.", HttpStatus.BAD_REQUEST);
        }
        if (longUpCityId > 0) {
            City city = cityServiceImpl.findById(longUpCityId);
            log.debug("longUpCityId: " + city.getId());
            cargo.setUpcity(city);
        }

        long longUnCityId;
        try {
            longUnCityId = Long.parseLong(uncity);
        } catch (Exception e) {
            return new ResponseEntity<>("Unload city is not valid.", HttpStatus.BAD_REQUEST);
        }
        if (longUnCityId > 0) {
            City city = cityServiceImpl.findById(longUnCityId);
            log.debug("longUnCityId: " + city.getId());
            cargo.setUncity(city);
        }


        try {
            distanceServiceImpl.getDistance(cargo.getUpcity(), cargo.getUncity());
        } catch (Exception e) {
            return new ResponseEntity<>("Distance between " + cargo.getUpcity().getName() + " and " + cargo.getUncity().getName() + " is not defined.", HttpStatus.BAD_REQUEST);
        }


        String cargoId;
        try {
            cargoId = cargoServiceImpl.saveCargo(cargo).toString();
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            log.info(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cargoId, HttpStatus.OK);
    } // saveCargo

    @PostMapping("/entities/cargo/delete/")
    @ResponseBody
    public ResponseEntity<String> delCargo(@RequestBody Map<String, String> reqParam, Model model) {
        log.debug("delCargo");

        String id     = reqParam.get("id");
        log.debug("Id    : " + id);

        Cargo cargo = new Cargo();

        try {
            cargo = cargoServiceImpl.findById(Long.parseLong(id));
        } catch (Exception e) {
            log.debug("Cargo ID not found.");
            return new ResponseEntity<>("Cargo ID not found.", HttpStatus.BAD_REQUEST);
        }

        try {
            cargoServiceImpl.delCargo(cargo);
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            log.info(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Cargo deleted successfully.", HttpStatus.OK);
    } // delCargo


    @PostMapping("/entities/cargo/find/{mode}")
    // Desde queries.findCargo
    public String findCargo(@RequestBody Map<String,String> bundle, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("findCargo: In,");
        log.debug("name    : " + bundle.get("name"));
        log.debug("status  : " + bundle.get("status"));
        log.debug("upcityid: " + bundle.get("upcityid"));
        log.debug("uncityid: " + bundle.get("uncityid"));
        log.debug("weight  : " + bundle.get("weight"));

        String name   = bundle.get("name");
        CargoStatus status = CargoStatus.fromLongName(bundle.get("status"));

        City upcity = null;
        try { upcity = cityServiceImpl.findById(Long.parseLong(bundle.get("upcityid"))); } catch (Exception ignored) { }

        City uncity = null;
        try { uncity = cityServiceImpl.findById(Long.parseLong(bundle.get("uncityid"))); } catch (Exception ignored) { }

        float weight = 0.0f;
        try { weight = Float.parseFloat(bundle.get("weight")); } catch (Exception ignored) { }

        HashMap<String, Object> searchMap = new HashMap<>();
        if (!name.equals("")) { searchMap.put("name",   name); }
                                searchMap.put("status", status);
        if (upcity != null) { searchMap.put("upcity",   upcity); }
        if (uncity != null) { searchMap.put("uncity",   uncity); }
        if (weight != 0.0f) { searchMap.put("weight",   weight); }

        List<Cargo> cargoList = cargoServiceImpl.getMultiple(searchMap);

        List<CargoSearch> cargoSearchList = new ArrayList<>();
        for (Cargo cl: cargoList) {
            cargoSearchList.add(new CargoSearch() {{
                setId(cl.getId().toString());
                setName(cl.getName());
                setStatus(cl.getStatus().toString());
                setUpCity(cl.getUpcity().getName());
                setUnCity(cl.getUncity().getName());
                setWeight(String.valueOf(cl.getWeight()));
            }});
        }


        model.addAttribute("mode", mode);
        model.addAttribute("cargoSearchList", cargoSearchList);

        log.debug("findCargo: Out,");
        return "entities/cargo/cargoquerybody";
    }





    // ***************************************************************************************************************** CITY


    @GetMapping({"/queries/cargo/head/{mode}"})
    public String queriesCargoHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("queryHeadCargo");
        model.addAttribute("mode", mode);
        model.addAttribute("statusValues", CargoStatus.values());
        return "entities/cargo/cargoqueryhead";
    }

    @GetMapping({"/queries/cargo/body"})
    public String queriesCargoBody() {
        log.debug("queryHeadCargo");
        return "entities/cargo/cargoquerybody";
    }

    @GetMapping({"/queries/cargo/load/{id}"})
    @ResponseBody
    public Map<String, String> queryCargoLoad(@PathVariable(value = "id") Long id) {
        log.debug("queryCargoLoad " + id);

        Cargo cargo = cargoServiceImpl.findById(id);

        if (cargo != null) {
            CargoOrder cargoOrder = new CargoOrder(cargo);
            return cargoOrder.toMap();
        }

        return null;
    }








}

