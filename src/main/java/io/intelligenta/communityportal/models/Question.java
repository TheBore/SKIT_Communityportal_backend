package io.intelligenta.communityportal.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question extends CreationAwareEntity{

    @NotBlank(message = "Body of the question is mandatory")
    private String title;

    private Boolean active;

    @ManyToOne
    private User author;

    @JsonIgnore
    @OneToMany(mappedBy = "question")
    private List<Message> messages;

    @ManyToOne
    private User deletedByUser;

    @ManyToOne
    private AreaOfInterest areaOfInterest;

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", active=" + active +
                '}';
    }
}
