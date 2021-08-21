package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class NAP extends CreationAwareEntity{

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

    @JsonFormat(pattern = "MM.yyyy", timezone = "Europe/Skopje")
    private LocalDate startDate;

    @JsonFormat(pattern = "MM.yyyy", timezone = "Europe/Skopje")
    private LocalDate endDate;

    private Boolean openForEvaluation;

    private Boolean active;

    @ManyToOne
    private Status status;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User updatedByUser;

    @ManyToOne
    private User deletedByUser;

/*    @JsonIgnore
    @OneToMany(mappedBy = "nap")
    private List<Measure> measures;*/

    @JsonIgnore
    @OneToMany(mappedBy = "nap")
    private List<Evaluation> evaluations;

    @JsonIgnore
    @OneToMany(mappedBy = "nap")
    private List<Problem> problems;

}
