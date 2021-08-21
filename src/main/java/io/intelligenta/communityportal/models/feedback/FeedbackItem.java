package io.intelligenta.communityportal.models.feedback;

import io.intelligenta.communityportal.models.CreationAwareEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FeedbackItem extends CreationAwareEntity {

    private String name;

    private String description;

    @Column(name = "is_required")
    private boolean required;

    @Convert(converter = StringListConverter.class)
    @Column(length = 5000)
    private List<String> options;

    @Enumerated(EnumType.STRING)
    private FeedbackItemType type;

    @ManyToOne
    private Feedback feedback;

}
