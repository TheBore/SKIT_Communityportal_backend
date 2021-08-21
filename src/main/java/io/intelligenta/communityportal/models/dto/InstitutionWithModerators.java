package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InstitutionWithModerators {

    public InstitutionWithModerators(Institution institution) {
        this.institution = institution;
    }

    private Institution institution;

    private List<Moderator> moderators = new ArrayList<>();

    public void addModerator(User user) {
        this.moderators.add(new Moderator(user));
    }
}
