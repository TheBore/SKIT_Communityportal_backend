package io.intelligenta.communityportal.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends CreationAwareEntity{

    @Column(length = 20000)
    private String body;

    private Boolean active;

    @ManyToOne
    private Question question;

    @ManyToOne
    private User author;

    @JsonIgnore
    @OneToMany(mappedBy = "parentMessage")
    private List<Message> childMessages;

    @ManyToOne
    private Message parentMessage;

    @ManyToOne
    private User deletedByUser;

    private String deletedByUserEmail;

    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", active=" + active +
                ", question=" + question +
                ", author=" + author +
                ", parentMessage=" + parentMessage +
                ", deletedByUser=" + deletedByUser +
                ", deletedByUserEmail=" + deletedByUserEmail +
                '}';
    }
}
