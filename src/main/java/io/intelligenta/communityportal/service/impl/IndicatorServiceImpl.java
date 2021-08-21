package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.IndicatorDto;
import io.intelligenta.communityportal.models.enumeration.IndicatorType;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.IndicatorService;
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
import java.util.stream.Collectors;

@Service
public class IndicatorServiceImpl extends BaseEntityCrudServiceImpl<Indicator, IndicatorRepository> implements IndicatorService {

    private final IndicatorRepository indicatorRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final ActivityRepository activityRepository;
    private final InstitutionRepository institutionRepository;
    private final ActivityInstitutionRepository activityInstitutionRepository;

    public IndicatorServiceImpl(IndicatorRepository indicatorRepository, UserRepository userRepository, StatusRepository statusRepository, ActivityRepository activityRepository, InstitutionRepository institutionRepository, ActivityInstitutionRepository activityInstitutionRepository) {
        this.indicatorRepository = indicatorRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.activityRepository = activityRepository;
        this.institutionRepository = institutionRepository;
        this.activityInstitutionRepository = activityInstitutionRepository;
    }

    @Override
    public Page<Indicator> findAllIndicators(Pageable pageable) {
        return indicatorRepository.findIndicatorsOrderByDateCreated(pageable);
    }

    @Override
    public Page<Indicator> findAllIndicatorsPageByActivityId(Long id, Pageable pageable) {
        return indicatorRepository.findIndicatorsByActivityId(id,pageable);
    }

    @Override
    public Indicator findById(Long id) {
        return indicatorRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);
    }

    @Override
    public Indicator createIndicator(IndicatorDto indicator) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Indicator newIndicator = new Indicator();

        newIndicator.setIndicatorType(IndicatorType.valueOf(indicator.getType()));

        if(newIndicator.getIndicatorType() == IndicatorType.BOOLEAN){
            newIndicator.setFinished(false);
        } else {
            newIndicator.setCounter(0);
        }

        newIndicator.setNameMk(indicator.getNameMk());
        newIndicator.setNameAl(indicator.getNameAl());
        newIndicator.setNameEn(indicator.getNameEn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate startDate = LocalDate.parse(indicator.getStartDate(), formatter);
        newIndicator.setStartDate(startDate);

        Status status = statusRepository.findById(indicator.getStatus()).orElseThrow(StatusNotFoundException::new);
        newIndicator.setStatus(status);

        Activity activity = activityRepository.findById(indicator.getActivity()).orElseThrow(ActivityNotFoundException::new);
        newIndicator.setActivity(activity);

        ActivityInstitution institution = activityInstitutionRepository.findById(indicator.getInstitution()).orElseThrow(InstitutionNotFoundException::new);
        newIndicator.setInstitution(institution);

        newIndicator.setCreatedByUser(user);
        newIndicator.setUpdatedByUser(user);
        newIndicator.setDateCreated(LocalDateTime.now());
        newIndicator.setDateUpdated(LocalDateTime.now());
        newIndicator.setActive(true);

        return indicatorRepository.save(newIndicator);
    }

    @Override
    public Indicator updateIndicator(IndicatorDto indicator, Long id) {
        Indicator updatedIndicator = indicatorRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedIndicator.setUpdatedByUser(user);
        updatedIndicator.setDateUpdated(LocalDateTime.now());

        updatedIndicator.setNameMk(indicator.getNameMk());
        updatedIndicator.setNameAl(indicator.getNameAl());
        updatedIndicator.setNameEn(indicator.getNameEn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate startDate = LocalDate.parse(indicator.getStartDate(), formatter);
        updatedIndicator.setStartDate(startDate);

        Status status = statusRepository.findById(indicator.getStatus()).orElseThrow(StatusNotFoundException::new);
        updatedIndicator.setStatus(status);

        Activity activity = activityRepository.findById(indicator.getActivity()).orElseThrow(ActivityNotFoundException::new);
        updatedIndicator.setActivity(activity);

        return indicatorRepository.save(updatedIndicator);
    }

    @Override
    public Indicator setActive(Long id) {
        Indicator updatedIndicator = indicatorRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedIndicator.setActive(true);
        updatedIndicator.setUpdatedByUser(user);
        updatedIndicator.setDateUpdated(LocalDateTime.now());

        return indicatorRepository.save(updatedIndicator);
    }

    @Override
    public Indicator setInactive(Long id) {
        Indicator updatedIndicator = indicatorRepository.findById(id).orElseThrow(IndicatorNotFoundException::new);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        updatedIndicator.setActive(false);
        updatedIndicator.setDeletedByUser(user);
        updatedIndicator.setDateUpdated(LocalDateTime.now());

        return indicatorRepository.save(updatedIndicator);
    }

    @Override
    public List<Indicator> findAllIndicatorsList() {
        return indicatorRepository.findAll();
    }

    @Override
    public List<Indicator> findAllIndicatorsByActivityId(Long id) {
        return indicatorRepository.findAll().stream().
                filter(indicator -> indicator.getActivity().getId().equals(id)).
                collect(Collectors.toList());
    }

    @Override
    public Indicator updateIndicatorOnEvaluation(IndicatorDto indicatorDto) {
        Indicator indicator = indicatorRepository.findById(indicatorDto.getId()).orElseThrow(IndicatorNotFoundException::new);

        Status status = statusRepository.findById(indicatorDto.getStatus()).orElseThrow(StatusNotFoundException::new);
        indicator.setStatus(status);

        if(indicator.getIndicatorType() == IndicatorType.BOOLEAN){
            indicator.setFinished(indicatorDto.getFinished());
        } else {
            if (indicator.getCounter() != null){
                indicator.setCounter(indicator.getCounter() + indicatorDto.getCounter());
            }
            else {
                indicator.setCounter(indicatorDto.getCounter());
            }
        }

        return indicatorRepository.save(indicator);
    }


    @Override
    protected IndicatorRepository getRepository() {
        return indicatorRepository;
    }
}
