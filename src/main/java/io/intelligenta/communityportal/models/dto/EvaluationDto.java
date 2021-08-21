package io.intelligenta.communityportal.models.dto;

import lombok.Data;

@Data
public class EvaluationDto {

    private String descriptionMk;
    private String descriptionAl;
    private String descriptionEn;

    private Boolean open;

    private Long nap;

}
