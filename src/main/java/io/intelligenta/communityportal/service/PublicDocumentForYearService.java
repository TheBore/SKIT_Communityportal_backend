package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.PublicDocumentForYear;
import io.intelligenta.communityportal.models.dto.MonitoringDto;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface PublicDocumentForYearService {

    void savePublicDocForYear(Integer year, Long type, String url, Long institutionId, byte[] content, String name, String fileContentType, Long size);

    List<PublicDocumentForYear> getUrlForYear(Integer year, Long institutionId);

    List<Integer> findAllYears();

    List<MonitoringDto> monitoring();

    PublicDocumentForYear getPubDocByTypeInstYear(Integer year, Long institutionId, Long typeId);
}
