package es.tatanca.logistics.entities.Credentials;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class CredentialsController {

    private final CredentialsServiceImpl userServiceImpl;



    // *****************************************************************************************************************
    //                                                    USER
    // *****************************************************************************************************************

    // USER
    @GetMapping("/entities/user/actual/")
    // queries.loadUser
    public String getActualUser(Model model) {
        log.debug("getActualUser");
        Credentials user = null;
        try { user = userServiceImpl.getCurrentUser(); } catch (Exception ignored) { }
        model.addAttribute("user", user);
        return "entities/user/user";
    }

    // USER
    @PostMapping("/entities/user/save/")
    // queries.loadUser
    public ResponseEntity<String> entitiesUserSave(@RequestBody Map<String, String> reqParam) {
        log.debug("entitiesUserSave " + reqParam.get("username"));

        if (reqParam.get("id").equals("")) {
            return new ResponseEntity<>("User id not found.", HttpStatus.BAD_REQUEST);
        }

        Credentials user = null;
        try {
            user = userServiceImpl.getCurrentUser();
        } catch (Exception e) {
            return new ResponseEntity<>("User id not found.", HttpStatus.BAD_REQUEST);
        }

        long reqId;
        try {
            reqId = Long.parseLong(reqParam.get("id"));
        } catch (Exception e) {
            return new ResponseEntity<>("The id is not valid.", HttpStatus.BAD_REQUEST);
        }
        if (!user.getId().equals(reqId)) {
            return new ResponseEntity<>("The user id is not valid.", HttpStatus.BAD_REQUEST);
        }

        // El usuario que estamos grabando es el usuario actual.

        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("id",       user.getId().toString());
        userMap.put("username", reqParam.get("username"));
        userMap.put("password", reqParam.get("password"));
        userMap.put("admin",    user.isAdmin() ? "true" : "false");
        userMap.put("employee", user.isEmployee() ? "true" : "false");
        userMap.put("driver",   user.isDriver() ? "true" : "false");

        return userSave(userMap);
    }


    // *****************************************************************************************************************
    //                                                    ADMIN
    // *****************************************************************************************************************

    // ADMIN
    @GetMapping("/user/load/")
    // adminLoadUser
    public String userLoad(Model model) {
        log.debug("userLoad.");
        return userLoadId(0L, model);
    }

    // ADMIN
    @GetMapping("/user/load/{id}")
    // adminUserLoadId
    public String userLoadId(@PathVariable(value = "id") Long id, Model model) {
        log.debug("userLoadId: In.");
        if (id > 0) {
            Credentials user = null;
            user = userServiceImpl.findById(id);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("isAdmin", user.isAdmin());
                model.addAttribute("isEmployee", user.isEmployee());
                model.addAttribute("isDriver", user.isDriver());

                log.debug("userLoadId: isAdmin/isEmployee/isDriver " + user.isAdmin() + "/" + user.isEmployee() + "/" + user.isDriver());
            }
        }
        log.debug("userLoadId: Out.");
        return "user/user";
    }



    // ADMIN
    @GetMapping({"/user/head/{mode}"})
    // user.html
    public String userHead(@PathVariable(value = "mode") int mode, Model model) {
        log.debug("userHead.");
        model.addAttribute("mode", mode);
        return "user/userqueryhead";
    }

    // ADMIN
    @PostMapping("/user/find/{mode}")
    public String userFind(@RequestBody Map<String, String> param, @PathVariable(value = "mode") int mode, Model model) {
        log.debug("userFind: In. username = " + param.get("username") + " / mode = " + mode);

        List<Credentials> userList = userServiceImpl.getLikeUsername(param.get("username"));

        model.addAttribute("mode", mode);
        model.addAttribute("userList", userList);

        log.debug("userFind: Out.");
        return "user/userquerybody";
    }

    @PostMapping("user/save/")
    @ResponseBody
    public ResponseEntity<String> userSave(@RequestBody Map<String, String> reqParam) {
        log.debug("userSave: In.");

        log.debug("username: "+reqParam.get("username"));
        log.debug("password: "+reqParam.get("password"));
        log.debug("admin   : "+reqParam.get("admin"));
        log.debug("employee: "+reqParam.get("employee"));
        log.debug("driver  : "+reqParam.get("driver"));

        if (reqParam.get("username") == null) {
            return new ResponseEntity<>("A empty username is not valid.", HttpStatus.BAD_REQUEST);
        }

        Credentials user = new Credentials();

        if (!reqParam.get("id").equals("")) {
            // El usuario existe.

            log.debug("userSave: Existe");

            try {
                user.setId(Long.parseLong(reqParam.get("id")));
            } catch (Exception e) {
                log.error("The id is no valid.");
                return new ResponseEntity<>("The id is not valid.", HttpStatus.BAD_REQUEST);
            }

            Credentials userq = userServiceImpl.findById(user.getId());

            if (userq == null) {
               log.error("User id not found.");
                return new ResponseEntity<>("User id not found.", HttpStatus.BAD_REQUEST);
            }

            if (reqParam.get("password").equals("")) {
                log.debug("userSave: password encripted (" + userq.getPassword() + ").");
                user.setPasswordEncripted(true);
                user.setPassword(userq.getPassword());
            } else {
                user.setPasswordEncripted(false);
                user.setPassword(reqParam.get("password"));
            }

        } else {

            user.setPasswordEncripted(false);
            user.setPassword(reqParam.get("password"));

        }

        Credentials userq = null;
        try {
            userq = userServiceImpl.findByUsername(reqParam.get("username"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if ((userq != null) && (!userq.getId().equals(user.getId()))) {
            log.error("Already exist a user with the same username.");
            return new ResponseEntity<>("Already exist a user with the same username.", HttpStatus.BAD_REQUEST);
        }
        user.setUsername(reqParam.get("username"));

       log.debug("userSave 2");

        if (!user.isPasswordEncripted()) {
            if (user.getPassword().equals("")) {
                return new ResponseEntity<>("A empty password is not valid.", HttpStatus.BAD_REQUEST);
            }
            user.setPassword(reqParam.get("password"));
        }

        log.debug("userSave 3");

        List<String> roles = new ArrayList<>();
        if (reqParam.get("admin").equals("true"))    { roles.add(Credentials.roleAdmin); }
        if (reqParam.get("employee").equals("true")) { roles.add(Credentials.roleEmployee); }
        if (reqParam.get("driver").equals("true"))   { roles.add(Credentials.roleDriver); }
        user.setRoles(roles);
        if (roles.size() == 0) {
            return new ResponseEntity<>("A empty roles is not valid.", HttpStatus.BAD_REQUEST);
        }

        log.debug("userSave 4");

        long savedId;
        try {
            savedId = userServiceImpl.saveUser(user);
        } catch (Exception e) {
            log.debug(e + " / " + e.getMessage() + " / " + e.getLocalizedMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        log.debug("userSave 5");

        if (savedId == 0) {
            return new ResponseEntity<>("An error has happend.", HttpStatus.BAD_REQUEST);
        }

        log.debug("userSave: Out.");
        return new ResponseEntity<>(String.valueOf(savedId), HttpStatus.OK);
    }







}
