package es.tatanca.test.Entities.City;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class CityController {

    private CityServiceImpl cityServiceImpl;

    @GetMapping("/entities/city")
    public String getCityPage(Model model) {
        System.out.println("getCityPage");
        return getCityPageById(0L, model);
    }

    @GetMapping("/entities/city/{id}")
    // queries.loadCity
    public String getCityPageById(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getCityPageById");
        if (id > 0) {
            City city = cityServiceImpl.findById(id);
            model.addAttribute("city", city);
        }
        return "entities/city/city";
    }

    @GetMapping("/city/{id}")
    @ResponseBody
    public City getCity(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getCity " + id);
        return cityServiceImpl.findById(id);
    }

    @PostMapping("/saveCity")
    @ResponseBody
    // Control de transacciones.
    public ResponseEntity<String> saveCity(@RequestBody City city, Model model) {
        System.out.println("saveCity "+city.getId());

        String cityId = "";
        try {
            cityId = cityServiceImpl.saveCity(city).toString();
        } catch (Exception e) {
            System.out.println(e.toString() + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cityId, HttpStatus.OK);
    }

    @PostMapping("/findCity/{mode}")
    // Desde queries.findCity
    public String findCityExt(@RequestBody City city, @PathVariable(value = "mode") int mode, Model model) {
        System.out.println("findCity "+city.getName());
        List<City> cityList = cityServiceImpl.getLikeName(city.getName());
        model.addAttribute("mode",mode);
        model.addAttribute("cityList", cityList);
        return "entities/city/cityquerybody";
    }

    // ***************************************************************************************************************** CITY

    @GetMapping({"/city/query/head/{mode}"})
    public String queryCityHead(@PathVariable(value = "mode") int mode, Model model) throws Exception {
        System.out.println("queryHeadCity");
        model.addAttribute("mode", mode);
        return "entities/city/cityqueryhead";
    }

    @GetMapping({"/city/query/body"})
    public String queryCityBody() throws Exception {
        System.out.println("queryHeadCity");
        return "entities/city/cityquerybody";
    }









}

