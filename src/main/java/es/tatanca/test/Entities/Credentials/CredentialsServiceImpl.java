package es.tatanca.test.Entities.Credentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredentialsServiceImpl implements CredentialsService, UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Long saveUser(Credentials user) {
        System.out.println("UserServiceImpl.saveUser: user.getUsername() = " + user.getUsername());
        String passwd= user.getPassword();
        String encodedPasswod = passwordEncoder.encode(passwd);
        user.setPassword(encodedPasswod);
        user = credentialsRepo.save(user);
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        System.out.println("UserServiceImpl.loadUserByUserName = " + userName);

        Optional<Credentials> opt = credentialsRepo.findUserByUsername(userName);

        if (opt.isEmpty())
            throw new UsernameNotFoundException("User with username: " + userName +" not found !");
        else {
            Credentials user = opt.get();
            Collection<? extends GrantedAuthority> mapRoles = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRoles);
        }

    }


}
