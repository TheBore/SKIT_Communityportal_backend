package io.intelligenta.communityportal.models.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.CreationAwareEntity;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Feedback extends CreationAwareEntity {

    private String name;

    private String description;

    private LocalDate dueDate;

    @ManyToOne
    private User creator;

    @ManyToOne
    private AreaOfInterest areaOfInterest;

    private Boolean isPublished;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "feedback")
    private List<FeedbackItem> feedbackItems;

    @JsonIgnore
    @OneToMany(mappedBy = "feedback")
    private List<FeedbackPublication> feedbackPublications;
}
