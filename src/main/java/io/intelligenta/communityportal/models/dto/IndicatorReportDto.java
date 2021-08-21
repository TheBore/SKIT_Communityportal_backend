package io.intelligenta.communityportal.models.dto;

import lombok.Data;

@Data
public class IndicatorReportDto {

    private String reportMk;
    private String reportAl;
    private String reportEn;

    private Boolean active;

    private Long activity;

    private Long evaluation;

    private Long status;

    private Long statusReport;
}
