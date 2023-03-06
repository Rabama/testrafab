package es.tatanca.test.Entities.Distance;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.test.Entities.Distance.DistanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DistanceServiceImpl implements DistanceService{
//    @Autowired
    private final DistanceRepository distanceRepo;

    @Transactional
    public Long saveDistance(Distance distance) throws Exception {
        distanceRepo.save(distance);
        return distance.getId();
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

    @Override
    public Distance getEqualByCampo(String campo, String valor) {
        Distance distance = distanceRepo.getEqualBy(campo, valor);
        return distance;
    }

    @Override
    public List<Distance> getLikeByCampo(String campo, String valor) {
        System.out.println("getLikeByCampo campo/valor " + campo + "/" + valor);

        List<Distance> distanceList = null;
        switch(campo) {

            default -> { }
        }
        return distanceList;
    }


}
