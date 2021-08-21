package io.intelligenta.communityportal.models.dto;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.TypeEnum.TypeAl;
import io.intelligenta.communityportal.models.TypeEnum.TypeEn;
import io.intelligenta.communityportal.models.TypeEnum.TypeMk;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class InstitutionPage {

    private Long id;

    private List<Tag> tags;

    private InstitutionCategory category;

    private Long categoryId;

    private String nameMk;

    private String nameAl;

    private String nameEn;

    private String addressMk;

    private String addressAl;

    private String addressEn;

    private TypeMk typeOfStreetMk;

    private TypeAl typeOfStreetAl;

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

    private String direktorLastName;

    private String direktorFirstName;

    private String direktorPhone;

    private String directorLocales;

    private String direktorEmail;

    private Long moderatorId;

    private String firstName;

    private String lastName;

    private String email;

    private String alternativeEmail;

    private String alternativeSecondEmail;

    private String password;

    private String moderatorPhone;

    private String moderatorAlternativePhone;

    private String moderatorAlternativeSecondPhone;

    private String locales;

    private String alternativeLocales;

    private String alternativeSecondLocales;

    private Institution parentInstitution;

    private Long parentInstitutionId;

    private Institution initialInstitution;

    private Long initialInstitutionId;

    private boolean noticeBoard;

    public InstitutionPage(Institution institution) {
        this.id = institution.getId();
        this.addressMk = institution.getAddressMk();
        this.addressAl = institution.getAddressAl();
        this.addressEn = institution.getAddressEn();
        this.nameAl = institution.getNameAl();
        this.nameMk = institution.getNameMk();
        this.nameEn = institution.getNameEn();
        this.institutionPhone = institution.getInstitutionPhone();
        this.institutionAlternativePhone = institution.getInstitutionAlternativePhone();
        this.institutionAlternativeSecondPhone = institution.getInstitutionAlternativeSecondPhone();
        this.institutionLocales = institution.getInstitutionLocales();
        this.institutionAlternativeLocales = institution.getInstitutionAlternativeLocales();
        this.institutionAlternativeSecondLocales = institution.getInstitutionAlternativeSecondLocales();
        this.webSite = institution.getWebSite();
        this.noticeBoard = institution.isNoticeBoard();
        this.typeOfStreetMk = institution.getTypeOfStreetMk();
        this.typeOfStreetAl = institution.getTypeOfStreetAl();
        this.typeOfStreetEn = institution.getTypeOfStreetEn();
        this.streetMk = institution.getStreetMk();
        this.streetAl = institution.getStreetAl();
        this.streetEn = institution.getStreetEn();
        this.streetNumberMk = institution.getStreetNumberMk();
//        this.streetNumberAl = institution.getStreetNumberAl();
        this.addressDetailsMk = institution.getAddressDetailsMk();
        this.addressDetailsAl = institution.getAddressDetailsAl();
        this.addressDetailsEn = institution.getAddressDetailsEn();
        this.cityMk = institution.getCityMk();
        this.cityAl = institution.getCityAl();
        this.cityEn = institution.getCityEn();
        this.postalCode = institution.getPostalCode();
        this.direktorFirstName = institution.getDirektorFirstName();
        this.direktorLastName = institution.getDirektorLastName();
        this.direktorEmail = institution.getDirektorEmail();
        this.direktorPhone = institution.getDirektorPhone();
        this.directorLocales = institution.getDirectorLocales();
        List<User> moderator = institution.getUsers().stream().filter(user -> user.getRole().equals(UserRole.ROLE_INSTITUTIONAL_MODERATOR)).collect(Collectors.toList());
        if (moderator.size() > 0 && moderator.get(0).getActive().equals(true)) {
            this.moderatorId = moderator.get(0).getId();
            this.firstName = moderator.get(0).getFirstName();
            this.lastName = moderator.get(0).getLastName();
            this.email = moderator.get(0).getEmail();
            this.alternativeEmail = moderator.get(0).getAlternativeEmail();
            this.alternativeSecondEmail = moderator.get(0).getAlternativeSecondEmail();
            this.password = "";
            this.moderatorPhone = moderator.get(0).getPhone();
            this.moderatorAlternativePhone = moderator.get(0).getAlternativePhone();
            this.moderatorAlternativeSecondPhone = moderator.get(0).getAlternativeSecondPhone();
            this.locales = moderator.get(0).getLocales();
            this.alternativeLocales = moderator.get(0).getAlternativeLocales();
            this.alternativeSecondLocales = moderator.get(0).getAlternativeSecondLocales();
        } else {
            this.moderatorId = 0l;
            this.firstName = "X";
            this.lastName = "X";
            this.email = "X";
            this.alternativeEmail = "X";
            this.alternativeSecondEmail = "X";
            this.password = "X";
            this.moderatorPhone = "X";
            this.moderatorAlternativePhone = "X";
            this.moderatorAlternativeSecondPhone = "X";
            this.locales = "X";
            this.alternativeLocales = "X";
            this.alternativeSecondLocales = "X";
        }
        this.tags = institution.getTags();
        this.category = institution.getCategory();
        this.parentInstitution = institution.getParentInstitution();
        this.initialInstitution = institution.getInitialInstitution();
    }
}
