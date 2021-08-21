package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import java.util.List;

@Data
public class AreaOfInterestDto {

    private Long id;

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String descriptionMk;
    private String descriptionAl;
    private String descriptionEn;

}
