package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.dto.IndicatorReportDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.IndicatorReportService;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndicatorReportServiceImpl extends BaseEntityCrudServiceImpl<IndicatorReport, IndicatorReportRepository> implements IndicatorReportService {

    private final IndicatorReportRepository indicatorReportRepository;
    private final IndicatorRepository indicatorRepository;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;
    private final StatusRepository statusRepository;
    private final Environment environment;
    private final EmailRepository emailRepository;
    private final ActivityRepository activityRepository;

    public IndicatorReportServiceImpl(IndicatorReportRepository indicatorReportRepository,
                                      IndicatorRepository indicatorRepository,
                                      UserRepository userRepository,
                                      EvaluationRepository evaluationRepository,
                                      StatusRepository statusRepository,
                                      Environment environment,
                                      EmailRepository emailRepository,
                                      ActivityRepository activityRepository) {
        this.indicatorReportRepository = indicatorReportRepository;
        this.indicatorRepository = indicatorRepository;
        this.userRepository = userRepository;
        this.evaluationRepository = evaluationRepository;
        this.statusRepository = statusRepository;
        this.environment = environment;
        this.emailRepository = emailRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public Page<IndicatorReport> findAllIndicatorReports(Pageable pageable) {
        return indicatorReportRepository.findIndicatorReportsByActiveOrderByDateUpdated(pageable);
    }

    @Override
    public IndicatorReport findById(Long id) {
        return indicatorReportRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);
    }

    @Override
    public IndicatorReport createIndicatorReport(IndicatorReportDto indicatorReport) {
        IndicatorReport newIndicatorReport = new IndicatorReport();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        // Set the status to: Filed (Поднесен) after creating a report
        newIndicatorReport.setStatus(statusRepository.findByStatusMkAndStatusType("Поднесен", StatusType.ИЗВЕШТАЈ));

        Activity activity = activityRepository.findById(indicatorReport.getActivity()).orElseThrow(ActivityNotFoundException::new);
        newIndicatorReport.setActivity(activity);

        Evaluation evaluation = evaluationRepository.findById(indicatorReport.getEvaluation()).orElseThrow(EvaluationNotFoundException::new);
        newIndicatorReport.setEvaluation(evaluation);

        newIndicatorReport.setCreatedByUser(user);
        newIndicatorReport.setUpdatedByUserEvaluator(user);
        newIndicatorReport.setActive(true);
        newIndicatorReport.setDateCreated(LocalDateTime.now());
        newIndicatorReport.setDateUpdated(LocalDateTime.now());

        newIndicatorReport.setReportMk(indicatorReport.getReportMk());
        newIndicatorReport.setReportEn(indicatorReport.getReportEn());
        newIndicatorReport.setReportAl(indicatorReport.getReportAl());

        return indicatorReportRepository.save(newIndicatorReport);
    }

    @Override
    public IndicatorReport updateIndicatorReport(IndicatorReportDto indicatorReport, Long activityId, Long evaluationId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityNotFoundException::new);

        Status status = statusRepository.findById(indicatorReport.getStatusReport()).orElseThrow(StatusNotFoundException::new);

        activityRepository.save(activity);

        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(EvaluationNotFoundException::new);

        // Trying to find indicator report by indicator and the currently open evaluation
        IndicatorReport updatedIndicatorReport = indicatorReportRepository.findByActivityAndEvaluation(activity,evaluation);

        Status statusReport;

        if(user.getRole() == (UserRole.ROLE_EVALUATOR))
        {
            statusReport = statusRepository.findByStatusMkAndStatusType("Поднесен", StatusType.ИЗВЕШТАЈ);
            updatedIndicatorReport.setUpdatedByUserEvaluator(user);
        }
        else {
            statusReport = statusRepository.findById(indicatorReport.getStatusReport()).orElseThrow(StatusNotFoundException::new);

            Status statusInDB = statusRepository.findById(updatedIndicatorReport.getStatus().getId()).orElseThrow(StatusNotFoundException::new);
            String statusName = statusInDB.getStatusMk();

            String statusNameFromDTO = status.getStatusMk();

            if(statusName.equals("Вратен на доработка")){
                updatedIndicatorReport.setUpdatedByUserModerator(user);
            }

            else if (statusNameFromDTO.equals("Поднесен") || statusNameFromDTO.equals("Прифатен") || statusNameFromDTO.equals("Заклучен")){
                updatedIndicatorReport.setUpdatedByUserModerator(user);
            }

            else {
                String from = environment.getProperty("spring.mail.username");

                List<String> emails = new ArrayList<>();
                emails.add(updatedIndicatorReport.getUpdatedByUserEvaluator().getEmail());

                Map<String, String> map = new HashMap<>();
                map.put("start", "Вашиот извештај за индикаторот " + activity.getNameMk() +
                        " при евалуацијата " + evaluation.getDescriptionMk() + " е " + statusReport.getStatusMk() +
                        ". Коментарот во врска со извештајот е следниот:\n");
                map.put("comment", indicatorReport.getReportEn() + "\n");
                map.put("end", "Ве молиме разгледајте го извештајот и променете го.");

                try {
                    emailRepository.sendHtmlMail(emails, "Извештај вратен на доработка", "IndicatorReportStatusChanged.html", map, from);
                } catch (Exception e) {
                    throw new EmailSendingException();
                }
            }


        }

        updatedIndicatorReport.setStatus(statusReport);

        updatedIndicatorReport.setDateUpdated(LocalDateTime.now());

        updatedIndicatorReport.setReportMk(indicatorReport.getReportMk());
        updatedIndicatorReport.setReportAl(indicatorReport.getReportAl());
        updatedIndicatorReport.setReportEn(indicatorReport.getReportEn());

        return indicatorReportRepository.save(updatedIndicatorReport);
    }

    @Override
    public IndicatorReport setActive(Long id) {
        IndicatorReport updatedIndicatorReport = indicatorReportRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedIndicatorReport.setDateUpdated(LocalDateTime.now());
        updatedIndicatorReport.setUpdatedByUserModerator(user);
        updatedIndicatorReport.setActive(true);

        return indicatorReportRepository.save(updatedIndicatorReport);
    }

        @Override
    public IndicatorReport setInactive(Long id) {
        IndicatorReport updatedIndicatorReport = indicatorReportRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedIndicatorReport.setDateUpdated(LocalDateTime.now());
        updatedIndicatorReport.setUpdatedByUserModerator(user);
        updatedIndicatorReport.setDeletedByUser(user);
        updatedIndicatorReport.setActive(false);

        return indicatorReportRepository.save(updatedIndicatorReport);
    }

    @Override
    public List<IndicatorReport> findAllIndicatorReportsList() {
        return indicatorReportRepository.findAll();
    }

    @Override
    public List<IndicatorReport> findAllByActivity(Long id) {
        return activityRepository.findById(id).orElseThrow(ActivityNotFoundException::new).
                getIndicatorReports();
    }

    @Override
    public Page<IndicatorReport> findAllIndicatorReportsPageByActivityId(Long id, Pageable pageable) {
        Activity activity = activityRepository.findById(id).orElseThrow(ActivityNotFoundException::new);

        return indicatorReportRepository.findAllByActivity(activity, pageable);
    }

    @Override
    protected IndicatorReportRepository getRepository() {
        return indicatorReportRepository;
    }
}
