package guru.qa.rococo.controller;

import guru.qa.rococo.model.UserDataJson;
import guru.qa.rococo.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/user")
public class UserDataController {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataController.class);

    private final UserDataService userDataService;

    @Autowired
    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/current")
    public UserDataJson currentUser(@RequestParam String username) {
        return userDataService.getCurrentUser(username);
    }

    @PostMapping("/update")
    public UserDataJson updateUserInfo(@RequestBody UserDataJson user) {
        return userDataService.update(user);
    }
}