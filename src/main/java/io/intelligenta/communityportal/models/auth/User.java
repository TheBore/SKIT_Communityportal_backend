package io.intelligenta.communityportal.models.auth;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.intelligenta.communityportal.models.*;
import lombok.Getter;
import lombok.Setter;
import org.docx4j.wml.PPrBase;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends CreationAwareEntity implements UserDetails {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    private String email;

    private String alternativeEmail;

    private String alternativeSecondEmail;

    @JsonIgnore
    private String password;

    private String username;

    private String phone;

    private String alternativePhone;

    private String alternativeSecondPhone;

    private String locales;

    private String alternativeLocales;

    private String alternativeSecondLocales;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToOne
    private Institution institution;

    @ManyToMany
    private List<AreaOfInterest> areasOfInterest;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Announcement> announcement;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Message> messages;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<NAP> naps;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<NAP> napList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<Measure> measures;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<Measure> measureList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<Activity> activities;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<Activity> activityList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<Indicator> indicators;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<Indicator> indicatorList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<IndicatorReport> indicatorReports;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUserModerator")
    private List<IndicatorReport> indicatorReportListModerator;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUserEvaluator")
    private List<IndicatorReport> indicatorReportListEvaluator;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<AreaOfInterest> areaOfInterestList;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<AreaOfInterest> areasOfInterestList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<NAPArea> napAreas;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<NAPArea> napAreaList;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<NAPArea> napAreasList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<NAPAreaType> napAreaTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<NAPAreaType> napAreaTypeList;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<NAPAreaType> napAreaTypesList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<Problem> problems;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<Problem> problemList;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<Problem> problemsList;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser")
    private List<StrategyGoal> strategyGoals;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedByUser")
    private List<StrategyGoal> strategyGoalList;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<StrategyGoal> strategyGoalsList;

    @Transient
    private Long institution_id;

    @Transient
    private List<Long> areasOfInterest_ids;

    private Boolean active;

    private String passwordResetString;

    @Nullable
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime passwordResetActiveUntil;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Skopje")
    private LocalDateTime dateRegistrationCompleted;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<Question> deletedQuestions;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<Message> deletedMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<NAP> deletedNaps;


    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<Measure> deletedMeasures;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<Activity> deletedActivities;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<Indicator> deletedIndicators;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<IndicatorReport> deletedIndicatorReports;

    @JsonIgnore
    @OneToMany(mappedBy = "deletedByUser")
    private List<AreaOfInterest> deletedAreasOfInterest;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                email.equals(user.email) &&
                username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username);
    }
}

