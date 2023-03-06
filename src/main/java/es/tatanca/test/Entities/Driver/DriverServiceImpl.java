package es.tatanca.test.Entities.Driver;

import es.tatanca.test.Entities.Driver.DriverRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    @Autowired
    private final DriverRepository driverRepo;

    @Override
    @Transactional
    public Driver update(final Driver driver) {
        System.out.println("DriverServiceImpl.update1: driver.getId() = " + driver.getId());
        final Driver updatableDriver = driverRepo.findById(driver.getId())
                .orElseThrow(NullPointerException::new);
        updatableDriver.setName(driver.getName());
        updatableDriver.setSurname(driver.getSurname());
        updatableDriver.setWorkedHours(driver.getWorkedHours());
        updatableDriver.setStatus(driver.getStatus());
        System.out.println("DriverServiceImpl.update2: driver.getId() = " + driver.getId());
        return updatableDriver;
    }

    public Long saveDriver(Driver driver) {
        System.out.println("DriverServiceImpl.saveDriver: driver.getNumber() = " + driver.getName());
        driver = driverRepo.save(driver);
        return driver.getId();
    }


    @Override
    public List<Driver> getLikeName(String valor) {
        System.out.println("getLikeName " + valor);
        List<Driver> driverList = driverRepo.getLikeName(valor);
        return driverList;
    }

    public List<Driver> getEqualStatus(Driver.EnumStatus valor) {
        System.out.println("getEqualStatus " + valor);
        List<Driver> driverList = driverRepo.getEqualStatus(valor);
        return driverList;
    }

    public List<Driver> getLikeNumberStatus(String valor1, Driver.EnumStatus valor2) {
        System.out.println("getLikeNumberStatus  number: " + valor1 + " and status: "+ valor2.toString());
        List<Driver> driverList = driverRepo.getLikeNumberStatus(valor1, valor2);
        return driverList;
    }


    public Driver findById(Long id) {
        System.out.println("findById " + id);
        Driver driver = null;
        Optional<Driver> opt = driverRepo.findById(id);
        if (opt.isPresent()) { driver = opt.get(); }
        return driver;
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> driverList = driverRepo.findAll();
        return driverList;
    }

    @Override
    public Driver getEqualByCampo(String campo, String valor) {
        Driver driver = driverRepo.getEqualBy(campo, valor);
        return driver;
    }

    @Override
    public List<Driver> getLikeByCampo(String campo, String valor) {
        System.out.println("getLikeByCampo campo/valor " + campo + "/" + valor);
        List<Driver> driverList = null;
        switch(campo) {
            case "name" -> driverList = driverRepo.getLikeName(valor);
            default -> { }
        }
        return driverList;
    }




}
