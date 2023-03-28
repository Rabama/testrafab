package es.tatanca.logistics.entities.City;


import es.tatanca.logistics.entities.Cargo.Cargo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class CityController {

    private CityServiceImpl cityServiceImpl;

    @GetMapping("/entities/city/load/")
    public String getCityPage(Model model) {
        log.debug("getCityPage");
        return getCityPageById(0L, model);
    }

    @GetMapping("/entities/city/load/{id}")
    // queries.loadCity
    public String getCityPageById(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getCityPageById");
        if (id > 0) {
            City city = cityServiceImpl.findById(id);
            model.addAttribute("city", city);
        } else if (id == -1) {
            City city = new City();
            model.addAttribute("city", city);
        }
        return "entities/city/city";
    }


    @PostMapping("/entities/city/save/")
    @ResponseBody
    // Control de transacciones.
    public ResponseEntity<String> saveCity(@RequestBody City city) {
        log.debug("saveCity "+city.getId());

        String cityId;
        try {
            cityId = cityServiceImpl.saveCity(city).toString();
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cityId, HttpStatus.OK);
    }

    @PostMapping("/entities/city/delete/")
    @ResponseBody
    // Control de transacciones.
    public ResponseEntity<String> delCity(@RequestBody Map<String, String> reqParam) {
        log.debug("delCity "+ reqParam.get("id"));

        String id     = reqParam.get("id");
        log.debug("Id    : " + id);

        City city = new City();

        try {
            city = cityServiceImpl.findById(Long.parseLong(id));
        } catch (Exception e) {
            log.debug("City ID not found.");
            return new ResponseEntity<>("City ID not found.", HttpStatus.BAD_REQUEST);
        }

        try {
            cityServiceImpl.delCity(city);
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            log.info(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("City deleted successfully.", HttpStatus.OK);
    } // delCity


    @PostMapping("/entities/city/find/{mode}")
    // Desde queries.findCity
    public String findCity(@RequestBody City city, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("findCity "+city.getName());

        List<City> cityList = cityServiceImpl.getLikeName(city.getName());

        model.addAttribute("mode", mode);
        model.addAttribute("cityList", cityList);

        return "entities/city/cityquerybody";
    }

    // ***************************************************************************************************************** CITY

    @GetMapping({"/queries/city/head/{mode}"})
    public String queryCityHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("queryCityHead " + mode);
        model.addAttribute("mode", mode);
        return "entities/city/cityqueryhead";
    }

    @GetMapping({"/queries/city/body"})
    public String queryCityBody() {
        log.debug("queryCityBody");
        return "entities/city/cityquerybody";
    }

    @GetMapping({"/queries/city/load/{id}"})
    @ResponseBody
    public City queryCityLoad(@PathVariable(value = "id") Long id) {
        log.debug("queryCityLoad " + id);
        return cityServiceImpl.findById(id);
    }









}

