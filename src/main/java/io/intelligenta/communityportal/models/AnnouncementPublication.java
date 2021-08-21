package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Entity
@Data
public class AnnouncementPublication extends BaseEntity {

    private boolean read;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="Europe/Skopje")
    private LocalDateTime readAt;

    @ManyToOne
    private Institution receiver;

    @ManyToOne
    private Announcement announcement;

    public AnnouncementPublication() {
    }

    public boolean getRead(){
        return read;
    }

}
