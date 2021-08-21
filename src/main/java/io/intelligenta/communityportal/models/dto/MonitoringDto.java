package io.intelligenta.communityportal.models.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class MonitoringDto {

    Integer year;

    HashMap<String,Integer> count;
}
