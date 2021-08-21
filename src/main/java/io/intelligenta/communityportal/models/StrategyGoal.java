package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Data
public class StrategyGoal extends CreationAwareEntity{

    private String nameMk;

    private String nameAl;

    private String nameEn;

    @Column(length = 4000)
    private String descriptionMk;

    @Column(length = 4000)
    private String descriptionAl;

    @Column(length = 4000)
    private String descriptionEn;

    private Boolean active;

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
    @ManyToMany(mappedBy = "strategyGoals")
    private List<Problem> problems;

}
