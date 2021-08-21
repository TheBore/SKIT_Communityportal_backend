package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.NAPAreaType;
import lombok.Data;

import javax.persistence.Column;

@Data
public class NapAreaDto {

    private Long id;

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String code;

    private Boolean active;

    @Column(length = 4000)
    private String descriptionMk;

    @Column(length = 4000)
    private String descriptionAl;

    @Column(length = 4000)
    private String descriptionEn;

    private Long napAreaTypeId;

}
