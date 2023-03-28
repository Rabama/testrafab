package es.tatanca.logistics.entities.Driver;

import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityServiceImpl;
import es.tatanca.logistics.entities.DriverSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class DriverController {

    private final CityServiceImpl cityServiceImpl;
    private final DriverServiceImpl driverServiceImpl;


    @GetMapping("/entities/driver/load/")
    public String getDriverPage(Model model) {
        log.debug("getDriverPage");
        return getDriverPageById(0L, model);
    }

    @GetMapping("/entities/driver/load/{id}")
    // queries.loadDriver
    public String getDriverPageById(@PathVariable(value = "id") Long id, Model model) {
        log.debug("getDriverPageById");

        // Las listas desplegables.
        model.addAttribute("statusValues", DriverStatus.values());

        if (id > 0) {
            Driver driver = driverServiceImpl.findById(id);
            if (driver != null) {
                model.addAttribute("driver", driver);

                City city = driver.getCity();
                if (city != null) {
                    model.addAttribute("cityid", city.getId());
                    model.addAttribute("cityname", city.getName());
                }

            }
        } else if (id == -1) {
            Driver driver = new Driver();
            model.addAttribute("driver", driver);
        }

        return "entities/driver/driver";
    }

    // *****************************************************************************************************************

    @PostMapping("/entities/driver/save/")
    @ResponseBody
    public ResponseEntity<String> saveDriver(@RequestBody Map<String, String> reqParam) {
        log.debug("saveDriver");

        String id       = reqParam.get("id");
        String name     = reqParam.get("name");
        String surname  = reqParam.get("surname");
        String workedh  = reqParam.get("workedhours");
        String status   = reqParam.get("status");
        String username = reqParam.get("username");
        String cityId   = reqParam.get("cityid");

        log.debug("Id      : " + id);
        log.debug("Name    : " + name);
        log.debug("Surname : " + surname);
        log.debug("WorkedH : " + workedh);
        log.debug("Status  : " + status);
        log.debug("Username: " + username);
        log.debug("CityId  : " + cityId);

        Driver driver = new Driver();

        // Si tenemos id lo asignamos para no crear otro registro.
        try { driver.setId(Long.parseLong(id)); } catch (Exception ignored) { }

        driver.setName(name);

        driver.setSurname(surname);

        try {
            driver.setWorkedHours(Double.valueOf(workedh));
        } catch (Exception e) {
            return new ResponseEntity<>("The worked hours are not correct.", HttpStatus.BAD_REQUEST);
        }

        driver.setStatus(DriverStatus.fromLongName(status));

        driver.setUsername(username);
        log.debug("1 Driver.username " + driver.getUsername());

        City city = null;
        try {
            long longCityId = Long.parseLong(cityId);
            city = cityServiceImpl.findById(longCityId);
        } catch (Exception e) {
            return new ResponseEntity<>("The city id is not valid.", HttpStatus.BAD_REQUEST);
        }
        if (city == null) {
            return new ResponseEntity<>("The city id is not valid.", HttpStatus.BAD_REQUEST);
        }
        driver.setCity(city);

        String driverId;
        try {
            driverId = driverServiceImpl.saveDriver(driver).toString();
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(driverId, HttpStatus.OK);
    } // saveDriver

    // *****************************************************************************************************************

    @PostMapping("/entities/driver/delete/")
    @ResponseBody
    public ResponseEntity<String> delDriver(@RequestBody Map<String, String> reqParam) {
        log.debug("delDriver");

        String id = reqParam.get("id");
        log.debug("Id : " + id);

        Driver driver = new Driver();

        try {
            driver = driverServiceImpl.findById(Long.parseLong(id));
        } catch (Exception e) {
            log.debug("Driver ID not found.");
            return new ResponseEntity<>("Driver ID not found.", HttpStatus.BAD_REQUEST);
        }

        try {
            driverServiceImpl.delDriver(driver);
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            log.info(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Driver deleted successfully.", HttpStatus.OK);
    } // delDriver


    // *****************************************************************************************************************


    @PostMapping("/entities/driver/find/{mode}")
    // Desde queries.findDriver
    public String findDriver(@RequestBody Map<String,String> bundle, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("findDriver: In,");
        log.debug("name  : " + bundle.get("name"));
        log.debug("status: " + bundle.get("status"));
        log.debug("cityid: " + bundle.get("cityid"));

        String name = bundle.get("name");
        DriverStatus status = DriverStatus.fromLongName(bundle.get("status"));

        City city = null;
        try { city = cityServiceImpl.findById(Long.parseLong(bundle.get("cityid"))); } catch (Exception ignored) { }

        HashMap<String, Object> searchMap = new HashMap<>();
        if (!name.equals("")) { searchMap.put("name", name); }
        searchMap.put("status", status);
        if (city != null)     { searchMap.put("city",   city); }

        List<Driver> driverList = driverServiceImpl.getMultiple(searchMap);

        List<DriverSearch> driverSearchList = new ArrayList<>();
        for (Driver dl: driverList) {
            driverSearchList.add(new DriverSearch() {{
                setId(dl.getId().toString());
                setName(dl.getName());
                setStatus(dl.getStatus().toString());
                if (dl.getCity() != null) { setCity(dl.getCity().getName()); }
            }});
        }

        model.addAttribute("mode", mode);
        model.addAttribute("driverList", driverSearchList);

        log.debug("findDriver: Out,");
        return "entities/driver/driverquerybody";
    }


    // ***************************************************************************************************************** CITY


    @GetMapping({"/queries/driver/head/{mode}"})
    public String queryDriverHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("queryDriverHead");
        model.addAttribute("mode", mode);
        model.addAttribute("statusValues", DriverStatus.values());
        return "entities/driver/driverqueryhead";
    }

    @GetMapping({"/queries/driver/body"})
    public String queryDriverBody() {
        log.debug("queryHeadDriver");
        return "entities/driver/driverquerybody";
    }

    @GetMapping({"/queries/driver/load/{id}"})
    @ResponseBody
    public Driver queryDriverLoad(@PathVariable(value = "id") Long id) {
        log.debug("queryDriverLoad " + id);
        return driverServiceImpl.findById(id);
    }
}
