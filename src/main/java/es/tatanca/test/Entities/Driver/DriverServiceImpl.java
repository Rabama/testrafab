package es.tatanca.test.Entities.Driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

//    @Autowired
    private final DriverRepository driverRepo;

    DriverServiceImpl(DriverRepository driverRepo) { this.driverRepo = driverRepo;}

    @Override
    public void saveDriver(Driver driver) {
        driverRepo.save(driver);
    }

    @Override
    public List<Driver> getAllDriver() {
        return driverRepo.findAll();
    }

    @Override
    public Driver getEqualByCampo(String campo, String valor) {
        Driver driver = null;
        switch (campo) {
            case "id" -> driver = driverRepo.getEqualById(valor);
            case "name" -> driver = driverRepo.getEqualByName(valor);
            case "surname" -> driver = driverRepo.getEqualBySurname(valor);
            case "workedHours" -> driver = driverRepo.getEqualByWorkedHours(valor);
            case "status" -> driver = driverRepo.getEqualByStatus(valor);
            case "orderId" -> driver = driverRepo.getEqualByOrderId(valor);
            case "cityId" -> driver = driverRepo.getEqualByCityId(valor);
            default -> {
            }
        }
        return driver;
    }

    @Override
    public List<Driver> getLikeByCampo(String campo, String valor) {
        List<Driver> driverList = null;
        switch (campo) {
            case "name" -> driverList = driverRepo.getLikeByName(valor);
            case "surname" -> driverList = driverRepo.getLikeBySurname(valor);
            case "status" -> driverList = driverRepo.getLikeByStatus(valor);
            default -> {
            }
        }
        return driverList;
    }


}
