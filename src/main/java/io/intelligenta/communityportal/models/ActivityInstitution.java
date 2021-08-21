package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInstitution extends CreationAwareEntity {

    private String nameMk;
    private String nameAl;
    private String nameEn;

    @ManyToOne
    private Institution institution;

    @JsonIgnore
    @ManyToMany(mappedBy = "activityInstitutions")
    private List<Activity> activityList;

    @JsonIgnore
    @OneToMany(mappedBy = "competentInstitution")
    private List<Activity> activities;
}
