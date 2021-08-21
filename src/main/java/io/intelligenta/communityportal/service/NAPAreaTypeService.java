package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.NAPAreaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NAPAreaTypeService {

    NAPAreaType createNAPAreaType (NAPAreaType napAreaType);

    NAPAreaType findById (Long id);

    Page<NAPAreaType> findAllPagedWithKeyword (String keyword, Pageable pageable);

    NAPAreaType updateNAPAreaType (NAPAreaType napAreaType);

    NAPAreaType setInactive (Long id);

    NAPAreaType setActive (Long id);

    List<NAPAreaType> findAllActive();

}
