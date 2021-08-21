package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Entity
@Data
public class AnnouncementComment extends BaseEntity {
    @ManyToOne
    Announcement announcement;

    @ManyToOne
    User submittedByUser;

    String comment;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="Europe/Skopje")
    LocalDateTime submittedAt;
}
