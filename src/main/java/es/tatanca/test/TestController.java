package es.tatanca.test;

import es.tatanca.test.Entities.City.*;
import es.tatanca.test.Entities.Credentials.Credentials;
import es.tatanca.test.Entities.Credentials.CredentialsService;
import es.tatanca.test.Entities.Credentials.CredentialsServiceImpl;
import es.tatanca.test.Entities.Distance.Distance;
import es.tatanca.test.Entities.Distance.DistanceServiceImpl;
import es.tatanca.test.Entities.Truck.*;
import es.tatanca.test.Entities.Cargo.*;
import es.tatanca.test.Entities.Driver.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class TestController {

//    @Autowired
    private final CredentialsServiceImpl credentialsServiceImpl;


    @GetMapping("/register")
    // Go to registration page
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    //
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    // After login
    public String index() {
        return "index";
    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute Credentials user, Model model) {

        System.out.println("CredentialsController.saveUser.username = " + user.getUsername());
        System.out.println("CredentialsController.saveUser.password = " + user.getPassword());
        System.out.println("CredentialsController.saveUser.roles    = " + user.getRoles());

        Long id = credentialsServiceImpl.saveUser(user);

        String message = "User '"+id+"' saved successfully !";
        model.addAttribute("msg", message);
        return "register";
    }

}
