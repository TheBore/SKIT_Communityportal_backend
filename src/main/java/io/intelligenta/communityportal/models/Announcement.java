package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Entity
@Data
public class Announcement extends CreationAwareEntity {

    private String title;

    @ManyToOne
    private User creator;

    @ManyToOne
    private AreaOfInterest areaOfInterest;

    @Column(length = 20000)
    private String body;

    @JsonIgnore
    @OneToMany(mappedBy = "announcement")
    private List<AnnouncementPublication> announcementPublications;

}
