package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.dto.InstitutionModerators;
import io.intelligenta.communityportal.models.dto.InstitutionPage;
import io.intelligenta.communityportal.models.dto.InstitutionWithModerators;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface InstitutionService extends BaseEntityCrudService<Institution> {

    List<Institution> findAll();

    Institution findById(Long id);

    InstitutionPage createInstitution(InstitutionPage institution);

    InstitutionPage updateInstitution(InstitutionPage institution);

    void deleteInstitutionById(Long Id);

    List<InstitutionWithModerators> extractInstitutionsWithModerators();

    InstitutionModerators extractOneInstitutionWithModerators(Long id);

    Page<InstitutionPage> findAllWithModerator(String keyword, Pageable pageable);

    Institution setInactive(Long id);

    Institution findInstitutionByUserEmail();

    Institution sendSelectedInstitution(Long institutionId, InstitutionPage institution, Long categoryId);

    Institution sendSelectedInstitutionWithParent(Long institutionId, InstitutionPage institution, Long parentInstitutionId);

    Page<InstitutionPage> findAllEdited(Pageable pageable);

    Institution recreateInstitution(InstitutionPage institution);

    String formatPhone(String phoneNumber);

    List<Institution> allByParentRecursively(List<Long> institutionsIds);

    List<Institution> allByTags(List<Long> tagIds);

    List<Institution> allByCategoriesRecursively(List<Long> ids);

    List<String> getDirectorEmailsForInstitution(Long institutionId);

}
