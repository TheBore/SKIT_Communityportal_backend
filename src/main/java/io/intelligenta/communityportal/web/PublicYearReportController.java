package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.exceptions.AttachmentNotFoundException;
import io.intelligenta.communityportal.models.exceptions.FileIsNullException;
import io.intelligenta.communityportal.models.report.PublicYearReport;
import io.intelligenta.communityportal.service.AttachmentService;
import io.intelligenta.communityportal.service.PublicYearReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

@RestController
@RequestMapping("/rest/yearlyreport")
@CrossOrigin("*")
public class PublicYearReportController {

    private PublicYearReportService publicYearReportService;
    private AttachmentService attachmentService;

    public PublicYearReportController(PublicYearReportService publicYearReportService, AttachmentService attachmentService) {
        this.publicYearReportService = publicYearReportService;
        this.attachmentService = attachmentService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<PublicYearReport> getRportByYearAndInstitution(@RequestParam Long institutionId, @RequestParam Integer year) {
        return publicYearReportService.findByInstitutionAndYear(year, institutionId);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<PublicYearReport> getAllByYear(@RequestParam("year") Integer year) {
        return publicYearReportService.findAllByYear(year);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/attach")
    public PublicYearReport changeAttachment(@RequestParam("pyrId") Long pyrId, @RequestParam("attachment") MultipartFile file) throws IOException {
        if (file != null) {
            byte[] content = file.getBytes();
            String name = file.getOriginalFilename();
            String fileContentType = file.getContentType();
            Long size = file.getSize();
            return publicYearReportService.changeDocument(pyrId, content, name, fileContentType, size);
        }
        throw new FileIsNullException();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/savereport")
    public PublicYearReport saveReport(@RequestParam String podatociSluzbLice,
                                       @RequestParam Integer brPrimeniBaranja,
                                       @RequestParam Integer brPozitivnoOdgBaranja,
                                       @RequestParam String odbieniIOtfrleniZalbi,
                                       @RequestParam Integer brNeodogovoreniBaranja,
                                       @RequestParam String vlozeniZalbi,
                                       @RequestParam Integer brUsvoeniZalbi,
                                       @RequestParam Integer brPreinaceniOdluki,
                                       @RequestParam String odbieniZalbi,
                                       @RequestParam String otfrelniZalbi,
                                       @RequestParam Long institutionId,
                                       @RequestParam Integer year) {

        return publicYearReportService.saveReport(podatociSluzbLice, brPrimeniBaranja, brPozitivnoOdgBaranja, odbieniIOtfrleniZalbi, brNeodogovoreniBaranja, vlozeniZalbi, brUsvoeniZalbi, brPreinaceniOdluki, odbieniZalbi, otfrelniZalbi, institutionId, year);

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/submitReport")
    public void submitReport(@RequestParam("pyrId") Long pyrId) throws Exception {
        publicYearReportService.submitReport(pyrId);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/updateReport")
    public PublicYearReport updateReport(@RequestParam String podatociSluzbLice,
                                         @RequestParam Integer brPrimeniBaranja,
                                         @RequestParam Integer brPozitivnoOdgBaranja,
                                         @RequestParam String odbieniIOtfrleniZalbi,
                                         @RequestParam Integer brNeodogovoreniBaranja,
                                         @RequestParam String vlozeniZalbi,
                                         @RequestParam Integer brUsvoeniZalbi,
                                         @RequestParam Integer brPreinaceniOdluki,
                                         @RequestParam String odbieniZalbi,
                                         @RequestParam String otfrelniZalbi,
                                         @RequestParam Long pyrId) {

        return publicYearReportService.updateReport(podatociSluzbLice, brPrimeniBaranja, brPozitivnoOdgBaranja, odbieniIOtfrleniZalbi, brNeodogovoreniBaranja, vlozeniZalbi, brUsvoeniZalbi, brPreinaceniOdluki, odbieniZalbi, otfrelniZalbi, pyrId);
    }

    @GetMapping("/exportToXlsx")
    public ResponseEntity exportProjectToXlsx() {
        byte[] bytes = publicYearReportService.exportToXlsx();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "Godisni izvestai" + ".xlsx" + "\"")
                .body(bytes);

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadAttachment")
    public Attachment uploadAttachment (@RequestParam("publicYearReportId") Long publicYearReportId, @RequestParam("attachment") MultipartFile file) throws IOException{

        byte[] content = file.getBytes();
        String mimeType = file.getContentType();
        String name = file.getOriginalFilename();
        Long size = file.getSize();

        return publicYearReportService.addAttachment(publicYearReportId, name, mimeType, size, content);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/download/{id}")
    public ResponseEntity downloadFile(@PathVariable(value = "id")Long Id) {

        PublicYearReport publicYearReport = publicYearReportService.findById(Id);
        Attachment attachment = publicYearReport.getSignedDoc();

        if(attachment == null){
            throw new AttachmentNotFoundException();
        }

        else {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                    .body(attachment.getContent());
        }
    }
}
