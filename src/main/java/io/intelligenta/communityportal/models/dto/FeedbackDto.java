package io.intelligenta.communityportal.models.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FeedbackDto {

    private Long id;

    private String name;

    private String description;

    private LocalDate dueDate;

    private Long areaOfInterestId;
}
