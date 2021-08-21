package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Status extends BaseEntity {

    private String statusMk;
    private String statusAl;
    private String statusEn;

    private Boolean isEvaluable;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Column(name ="statusType", updatable = false, insertable = false)
    private String statusString;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<IndicatorReport> indicatorReports;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<Measure> measures;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<Activity> activities;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<Indicator> indicators;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<NAP> naps;

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
        this.statusString = statusType.toString();
    }

}
