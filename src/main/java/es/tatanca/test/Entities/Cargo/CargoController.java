package es.tatanca.test.Entities.Cargo;

import es.tatanca.test.Entities.City.City;
import es.tatanca.test.Entities.City.CityServiceImpl;
import es.tatanca.test.Entities.Truck.TruckStatus;
import es.tatanca.test.TestConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class CargoController {

    private final CityServiceImpl cityServiceImpl;
    private final CargoServiceImpl cargoServiceImpl;


    @GetMapping("/entities/cargo")
    public String getCargoPage(Model model) {
        System.out.println("getCargoPage");
        return getCargoPageById(0L, model);
    }

    @GetMapping("/entities/cargo/{id}")
    // queries.loadCargo
    public String getCargoPageById(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getCargoPageById");

        // Las listas desplegables.
        model.addAttribute("statusValues", CargoStatus.values());

        if (id > 0) {
            Cargo cargo = cargoServiceImpl.findById(id);
            model.addAttribute("cargo", cargo);

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

        }
        return "entities/cargo/cargo";
    }

    @GetMapping("/cargo/{id}")
    @ResponseBody
    public Cargo getCargo(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("getCargo " + id);
        return cargoServiceImpl.findById(id);
    }


    @GetMapping("/cargo/order/{id}")
    @ResponseBody
    public Map<String, String> getCargoOrder(@PathVariable(value = "id") Long id) {
        System.out.println("getCargoOrder " + id);

        Map<String, String> bundle = new HashMap<String, String>();
        Cargo cargo = cargoServiceImpl.findById(id);

        if (cargo != null) {
            bundle.put("id",     cargo.getId().toString());
            bundle.put("name",   cargo.getName());
            bundle.put("upcity", cargo.getUpcity().getName());
            bundle.put("uncity", cargo.getUncity().getName());
            bundle.put("weight", String.valueOf(cargo.getWeight()));
        }

        return bundle;
    }

    @PostMapping("/saveCargo")
    @ResponseBody
    public String saveCargo(@RequestBody Map<String, String> reqParam, Model model) {
        String id     = reqParam.get("id");
        String name   = reqParam.get("name");
        String weight = reqParam.get("weight");
        String status = reqParam.get("status");
        String upcity = reqParam.get("upcity");
        String uncity = reqParam.get("uncity");
        String address0 = reqParam.get("address0");
        String address1 = reqParam.get("address1");

        System.out.println("Id    : " + id);
        System.out.println("Name  : " + name);
        System.out.println("Weight: " + weight);
        System.out.println("Status: " + status);
        System.out.println("UpCity: " + upcity);
        System.out.println("UnCity: " + uncity);
        System.out.println("Address0: " + address0);
        System.out.println("Address1: " + address1);

        Cargo cargo = new Cargo();
        try {
            cargo.setId(Long.parseLong(id));
        } catch (Exception e) {

        }

        cargo.setName(name);

        try {
            cargo.setWeight(Double.parseDouble(weight));
        } catch (Exception e) {

        }

        cargo.setStatus(CargoStatus.fromLongName(status));

        long longUpCityId = 0L;
        try {
            longUpCityId = Long.parseLong(upcity);
        } catch (Exception e) {

        }
        if (longUpCityId > 0) {
            System.out.println("longUpUpCityId1 : " + longUpCityId);
            City city = cityServiceImpl.findById(longUpCityId);
            System.out.println("longUpCityId2 : " + city.getId());
            cargo.setUpcity(city);
        }

        long longUnCityId = 0L;
        try {
            longUnCityId = Long.parseLong(uncity);
        } catch (Exception e) {

        }
        if (longUnCityId > 0) {
            System.out.println("longUnUpCityId1 : " + longUnCityId);
            City city = cityServiceImpl.findById(longUnCityId);
            System.out.println("longUnCityId2 : " + city.getId());
            cargo.setUncity(city);
        }


        Long cargoId = cargoServiceImpl.saveCargo(cargo);
        return cargoId.toString();
    } // saveCargo


    @PostMapping("/findCargo/{mode}")
    // Desde queries.findCargo
    public String findCargo(@RequestBody Cargo cargo, @PathVariable(value = "mode") int mode, Model model) {
        System.out.println("findCargo nane: "+cargo.getName()+" and status "+cargo.getStatus());

        List<Cargo> cargoList = null;

//        AppConfig.CargoStatusConverter tsc = new AppConfig.CargoStatusConverter();
//        int status = 0;
//        if (cargo.getStatus() == Cargo.EnumStatus.ASSIGNED) status = 1;

        if (Objects.equals(cargo.getName(), "")) {
            cargoList = cargoServiceImpl.getEqualStatus(cargo.getStatus());
        } else {
            cargoList = cargoServiceImpl.getLikeNameStatus(cargo.getName(), cargo.getStatus());
        }

        model.addAttribute("mode", mode);
        model.addAttribute("cargoList", cargoList);
        return "entities/cargo/cargoquerybody";
    }





    // ***************************************************************************************************************** CITY


    @GetMapping({"/cargo/query/head/{mode}"})
    public String queryCargoHead(@PathVariable(value = "mode") int mode, Model model) throws Exception {
        System.out.println("queryHeadCargo");
        model.addAttribute("mode", mode);
        return "entities/cargo/cargoqueryhead";
    }

    @GetMapping({"/cargo/query/body"})
    public String queryCargoBody() throws Exception {
        System.out.println("queryHeadCargo");
        return "entities/cargo/cargoquerybody";
    }









}

