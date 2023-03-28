package es.tatanca.logistics;

import es.tatanca.logistics.entities.Cargo.CargoServiceImpl;
import es.tatanca.logistics.entities.City.City;
import es.tatanca.logistics.entities.City.CityServiceImpl;
import es.tatanca.logistics.entities.Driver.DriverServiceImpl;
import es.tatanca.logistics.entities.Order.OrderServiceImpl;
import es.tatanca.logistics.entities.Truck.TruckServiceImpl;
import es.tatanca.logistics.entities.Waypoint.WaypointServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class TestApplicationTests {

    @Autowired
    private CityServiceImpl cityServiceImpl;

    @Autowired
    private DriverServiceImpl driverServiceImpl;

    @Autowired
    private TruckServiceImpl truckServiceImpl;

    @Autowired
    private CargoServiceImpl cargoServiceImpl;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private WaypointServiceImpl waypointServiceImpl;



    @Test
    void contextLoads() {
    }

    @Test
    public void nose() {
        log.debug("nose");

        City city = new City();
        city.setName("test01");
        Long id = cityServiceImpl.saveCity(city);
        city = cityServiceImpl.findById(id);
        Assertions.assertEquals("test01", city.getName());

        TestDriver testDriver = new TestDriver(cityServiceImpl, driverServiceImpl);
        testDriver.test01();
        testDriver.test02();

        TestTruck testTruck = new TestTruck(truckServiceImpl);
        testTruck.test01();


        TestBusiness testBusiness = new TestBusiness(cityServiceImpl, driverServiceImpl, truckServiceImpl, cargoServiceImpl, orderServiceImpl, waypointServiceImpl);
        testBusiness.test01();

    }



}
