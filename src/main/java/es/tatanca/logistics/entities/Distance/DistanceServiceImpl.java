package es.tatanca.logistics.entities.Distance;


import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoRepository;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class DistanceServiceImpl implements DistanceService {

//    @Autowired
    private final DistanceRepository distanceRepo;
    private final CargoRepository cargoRepo;

    private final CityServiceImpl cityServiceImpl;

    private final Float getHoursSameCity;
    private final Float getMediaVelocity;

    @Transactional
    public Long saveDistance(Distance distance) throws Exception {

        // Checking the distance is not being used in an order.
        if (!cargoRepo.getEqualCities(distance.getCity0(), distance.getCity1()).equals(Collections.emptyList())) {
            log.debug("saveDistance. 1");
            List<Cargo> cargoList = cargoRepo.getEqualCities(distance.getCity0(), distance.getCity1());
            for(Cargo cargo: cargoList) {
                if (    cargo.getStatus().equals(CargoStatus.ASSIGNED) ||
                        cargo.getStatus().equals(CargoStatus.SHIPPED) ) {
                    log.debug("saveDistance.Exc 1");
                    throw new RuntimeException("You can't modify the distance between two cities in an unfinished cargo.");
                }
            }
        }
        if (!cargoRepo.getEqualCities(distance.getCity1(), distance.getCity0()).equals(Collections.emptyList())) {
            log.debug("saveDistance. 2");
            List<Cargo> cargoList = cargoRepo.getEqualCities(distance.getCity1(), distance.getCity0());
            for(Cargo cargo: cargoList) {
                if (    cargo.getStatus().equals(CargoStatus.ASSIGNED) ||
                        cargo.getStatus().equals(CargoStatus.SHIPPED) ) {
                    log.debug("saveDistance. Exc 2");
                    throw new RuntimeException("You can't modify the distance between two cities in an unfinished cargo.");
                }
            }
        }

        // Checking distance is not already created.
        if (getDistance(distance.getCity0(), distance.getCity1()) != 0.0F) {
            throw new RuntimeException("That distance is already created.");
        }

        distanceRepo.save(distance);
        return distance.getId();
    }

    @Transactional
    public void delDistance(Distance distance) throws Exception {

        // Checking the distance is not being used in an order.
        if (!cargoRepo.getEqualCities(distance.getCity0(), distance.getCity1()).equals(Collections.emptyList())) {
            log.debug("saveDistance. 1");
            List<Cargo> cargoList = cargoRepo.getEqualCities(distance.getCity0(), distance.getCity1());
            for(Cargo cargo: cargoList) {
                if (    cargo.getStatus().equals(CargoStatus.ASSIGNED) ||
                        cargo.getStatus().equals(CargoStatus.SHIPPED) ) {
                    log.debug("saveDistance.Exc 1");
                    throw new RuntimeException("You can't modify the distance between two cities in an unfinished cargo.");
                }
            }
        }
        if (!cargoRepo.getEqualCities(distance.getCity1(), distance.getCity0()).equals(Collections.emptyList())) {
            log.debug("saveDistance. 2");
            List<Cargo> cargoList = cargoRepo.getEqualCities(distance.getCity1(), distance.getCity0());
            for(Cargo cargo: cargoList) {
                if (    cargo.getStatus().equals(CargoStatus.ASSIGNED) ||
                        cargo.getStatus().equals(CargoStatus.SHIPPED) ) {
                    log.debug("saveDistance. Exc 2");
                    throw new RuntimeException("You can't modify the distance between two cities in an unfinished cargo.");
                }
            }
        }


        distanceRepo.delete(distance);
    }

    public Distance findById(Long id) {
        Distance distance = null;
        Optional<Distance> opt = distanceRepo.findById(id);
        if (opt.isPresent()) { distance = opt.get(); }
        return distance;
    }

    @Override
    public List<Distance> findAll() {
        List<Distance> distanceList = distanceRepo.findAll();
        return distanceList;
    }


    public List<Distance> getLessOrEqualByDistance(float distance) {
        log.debug("getLessOrEqualByDistance  distance: " + distance);
        return distanceRepo.getLessOrEqualByDistance(distance);
    }

    @Override
    public Float getDistance(City city, City city1) throws RuntimeException {
        log.debug("getDistance: " + city.getName() + " / " + city1.getName());

        Distance distance = null;
        try {
            distance = distanceRepo.getDistance(city, city1);
        } catch (Exception e1) {
            try {
                distance = distanceRepo.getDistance(city1, city);
            } catch (Exception e2) {
                throw new RuntimeException("Error: No distance");
            }
        }
        log.debug("getDistance: " + distance.getDistance());
        return distance.getDistance();
    }

    // *****************************************************************************************************************
    public Float getHours(Long city1Id, Long city2Id) {
        log.debug("getHours: " + city1Id + " / " + city2Id);

        if (city1Id.equals(city2Id)) {
            return getHoursSameCity;
        }

        City city1 = cityServiceImpl.findById(city1Id);
        if (city1 == null) {
            log.debug("getDistance: 0.0F");
            return 0.0F;
        }

        City city2 = cityServiceImpl.findById(city2Id);
        if (city2 == null) {
            log.debug("getDistance: 0.0F");
            return 0.0F;
        }

        Distance distance;
        try {
            distance = distanceRepo.getDistance(city1, city2);
            log.debug("getHours: " + city1Id + " / " + city2Id + " : " + distance.getDistance());
        } catch (Exception e1) {
            distance = distanceRepo.getDistance(city2, city1);
            log.debug("getHours: " + city2Id + " / " + city1Id + " : " + distance.getDistance());
        }

        log.debug("getHours: " + distance.getDistance() + " / " + distance.getDistance()/getMediaVelocity);
        return distance.getDistance() / getMediaVelocity;
    }


}
