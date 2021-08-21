package io.intelligenta.communityportal.service;
import io.intelligenta.communityportal.models.ActivityInstitution;
import io.intelligenta.communityportal.models.dto.ActivityInstitutionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ActivityInstitutionService {

    Page<ActivityInstitution> findAllPaged(String keyword, Pageable pageable);

    List<ActivityInstitution> findAll();

    ActivityInstitution create(ActivityInstitutionDto activityInstitution);

    ActivityInstitution updateInst(ActivityInstitutionDto activityInstitution);

    ActivityInstitution findById(Long activityInstitutionId);

    void deleteById(Long activityInstitutionId);

}
