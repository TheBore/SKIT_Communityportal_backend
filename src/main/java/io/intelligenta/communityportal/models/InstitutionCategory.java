package io.intelligenta.communityportal.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionCategory extends CreationAwareEntity {

    private String nameMk;

    private String nameAl;

    private String nameEn;

    private Long orderId;

    @Nullable
    @ManyToOne
    private InstitutionCategory parentCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Institution> institutionList;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory")
    private List<InstitutionCategory> childCategories;


    @Override
    public String toString() {
        return "InstitutionCategory{" +
                "nameMk='" + nameMk + '\'' +
                ", nameAl='" + nameAl + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", parentCategory=" + parentCategory +
                '}';
    }

}
