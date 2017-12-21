package mt.near.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mtoader on 12/21/2017.
 */
@RestController
public class NearController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome!!!";
    }
}
