package es.tatanca.test.Entities.City;

//import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    //    @Autowired
    private final CityRepository cityRepo;

    @Transactional
    public Long saveCity(City city) throws Exception {
        city = cityRepo.save(city);
        return city.getId();
    }

    @org.springframework.transaction.annotation.Transactional
    public void saveCity1(City city) {
        String name = city.getName();

//        city.setName("city001");
        cityRepo.save(city);

        if (name.equals("-1")) {
            throw new RuntimeException("Excepción en saveCity1");
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void saveCity2(City city) {
        String name = city.getName();

//        city.setName("city002");
        cityRepo.save(city);

        if (name.equals("-2")) {
            throw new RuntimeException("Excepción en saveCity2");
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void saveCity3(City city) {
        String name = city.getName();

//        city.setName("city003");
        cityRepo.save(city);

        if (name.equals("-3")) {
            throw new RuntimeException("Excepción en saveCity3");
        }
    }


    @Override
    public List<City> getLikeName(String valor) {
        System.out.println("getLikeName " + valor);
        List<City> cityList = cityRepo.getLikeName(valor);
        return cityList;
    }

    public City findById(Long id) {
        City city = null;
        Optional<City> opt = cityRepo.findById(id);
        if (opt.isPresent()) { city = opt.get(); }
        return city;
    };

    @Override
    public List<City> findAll() {
        List<City> cityList = cityRepo.findAll();
        return cityList;
    }

    @Override
    public City getEqualByCampo(String campo, String valor) {
        City city = cityRepo.getEqualBy(campo, valor);
        return city;
    }

    @Override
    public List<City> getLikeByCampo(String campo, String valor) {
        System.out.println("getLikeByCampo campo/valor " + campo + "/" + valor);

//        String query = "select a.id, a.name, a.truck.id, b.number as truck from City a LEFT JOIN Truck b on a.truck.id=b.id where a.id = 1";
/*
        List<Object[]> lista = entityManager.createQuery(query).getResultList();
        if (lista.size() > 0) {
            lista.id     = Long.parseLong(lista.get(0)[0].toString());
            lista.name   = lista.get(0)[1].toString();
            lista.truck  = Long.parseLong(lista.get(0)[2].toString());
            lista.number = lista.get(0)[3].toString();

            System.out.println("t01.id        " + lista.id);
            System.out.println("t01.campo1    " + lista.campo1);
            System.out.println("t01.t02id     " + lista.t02id);
            System.out.println("t01.t02campo1 " + lista.t02campo1);
        }
        return cityList;
*/
//        List<CityQuery> lista =  entityManager.createQuery(query).getResultList();
//        return cityRepo.getLikeBy2(campo, valor);
//        return cityRepo.getLikeBy2(campo);

        List<City> cityList = null;
        switch(campo) {
            case "name" -> { cityList = cityRepo.getLikeName(valor); }
            default -> { }
        }
        return cityList;
    }


}
