package es.tatanca.test.Entities.Distance;


import org.springframework.beans.factory.annotation.Autowired;
import es.tatanca.test.Entities.Distance.DistanceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DistanceServiceImpl implements DistanceService{
//    @Autowired
    private final DistanceRepository distanceRepo;

    DistanceServiceImpl(DistanceRepository distanceRepo) {this.distanceRepo = distanceRepo;}

    @Override
    public void saveDistance(Distance distance) { distanceRepo.save(distance); }

    @Override
    public List<Distance> getAllDistance() { return distanceRepo.findAll(); }

    @Override
    public Distance getEqualByCampo(String campo, String valor) {
        Distance distance = null;
        switch(campo) {
            case "id" -> distance = distanceRepo.getEqualById(valor);
            case "distance" -> distance = distanceRepo.getEqualByDistance(valor);
            case "time" -> distance = distanceRepo.getEqualByTime(valor);
            case "city0" -> distance = distanceRepo.getEqualByCity0(valor);
            case "city1" -> distance = distanceRepo.getEqualByCity1(valor);
        }
        return distance;
    }
}
