package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.report.PublicYearReport;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

public interface PublicYearReportRepository extends JpaSpecificationRepository<PublicYearReport> {

    List<PublicYearReport> findAllByYear(Integer year);

    List<PublicYearReport> findByInstitutionIdAndYear(Long institutionId, Integer year);

    List<PublicYearReport> findAllByInstitutionId(Long institutionId);
}
