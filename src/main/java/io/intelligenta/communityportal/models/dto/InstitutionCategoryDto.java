package io.intelligenta.communityportal.models.dto;

import lombok.Data;

@Data
public class InstitutionCategoryDto {

    private Long id;

    private String nameMk;

    private String nameAl;

    private String nameEn;

    private Long parentCategory;
}
