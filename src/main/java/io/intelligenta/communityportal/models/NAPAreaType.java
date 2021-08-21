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
public class NAPAreaType extends CreationAwareEntity{

    private String nameMk;

    private String nameAl;

    private String nameEn;

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
    @OneToMany(mappedBy = "napAreaType")
    private List<NAPArea> napAreas;

    @Override
    public String toString() {
        return "NAPAreaType{" +
                "nameMk='" + nameMk + '\'' +
                ", nameAl='" + nameAl + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", active=" + active +
                ", descriptionMk='" + descriptionMk + '\'' +
                ", descriptionAl='" + descriptionAl + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                '}';
    }
}
