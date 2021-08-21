package io.intelligenta.communityportal.models.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class AnnouncementDto {

    private Long id;

    private String title;

    private Long areaOfInterestId;

    @Column(length = 20000)
    private String body;
}
