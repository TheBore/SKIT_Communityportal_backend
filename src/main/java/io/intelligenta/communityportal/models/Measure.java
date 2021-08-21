package io.intelligenta.communityportal.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Measure extends CreationAwareEntity{

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String titleMk;
    private String titleAl;
    private String titleEn;

    @Column(length = 4000)
    private String descriptionMk;

    @Column(length = 4000)
    private String descriptionAl;

    @Column(length = 4000)
    private String descriptionEn;

    @JsonFormat(pattern = "dd.MM.yyyy", timezone = "Europe/Skopje")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd.MM.yyyy", timezone = "Europe/Skopje")
    private LocalDate endDate;

    private Boolean active;

    private String fileName;
    private String mimeType;

    @JsonIgnore
    @Lob
    private byte[] content;

/*    @ManyToOne
    private NAP nap;*/

/*    @ManyToOne
    private StrategyGoal strategyGoal;*/

    @ManyToOne
    private Status status;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User updatedByUser;

    @ManyToOne
    private User deletedByUser;

    @JsonIgnore
    @OneToMany(mappedBy = "measure")
    private List<Activity> activities;

    @ManyToOne
    private Problem problem;

}
