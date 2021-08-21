package io.intelligenta.communityportal.models.feedback;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.CreationAwareEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
@Data
public class FeedbackItemAnswer extends CreationAwareEntity {

    @ManyToOne
    private FeedbackItem item;

    @ManyToOne
    private FeedbackPublication publication;

    private String value;

    @OneToOne
    private Attachment attachment;
}
