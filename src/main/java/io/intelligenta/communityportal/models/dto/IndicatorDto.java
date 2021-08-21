package io.intelligenta.communityportal.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.IndicatorReport;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Data
public class IndicatorDto {

    private Long id;

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String startDate;
    private String endDate;

    private String type;

    private Integer counter;
    private Boolean finished;

    private Boolean active;

    private Long activity;

    private Long status;

    private Long institution;

//    private User createdByUser;
//    private User updatedByUser;
//    private User deletedByUser;
//
//    private List<IndicatorReport> indicatorReports;
}
