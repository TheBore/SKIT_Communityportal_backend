package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.ActivityInstitution;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.dto.ActivityInstitutionDto;
import io.intelligenta.communityportal.models.exceptions.ActivityInstitutionNotFoundException;
import io.intelligenta.communityportal.models.exceptions.InstitutionNotFoundException;
import io.intelligenta.communityportal.repository.ActivityInstitutionRepository;
import io.intelligenta.communityportal.repository.InstitutionRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.ActivityInstitutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityInstitutionServiceImpl implements ActivityInstitutionService {

    private final ActivityInstitutionRepository activityInstitutionRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public ActivityInstitutionServiceImpl(ActivityInstitutionRepository activityInstitutionRepository, UserRepository userRepository, InstitutionRepository institutionRepository) {
        this.activityInstitutionRepository = activityInstitutionRepository;
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    @Override
    public Page<ActivityInstitution> findAllPaged(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase();
        if(!keyword.equals("") && !keyword.equals("undefined") && !keyword.equals("null")){
            return activityInstitutionRepository.findAllWithKeyword(keyword, pageable);
        }
        else{
            return activityInstitutionRepository.findAllWithoutKeyword(pageable);
        }
    }

    @Override
    public List<ActivityInstitution> findAll() {
        return activityInstitutionRepository.findAll();
    }

    @Override
    public ActivityInstitution create(ActivityInstitutionDto activityInstitution) {
        ActivityInstitution newActivityInstitution = new ActivityInstitution();

        newActivityInstitution.setNameMk(activityInstitution.getNameMk());
        newActivityInstitution.setNameAl(activityInstitution.getNameAl());
        newActivityInstitution.setNameEn(activityInstitution.getNameEn());

        if(activityInstitution.getInstitution() != null){
            Institution institution = institutionRepository.findById(activityInstitution.getInstitution()).orElseThrow(InstitutionNotFoundException::new);
            newActivityInstitution.setInstitution(institution);
        } else {
            newActivityInstitution.setInstitution(null);
        }

        newActivityInstitution.setDateCreated(LocalDateTime.now());

        return activityInstitutionRepository.save(newActivityInstitution);
    }

    @Override
    public ActivityInstitution updateInst(ActivityInstitutionDto activityInstitution) {
        ActivityInstitution updatedActivityInstitution = activityInstitutionRepository.findById(activityInstitution.getId()).orElseThrow(ActivityInstitutionNotFoundException::new);

        updatedActivityInstitution.setNameMk(activityInstitution.getNameMk());
        updatedActivityInstitution.setNameAl(activityInstitution.getNameAl());
        updatedActivityInstitution.setNameEn(activityInstitution.getNameEn());

        if(activityInstitution.getInstitution() != null) {
            Institution institution = institutionRepository.findById(activityInstitution.getInstitution()).orElseThrow(InstitutionNotFoundException::new);
            updatedActivityInstitution.setInstitution(institution);
        } else {
            updatedActivityInstitution.setInstitution(null);
        }

        return activityInstitutionRepository.save(updatedActivityInstitution);
    }

    @Override
    public ActivityInstitution findById(Long activityInstitutionId) {
        return activityInstitutionRepository.findById(activityInstitutionId).orElseThrow(ActivityInstitutionNotFoundException::new);
    }

    @Override
    public void deleteById(Long activityInstitutionId) {
        activityInstitutionRepository.deleteById(activityInstitutionId);
    }
}
