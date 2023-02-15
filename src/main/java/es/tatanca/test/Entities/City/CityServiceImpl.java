package es.tatanca.test.Entities.City;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

//    @Autowired
    private final CityRepository cityRepo;

    CityServiceImpl(CityRepository cityRepo) {
        this.cityRepo = cityRepo;
    }

    @Override
    public void saveCity(City city) {
        cityRepo.save(city);
    }

    @Override
    public List<City> getAllCity() {
        return cityRepo.findAll();
    }

    @Override
    public City getEqualByCampo(String campo, String valor) {
        City city = null;
        switch (campo) {
            case "id" ->     city = cityRepo.getEqualById(valor);
            case "name" -> city = cityRepo.getEqualByCampo(valor);
            default -> {
            }
        }
        return city;
    }

    @Override
    public List<City> getLikeByCampo(String campo, String valor) {
        List<City> cityList = null;
        switch (campo) {
            case "name" -> cityList = cityRepo.getLikeByCampo(valor);
            default -> {
            }
        }
        return cityList;
    }



}
