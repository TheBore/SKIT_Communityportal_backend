package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.NAPDto;
import io.intelligenta.communityportal.models.exceptions.IndicatorReportNotFound;
import io.intelligenta.communityportal.models.exceptions.NAPNotFoundException;
import io.intelligenta.communityportal.models.exceptions.StatusNotFoundException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.EvaluationRepository;
import io.intelligenta.communityportal.repository.IndicatorReportRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.StatusRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.NAPService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NAPServiceImpl extends BaseEntityCrudServiceImpl<NAP, NAPRepository> implements NAPService {

    private final NAPRepository napRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final EvaluationRepository evaluationRepository;
    private final IndicatorReportRepository indicatorReportRepository;

    public NAPServiceImpl(NAPRepository napRepository, UserRepository userRepository, StatusRepository statusRepository, EvaluationRepository evaluationRepository, IndicatorReportRepository indicatorReportRepository){
        this.napRepository = napRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.evaluationRepository = evaluationRepository;
        this.indicatorReportRepository = indicatorReportRepository;
    }


    @Override
    public NAP createNAP(NAPDto nap) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        NAP newNap = new NAP();

        Status status = statusRepository.findById(nap.getStatus()).orElseThrow(StatusNotFoundException::new);
        newNap.setStatus(status);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate startDate = LocalDate.parse(nap.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(nap.getEndDate(), formatter);
        newNap.setStartDate(startDate);
        newNap.setEndDate(endDate);

        newNap.setNameMk(nap.getNameMk());
        newNap.setNameAl(nap.getNameAl());
        newNap.setNameEn(nap.getNameEn());

        newNap.setDescriptionMk(nap.getDescriptionMk());
        newNap.setDescriptionAl(nap.getDescriptionAl());
        newNap.setDescriptionEn(nap.getDescriptionEn());

        newNap.setCreatedByUser(user);
        newNap.setActive(true);
        newNap.setOpenForEvaluation(false);

        newNap.setDateCreated(LocalDateTime.now());
        newNap.setDateUpdated(LocalDateTime.now());

        return napRepository.save(newNap);
    }

    @Override
    public NAP findById(Long id) {
        return napRepository.findById(id).orElseThrow(NAPNotFoundException::new);
    }

    @Override
    public Page<NAP> findAllByDateCreatedDesc(Pageable pageable) {
        return napRepository.findAllOrderByDateCreated(pageable);
    }

    @Override
    public List<NAP> findAllByDateCreatedDescAndActiveList() {
        return napRepository.findAllByOrderByDateCreated();
    }

    @Override
    public NAP updateNAP(NAPDto nap, Long napId) {
        NAP updatedNAP = napRepository.findById(napId).orElseThrow(NAPNotFoundException::new);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedNAP.setDateUpdated(LocalDateTime.now());

        updatedNAP.setNameMk(nap.getNameMk());
        updatedNAP.setNameAl(nap.getNameAl());
        updatedNAP.setNameEn(nap.getNameEn());

        Status status = statusRepository.findById(nap.getStatus()).orElseThrow(StatusNotFoundException::new);
        updatedNAP.setStatus(status);

//        updatedNAP.setTitleMk(nap.getTitleMk());
//        updatedNAP.setTitleAl(nap.getTitleAl());
//        updatedNAP.setTitleEn(nap.getTitleEn());

        updatedNAP.setDescriptionMk(nap.getDescriptionMk());
        updatedNAP.setDescriptionAl(nap.getDescriptionAl());
        updatedNAP.setDescriptionEn(nap.getDescriptionEn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate startDate = LocalDate.parse(nap.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(nap.getEndDate(), formatter);
        updatedNAP.setStartDate(startDate);
        updatedNAP.setEndDate(endDate);
//        //updatedNAP.setOpenForEvaluation(nap.getOpenForEvaluation());
        updatedNAP.setUpdatedByUser(user);

        napRepository.save(updatedNAP);

        return updatedNAP;
    }

    @Override
    public NAP setInactive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        NAP updatedNAP = napRepository.findById(id).orElseThrow(NAPNotFoundException::new);

        updatedNAP.setDateUpdated(LocalDateTime.now());
        updatedNAP.setActive(false);
        updatedNAP.setDeletedByUser(user);

        return napRepository.save(updatedNAP);
    }

    @Override
    public NAP setActive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        NAP updatedNAP = napRepository.findById(id).orElseThrow(NAPNotFoundException::new);

        updatedNAP.setDateUpdated(LocalDateTime.now());
        updatedNAP.setActive(true);
        updatedNAP.setUpdatedByUser(user);
        updatedNAP.setDeletedByUser(null);

        return napRepository.save(updatedNAP);
    }

    @Override
    public void changeEvaluationStatus(boolean checked, Long id) {
        NAP updatedNAP = napRepository.findById(id).orElseThrow(NAPNotFoundException::new);

        updatedNAP.setOpenForEvaluation(checked);

        Evaluation evaluation = evaluationRepository.findByNapAndOpen(updatedNAP, true);
        evaluation.setOpen(false);

        evaluationRepository.save(evaluation);

        List<IndicatorReport> indicatorReports = indicatorReportRepository.findAllByEvaluation(evaluation);

        for (int i = 0; i < indicatorReports.size(); i++) {
            IndicatorReport indicatorReport = indicatorReportRepository.
                    findById(indicatorReports.get(i).getId()).
                    orElseThrow(IndicatorReportNotFound::new);

            Status status = statusRepository.findByStatusMkAndStatusType("Заклучен", StatusType.ИЗВЕШТАЈ);

            indicatorReport.setStatus(status);

            indicatorReportRepository.save(indicatorReport);
        }

        napRepository.save(updatedNAP);
    }

    @Override
    protected NAPRepository getRepository() {
        return napRepository;
    }
}
