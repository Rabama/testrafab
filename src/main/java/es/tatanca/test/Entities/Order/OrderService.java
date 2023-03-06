package es.tatanca.test.Entities.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Long saveOrder(Map<String,String> bundle) throws Exception;

//    public List<Order> getLikeNumber(String valor);

    List<Order> findAll();

}
