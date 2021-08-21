package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

@Data
public class InstitutionModerators {

        private Institution institution;

        private List<User> moderators = new ArrayList<>();

}
