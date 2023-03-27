package es.tatanca.logistics.entities.Credentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
@Service
@AllArgsConstructor
@Slf4j
public class CredentialsServiceImpl implements CredentialsService, UserDetailsService {

//    @Autowired
    private final CredentialsRepository userRepo;

//    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public long count() {
        return userRepo.count();
    }

    @Override
    public Credentials findById(Long id) {
        log.debug("UserServiceImpl.findById " + id);
        Credentials user = null;
        Optional<Credentials> opt = userRepo.findById(id);
        if (opt.isPresent()) { user = opt.get(); }
        return user;
    }

    public List<Credentials> getLikeUsername(String valor) {
        List<Credentials> userList = userRepo.getLikeUsername(valor);
        return userList;
    }

    public Credentials getCurrentUser() throws Exception {
        log.debug("getCurrentUser: In.");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Credentials user = findByUsername(username);

        log.debug("getCurrentUser: Out (user = "+user.getUsername()+").");
        return user;
    } // getCurrentUser



    public Credentials findByUsername(String username) throws Exception {
        Optional<Credentials> opt = userRepo.findUserByUsername(username);
        return opt.orElse(null);
     }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        log.debug("UserServiceImpl.loadUserByUserName = " + userName);

        Optional<Credentials> opt = userRepo.findUserByUsername(userName);

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("Username: " + userName + " not found !");
        }

        Credentials user = opt.get();

        Collection<? extends GrantedAuthority> mapRoles = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRoles);

    } // loadUserByUsername



    // ADMIN
    @Override
    public Long saveUser(Credentials user) {
        log.debug("UserServiceImpl.saveUser: In. User.getUsername() = " + user.getUsername());

        if (!user.isPasswordEncripted()) {
            log.debug("UserServiceImpl.saveUser: Password not encripted.");
            String passwd = user.getPassword();
            String encodedPassword = passwordEncoder.encode(passwd);
            user.setPassword(encodedPassword);
        }

        try {
            user = userRepo.save(user);
        } catch (Exception e) {

        }

        log.debug("UserServiceImpl.saveUser: Out.");
        return user.getId();
    }




}
