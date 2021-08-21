package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.CreationAwareEntity;
import io.intelligenta.communityportal.models.IndicatorReport;
import io.intelligenta.communityportal.models.Measure;
import io.intelligenta.communityportal.models.NAP;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation extends CreationAwareEntity {

    private String descriptionMk;
    private String descriptionAl;
    private String descriptionEn;

    private Boolean open;

    private LocalDateTime dateCreated;

    @ManyToOne
    private NAP nap;

    @JsonIgnore
    @OneToMany
    private List<IndicatorReport> indicatorReports;

}
