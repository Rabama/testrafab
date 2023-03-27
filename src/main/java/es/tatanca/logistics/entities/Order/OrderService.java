package es.tatanca.logistics.entities.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

//    Long saveOrder(Map<String,String> bundle) throws Exception;

    List<Order> findAll();

}
