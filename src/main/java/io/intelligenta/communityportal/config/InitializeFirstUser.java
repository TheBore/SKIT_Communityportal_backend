package io.intelligenta.communityportal.config;

import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.service.auth.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitializeFirstUser {

    private final UserService userService;

    public InitializeFirstUser(UserService userService) {
        this.userService = userService;
    }

    UserRole admin;
    UserRole instModerator;
    UserRole user;

    @PostConstruct
    public void createInitialUser() {

        if (this.userService.count() == 0) {
            this.userService.createAdmin();
        }
    }
}
