package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Measure;
import io.intelligenta.communityportal.models.Problem;
import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.MeasureDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.MeasureRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.ProblemRepository;
import io.intelligenta.communityportal.repository.StatusRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.MeasureService;
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
public class MeasureServiceImpl extends BaseEntityCrudServiceImpl<Measure, MeasureRepository> implements MeasureService {

    private final MeasureRepository measureRepository;
    private final UserRepository userRepository;
    private final NAPRepository napRepository;
    private final StatusRepository statusRepository;
    private final ProblemRepository problemRepository;

    public MeasureServiceImpl(MeasureRepository measureRepository, UserRepository userRepository, NAPRepository napRepository, StatusRepository statusRepository, ProblemRepository problemRepository){
        this.measureRepository = measureRepository;
        this.userRepository = userRepository;
        this.napRepository = napRepository;
        this.statusRepository = statusRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    public Measure createMeasure(MeasureDto measureDto) {

        //If we want to create a Measure without attachment and in that case
        //if it throws any exception than we have to set mimeType, byte and content = null
        Measure measure = new Measure();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null)
        {
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
            measure.setCreatedByUser(user);
        }
        measure.setActive(measureDto.getActive());
        measure.setDateCreated(LocalDateTime.now());
        measure.setDateUpdated(LocalDateTime.now());

        measure.setNameMk(measureDto.getNameMk());
        measure.setNameAl(measureDto.getNameAl());
        measure.setNameEn(measureDto.getNameEn());

//        measure.setTitleMK(measureDto.getTitleMK());
//        measure.setTitleAL(measureDto.getTitleAL());
//        measure.setTitleEN(measureDto.getTitleEN());

        if(measureDto.getProblem() == null){
            throw new MeasureWithoutProblemException();
        }
        else{
            Problem problem = problemRepository.findById(measureDto.getProblem()).orElseThrow(ProblemNotFoundException::new);
            measure.setProblem(problem);
        }

        measure.setDescriptionMk(measureDto.getDescriptionMk());
        measure.setDescriptionAl(measureDto.getDescriptionAl());
        measure.setDescriptionEn(measureDto.getDescriptionEn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate startDate = LocalDate.parse(measureDto.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(measureDto.getEndDate(), formatter);

        measure.setStartDate(startDate);
        measure.setEndDate(endDate);
        measure.setActive(true);

 /*       NAP nap = napRepository.findById(measureDto.getNap()).orElseThrow(NAPNotFoundException::new);
        measure.setNap(nap);*/
        Status status = statusRepository.findById(measureDto.getStatus()).orElseThrow(StatusNotFoundException::new);
        measure.setStatus(status);


//        measure.setActivities(measureDto.getActivities());

//        if (measureDto.getAttachment() != null) {
//            measure.setFileName(measureDto.getAttachment().getOriginalFilename());
//            measure.setMimeType(measureDto.getAttachment().getContentType());
//            measure.setContent(measureDto.getAttachment().getBytes());
//        }

        return measureRepository.save(measure);
    }

    @Override
    public Measure findMeasureById(Long id) {
        return measureRepository.findById(id).orElseThrow(MeasureNotFoundException::new);
    }

    @Override
    public Page<Measure> findAllMeasures(Pageable pageable) {
        return measureRepository.findAllOrderByDateCreatedDesc(pageable);
    }

    @Override
    public Measure updateMeasure(MeasureDto measureDto) {

        //If we want to update a Measure without attachment and in that case if it throws any exception than we have to set mimeType, byte and content = null

        Measure updatedMeasure = measureRepository.findById(measureDto.getId()).orElseThrow(MeasureNotFoundException::new);

        updatedMeasure.setDateUpdated(LocalDateTime.now());
        updatedMeasure.setNameMk(measureDto.getNameMk());
        updatedMeasure.setNameAl(measureDto.getNameAl());
        updatedMeasure.setNameEn(measureDto.getNameEn());
//        updatedMeasure.setTitleMK(measureDto.getTitleMK());
//        updatedMeasure.setTitleAL(measureDto.getTitleAL());
//        updatedMeasure.setTitleEN(measureDto.getTitleEN());
        updatedMeasure.setDescriptionMk(measureDto.getDescriptionMk());
        updatedMeasure.setDescriptionAl(measureDto.getDescriptionAl());
        updatedMeasure.setDescriptionEn(measureDto.getDescriptionEn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate startDate = LocalDate.parse(measureDto.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(measureDto.getEndDate(), formatter);
        updatedMeasure.setStartDate(startDate);
        updatedMeasure.setEndDate(endDate);

        Status status = statusRepository.findById(measureDto.getStatus()).orElseThrow(StatusNotFoundException::new);
        updatedMeasure.setStatus(status);


//        if(measureDto.getAttachment() != null) {
//            updatedMeasure.setFileName(measureDto.getAttachment().getOriginalFilename());
//            updatedMeasure.setMimeType(measureDto.getAttachment().getContentType());
//            updatedMeasure.setContent(measureDto.getAttachment().getBytes());
//        }

        measureRepository.save(updatedMeasure);

        return updatedMeasure;
    }

    @Override
    public Measure setInactive(Long id) {
        Measure updatedMeasure = measureRepository.findById(id).orElseThrow(MeasureNotFoundException::new);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication!= null){
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
            updatedMeasure.setDeletedByUser(user);
        }
        updatedMeasure.setDateUpdated(LocalDateTime.now());
        updatedMeasure.setActive(false);
        return measureRepository.save(updatedMeasure);
    }

    @Override
    public Measure setActive(Long id) {
        Measure updatedMeasure = measureRepository.findById(id).orElseThrow(MeasureNotFoundException::new);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null)
        {
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
            updatedMeasure.setUpdatedByUser(user);
        }
        updatedMeasure.setDateUpdated(LocalDateTime.now());
        updatedMeasure.setActive(true);

        return measureRepository.save(updatedMeasure);
    }

    @Override
    public List<Measure> findAllMeasuresByProblemId(Long id) {
        return measureRepository.findAll().
                stream().filter(measure -> measure.getProblem().getId().equals(id)).
                collect(Collectors.toList());
    }

/*    @Override
    public List<Measure> findAllMeasuresByNapId(Long id) {
        NAP nap = napRepository.findById(id).orElseThrow(NAPNotFoundException::new);
        return measureRepository.findAllByNapId(id);
    }*/

/*
    @Override
    public List<Measure> findAllMeasuresByStrategyGoalId(Long id) {
        *//*return measureRepository.findAll().
                stream().
                filter(measure -> measure.getStrategyGoal().getId().equals(id)).
                collect(Collectors.toList());*//*
        return null;
    }*/

    @Override
    protected MeasureRepository getRepository() {
        return measureRepository;
    }
}
