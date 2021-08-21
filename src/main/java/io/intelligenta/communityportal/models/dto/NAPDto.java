package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.Status;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Data
public class NAPDto
{
    private String nameMk;
    private String nameAl;
    private String nameEn;

//    private String titleMk;
//    private String titleAl;
//    private String titleEn;

    private String startDate;
    private String endDate;

    private String descriptionMk;
    private String descriptionAl;
    private String descriptionEn;

//    private Boolean openForEvaluation;
//
//    private Boolean active;

    private Long status;
}
