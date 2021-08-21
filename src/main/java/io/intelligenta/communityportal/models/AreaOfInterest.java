package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.feedback.Feedback;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class AreaOfInterest extends CreationAwareEntity{

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String descriptionMk;
    private String descriptionAl;
    private String descriptionEn;

    private Boolean active;

    @JsonIgnore
    @ManyToMany(mappedBy = "areasOfInterest")
    private List<User> users;

    @JsonIgnore
    @ManyToOne
    private User createdByUser;

    @JsonIgnore
    @ManyToOne
    private User updatedByUser;

    @JsonIgnore
    @ManyToOne
    private User deletedByUser;

    @JsonIgnore
    @OneToMany(mappedBy = "areaOfInterest")
    private List<Announcement> announcements;

    @JsonIgnore
    @OneToMany(mappedBy = "areaOfInterest")
    private List<Feedback> feedbacks;

    @JsonIgnore
    @OneToMany(mappedBy = "areaOfInterest")
    private List<Question> questions;

    @Override
    public String toString() {
        return "AreaOfInterest{" +
                "nameMk='" + nameMk + '\'' +
                ", nameAl='" + nameAl + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", descriptionMk='" + descriptionMk + '\'' +
                ", descriptionAl='" + descriptionAl + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                ", active=" + active +
                '}';
    }
}
