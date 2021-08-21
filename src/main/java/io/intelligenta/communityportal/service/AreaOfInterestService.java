package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.dto.AreaOfInterestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AreaOfInterestService {

    AreaOfInterest createAreaOfInterest (AreaOfInterestDto areaOfInterestDto);

    AreaOfInterest updateAreaOfInterest (AreaOfInterestDto areaOfInterestDto);

    AreaOfInterest findById(Long id);

    Page<AreaOfInterest> findAllAreasOfInterest(String keyword, Pageable pageable);

    AreaOfInterest setInactive (Long id);

    AreaOfInterest setActive (Long id);

    List<AreaOfInterest> findAll();

    List<AreaOfInterest> findAreasOfInterestForUser();
}
