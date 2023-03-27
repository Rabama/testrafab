package es.tatanca.logistics;

import es.tatanca.logistics.entities.Credentials.CredentialsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
public class AppController {

//    @Autowired
    private final CredentialsServiceImpl credentialsServiceImpl;

    // *****************************************************************************************************************

    @GetMapping("/accessDenied")
    // Go to registration page
    public String accessDenied() {
        log.debug("accessDenied");
        return "redirect:/accessDenied.html";
    }

    // *****************************************************************************************************************

    @GetMapping("/register")
    // Go to registration page
    public String register() {
        return "register";
    }

    // *****************************************************************************************************************

    // En SecurityConfig derivamos todos los accesos no autenticados a /login.
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // En SecurityConfig derivamos todos los recientes accesos a index.
    @GetMapping("/index")
    // After login
    public String index(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.debug("Username   : " + auth.getPrincipal());
        log.debug("Credentials: " + auth.getCredentials());
        log.debug("Authorities: " + auth.getAuthorities());
        log.debug("Details    : " + auth.getDetails());


        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        log.debug(roles.toString());

        if (roles.contains("Admin")) {
            model.addAttribute("AuthAdmin", true);
        }
        if (roles.contains("Employee")) {
            model.addAttribute("AuthEmployee", true);
        }
        if (roles.contains("Driver")) {
            model.addAttribute("AuthDriver", true);
        }

        return "index";
    }


  /*  @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute Credentials user, Model model) {

        System.out.println("CredentialsController.saveUser.username = " + user.getUsername());
        System.out.println("CredentialsController.saveUser.password = " + user.getPassword());
        System.out.println("CredentialsController.saveUser.roles    = " + user.getRoles());

        Long id = credentialsServiceImpl.saveUser(user);

        String message = "User '"+id+"' saved successfully !";
        model.addAttribute("msg", message);
        return "register";
    }*/

}
