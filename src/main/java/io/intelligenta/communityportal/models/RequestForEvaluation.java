package io.intelligenta.communityportal.models;

import io.intelligenta.communityportal.models.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestForEvaluation extends CreationAwareEntity {

    @ManyToOne
    private User sender;

    @ManyToOne
    private Institution receiver;

    private String title;

    private String body;

}
