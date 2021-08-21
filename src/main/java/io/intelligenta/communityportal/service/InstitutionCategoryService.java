package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.dto.InstitutionCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstitutionCategoryService extends BaseEntityCrudService<InstitutionCategory> {

    InstitutionCategory findInstitutionCategoryById (Long Id);
    InstitutionCategory addInstitutionCategory (InstitutionCategoryDto institutionCategory);
    InstitutionCategory updateInstitutionCategory (InstitutionCategoryDto institutionCategory);

    List<InstitutionCategory> findAllCat();
    Page<InstitutionCategory> listAll(String keyword, Pageable pageable);

    void deleteById(Long institutionCategoryId);

    List<InstitutionCategory> changeCategoriesOrder(List<Long> categoryIds);
}
