package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Measure;
import io.intelligenta.communityportal.models.dto.MeasureDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface MeasureService extends BaseEntityCrudService<Measure> {

    Measure createMeasure (MeasureDto measureDto);

    Measure updateMeasure (MeasureDto measureDto);

    Measure findMeasureById (Long id);

    Page<Measure> findAllMeasures (Pageable pageable);

    Measure setInactive (Long id);

    Measure setActive (Long id);

    List<Measure> findAllMeasuresByProblemId(Long id);

//    List<Measure> findAllMeasuresByStrategyGoalId(Long id);
}
