package io.intelligenta.communityportal.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProblemDto {

    private Long id;

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String descriptionMk;
    private String descriptionAl;
    private String descriptionEn;

    private Boolean active;

    private Long napArea;

    private Long nap;

    private List<Long> strategyGoals;

//    private User createdByUser;
//    private User updatedByUser;
//    private User deletedByUser;
//
//    private List<Measure> measures;
//    private List<StrategyGoal> strategyGoals;
}
