package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.PublicDocumentForYear;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface PublicDocumentForYearRepository extends JpaSpecificationRepository<PublicDocumentForYear> {

    List<PublicDocumentForYear> findByYearAndInstitutionId(Integer year, Long institutionId);

    PublicDocumentForYear findByYearAndInstitutionIdAndTypeId(Integer year, Long institutionId, Long typeId);

    List<PublicDocumentForYear> findByYear(Integer year);

    Optional<PublicDocumentForYear> findByYearAndTypeId(Integer year, Long institutionId);

    @Query("select distinct year from PublicDocumentForYear")
    List<Integer> findAllYears();

    List<PublicDocumentForYear> findAllByInstitutionId(Long institutionId);
}
