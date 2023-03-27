package es.tatanca.logistics;

import es.tatanca.logistics.entities.Cargo.Cargo;
import es.tatanca.logistics.entities.Cargo.CargoRepository;
import es.tatanca.logistics.entities.Cargo.CargoStatus;
import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityRepository;
import es.tatanca.logistics.entities.Credentials.CredentialsRepository;
import es.tatanca.logistics.entities.Driver.Driver;
import es.tatanca.logistics.entities.Driver.DriverRepository;
import es.tatanca.logistics.entities.Driver.DriverStatus;
import es.tatanca.logistics.entities.Truck.Truck;
import es.tatanca.logistics.entities.Truck.TruckRepository;
import es.tatanca.logistics.entities.Truck.TruckServiceImpl;
import es.tatanca.logistics.entities.Truck.TruckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.stream.IntStream;

@Configuration
@Slf4j
public class LoadDatabase {

    private final int TrucksNumber  = 10;
    private final int CitiesNumber  =  9;
    private final int DriversNumber =  8;
    private final int CargoNumber   =  7;

    private boolean vacia = false;

    @Bean
    CommandLineRunner siVacia(CredentialsRepository userRepository) {
        long users = userRepository.count();
        vacia = (users == 0);
        return null;
    }



    void newTruck(TruckRepository truckRepo, int aux) {
        log.debug("New truck " + aux);
        Truck truck = new Truck();
        String number = String.format("%1$4s", aux).replace(' ', '0');
        truck.setNumber(number);
        truck.setCapacity((double)aux);
//        if ((aux%2)== 0) { truck.setStatus(Truck.EnumStatus.OK); } else { truck.setStatus(Truck.EnumStatus.NOK);  };
        if ((aux%2)== 0) { truck.setStatus(TruckStatus.OK); } else { truck.setStatus(TruckStatus.NOK);  }
        truckRepo.save(truck);
    }

    @Bean
    CommandLineRunner initTrucks(TruckRepository truckRepo) {
        if (!vacia) return null;

        return args -> {
//            IntStream.range(0,20).forEach( i -> newTruck(i) );
//            IntStream.range(0,20).forEach(this::newTruck);
            IntStream.range(0,TrucksNumber).forEach( i -> newTruck(truckRepo, i+1) );
        };
    }


    @Bean
    CommandLineRunner initCities(CityRepository cityRepo) {
        if (!vacia) return null;
        return args -> IntStream.range(0,CitiesNumber).forEach(i -> {
            log.debug("New city " + (i+1));
            City city = new City();
            city.setName("City " + (i+1));
            cityRepo.save(city);
        } );
    }


    @Bean
    CommandLineRunner initTrucksCities(TruckRepository truckRepo, TruckServiceImpl truckServiceImpl, CityRepository cityRepo) {
        if (!vacia) return null;
        return args -> IntStream.range(0,TrucksNumber).forEach(i -> {
            long indice = (long)i+1;
//                log.debug("truck -> city " + (i+1));
            Optional<Truck> optTruck = truckRepo.findById(indice);
            if (optTruck.isPresent()) {
                Truck truck = optTruck.get();

                truck.setCapacity((double) indice);
                log.debug("Asignada capacidad " + indice + " al truck " + truck.getId());
                truckRepo.save(truck);

                // Asignamos a las 5 primeras ciudades.
                Optional<City> optCity = cityRepo.findById((long)(1+i%5));
                if (optCity.isPresent()) {
                    City city = optCity.get();

                    truck.setCity(city);
                    truckServiceImpl.saveTruck(truck);
                    log.debug("Asignada city " + truck.getCity().getName() + " al truck " + truck.getId());

                } else {
                    log.debug("ERROR: El city.id " + (i+1) + " no existe.");
                }

            } else {
                log.debug("ERROR: El truck.id " + (i+1) + " no existe.");
            }

        } );
    }


    @Bean
    CommandLineRunner initDrivers(DriverRepository driverRepo) {
        if (!vacia) return null;
        return args -> IntStream.range(0,DriversNumber).forEach(i -> {
            log.debug("New driver " + (i+1));
            Driver driver = new Driver();
            driver.setName("Driver " + (i+1));
            driver.setSurname("surname " + (i+1));
            driver.setWorkedHours(0.0);
            if ((i+1)%2 == 0) { driver.setStatus(DriverStatus.WORK); } else { driver.setStatus(DriverStatus.REST); }
            driverRepo.save(driver);
        } );
    }


    @Bean
    CommandLineRunner initCargos(CargoRepository cargoRepo, CityRepository cityRepo) {
        if (!vacia) return null;
        return args -> IntStream.range(0,CargoNumber).forEach(i -> {
            log.debug("New cargo " + (i+1));
            Cargo cargo = new Cargo();
            cargo.setName("Cargo " + (i+1));
            cargo.setWeight((double)i+1);
            cargo.setStatus(CargoStatus.READY);

            long upCityId = (i+1) % CargoNumber + 1;
            long unCityId = (i+2) % CargoNumber + 1;
            Optional<City> upCity = cityRepo.findById(upCityId);
            Optional<City> unCity = cityRepo.findById(unCityId);
            upCity.ifPresent(cargo::setUpcity);
            unCity.ifPresent(cargo::setUncity);

            cargoRepo.save(cargo);
        } );
    }




} // LoadDatabase
