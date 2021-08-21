package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.dto.NapAreaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NAPAreaService {

    NAPArea createNAPArea (NapAreaDto napAreaDto);

    NAPArea findById (Long id);

    Page<NAPArea> findAllPaged (String keyword, Pageable pageable);

    NAPArea updateNAPArea (NapAreaDto napAreaDto);

    NAPArea setInactive (Long id);

    NAPArea setActive (Long id);

    List<NAPArea> findAllList();

}
