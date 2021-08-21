package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.dto.InstitutionModerators;
import io.intelligenta.communityportal.models.dto.InstitutionPage;
import io.intelligenta.communityportal.models.dto.InstitutionWithModerators;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.InstitutionCategoryService;
import io.intelligenta.communityportal.service.InstitutionService;
import io.intelligenta.communityportal.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class InstitutionServiceImpl extends BaseEntityCrudServiceImpl<Institution, InstitutionRepository> implements InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final TagService tagService;
    private final InstitutionCategoryService institutionCategoryService;
    private final UserRepository userRepository;
    private final InstitutionCategoryRepository institutionCategoryRepository;
    private final FeedbackPublicationRepository feedbackPublicationRepository;
    private final PublicYearReportRepository publicYearReportRepository;
    private final PublicDocumentForYearRepository publicDocumentForYearRepository;
    private final TagRepository tagRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public InstitutionServiceImpl(InstitutionRepository institutionRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TagService tagService, InstitutionCategoryService institutionCategoryService, UserRepository userRepository, InstitutionCategoryRepository institutionCategoryRepository, FeedbackPublicationRepository feedbackPublicationRepository, PublicYearReportRepository publicYearReportRepository, PublicDocumentForYearRepository publicDocumentForYearRepository, TagRepository tagRepository) {
        this.institutionRepository = institutionRepository;
        this.tagService = tagService;
        this.institutionCategoryService = institutionCategoryService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.institutionCategoryRepository = institutionCategoryRepository;
        this.feedbackPublicationRepository = feedbackPublicationRepository;
        this.publicYearReportRepository = publicYearReportRepository;
        this.publicDocumentForYearRepository = publicDocumentForYearRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public void deleteInstitutionById(Long Id) throws InstitutionNotDeletedException {
        Institution institution = this.institutionRepository.findById(Id).orElseThrow(InstitutionNotFoundException::new);
        this.institutionRepository.deleteById(Id);

//        for (User u : institution.getUsers()) {
//            if(u.getInstitution() != null){
//                u.setInstitution(null);
//            }
//        }

//        List<PublicYearReport> publicYearReports = this.publicYearReportRepository.findAllByInstitutionId(Id);
//        this.publicYearReportRepository.deleteAll(publicYearReports);

//        List<PublicDocumentForYear> publicDocumentForYears = this.publicDocumentForYearRepository.findAllByInstitutionId(Id);
//        this.publicDocumentForYearRepository.deleteAll(publicDocumentForYears);

//        List<FeedbackPublication> feedbackPublications = this.feedbackPublicationRepository.findAllByInstitutionId(Id);
//        this.feedbackPublicationRepository.deleteAll(feedbackPublications);
//        List<FeedbackPublication> feedbackPublications = this.feedbackPublicationRepository.findAllByInstitutionId(Id)
//                .stream()
//                .filter(f -> f.getInstitution().getId().equals(Id))
//                .collect(Collectors.toList());
//
//        feedbackPublications.forEach(f -> {
//            f.getInstitution().setId(null);
//        });
    }

    @Override
    public List<InstitutionWithModerators> extractInstitutionsWithModerators() {
        List<Institution> institutions = this.institutionRepository.findAll();
        List<User> moderators = this.userRepository.findByRoleAndActive(UserRole.ROLE_INSTITUTIONAL_MODERATOR, true);
        List<User> admins = this.userRepository.findByRoleAndActive(UserRole.ROLE_ADMIN, true);
        Map<Long, InstitutionWithModerators> institutionWithModerators = new HashMap<>();
        institutions.stream()
                .forEach(inst -> {
                    if (inst.getInstitutionPhone() != null) {
                        inst.setInstitutionPhone(formatPhone(inst.getInstitutionPhone()));
                    }
                    if (inst.getDirektorPhone() != null) {
                        inst.setDirektorPhone(formatPhone(inst.getDirektorPhone()));
                    }
                    institutionRepository.save(inst);
                    institutionWithModerators.put(inst.getId(), new InstitutionWithModerators(inst));
                });
        moderators.stream()
                .filter(user -> user.getInstitution() != null)
                .forEach(user -> {
                    if (user.getPhone() != null) {
                        user.setPhone(formatPhone(user.getPhone()));
                    }
                    userRepository.save(user);
                    institutionWithModerators.get(user.getInstitution().getId()).addModerator(user);
                });
        admins.stream()
                .forEach(admin -> {
                    if (admin.getPhone() != null) {
                        admin.setPhone(formatPhone(admin.getPhone()));
                    }
                    userRepository.save(admin);
                });

        return new ArrayList<>(institutionWithModerators.values());
    }

    @Override
    public InstitutionModerators extractOneInstitutionWithModerators(Long id) {
        Institution institution = this.institutionRepository.findById(id).orElseThrow(InstitutionNotFoundException::new);
        List<User> moderators = this.userRepository.findByRoleAndInstitutionIdAndActive(UserRole.ROLE_INSTITUTIONAL_MODERATOR, id, true);
        InstitutionModerators institutionModerators = new InstitutionModerators();
        institutionModerators.setInstitution(institution);
        institutionModerators.setModerators(moderators);
        return institutionModerators;
    }

    @Override
    public Page<InstitutionPage> findAllWithModerator(String keyword, Pageable pageable) {
        Long size = 0l;
        keyword = keyword.toLowerCase();

        List<Institution> institutions = keyword.equals("") ? getRepository().findAllByActiveAndEdited(true, false, pageable).getContent() : getRepository().findWithFilter(keyword, pageable).getContent();
        List<InstitutionPage> responseList = new ArrayList<>();
        if (keyword.equals("")) {
            size = getRepository().findAll().stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).count();
        } else {
            size = (long) getRepository().findFilter(keyword).size();
        }
        institutions.forEach(institution -> responseList.add(new InstitutionPage(institution)));

        return new PageImpl<>(responseList, pageable, size);
    }


    public List<Institution> findAll() {
        return institutionRepository.findAll().stream().filter(institution -> !institution.isEdited()).filter(Institution::isActive).collect(Collectors.toList());
    }

    public Institution findById(Long id) {
        return institutionRepository.findById(id).orElseThrow(InstitutionNotFoundException::new);
    }


    @Override
    public InstitutionPage createInstitution(InstitutionPage institution) {
        Institution newInstitution = new Institution();
        User moderator = new User();

        List<Tag> tags = null;

        if (institution.getTags() != null) {
            tags = tagService.findAll(institution.getTags()
                    .stream()
                    .map(tag -> tag.getId())
                    .collect(Collectors.toList()));
        }
        newInstitution.setTags(tags);

        if (institution.getCategoryId() != null) {
            newInstitution.setCategory(institutionCategoryRepository.findById(institution.getCategoryId()).orElseThrow(InstitutionCategoryNotFoundException::new));
        }
        newInstitution.setWebSite(institution.getWebSite());
//        newInstitution.setAddressMk(institution.getAddressMk());
//        newInstitution.setAddressAl(institution.getAddressAl());
        newInstitution.setTypeOfStreetMk(institution.getTypeOfStreetMk());
        newInstitution.setTypeOfStreetAl(institution.getTypeOfStreetAl());
        newInstitution.setTypeOfStreetEn(institution.getTypeOfStreetEn());

        if (institution.getStreetMk() != null) {
            String streetMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            streetMk = streetMk.trim();
            newInstitution.setStreetMk(streetMk);
        }

        if (institution.getStreetAl() != null) {
            String streetAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetAl = streetAl.trim();
            newInstitution.setStreetAl(streetAl);
        }

        if (institution.getStreetEn() != null) {
            String streetEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetEn = streetEn.trim();
            newInstitution.setStreetEn(streetEn);
        }

        newInstitution.setStreetNumberMk(institution.getStreetNumberMk());
//        newInstitution.setStreetNumberAl(institution.getStreetNumberAl());
        newInstitution.setAddressDetailsMk(institution.getAddressDetailsMk());
        newInstitution.setAddressDetailsAl(institution.getAddressDetailsAl());
        newInstitution.setAddressDetailsEn(institution.getAddressDetailsEn());
        newInstitution.setCityMk(institution.getCityMk());
        newInstitution.setCityAl(institution.getCityAl());
        newInstitution.setCityEn(institution.getCityEn());
        newInstitution.setPostalCode(institution.getPostalCode());
        newInstitution.setNameAl(institution.getNameAl());
        newInstitution.setNameMk(institution.getNameMk());
        newInstitution.setNameEn(institution.getNameEn());
        newInstitution.setNoticeBoard(institution.isNoticeBoard());

        String streetMacedonian = "";
        if (institution.getStreetMk() != null) {
            String streetMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            streetMk = streetMk.trim();
            boolean typeMk = institution.getTypeOfStreetMk() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsMk() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityMk() != null;


            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + streetMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    newInstitution.setAddressMk(streetMacedonian);
                } else {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + streetMk + " бр." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    newInstitution.setAddressMk(streetMacedonian);
                }
            } else {
                streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + streetMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                newInstitution.setAddressMk(streetMacedonian);
            }
        }

        String streetAlbanian = "";
        if (institution.getStreetAl() != null) {
            String streetAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetAl = streetAl.trim();
            boolean typeMk = institution.getTypeOfStreetAl() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsAl() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityAl() != null;
            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + streetAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    newInstitution.setAddressAl(streetAlbanian);
                } else {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + streetAl + " nr." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    newInstitution.setAddressAl(streetAlbanian);
                }
            } else {
                streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + streetAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                newInstitution.setAddressAl(streetAlbanian);
            }
        }

        String streetEnglish = "";
        if (institution.getStreetEn() != null) {
            String streetEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetEn = streetEn.trim();
            boolean typeMk = institution.getTypeOfStreetEn() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsEn() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityEn() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + streetEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    newInstitution.setAddressEn(streetEnglish);
                } else {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + streetEn + " num." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    newInstitution.setAddressEn(streetEnglish);
                }
            } else {
                streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + streetEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                newInstitution.setAddressEn(streetEnglish);
            }
        }

        if (institution.getInstitutionPhone() != null) {
            newInstitution.setInstitutionPhone(formatPhone(institution.getInstitutionPhone()));
            if (institution.getInstitutionLocales() != null) {
                newInstitution.setInstitutionLocales(institution.getInstitutionLocales());
            }
        }

        if (institution.getInstitutionAlternativePhone() != null) {
            newInstitution.setInstitutionAlternativePhone(formatPhone(institution.getInstitutionAlternativePhone()));
            if (institution.getInstitutionAlternativeLocales() != null) {
                newInstitution.setInstitutionAlternativeLocales(institution.getInstitutionAlternativeLocales());
            }
        }

        if (institution.getInstitutionAlternativeSecondPhone() != null) {
            newInstitution.setInstitutionAlternativeSecondPhone(formatPhone(institution.getInstitutionAlternativeSecondPhone()));
            if (institution.getInstitutionAlternativeSecondLocales() != null) {
                newInstitution.setInstitutionAlternativeSecondLocales(institution.getInstitutionAlternativeSecondLocales());
            }
        }

        newInstitution.setDirektorEmail(institution.getDirektorEmail());
        newInstitution.setDirektorFirstName(institution.getDirektorFirstName());
        newInstitution.setDirektorLastName(institution.getDirektorLastName());

        if (institution.getDirektorPhone() != null) {
            newInstitution.setDirektorPhone(formatPhone(institution.getDirektorPhone()));
            newInstitution.setDirectorLocales(institution.getDirectorLocales());
        }

        newInstitution.setActive(true);
        newInstitution.setDateCreated(LocalDateTime.now());
        newInstitution.setDateUpdated(LocalDateTime.now());
        if (institution.getParentInstitutionId() != null) {
            newInstitution.setParentInstitution(institutionRepository.findById(institution.getParentInstitutionId()).orElseThrow(InstitutionNotFoundException::new));
        }
        institutionRepository.save(newInstitution);

        //create user code commented
//        moderator.setFirstName(institution.getFirstName());
//        moderator.setLastName(institution.getLastName());
//
//        if (institution.getModeratorPhone() != null) {
//            moderator.setPhone(formatPhone(institution.getModeratorPhone()));
//            if (institution.getLocales() != null) {
//                moderator.setLocales(institution.getLocales());
//            }
//        }
//
//        if (institution.getModeratorAlternativePhone() != null) {
//            moderator.setAlternativePhone(formatPhone(institution.getModeratorAlternativePhone()));
//            if (institution.getAlternativeLocales() != null) {
//                moderator.setAlternativeLocales(institution.getAlternativeLocales());
//            }
//        }
//
//        if (institution.getModeratorAlternativeSecondPhone() != null) {
//            moderator.setAlternativeSecondPhone(formatPhone(institution.getModeratorAlternativeSecondPhone()));
//            if (institution.getAlternativeSecondLocales() != null) {
//                moderator.setAlternativeSecondLocales(institution.getAlternativeSecondLocales());
//            }
//        }
//
//        moderator.setEmail(institution.getEmail().toLowerCase());
//        if (institution.getAlternativeEmail() != null) {
//
//            moderator.setAlternativeEmail(institution.getAlternativeEmail().toLowerCase());
//        }
//        if (institution.getAlternativeSecondEmail() != null) {
//            moderator.setAlternativeSecondEmail(institution.getAlternativeSecondEmail().toLowerCase());
//        }
//        moderator.setActive(true);
//        moderator.setRole(UserRole.ROLE_INSTITUTIONAL_MODERATOR);
//        moderator.setUserRole(UserRole.ROLE_INSTITUTIONAL_MODERATOR);
//        moderator.setPassword(bCryptPasswordEncoder.encode(institution.getPassword()));
//        moderator.setUsername(institution.getEmail());
//        moderator.setInstitution(newInstitution);
//        moderator.setDateRegistrationCompleted(LocalDateTime.now());
//        moderator.setDateCreated(LocalDateTime.now());
//        moderator.setDateUpdated(LocalDateTime.now());
//        userRepository.save(moderator);
        return institution;
    }

    @Override
    public String formatPhone(String phoneNumber) {
        String newPhone = phoneNumber.replaceAll("[^0-9;]", "");
        newPhone = newPhone.trim();
        String[] arrayPhone = newPhone.split("");
        String space = " ";

        List<String> phoneList = Arrays.asList(arrayPhone);
        for (int i = 0; i < phoneList.size(); i++) {
            // mobilen (07X XXX XXX)
            if (phoneList.size() == 9 && phoneList.get(1).equals("7")) {
                phoneList = Collections.singletonList(phoneList.subList(0, 3) + space + phoneList.subList(3, 6) + space + phoneList.subList(6, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // fiksen Skopje so poziven (02 XXXX XXX)
            else if (phoneList.size() == 9 && phoneList.get(1).equals("2")) {
                phoneList = Collections.singletonList(phoneList.subList(0, 2) + space + phoneList.subList(2, 6) + space + phoneList.subList(6, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // mobilen bez 0 na pocetok (7X XXX XXX)
            else if (phoneList.size() == 8 && phoneList.get(0).equals("7")) {
                phoneList = Collections.singletonList("0" + phoneList.subList(0, 2) + space + phoneList.subList(2, 5) + space + phoneList.subList(5, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // fiksen drugi gradovi so poziven (0XX XXX XXX)
            else if (phoneList.size() == 9) {
                phoneList = Collections.singletonList(phoneList.subList(0, 3) + space + phoneList.subList(3, 6) + space + phoneList.subList(6, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // fiksen Skopje bez poziven (XXXX XXX)
            else if (phoneList.size() == 7) {
                phoneList = Collections.singletonList(phoneList.subList(0, 4) + space + phoneList.subList(4, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // fiksen drugi gradovi bez poziven(XXX XXX)
            else if (phoneList.size() == 6) {
                phoneList = Collections.singletonList(phoneList.subList(0, 3) + space + phoneList.subList(3, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // mobilen (+389 7X XXX XXX)
            else if (phoneList.size() == 11 && phoneList.get(3).equals("7")) {
                phoneList = Collections.singletonList("0" + phoneList.subList(3, 5) + space + phoneList.subList(5, 8) + space + phoneList.subList(8, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
            // fiksen (+389 2 XXXX XXX)
            else if (phoneList.size() == 11 && phoneList.get(3).equals("2")) {
                phoneList = Collections.singletonList("0" + phoneList.subList(3, 4) + space + phoneList.subList(4, 8) + space + phoneList.subList(8, phoneList.size()));
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            } else {
                String str2 = phoneList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "");
                return str2;
            }
        }
        return phoneList.toString();
    }

    @Override
    public List<Institution> allByParentRecursively(List<Long> institutionsIds) {
        List<BigInteger> institutionIds = new ArrayList<>();
        for (long id : institutionsIds) {
            institutionIds.addAll(institutionRepository.getAllByParentInstitutionRecursively(id));
        }
        List<BigInteger> institutionIdsWithoutDuplicates = new ArrayList<>(new HashSet<>(institutionIds));
        List<Institution> institutionList = new ArrayList<>();
        for (BigInteger id : institutionIdsWithoutDuplicates) {
            institutionList.add(institutionRepository.findById((id.longValue())).get());
        }
        return institutionList;
    }

    @Override
    public List<Institution> allByTags(List<Long> tagIds) {
        List<Institution> institutions = new ArrayList<>();
        for (int i = 0; i < tagIds.size(); i++) {
            Tag tag = tagRepository.findById(tagIds.get(i)).orElseThrow(TagNotFoundException::new);
            institutions.addAll(institutionRepository.findAllByTags(tag));
        }
        return institutions;
    }

    @Override
    public List<Institution> allByCategoriesRecursively(List<Long> ids) {
        List<Institution> institutions = new ArrayList<>();
        List<BigInteger> recursiveCategories = new ArrayList<>();
        for (Long id : ids) {
            recursiveCategories.
                    addAll(institutionCategoryRepository.
                            getAllByCategoryRecursively(id));
        }
        recursiveCategories.forEach((item) -> {
            InstitutionCategory institutionCategory = institutionCategoryRepository.findById(item.longValue()).orElseThrow(InstitutionCategoryNotFoundException::new);
            institutions.addAll(institutionRepository.findAllByCategory(institutionCategory).stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList()));
        });
        return new ArrayList<>(new HashSet<>(institutions));
    }


    @Override
    public InstitutionPage updateInstitution(InstitutionPage institution) {
        Institution updatedInstitution = institutionRepository.findById(institution.getId()).orElseThrow(InstitutionNotFoundException::new);

        updatedInstitution.setNameMk(institution.getNameMk());
        updatedInstitution.setNameAl(institution.getNameAl());
        updatedInstitution.setNameEn(institution.getNameEn());
//        updatedInstitution.setAddressMk(institution.getAddressMk());
//        updatedInstitution.setAddressAl(institution.getAddressAl());
        updatedInstitution.setTypeOfStreetMk(institution.getTypeOfStreetMk());
        updatedInstitution.setTypeOfStreetAl(institution.getTypeOfStreetAl());
        updatedInstitution.setTypeOfStreetEn(institution.getTypeOfStreetEn());

        if (institution.getStreetMk() != null) {
            String streetMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            streetMk = streetMk.trim();
            updatedInstitution.setStreetMk(streetMk);
        }

        if (institution.getStreetAl() != null) {
            String streetAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetAl = streetAl.trim();
            updatedInstitution.setStreetAl(streetAl);
        }

        if (institution.getStreetEn() != null) {
            String streetEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetEn = streetEn.trim();
            updatedInstitution.setStreetEn(streetEn);
        }

        updatedInstitution.setStreetNumberMk(institution.getStreetNumberMk());
//        updatedInstitution.setStreetNumberAl(institution.getStreetNumberAl());
        updatedInstitution.setAddressDetailsMk(institution.getAddressDetailsMk());
        updatedInstitution.setAddressDetailsAl(institution.getAddressDetailsAl());
        updatedInstitution.setAddressDetailsEn(institution.getAddressDetailsEn());
        updatedInstitution.setCityMk(institution.getCityMk());
        updatedInstitution.setCityAl(institution.getCityAl());
        updatedInstitution.setCityEn(institution.getCityEn());
        updatedInstitution.setPostalCode(institution.getPostalCode());
        updatedInstitution.setNoticeBoard(institution.isNoticeBoard());

        String streetMacedonian = "";
        if (institution.getStreetMk() != null) {
            String strMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            strMk = strMk.trim();
            boolean typeMk = institution.getTypeOfStreetMk() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsMk() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityMk() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    updatedInstitution.setAddressMk(streetMacedonian);
                } else {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " бр." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    updatedInstitution.setAddressMk(streetMacedonian);
                }
            } else {
                streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                updatedInstitution.setAddressMk(streetMacedonian);
            }
        }

        String streetAlbanian = "";
        if (institution.getStreetAl() != null) {
            String strAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strAl = strAl.trim();
            boolean typeMk = institution.getTypeOfStreetAl() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsAl() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityAl() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    updatedInstitution.setAddressAl(streetAlbanian);
                } else {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " nr." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    updatedInstitution.setAddressAl(streetAlbanian);
                }
            } else {
                streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                updatedInstitution.setAddressAl(streetAlbanian);
            }
        }

        String streetEnglish = "";
        if (institution.getStreetEn() != null) {
            String strEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strEn = strEn.trim();
            boolean typeMk = institution.getTypeOfStreetEn() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsEn() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityEn() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    updatedInstitution.setAddressEn(streetEnglish);
                } else {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " num." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    updatedInstitution.setAddressEn(streetEnglish);
                }
            } else {
                streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                updatedInstitution.setAddressEn(streetEnglish);
            }
        }

        List<Tag> tags = institution.getTags();
        if (institution.getTags() != null) {
            tags = tagService.findAll(institution.getTags()
                    .stream()
                    .map(tag -> tag.getId())
                    .collect(Collectors.toList()));
        }
        for(Tag tag : tags){
            updatedInstitution.getTags().add(tag);
        }

        if (institution.getInstitutionPhone() != null) {
            updatedInstitution.setInstitutionPhone(formatPhone(institution.getInstitutionPhone()));
        }
        updatedInstitution.setInstitutionLocales(institution.getInstitutionLocales());

        if (institution.getInstitutionAlternativePhone() != null) {
            updatedInstitution.setInstitutionAlternativePhone(formatPhone(institution.getInstitutionAlternativePhone()));
        }
        updatedInstitution.setInstitutionAlternativeLocales(institution.getInstitutionAlternativeLocales());

        if (institution.getInstitutionAlternativeSecondPhone() != null) {
            updatedInstitution.setInstitutionAlternativeSecondPhone(formatPhone(institution.getInstitutionAlternativeSecondPhone()));
        }
        updatedInstitution.setInstitutionAlternativeSecondLocales(institution.getInstitutionAlternativeSecondLocales());

        if (institution.getInstitutionPhone() != null) {
            updatedInstitution.setInstitutionPhone(formatPhone(institution.getInstitutionPhone()));
        }
        updatedInstitution.setWebSite(institution.getWebSite());
        updatedInstitution.setDirektorFirstName(institution.getDirektorFirstName());
        updatedInstitution.setDirektorLastName(institution.getDirektorLastName());

        if (institution.getDirektorPhone() != null) {
            updatedInstitution.setDirektorPhone(formatPhone(institution.getDirektorPhone()));
        }
        updatedInstitution.setDirectorLocales(institution.getDirectorLocales());

        updatedInstitution.setDirektorEmail(institution.getDirektorEmail());
        updatedInstitution.setTags(institution.getTags());

        if (institution.getCategoryId() != null) {
            if (!institution.getCategoryId().equals(updatedInstitution.getCategory().getId())) {
                updatedInstitution.setCategory(institutionCategoryRepository.findById(institution.getCategoryId()).orElseThrow(InstitutionCategoryNotFoundException::new));
            }
        }
        if (institution.getParentInstitutionId() != null) {
            Institution parentInstitution = institutionRepository.findById(institution.getParentInstitutionId()).orElse(null);
            if (updatedInstitution.getParentInstitution() == null || !updatedInstitution.getParentInstitution().equals(parentInstitution)) {
                updatedInstitution.setParentInstitution(parentInstitution);
            } else {
                throw new InstitutionParentChildException();
            }
        } else {
            updatedInstitution.setParentInstitution(null);
        }

        institutionRepository.save(updatedInstitution);
        //update or create user code commented
//        User user = userRepository.findById(institution.getModeratorId()).orElse(null);
//
//        if (user != null) {
//            user.setEmail(institution.getEmail().toLowerCase());
//            if (institution.getAlternativeEmail() != null) {
//                user.setAlternativeEmail(institution.getAlternativeEmail().toLowerCase());
//            }
//            if (user.getAlternativeSecondEmail() != null) {
//                user.setAlternativeSecondEmail(institution.getAlternativeSecondEmail().toLowerCase());
//            }
//            user.setFirstName(institution.getFirstName());
//            user.setLastName(institution.getLastName());
//
//            if (institution.getModeratorPhone() != null) {
//                user.setPhone(formatPhone(institution.getModeratorPhone()));
//            }
//            user.setLocales(institution.getLocales());
//
//            if (institution.getModeratorAlternativePhone() != null) {
//                user.setAlternativePhone(formatPhone(institution.getModeratorAlternativePhone()));
//            }
//            user.setAlternativeLocales(institution.getAlternativeLocales());
//
//            if (institution.getModeratorAlternativeSecondPhone() != null) {
//                user.setAlternativeSecondPhone(formatPhone(institution.getModeratorAlternativeSecondPhone()));
//            }
//            user.setAlternativeSecondLocales(institution.getAlternativeSecondLocales());
//
//            user.setDateUpdated(LocalDateTime.now());
//            userRepository.save(user);
//        } else {
//            String email = institution.getEmail().toLowerCase();
//
//            if (userRepository.existsByEmail(email)) {
//                User newUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
//                newUser.setEmail(institution.getEmail().toLowerCase());
//
//                if (institution.getAlternativeEmail() != null) {
//                    newUser.setAlternativeEmail(institution.getAlternativeEmail());
//                }
//                if (institution.getAlternativeSecondEmail() != null) {
//                    newUser.setAlternativeSecondEmail(institution.getAlternativeSecondEmail());
//                }
//
//                newUser.setFirstName(institution.getFirstName());
//                newUser.setLastName(institution.getLastName());
//                newUser.setActive(true);
//                newUser.setInstitution(updatedInstitution);
//                if (institution.getModeratorPhone() != null) {
//                    newUser.setPhone(formatPhone(institution.getModeratorPhone()));
//                }
//                newUser.setLocales(institution.getLocales());
//
//                if (institution.getModeratorAlternativePhone() != null) {
//                    newUser.setAlternativePhone(institution.getModeratorAlternativePhone());
//                }
//                newUser.setAlternativeLocales(institution.getAlternativeLocales());
//
//                if (institution.getModeratorAlternativeSecondPhone() != null) {
//                    newUser.setAlternativeSecondPhone(institution.getModeratorAlternativeSecondPhone());
//                }
//                newUser.setAlternativeSecondLocales(institution.getAlternativeSecondLocales());
//
//                newUser.setPassword(bCryptPasswordEncoder.encode(institution.getPassword()));
//                newUser.setDateUpdated(LocalDateTime.now());
//                userRepository.save(newUser);
//            } else {
//                User anotherUser = new User();
//                anotherUser.setFirstName(institution.getFirstName());
//                anotherUser.setLastName(institution.getLastName());
//
//                if (institution.getModeratorPhone() != null) {
//                    anotherUser.setPhone(formatPhone(institution.getModeratorPhone()));
//                }
//                anotherUser.setLocales(institution.getLocales());
//
//                if (institution.getModeratorAlternativePhone() != null) {
//                    anotherUser.setAlternativePhone(formatPhone(institution.getModeratorAlternativePhone()));
//                }
//                anotherUser.setAlternativeLocales(institution.getAlternativeLocales());
//
//                if (institution.getModeratorAlternativeSecondPhone() != null) {
//                    anotherUser.setAlternativeSecondPhone(formatPhone(institution.getModeratorAlternativeSecondPhone()));
//                }
//                anotherUser.setAlternativeSecondLocales(institution.getAlternativeSecondLocales());
//
//                anotherUser.setEmail(institution.getEmail().toLowerCase());
//                if (institution.getAlternativeEmail() != null) {
//                    anotherUser.setAlternativeEmail(institution.getAlternativeEmail().toLowerCase());
//                }
//                if (institution.getAlternativeSecondEmail() != null) {
//                    anotherUser.setAlternativeSecondEmail(institution.getAlternativeSecondEmail().toLowerCase());
//                }
//                anotherUser.setActive(true);
//                anotherUser.setRole(UserRole.ROLE_INSTITUTIONAL_MODERATOR);
//                anotherUser.setUserRole(UserRole.ROLE_INSTITUTIONAL_MODERATOR);
//                anotherUser.setPassword(bCryptPasswordEncoder.encode(institution.getPassword()));
//                anotherUser.setUsername(institution.getEmail());
//                anotherUser.setInstitution(updatedInstitution);
//                anotherUser.setDateRegistrationCompleted(LocalDateTime.now());
//                anotherUser.setDateCreated(LocalDateTime.now());
//                anotherUser.setDateUpdated(LocalDateTime.now());
//                userRepository.save(anotherUser);
//            }
//        }
        return institution;
    }


    @Override
    protected InstitutionRepository getRepository() {
        return institutionRepository;
    }


    @Override
    public Institution setInactive(Long institutionId) {
        Institution updateInstitution = institutionRepository.findById(institutionId).orElseThrow(InstitutionNotFoundException::new);
        List<User> users = updateInstitution.getUsers();
        users.forEach(u -> {
            u.setActive(false);
            u.setDateUpdated(LocalDateTime.now());
            this.userRepository.save(u);
        });

        updateInstitution.setDateUpdated(LocalDateTime.now());
        updateInstitution.setActive(false);

        return institutionRepository.save(updateInstitution);
    }

    @Override
    public Institution findInstitutionByUserEmail() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Institution institution = user.getInstitution();
        return institutionRepository.findById(institution.getId()).orElseThrow(InstitutionNotFoundException::new);

    }

    @Override
    public Institution sendSelectedInstitution(Long institutionId, InstitutionPage institution, Long categoryId) {
        Institution updatedInstitution = institutionRepository.findById(institutionId).orElseThrow(InstitutionNotFoundException::new);
        Institution newInstitution = new Institution();
        User moderator = new User();
        List<Tag> tags = null;
        if (institution.getTags().size() > 0) {
            tags = tagService.findAll(institution.getTags()
                    .stream()
                    .map(tag -> tag.getId())
                    .collect(Collectors.toList()));
        }
        newInstitution.setTags(tags);

        if (institution.getCategory() != null) {
            InstitutionCategory institutionCategory = this.institutionCategoryRepository.findById(categoryId).orElseThrow(InstitutionCategoryNotFoundException::new);
            newInstitution.setCategory(institutionCategory);
        }
        newInstitution.setWebSite(institution.getWebSite());
//        newInstitution.setAddressMk(institution.getAddressMk());
//        newInstitution.setAddressAl(institution.getAddressAl());
        newInstitution.setTypeOfStreetMk(institution.getTypeOfStreetMk());
        newInstitution.setTypeOfStreetAl(institution.getTypeOfStreetAl());
        newInstitution.setTypeOfStreetEn(institution.getTypeOfStreetEn());

        if (institution.getStreetMk() != null) {
            String streetMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            streetMk = streetMk.trim();
            newInstitution.setStreetMk(streetMk);
        } else {
            newInstitution.setStreetMk(updatedInstitution.getStreetMk());
        }

        if (institution.getStreetAl() != null) {
            String streetAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetAl = streetAl.trim();
            newInstitution.setStreetAl(streetAl);
        } else {
            newInstitution.setStreetAl(updatedInstitution.getStreetAl());
        }

        if (institution.getStreetEn() != null) {
            String streetEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetEn = streetEn.trim();
            newInstitution.setStreetEn(streetEn);
        } else {
            newInstitution.setStreetEn(updatedInstitution.getStreetEn());
        }

        newInstitution.setStreetNumberMk(institution.getStreetNumberMk());
//        newInstitution.setStreetNumberAl(institution.getStreetNumberAl());
        newInstitution.setAddressDetailsMk(institution.getAddressDetailsMk());
        newInstitution.setAddressDetailsAl(institution.getAddressDetailsAl());
        newInstitution.setAddressDetailsEn(institution.getAddressDetailsEn());
        newInstitution.setCityMk(institution.getCityMk());
        newInstitution.setCityAl(institution.getCityAl());
        newInstitution.setCityEn(institution.getCityEn());
        newInstitution.setPostalCode(institution.getPostalCode());
        newInstitution.setNameAl(institution.getNameAl());
        newInstitution.setNameMk(institution.getNameMk());
        newInstitution.setNameEn(institution.getNameEn());
        newInstitution.setNoticeBoard(institution.isNoticeBoard());

        String streetMacedonian = "";
        if (institution.getStreetMk() != null) {
            String strMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            strMk = strMk.trim();
            boolean typeMk = institution.getTypeOfStreetMk() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsMk() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityMk() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    newInstitution.setAddressMk(streetMacedonian);
                } else {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " бр." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    newInstitution.setAddressMk(streetMacedonian);
                }
            } else {
                streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                newInstitution.setAddressMk(streetMacedonian);
            }
        }

        String streetAlbanian = "";
        if (institution.getStreetAl() != null) {
            String strAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strAl = strAl.trim();
            boolean typeMk = institution.getTypeOfStreetAl() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsAl() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityAl() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    newInstitution.setAddressAl(streetAlbanian);
                } else {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " nr." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    newInstitution.setAddressAl(streetAlbanian);
                }
            } else {
                streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                newInstitution.setAddressAl(streetAlbanian);
            }
        }

        String streetEnglish = "";
        if (institution.getStreetEn() != null) {
            String strEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strEn = strEn.trim();
            boolean typeMk = institution.getTypeOfStreetEn() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsEn() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityEn() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    newInstitution.setAddressEn(streetEnglish);
                } else {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " num." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    newInstitution.setAddressEn(streetEnglish);
                }
            } else {
                streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                newInstitution.setAddressEn(streetEnglish);
            }
        }


        if (institution.getInstitutionPhone() != null) {
            newInstitution.setInstitutionPhone(formatPhone(institution.getInstitutionPhone()));
        }
        newInstitution.setInstitutionLocales(institution.getInstitutionLocales());

        if (institution.getInstitutionAlternativePhone() != null) {
            newInstitution.setInstitutionAlternativePhone(formatPhone(institution.getInstitutionAlternativePhone()));
        }
        newInstitution.setInstitutionAlternativeLocales(institution.getInstitutionAlternativeLocales());

        if (institution.getInstitutionAlternativeSecondPhone() != null) {
            newInstitution.setInstitutionAlternativeSecondPhone(formatPhone(institution.getInstitutionAlternativeSecondPhone()));
        }
        newInstitution.setInstitutionAlternativeSecondLocales(institution.getInstitutionAlternativeSecondLocales());

        newInstitution.setDirektorEmail(institution.getDirektorEmail());
        newInstitution.setDirektorFirstName(institution.getDirektorFirstName());
        newInstitution.setDirektorLastName(institution.getDirektorLastName());

        if (institution.getDirektorPhone() != null) {
            newInstitution.setDirektorPhone(formatPhone(institution.getDirektorPhone()));
        }
        newInstitution.setDirectorLocales(institution.getDirectorLocales());

        newInstitution.setActive(true);
        newInstitution.setDateCreated(LocalDateTime.now());
        newInstitution.setDateUpdated(LocalDateTime.now());
        newInstitution.setInitialInstitution(updatedInstitution);
        newInstitution.setEdited(true);

        institutionRepository.save(newInstitution);
        return newInstitution;
    }

    @Override
    public Institution sendSelectedInstitutionWithParent(Long institutionId, InstitutionPage institution, Long parentInstitutionId) {
        Institution updatedInstitution = institutionRepository.findById(institutionId).orElseThrow(InstitutionNotFoundException::new);

        Institution newInstitution = new Institution();
        User moderator = new User();
        List<Tag> tags = null;
        if (institution.getTags().size() > 0) {
            tags = tagService.findAll(institution.getTags()
                    .stream()
                    .map(tag -> tag.getId())
                    .collect(Collectors.toList()));
        }
        newInstitution.setTags(tags);
        System.out.println(tags);
        if (institution.getParentInstitution() != null) {
            Institution parentInstitution = this.institutionRepository.findById(parentInstitutionId).orElseThrow(InstitutionNotFoundException::new);
            newInstitution.setParentInstitution(parentInstitution);
        }
        newInstitution.setWebSite(institution.getWebSite());
//        newInstitution.setAddressMk(institution.getAddressMk());
//        newInstitution.setAddressAl(institution.getAddressAl());
        newInstitution.setTypeOfStreetMk(institution.getTypeOfStreetMk());
        newInstitution.setTypeOfStreetAl(institution.getTypeOfStreetAl());
        newInstitution.setTypeOfStreetEn(institution.getTypeOfStreetEn());

        if (institution.getStreetMk() != null) {
            String streetMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            streetMk = streetMk.trim();
            newInstitution.setStreetMk(streetMk);
        } else {
            newInstitution.setStreetMk(updatedInstitution.getStreetMk());
        }

        if (institution.getStreetAl() != null) {
            String streetAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetAl = streetAl.trim();
            newInstitution.setStreetAl(streetAl);
        } else {
            newInstitution.setStreetAl(updatedInstitution.getStreetAl());
        }

        if (institution.getStreetEn() != null) {
            String streetEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            streetEn = streetEn.trim();
            newInstitution.setStreetEn(streetEn);
        } else {
            newInstitution.setStreetEn(updatedInstitution.getStreetEn());
        }

        newInstitution.setStreetNumberMk(institution.getStreetNumberMk());
//        newInstitution.setStreetNumberAl(institution.getStreetNumberAl());
        newInstitution.setAddressDetailsMk(institution.getAddressDetailsMk());
        newInstitution.setAddressDetailsAl(institution.getAddressDetailsAl());
        newInstitution.setAddressDetailsEn(institution.getAddressDetailsEn());
        newInstitution.setCityMk(institution.getCityMk());
        newInstitution.setCityAl(institution.getCityAl());
        newInstitution.setCityEn(institution.getCityEn());
        newInstitution.setPostalCode(institution.getPostalCode());
        newInstitution.setNameAl(institution.getNameAl());
        newInstitution.setNameMk(institution.getNameMk());
        newInstitution.setNameEn(institution.getNameEn());
        newInstitution.setNoticeBoard(institution.isNoticeBoard());

        String streetMacedonian = "";
        if (institution.getStreetMk() != null) {
            String strMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            strMk = strMk.trim();
            boolean typeMk = institution.getTypeOfStreetMk() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsMk() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityMk() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    newInstitution.setAddressMk(streetMacedonian);
                } else {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " бр." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    newInstitution.setAddressMk(streetMacedonian);
                }
            } else {
                streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                newInstitution.setAddressMk(streetMacedonian);
            }
        }

        String streetAlbanian = "";
        if (institution.getStreetAl() != null) {
            String strAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strAl = strAl.trim();
            boolean typeMk = institution.getTypeOfStreetAl() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsAl() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityAl() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    newInstitution.setAddressAl(streetAlbanian);
                } else {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " nr." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    newInstitution.setAddressAl(streetAlbanian);
                }
            } else {
                streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                newInstitution.setAddressAl(streetAlbanian);
            }

        }

        String streetEnglish = "";
        if (institution.getStreetEn() != null) {
            String strEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strEn = strEn.trim();
            boolean typeMk = institution.getTypeOfStreetEn() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsEn() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityEn() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    newInstitution.setAddressEn(streetEnglish);
                } else {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " num." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    newInstitution.setAddressEn(streetEnglish);
                }
            } else {
                streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                newInstitution.setAddressEn(streetEnglish);
            }
        }

        if (institution.getInstitutionPhone() != null) {
            newInstitution.setInstitutionPhone(formatPhone(institution.getInstitutionPhone()));
        }
        newInstitution.setInstitutionLocales(institution.getInstitutionLocales());

        if (institution.getInstitutionAlternativePhone() != null) {
            newInstitution.setInstitutionAlternativePhone(formatPhone(institution.getInstitutionAlternativePhone()));
        }
        newInstitution.setInstitutionAlternativeLocales(institution.getInstitutionAlternativeLocales());

        if (institution.getInstitutionAlternativeSecondPhone() != null) {
            newInstitution.setInstitutionAlternativeSecondPhone(formatPhone(institution.getInstitutionAlternativeSecondPhone()));
        }
        newInstitution.setInstitutionAlternativeSecondLocales(institution.getInstitutionAlternativeSecondLocales());

        newInstitution.setDirektorEmail(institution.getDirektorEmail());
        newInstitution.setDirektorFirstName(institution.getDirektorFirstName());
        newInstitution.setDirektorLastName(institution.getDirektorLastName());

        if (institution.getDirektorPhone() != null) {
            newInstitution.setDirektorPhone(formatPhone(institution.getDirektorPhone()));
        }
        newInstitution.setDirectorLocales(institution.getDirectorLocales());

        newInstitution.setActive(true);
        newInstitution.setDateCreated(LocalDateTime.now());
        newInstitution.setDateUpdated(LocalDateTime.now());
        newInstitution.setInitialInstitution(updatedInstitution);
        newInstitution.setEdited(true);

        institutionRepository.save(newInstitution);
        return newInstitution;
    }

    @Override
    public Page<InstitutionPage> findAllEdited(Pageable pageable) {
        Long size = institutionRepository.count();

        List<Institution> institutions = getRepository().findAllByActiveAndEdited(true, true, pageable).getContent();
        List<InstitutionPage> responseList = new ArrayList<>();
        institutions.forEach(institution -> responseList.add(new InstitutionPage(institution)));

        return new PageImpl<>(responseList, pageable, size);
    }

    @Override
    public Institution recreateInstitution(InstitutionPage institution) {
        Institution recreated = institutionRepository.findById(institution.getInitialInstitution().getId()).orElseThrow(InstitutionNotFoundException::new);

        if (institution.getCategory() != null) {
            recreated.setCategory(institutionCategoryRepository.findById(institution.getCategory().getId()).orElseThrow(InstitutionCategoryNotFoundException::new));
        }
        recreated.setId(institution.getInitialInstitution().getId());
        recreated.setWebSite(institution.getWebSite());
//        recreated.setAddressMk(institution.getAddressMk());
//        recreated.setAddressAl(institution.getAddressAl());
        recreated.setTypeOfStreetMk(institution.getTypeOfStreetMk());
        recreated.setTypeOfStreetAl(institution.getTypeOfStreetAl());
        recreated.setTypeOfStreetEn(institution.getTypeOfStreetEn());

        if (institution.getStreetMk() != null) {
            String streetMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            recreated.setStreetMk(streetMk);
        }

        if (institution.getStreetAl() != null) {
            String streetAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            recreated.setStreetAl(streetAl);
        }

        if (institution.getStreetEn() != null) {
            String streetEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            recreated.setStreetEn(streetEn);
        }

        recreated.setStreetNumberMk(institution.getStreetNumberMk());
//        recreated.setStreetNumberAl(institution.getStreetNumberAl());
        recreated.setAddressDetailsMk(institution.getAddressDetailsMk());
        recreated.setAddressDetailsAl(institution.getAddressDetailsAl());
        recreated.setAddressDetailsEn(institution.getAddressDetailsEn());
        recreated.setCityMk(institution.getCityMk());
        recreated.setCityAl(institution.getCityAl());
        recreated.setCityEn(institution.getCityEn());
        recreated.setPostalCode(institution.getPostalCode());
        recreated.setNameAl(institution.getNameAl());
        recreated.setNameMk(institution.getNameMk());
        recreated.setNameEn(institution.getNameEn());
        recreated.setNoticeBoard(institution.isNoticeBoard());

        String streetMacedonian = "";
        if (institution.getStreetMk() != null) {
            String strMk = institution.getStreetMk().replaceAll("[^АБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзѕијклљмнњопрстќуфхцчџш0-9-\\s+]", "");
            strMk = strMk.trim();
            boolean typeMk = institution.getTypeOfStreetMk() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsMk() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityMk() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    recreated.setAddressMk(streetMacedonian);
                } else {
                    streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " бр." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                    recreated.setAddressMk(streetMacedonian);
                }
            } else {
                streetMacedonian = (typeMk ? institution.getTypeOfStreetMk() : "") + "." + strMk + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsMk() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityMk() : "");
                recreated.setAddressMk(streetMacedonian);
            }
        }

        String streetAlbanian = "";
        if (institution.getStreetAl() != null) {
            String strAl = institution.getStreetAl().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strAl = strAl.trim();
            boolean typeMk = institution.getTypeOfStreetAl() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsAl() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityAl() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    recreated.setAddressAl(streetAlbanian);
                } else {
                    streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " nr." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                    recreated.setAddressAl(streetAlbanian);
                }
            } else {
                streetAlbanian = (typeMk ? institution.getTypeOfStreetAl() : "") + "." + strAl + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsAl() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityAl() : "");
                recreated.setAddressAl(streetAlbanian);
            }
        }

        String streetEnglish = "";
        if (institution.getStreetEn() != null) {
            String strEn = institution.getStreetEn().replaceAll("[^A-Za-z0-9-\\s+]", "");
            strEn = strEn.trim();
            boolean typeMk = institution.getTypeOfStreetEn() != null;
            boolean num = institution.getStreetNumberMk() != null;
            boolean details = institution.getAddressDetailsEn() != null;
            boolean postal = institution.getPostalCode() != null;
            boolean city = institution.getCityEn() != null;

            if (institution.getStreetNumberMk() != null) {
                if (institution.getStreetNumberMk().equals("б.б") || institution.getStreetNumberMk().equals("б.б.") || institution.getStreetNumberMk().equals("b.b") || institution.getStreetNumberMk().equals("b.b.") || institution.getStreetNumberMk().equals("bb") || institution.getStreetNumberMk().equals("бб")) {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    recreated.setAddressEn(streetEnglish);
                } else {
                    streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " num." + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                    recreated.setAddressEn(streetEnglish);
                }
            } else {
                streetEnglish = (typeMk ? institution.getTypeOfStreetEn() : "") + "." + strEn + " " + (num ? institution.getStreetNumberMk() : "") + " " + (details ? institution.getAddressDetailsEn() : "") + ", " + (postal ? institution.getPostalCode() : "") + ", " + (city ? institution.getCityEn() : "");
                recreated.setAddressEn(streetEnglish);
            }
        }

        recreated.setInstitutionPhone(institution.getInstitutionPhone());
        recreated.setInstitutionLocales(institution.getInstitutionLocales());

        recreated.setInstitutionAlternativePhone(institution.getInstitutionAlternativePhone());
        recreated.setInstitutionAlternativeLocales(institution.getInstitutionAlternativeLocales());

        recreated.setInstitutionAlternativeSecondPhone(institution.getInstitutionAlternativeSecondPhone());
        recreated.setInstitutionAlternativeSecondLocales(institution.getInstitutionAlternativeSecondLocales());

        recreated.setDirektorEmail(institution.getDirektorEmail());
        recreated.setDirektorFirstName(institution.getDirektorFirstName());
        recreated.setDirektorLastName(institution.getDirektorLastName());
        recreated.setDirektorPhone(institution.getDirektorPhone());
        recreated.setDirectorLocales(institution.getDirectorLocales());

        recreated.setActive(true);
        recreated.setDateCreated(LocalDateTime.now());
        recreated.setDateUpdated(LocalDateTime.now());
        recreated.setEdited(false);
        if (institution.getParentInstitution() != null) {
            recreated.setParentInstitution(institutionRepository.findById(institution.getParentInstitution().getId()).orElseThrow(InstitutionNotFoundException::new));
        }
        recreated.setTags(institution.getTags());
        this.institutionRepository.deleteById(institution.getId());
        this.institutionRepository.save(recreated);
        return recreated;
    }


    @Override
    public List<String> getDirectorEmailsForInstitution(Long institutionId) {
        List<User> users = this.findById(institutionId).getUsers();
        List<String> emails = new ArrayList<>();

        users.forEach
                (user -> user.getAreasOfInterest()
                        .forEach(areaOfInterest -> {
                                    if (areaOfInterest.getNameMk().equals("Директор")) {
                                        emails.add(user.getEmail());
                                    }
                        }
                ));

        return emails;
    }
}
