package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.Institution;
import lombok.Data;

import java.util.List;

@Data
public class TagWithInstitutionDto {

    private Long id;

    private String nameMk;

    private String nameAl;

    private List<Institution> institutions;
}
