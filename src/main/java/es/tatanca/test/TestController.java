package es.tatanca.test;

import es.tatanca.test.Entities.City.*;
import es.tatanca.test.Entities.Truck.*;
import es.tatanca.test.Entities.Cargo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TestController {

//    @Autowired
    private final CityServiceImpl cityServiceImpl;
//    @Autowired
    private final TruckServiceImpl truckServiceImpl;
//    @Autowired
    private final CargoServiceImpl cargoServiceImpl;

    TestController(CityServiceImpl cityServiceImpl, TruckServiceImpl truckServiceImpl, CargoServiceImpl cargoServiceImpl) {
        this.cityServiceImpl = cityServiceImpl;
        this.truckServiceImpl = truckServiceImpl;
        this.cargoServiceImpl = cargoServiceImpl;
    }

    @GetMapping("/")
    public String viewHomePage() {
        return "redirect:/index.html";
    }

    @GetMapping("/city")
    public String viewCity(Model model) {
        System.out.println("viewCity");
        City city = null;
        model.addAttribute("city", city);
        return "city";
    }

    @PostMapping("/city/save")
    public String saveCity(@ModelAttribute("city") City city) {
        cityServiceImpl.saveCity(city);
        return "redirect:/";
    }


    @GetMapping("/city/{id}")
    public String getCity(@PathVariable(value = "id") String valor, Model model) {
        System.out.println("getCity " + valor);
        City city = cityServiceImpl.getEqualByCampo("id", valor);
        model.addAttribute("city", city);
        return "city";
    }

    @GetMapping("/truck")
    public String viewTruck(Model model) {
        System.out.println("viewTruck");
        Truck truck = null;
        model.addAttribute("truck", truck);
        return "truck";
    }
    @PostMapping("/truck/save")
    public String saveTruck(@ModelAttribute("truck") Truck truck) {
        truckServiceImpl.saveTruck(truck);
        return "redirect:/";
    }
    @GetMapping("/truck/{id}")
    public String getTruck(@PathVariable(value = "id") String valor, Model model) {
        System.out.println("getTruck " + valor);
        Truck truck = truckServiceImpl.getEqualByCampo("id", valor);
        model.addAttribute("truck", truck);
        return "truck";
    }

    @GetMapping("/cargo")
    public String viewCargo(Model model) {
        System.out.println("viewCargo");
        Cargo cargo = null;
        model.addAttribute("cargo", cargo);
        return "cargo";
    }
    @PostMapping("/cargo/save")
    public String saveCargo(@ModelAttribute("cargo") Cargo cargo) {
        cargoServiceImpl.saveCargo(cargo);
        return "redirect:/";
    }
    @GetMapping("/cargo/{id}")
    public String getCargo(@PathVariable(value = "id") String valor, Model model) {
        System.out.println("getCargo " + valor);
        Cargo cargo = cargoServiceImpl.getEqualByCampo("id", valor);
        model.addAttribute("cargo", cargo);
        return "cargo";
    }

    @GetMapping("/queries/equal/{table}/{campo}/{valor}")
    public String queriesCityEqual(@PathVariable(value = "table") String table, @PathVariable(value = "campo") String campo, @PathVariable(value = "valor") String valor, Model model) {
        System.out.println("queriesCityEqual");
        String outString = "";
        switch(table) {
            case "city"-> {
                City city = cityServiceImpl.getEqualByCampo(campo, valor);
                if (city != null) {
                    List<City> cityList = new ArrayList<>();
                    cityList.add(city);
                    model.addAttribute("prueba", "/queries/city/equal/" + campo + "/" + valor);
                    model.addAttribute("queryList", cityList);
                    outString = "queries/cityquery";
                }
            }
            case "truck"-> {
                Truck truck = truckServiceImpl.getEqualByCampo(campo, valor);
                if (truck != null) {
                    List<Truck> truckList = new ArrayList<>();
                    truckList.add(truck);
                    model.addAttribute("prueba", "/queries/truck/equal/" + campo + "/" + valor);
                    model.addAttribute("queryList", truckList);
                    outString = "queries/truckquery";
                }
            }
            case "cargo"-> {
                Cargo cargo = cargoServiceImpl.getEqualByCampo(campo, valor);
                if (cargo != null) {
                    List<Cargo> cargoList = new ArrayList<>();
                    cargoList.add(cargo);
                    model.addAttribute("prueba", "/queries/cargo/equal/" + campo + "/" + valor);
                    model.addAttribute("queryList", cargoList);
                    outString = "queries/cargoquery";
                }
            }
            default -> {

            }
        }
        return outString;
    }

    @GetMapping("/queries/like/{table}/{campo}/{valor}")
    public String queriesCityLike(@PathVariable(value = "table") String table, @PathVariable(value = "campo") String campo, @PathVariable(value = "valor") String valor, Model model) {
        System.out.println("queriesCityLike");
        String outString = "";
        switch(table) {
            case "city"-> {
                List<City> cityList = cityServiceImpl.getLikeByCampo(campo, valor);
                if (cityList != null) {
                    System.out.println("queriesCityLike: " + cityList.size() + " entradas.");
                    for (City city : cityList) {
                        System.out.println("  City: " + city.id + " / " + city.name);
                    }
                    model.addAttribute("prueba", "/queries/city/like/" + campo + "/" + valor);
                    model.addAttribute("queryList", cityList);
                }
                outString = "queries/cityquery";
            }
            case "truck"-> {
                List<Truck> truckList = truckServiceImpl.getLikeByCampo(campo, valor);
                if (truckList != null) {
                    System.out.println("queriesTruckLike: " + truckList.size() + " entradas.");
                    for (Truck truck : truckList) {
                        System.out.println("  Truck: " + truck.id + " / " + truck.number);
                    }
                    model.addAttribute("prueba", "/queries/truck/like/" + campo + "/" + valor);
//                    model.addAttribute("queryField", Truck);
                    model.addAttribute("queryList", truckList);
                }
                outString = "queries/truckquery";
            }
            case "cargo"-> {
                List<Cargo> cargoList = cargoServiceImpl.getLikeByCampo(campo, valor);
                if (cargoList != null) {
                    System.out.println("queriesCargoLike: " + cargoList.size() + " entradas.");
                    for (Cargo cargo : cargoList) {
                        System.out.println("  Cargo: " + cargo.id + " / " + cargo.name);
                    }
                    model.addAttribute("prueba", "/queries/cargo/like/" + campo + "/" + valor);
//                    model.addAttribute("queryField", Cargo);
                    model.addAttribute("queryList", cargoList);
                }
                outString = "queries/cargoquery";
            }
            default -> {

            }
        }
        return outString;
    }


}