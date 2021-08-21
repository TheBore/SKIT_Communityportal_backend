package io.intelligenta.communityportal.models.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.intelligenta.communityportal.models.CreationAwareEntity;
import io.intelligenta.communityportal.models.Institution;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class FeedbackPublication extends CreationAwareEntity {

    @ManyToOne
    private Feedback feedback;

    @ManyToOne
    private Institution institution;

    private boolean read;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="Europe/Skopje")
    private LocalDateTime readAt;

    private boolean finished;

    public boolean getRead(){
        return read;
    }
}
