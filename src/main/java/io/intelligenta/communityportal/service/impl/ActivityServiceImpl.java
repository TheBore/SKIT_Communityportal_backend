package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.dto.ActivityDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.ActivityService;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class ActivityServiceImpl extends BaseEntityCrudServiceImpl<Activity, ActivityRepository> implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final MeasureRepository measureRepository;
    private final StatusRepository statusRepository;
    private final InstitutionRepository institutionRepository;
    private final ActivityInstitutionRepository activityInstitutionRepository;
    private final EvaluationRepository evaluationRepository;
    private final IndicatorReportRepository indicatorReportRepository;
    private final EmailRepository emailRepository;
    private final Environment environment;

    public ActivityServiceImpl(ActivityRepository activityRepository,
                               UserRepository userRepository, MeasureRepository measureRepository, StatusRepository statusRepository, InstitutionRepository institutionRepository, ActivityInstitutionRepository activityInstitutionRepository, EvaluationRepository evaluationRepository, IndicatorReportRepository indicatorReportRepository, EmailRepository emailRepository, Environment environment) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.measureRepository = measureRepository;
        this.statusRepository = statusRepository;
        this.institutionRepository = institutionRepository;
        this.activityInstitutionRepository = activityInstitutionRepository;
        this.evaluationRepository = evaluationRepository;
        this.indicatorReportRepository = indicatorReportRepository;
        this.emailRepository = emailRepository;
        this.environment = environment;
    }

    @Override
    public Page<Activity> findAllActivities(Pageable pageable) {
        return activityRepository.findAllOrderByDateCreated(pageable);
    }

    @Override
    public Activity findById(Long id) {
        return activityRepository.findById(id).orElseThrow(ActivityNotFoundException::new);
    }

    @Override
    public Activity createActivity(ActivityDto activity) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Activity newActivity = new Activity();

        newActivity.setNameMk(activity.getNameMk());
        newActivity.setNameAl(activity.getNameAl());
        newActivity.setNameEn(activity.getNameEn());

        Measure measure = measureRepository.findById(activity.getMeasure()).orElseThrow(MeasureNotFoundException::new);
        newActivity.setMeasure(measure);

        Status status = statusRepository.findById(activity.getStatus()).orElseThrow(StatusNotFoundException::new);
        newActivity.setStatus(status);

        ActivityInstitution competentInstitution = activityInstitutionRepository.findById(activity.getCompetentInstitution()).orElseThrow(ActivityNotFoundException::new);
        newActivity.setCompetentInstitution(competentInstitution);

        List<ActivityInstitution> institutionList = new ArrayList<>();

        if(activity.getActivityInstitutions() != null && activity.getActivityInstitutions().size() != 0){
            for( int i = 0; i < activity.getActivityInstitutions().size(); i++){
                ActivityInstitution includedInstitution = activityInstitutionRepository.
                        findById(activity.getActivityInstitutions().get(i)).orElseThrow(InstitutionNotFoundException::new);
                institutionList.add(includedInstitution);
            }
            newActivity.setActivityInstitutions(institutionList);
        }
        else {
            newActivity.setActivityInstitutions(null);
        }

//        newActivity.setEndDate(activity.getEndDate());

        if(activity.getActivityDateType() != null){
            newActivity.setActivityDateType(activity.getActivityDateType());
        }
        else{
            newActivity.setActivityDateType(ActivityDateType.NOTDEFINED);
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate yearDate;

        if(activity.getActivityDateType() == null || activity.getActivityDateType().equals(ActivityDateType.FIRSTHALF) || activity.getActivityDateType().equals(ActivityDateType.NOTDEFINED)){
            if(activity.getYearDate() != null){
                yearDate = LocalDate.parse("1/1/" + activity.getYearDate(), formatter);
            }
            else{
                yearDate = null;
            }

        }
        else{
            if(activity.getYearDate() != null){
                yearDate = LocalDate.parse("6/1/" + activity.getYearDate(), formatter);
            }
            else {
                yearDate = null;
            }
        }

        newActivity.setYearDate(yearDate);
        newActivity.setContinuously(activity.isContinuously());

        newActivity.setCreatedByUser(user);
        newActivity.setUpdatedByUser(user);
        newActivity.setActive(true);
        newActivity.setDateCreated(LocalDateTime.now());
        newActivity.setDateUpdated(LocalDateTime.now());

        if(activity.getFinancialImplications() == null)
            newActivity.setFinancialImplications(false);
        else
            newActivity.setFinancialImplications(activity.getFinancialImplications());

        return activityRepository.save(newActivity);
    }

    @Override
    public Activity updateActivity(ActivityDto activity, Long activityId) {
            Activity updatedActivity = activityRepository.findById(activityId).orElseThrow(ActivityNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedActivity.setDateUpdated(LocalDateTime.now());
        updatedActivity.setUpdatedByUser(user);

        updatedActivity.setNameMk(activity.getNameMk());
        updatedActivity.setNameAl(activity.getNameAl());
        updatedActivity.setNameEn(activity.getNameEn());

//        Measure measure = measureRepository.findById(activity.getMeasure()).orElseThrow(MeasureNotFoundException::new);
//        updatedActivity.setMeasure(measure);

        Status status = statusRepository.findById(activity.getStatus()).orElseThrow(StatusNotFoundException::new);
        updatedActivity.setStatus(status);

        ActivityInstitution competentInstitution = activityInstitutionRepository.findById(activity.getCompetentInstitution()).orElseThrow(ActivityNotFoundException::new);
        updatedActivity.setCompetentInstitution(competentInstitution);

        List<ActivityInstitution> institutionList = new ArrayList<>();

        if(activity.getActivityInstitutions() != null && activity.getActivityInstitutions().size() != 0){
            for( int i = 0; i < activity.getActivityInstitutions().size(); i++){
                ActivityInstitution includedInstitution = activityInstitutionRepository.
                        findById(activity.getActivityInstitutions().get(i)).orElseThrow(InstitutionNotFoundException::new);
                institutionList.add(includedInstitution);
            }
            updatedActivity.setActivityInstitutions(institutionList);
        }
        else {
            updatedActivity.setActivityInstitutions(null);
        }

//        updatedActivity.setEndDate(activity.getEndDate());

        updatedActivity.setActivityDateType(activity.getActivityDateType());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate yearDate;

        if(activity.getActivityDateType() == null || activity.getActivityDateType().equals(ActivityDateType.FIRSTHALF) || activity.getActivityDateType().equals(ActivityDateType.NOTDEFINED)){
            if(activity.getYearDate() != null){
                yearDate = LocalDate.parse("1/1/" + activity.getYearDate(), formatter);
            }
            else{
                yearDate = null;
            }

        }
        else{
            if(activity.getYearDate() != null){
                yearDate = LocalDate.parse("6/1/" + activity.getYearDate(), formatter);
            }
            else {
                yearDate = null;
            }
        }

        updatedActivity.setYearDate(yearDate);
        updatedActivity.setContinuously(activity.isContinuously());


        if(activity.getFinancialImplications() == null)
            updatedActivity.setFinancialImplications(false);
        else
            updatedActivity.setFinancialImplications(activity.getFinancialImplications());

        return activityRepository.save(updatedActivity);
    }

    @Override
    public Activity setActive(Long id) {
        Activity updatedActivity = activityRepository.findById(id).orElseThrow(ActivityNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedActivity.setActive(true);
        updatedActivity.setUpdatedByUser(user);
        updatedActivity.setDateUpdated(LocalDateTime.now());

        return activityRepository.save(updatedActivity);
    }

    @Override
    public Activity setInactive(Long id) {
        Activity updatedActivity = activityRepository.findById(id).orElseThrow(ActivityNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedActivity.setActive(false);
        updatedActivity.setDeletedByUser(user);
        updatedActivity.setDateUpdated(LocalDateTime.now());

        return activityRepository.save(updatedActivity);
    }

    @Override
    public List<Activity> findAllActivitiesList() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findAllActivitiesByMeasureId(Long id) {
        return activityRepository.findAll().
                stream().filter(activity -> activity.getMeasure().getId().equals(id)).
                collect(Collectors.toList());
    }

    @Override
    public List<ActivityInstitution> allInstitutionsByActivity(Long id) {

        List<ActivityInstitution> institutionList = new ArrayList<>();

        institutionList.add(activityRepository.findById(id).
                orElseThrow(ActivityNotFoundException::new).
                getCompetentInstitution());

        institutionList.addAll(activityRepository.findById(id).
                orElseThrow(ActivityNotFoundException::new).
                getActivityInstitutions());

        return institutionList;
    }

    @Override
    public List<Activity> allActivitiesByInstitution(Long id) {
        return activityRepository.
                findAll().
                stream().
                filter(activity ->
                        activity.getCompetentInstitution() != null &&
                        activity.getCompetentInstitution().getInstitution() != null &&
                        activity.getCompetentInstitution().getInstitution().getId().equals(id)).
                collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "* 0 12 * * MON")
    public void sendWeeklyMail() {
        List<IndicatorReport> allIndicatorReports = indicatorReportRepository.findAll();
        List<IndicatorReport> indicatorReports = new ArrayList<>();
        for (IndicatorReport i : allIndicatorReports){
            if(i.getEvaluation().getOpen() && i.getStatus().getStatusMk().equals("Поднесен")){
                indicatorReports.add(i);
            }
        }
        String from = environment.getProperty("spring.mail.username");
        if(indicatorReports.size() > 0){
            indicatorReports.forEach(indicator -> {

                List<User> users = userRepository.findByRoleAndActive(UserRole.ROLE_MODERATOR_EVALUATOR, true);
//                users.addAll(userRepository.findByRoleAndActive(UserRole.ROLE_INSTITUTIONAL_MODERATOR, true));
                List<String> emails = new ArrayList<>();

                users.forEach(user -> {
                    if(user.getEmail() != null && !user.getEmail().equals("") && user.getActive()){
                        emails.add(user.getEmail());
                    }
                });

                Map<String, String> map = new HashMap<>();
                map.put("numberOfReports", Integer.toString(indicatorReports.size()));

                try {
                    emailRepository.sendHtmlMail(emails, "Неделно известување за поднесени извештаи", "ReportsWeeklyUpdate.html", map, from);
                }
                catch (Exception e){
                    throw new EmailSendingException();
                }
            });
        }

    }

    @Override
    protected ActivityRepository getRepository() {
        return activityRepository;
    }
}
