package io.intelligenta.communityportal.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.NAP;
import io.intelligenta.communityportal.models.Problem;
import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeasureDto {

    private Long id;

//    @JsonProperty
//    private MultipartFile attachment;

    private String nameMk;

    private String nameAl;

    private String nameEn;

//    private String titleMK;
//
//    private String titleAL;
//
//    private String titleEN;

    private String descriptionMk;

    private String descriptionAl;

    private String descriptionEn;

    private String startDate;

    private String endDate;

    private Long nap;

    private Long status;

    private Long problem;

//    private Long strategyGoal;

//    private User updatedByUser;

    private Boolean active;

//    private List<Activity> activities;

/*    private String fileName;

    private String mimeType;

    private byte[] content;*/
}
