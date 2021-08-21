package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

@Entity
@Data
public class AnnouncementPublicationUser extends BaseEntity {

    private Long userId;

    private Long annPubId;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="Europe/Skopje")
    private LocalDateTime readAt;

}
