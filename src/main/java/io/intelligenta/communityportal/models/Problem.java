package io.intelligenta.communityportal.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Problem extends CreationAwareEntity{

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

    @ManyToOne
    private NAPArea napArea;

    @ManyToOne
    private NAP nap;

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
    @OneToMany(mappedBy = "problem")
    private List<Measure> measures;

    @ManyToMany
    private List<StrategyGoal> strategyGoals;

}
