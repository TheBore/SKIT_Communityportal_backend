package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.report.PublicYearReport;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

public interface PublicYearReportService {
    List<PublicYearReport> findByInstitutionAndYear(Integer year, Long institutionId);

    PublicYearReport saveReport(String podatociSluzbLice,
                                Integer brPrimeniBaranja,
                                Integer brPozitivnoOdgBaranja,
                                String odbieniIOtfrleniZalbi,
                                Integer brNeodogovoreniBaranja,
                                String vlozeniZalbi,
                                Integer brUsvoeniZalbi,
                                Integer brPreinaceniOdluki,
                                String odbieniZalbi,
                                String otfrelniZalbi,
                                Long institutionId,
                                Integer year);

    void submitReport(Long pyrId) throws Exception;

    PublicYearReport updateReport(String podatociSluzbLice, Integer brPrimeniBaranja, Integer brPozitivnoOdgBaranja, String odbieniIOtfrleniZalbi, Integer brNeodogovoreniBaranja, String vlozeniZalbi, Integer brUsvoeniZalbi, Integer brPreinaceniOdluki, String odbieniZalbi, String otfrelniZalbi, Long pyrId);

    List<PublicYearReport> findAllByYear(Integer year);

    PublicYearReport changeDocument(Long pyrId, byte[] content, String name, String fileContentType, Long size);

    byte[] exportToXlsx();

    Attachment addAttachment(Long publicYearReportId, String name, String mimeType, Long size, byte[] content);

    PublicYearReport findById (Long Id);
}
