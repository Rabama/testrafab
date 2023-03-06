package es.tatanca.test.Entities.DriverHasOrder;

import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Order.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DriverHasOrderServiceImpl implements DriverHasOrderService {

    private final DriverHasOrderRepository driverHasOrderRepository;

    @Override
    @Transactional
    public Long save(Order order, Driver driver, DriverHasOrder.EnumStatus status) {

        DriverHasOrder driverHasOrder = new DriverHasOrder();
        driverHasOrder.setOrder(order);
        driverHasOrder.setDriver(driver);
        driverHasOrder.setStatus(status);

        // Tenemos que eliminar los registros de la orden. O modificarlos.
        driverHasOrderRepository.deleteDriverHasOrderByOrderDriver(order, driver);


        return driverHasOrderRepository.save(driverHasOrder).getId();
    } // save


    public List<DriverHasOrder> getDriverHasOrderByOrder(Order order) {
        return driverHasOrderRepository.getDriverHasOrderByOrder(order.getId());
    } //


}
