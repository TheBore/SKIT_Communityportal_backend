package io.intelligenta.communityportal.models;

import io.intelligenta.communityportal.models.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorReport extends CreationAwareEntity{

    @Column(length = 4000)
    private String reportMk;
    @Column(length = 4000)
    private String reportAl;
    @Column(length = 4000)
    private String reportEn;

    private Boolean hasEvaluation;

    private Boolean active;

    @ManyToOne
    private Status status;

//    @ManyToOne
//    private Indicator indicator;

    @ManyToOne
    private Activity activity;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User updatedByUserModerator;

    @ManyToOne
    private User updatedByUserEvaluator;

    @ManyToOne
    private User deletedByUser;

    @ManyToOne
    private Evaluation evaluation;

}
