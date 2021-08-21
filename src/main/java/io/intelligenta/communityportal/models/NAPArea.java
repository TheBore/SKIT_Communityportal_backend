package io.intelligenta.communityportal.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.intelligenta.communityportal.models.auth.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class NAPArea extends CreationAwareEntity{

    private String nameMk;
    private String nameAl;
    private String nameEn;

    private String code;

    private Boolean active;

    @Column(length = 4000)
    private String descriptionMk;

    @Column(length = 4000)
    private String descriptionAl;

    @Column(length = 4000)
    private String descriptionEn;

    @JsonIgnore
    @ManyToOne
    private User createdByUser;

    @JsonIgnore
    @ManyToOne
    private User updatedByUser;

    @JsonIgnore
    @ManyToOne
    private User deletedByUser;

    @JsonIgnore
    @OneToMany(mappedBy = "napArea")
    private List<Problem> problems;


    @ManyToOne
    private NAPAreaType napAreaType;

    @Override
    public String toString() {
        return "NAPArea{" +
                "nameMk='" + nameMk + '\'' +
                ", nameAl='" + nameAl + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", code='" + code + '\'' +
                ", active=" + active +
                ", descriptionMk='" + descriptionMk + '\'' +
                ", descriptionAl='" + descriptionAl + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                '}';
    }
}