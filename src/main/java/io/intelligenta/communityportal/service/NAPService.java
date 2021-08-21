package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.NAP;
import io.intelligenta.communityportal.models.dto.NAPDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NAPService extends BaseEntityCrudService<NAP> {

    NAP createNAP(NAPDto nap);

    NAP findById (Long id);

    Page<NAP> findAllByDateCreatedDesc (Pageable pageable);

    List<NAP> findAllByDateCreatedDescAndActiveList();

    NAP updateNAP (NAPDto nap, Long id);

    NAP setInactive(Long id);

    NAP setActive(Long id);

    void changeEvaluationStatus(boolean checked, Long id);

}
