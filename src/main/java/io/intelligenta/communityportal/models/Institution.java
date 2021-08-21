package io.intelligenta.communityportal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.intelligenta.communityportal.models.TypeEnum.TypeAl;
import io.intelligenta.communityportal.models.TypeEnum.TypeEn;
import io.intelligenta.communityportal.models.TypeEnum.TypeMk;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.InstitutionPage;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Institution extends CreationAwareEntity {

    @Column(unique = false)
    private String nameMk;

    @Column(unique = false)
    private String nameAl;

    @Column(unique = false)
    private String nameEn;

    private String addressMk;

    private String addressAl;

    private String addressEn;

    @Enumerated(EnumType.STRING)
    private TypeMk typeOfStreetMk;

    @Enumerated(EnumType.STRING)
    private TypeAl typeOfStreetAl;

    @Enumerated(EnumType.STRING)
    private TypeEn typeOfStreetEn;

    private String streetMk;

    private String streetAl;

    private String streetEn;

    private String streetNumberMk;

//    private String streetNumberAl;

    private String addressDetailsMk;

    private String addressDetailsAl;

    private String addressDetailsEn;

    private String cityMk;

    private String cityAl;

    private String cityEn;

    private String postalCode;

    private String institutionPhone;

    private String institutionAlternativePhone;

    private String institutionAlternativeSecondPhone;

    private String institutionLocales;

    private String institutionAlternativeLocales;

    private String institutionAlternativeSecondLocales;

    private String webSite;

    @Column(name = "direktor_first_name")
    private String direktorFirstName;

    private String direktorLastName;

    private String direktorPhone;

    private String directorLocales;

    private String direktorEmail;

    @ManyToMany
    private List<Tag> tags;

    @Nullable
    @ManyToOne
    private InstitutionCategory category;

    @Nullable
    @ManyToOne
    private Institution parentInstitution;

    @Nullable
    @OneToOne
    private Institution initialInstitution;

    @JsonIgnore
    @OneToMany(mappedBy = "institution")
    private List<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "institution")
    private List<Indicator> indicators;

    @JsonIgnore
    @OneToMany(mappedBy = "institution")
    private List<ActivityInstitution> activityInstitutions;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "competentInstitutions")
//    private List<Activity> indicatorList;

    @JsonIgnore
    @ManyToMany(mappedBy = "institutions")
    private List<BBBUserMeeting> bbbUserMeetings;

    @Nullable
    private boolean active = true;

    @Nullable
    private boolean edited = false;

    @Nullable
    private boolean noticeBoard = false;

    public Institution(InstitutionPage institutionPage) {

    }

}
