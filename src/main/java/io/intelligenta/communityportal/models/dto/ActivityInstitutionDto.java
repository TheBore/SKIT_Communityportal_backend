package io.intelligenta.communityportal.models.dto;

import lombok.Data;

@Data
public class ActivityInstitutionDto {

    private Long id;

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private Long institution;

//    private List<Activity> activityList;
}
