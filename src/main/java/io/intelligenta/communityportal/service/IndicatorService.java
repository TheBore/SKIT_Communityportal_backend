package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Indicator;
import io.intelligenta.communityportal.models.dto.IndicatorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IndicatorService {

    Page<Indicator> findAllIndicators(Pageable pageable);

    Page<Indicator> findAllIndicatorsPageByActivityId(Long id,Pageable pageable);

    Indicator findById(Long id);

    Indicator createIndicator(IndicatorDto indicatorDto);

    Indicator updateIndicator(IndicatorDto indicator, Long id);

    Indicator setActive(Long id);

    Indicator setInactive(Long id);

    List<Indicator> findAllIndicatorsList();

    List<Indicator> findAllIndicatorsByActivityId(Long id);

    Indicator updateIndicatorOnEvaluation(IndicatorDto indicatorDto);

}
