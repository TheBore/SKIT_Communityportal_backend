package io.intelligenta.communityportal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BBBUserMeeting extends CreationAwareEntity{

    private String meetingId;

    private String name;

    private String moderatorPw;
    private String attendeePw;

    private Boolean openSession;

    private LocalDate meetingDate;

    @ManyToMany
    private List<Institution> institutions;

}
