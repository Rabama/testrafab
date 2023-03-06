package es.tatanca.test.Entities.Driver;

import es.tatanca.test.TestConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class DriverController {

//    @Autowired
    private DriverServiceImpl driverServiceImpl;

    @GetMapping("/entities/driver")
    public String getDriverPage(Model model) {
        System.out.println("getDriverPage");
        return getDriverPageById(0L, model);
    }

    @GetMapping("/entities/driver/{id}")
    // queries.loadDriver
    public String getDriverPageById(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getDriverPageById");
        if (id > 0) {
            Driver driver = driverServiceImpl.findById(id);
            if (driver != null) {
                model.addAttribute("driver", driver);
            }
        }
        return "entities/driver/driver";
    }

    @GetMapping("/driver/{id}")
    @ResponseBody
    public Driver getDriver(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getDriver " + id);
        Driver driver = driverServiceImpl.findById(id);
        System.out.println("getDriver " + id + "/ driver: " + driver);
        return driver;
    }

    @PostMapping("/saveDriver")
    @ResponseBody
//    public String saveDriver(@RequestBody Driver driver, Model model) {
    public String saveDriver(@RequestBody Map<String, String> reqParam, Model model) {
        String id       = reqParam.get("id");
        String name     = reqParam.get("name");
        String surname  = reqParam.get("surname");
        String workedh  = reqParam.get("workedhours");
        String status   = reqParam.get("status");

        System.out.println("Id      : " + id);
        System.out.println("Name    : " + name);
        System.out.println("Surname : " + surname);
        System.out.println("WorkedH : " + workedh);
        System.out.println("Status  : " + status);

        Driver driver = new Driver();

        try {
            driver.setId(Long.parseLong(id));
        } catch (Exception e) {

        }

        driver.setName(name);
        driver.setSurname(surname);

        try {
            driver.setWorkedHours(Double.valueOf(workedh));
        } catch (Exception e) {

        }

        TestConfig.DriverStatusConverter tsc = new TestConfig.DriverStatusConverter();
        driver.setStatus(tsc.convert(status));

        Long driverId = driverServiceImpl.saveDriver(driver);

        return driverId.toString();
    } // saveDriver

    @PostMapping("/findDriver/{mode}")
    // Desde queries.findDriver
    public String findDriver(@RequestBody Driver driver, @PathVariable(value = "mode") int mode, Model model) {
        System.out.println("findDriver nsme: "+driver.getName()+" and status "+driver.getStatus());

        List<Driver> driverList = null;

        TestConfig.DriverStatusConverter tsc = new TestConfig.DriverStatusConverter();
        int status = 0;
        if (driver.getStatus() == Driver.EnumStatus.REST) status = 1;

        if (Objects.equals(driver.getName(), "")) {
            driverList = driverServiceImpl.getEqualStatus(driver.getStatus());
        } else {
            driverList = driverServiceImpl.getLikeNumberStatus(driver.getName(), driver.getStatus());
        }

        // ANTERIOR        List<Driver> driverList = driverServiceImpl.getLikeNumber(driver.getNumber());

        model.addAttribute("mode", mode);
        model.addAttribute("driverList", driverList);
        return "entities/driver/driverquerybody";
    } // findDriver



    // ***************************************************************************************************************** CITY

    @GetMapping({"/driver/query/head/{mode}"})
    public String queryDriverHead(@PathVariable(value = "mode") int mode, Model model) throws Exception {
        System.out.println("queryDriverHead");
        model.addAttribute("mode", mode);
        return "entities/driver/driverqueryhead";
    }

    @GetMapping({"/driver/query/body"})
    public String queryDriverBody() throws Exception {
        System.out.println("queryHeadDriver");
        return "entities/driver/driverquerybody";
    }

}
