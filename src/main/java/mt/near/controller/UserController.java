package mt.near.controller;

import mt.near.domain.UserEntity;
import mt.near.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
public class UserController {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Autowired
    public UserController(final BCryptPasswordEncoder encoder, final UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/current")
    public UserEntity currentUserName(Authentication auth) {
        return (UserEntity) auth.getPrincipal();
    }

    @PostMapping("/register")
    public void registerConsumer(@RequestBody @Validated(UserEntity.ValidationRegister.class) UserEntity user) {
        LOGGER.info("Consumer " + user.getUsername() + " registers now ...");
        userService.saveUser(user);
    }

}
