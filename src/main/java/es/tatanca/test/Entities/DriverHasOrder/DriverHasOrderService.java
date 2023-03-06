package es.tatanca.test.Entities.DriverHasOrder;

import es.tatanca.test.Entities.Driver.Driver;
import es.tatanca.test.Entities.Order.Order;

public interface DriverHasOrderService {

    public Long save(Order order, Driver driver, DriverHasOrder.EnumStatus status);

}
