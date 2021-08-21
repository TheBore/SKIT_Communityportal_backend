package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.enumeration.IndicatorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indicator extends CreationAwareEntity{

    private String nameMk;
    private String nameAl;
    private String nameEn;

    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;

    private Boolean finished;

    private Integer counter;

    @JsonFormat(pattern = "dd.MM.yyyy", timezone = "Europe/Skopje")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd.MM.yyyy", timezone = "Europe/Skopje")
    private LocalDate endDate;

    private Boolean active;

    @ManyToOne
    private Activity activity;

    @ManyToOne
    private Status status;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User updatedByUser;

    @ManyToOne
    private User deletedByUser;

    @ManyToOne
    private ActivityInstitution institution;

//    @JsonIgnore
//    @OneToMany(mappedBy = "indicator")
//    private List<IndicatorReport> indicatorReports;

}
