package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.ActivityDateType;
import lombok.Data;

import java.util.List;

@Data
public class ActivityDto {
    private String nameMk;
    private String nameAl;
    private String nameEn;

    private Boolean financialImplications;

    private String endDate;

    private Boolean active;

    private Long measure;

    private Long status;

    private Long competentInstitution;

    private List<Long> activityInstitutions;

    private ActivityDateType activityDateType;

    private String yearDate;

    private boolean continuously;

}
