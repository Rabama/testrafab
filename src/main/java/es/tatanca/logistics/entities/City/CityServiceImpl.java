package es.tatanca.logistics.entities.City;

//import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoRepository;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class CityServiceImpl implements CityService {

    //    @Autowired
    private final CityRepository cityRepo;
    private final CargoRepository cargoRepo;

    @Override
    public Long saveCity(City city) {
        log.debug("CityServiceImpl.saveCity: city.getName() = " + city.getName());
        city = cityRepo.save(city);
        return city.getId();
    }

    public void delCity(City city) {
        log.debug("CityServiceImpl.delCity: city.getName() = " + city.getId() + city.getName());

        if (cargoRepo.getDiffStatusEqUpcity(CargoStatus.READY, city).size() > 0) {
            throw new RuntimeException("Error: City " + city.getName() + " has a cargo uploading in that city.");
        } else if (cargoRepo.getDiffStatusEqUncity(CargoStatus.READY, city).size() > 0) {
            throw new RuntimeException("Error: City " + city.getName() + " has a cargo unloading in that city.");
        }
        cityRepo.delete(city);
    }

    @Override
    public City findById(Long id) {
        log.debug("CityServiceImpl.findById " + id);

        return cityRepo.findById(id).orElse(null);
    }

    @Override
    public List<City> findAll() {
        log.debug("CityServiceImpl.findAll");
        return cityRepo.findAll();
    }

    @Override
    public List<City> getLikeByCampo(String campo, String valor) {
        log.debug("CityServiceImpl.getLikeByCampo campo/valor " + campo + "/" + valor);
        List<City> cityList = null;
        switch(campo) {
            case "name" -> { cityList = cityRepo.getLikeName(valor); }
            default -> { }
        }
        return cityList;
    }


    @Override
    public List<City> getLikeName(String valor) {
        log.debug("CityServiceImpl.getLikeName " + valor);
        return cityRepo.getLikeName(valor);
    }



}
