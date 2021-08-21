package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends CreationAwareEntity{

    @Column(length = 500)
    private String nameMk;
    @Column(length = 500)
    private String nameAl;
    @Column(length = 500)
    private String nameEn;

    private Boolean financialImplications;

    private String endDate;

    private Boolean active;

    @ManyToOne
    private Measure measure;

    @ManyToOne
    private Status status;

    @ManyToOne
    private ActivityInstitution competentInstitution;

    @ManyToMany
    private List<ActivityInstitution> activityInstitutions;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User updatedByUser;

    @ManyToOne
    private User deletedByUser;

    @JsonIgnore
    @OneToMany(mappedBy = "activity")
    private List<Indicator> indicators;

    @JsonIgnore
    @OneToMany(mappedBy = "activity")
    private List<IndicatorReport> indicatorReports;

    @Enumerated(EnumType.STRING)
    private ActivityDateType activityDateType;

    @JsonFormat(pattern = "yyyy", timezone = "Europe/Skopje")
    private LocalDate yearDate;

    private Boolean continuously;

}
