package io.intelligenta.communityportal.models.dto;


import io.intelligenta.communityportal.models.auth.User;
import lombok.Getter;

@Getter
public class Moderator {

    public Moderator(User user) {
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.alternativeEmail = user.getAlternativeEmail();
        this.alternativeSecondEmail = user.getAlternativeSecondEmail();
        this.phone = user.getPhone();
        this.alternativePhone = user.getAlternativePhone();
        this.alternativeSecondPhone = user.getAlternativeSecondPhone();
    }

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String alternativeEmail;

    private String alternativeSecondEmail;

    private String phone;

    private String alternativePhone;

    private String alternativeSecondPhone;
}
