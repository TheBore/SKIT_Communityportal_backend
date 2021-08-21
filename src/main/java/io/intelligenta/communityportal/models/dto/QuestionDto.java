package io.intelligenta.communityportal.models.dto;

import lombok.Data;

@Data
public class QuestionDto {

    private Long id;

    private String title;

    private Long areaOfInterestId;
}
