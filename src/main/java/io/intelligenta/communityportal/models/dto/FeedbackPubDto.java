package io.intelligenta.communityportal.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.feedback.Feedback;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

@Data
public class FeedbackPubDto {

    private Long id;

    private Feedback feedback;

    private Integer numOfQuestions;

    private Institution institution;

    private boolean read;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime readAt;

    private boolean finished;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime dateCreated;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime dateUpdated;


}
