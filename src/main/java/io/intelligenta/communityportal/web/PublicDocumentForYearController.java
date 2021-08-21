package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.PublicDocumentForYear;
import io.intelligenta.communityportal.models.dto.MonitoringDto;
import io.intelligenta.communityportal.service.PublicDocumentForYearService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("/rest/publicdocument")
@CrossOrigin("*")
public class PublicDocumentForYearController {

    PublicDocumentForYearService publicDocumentForYearService;


    PublicDocumentForYearController(PublicDocumentForYearService publicDocumentForYearService) {
        this.publicDocumentForYearService = publicDocumentForYearService;
    }

    @GetMapping("/years")
    public List<Integer> getAllYears() {
        return publicDocumentForYearService.findAllYears();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getone")
    public PublicDocumentForYear getPubDocByTypeInstYear(@RequestParam("year") Integer year, @RequestParam("institutionId") Long institutionId, @RequestParam("typeId") Long typeId) {
        return publicDocumentForYearService.getPubDocByTypeInstYear(year, institutionId, typeId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/url")
    // da mozenajaveniot moderator ili user da go gleda linkovite ili prikacenite fajlovi za svojata institucija
    public List<PublicDocumentForYear> getUrlForYear(@RequestParam("year") Integer year, @RequestParam("institutionId") Long institutionId) {
        return publicDocumentForYearService.getUrlForYear(year, institutionId);
    }

    @PostMapping("/save")
    public void savePublicDocForYear(@RequestParam("year") Integer year, @RequestParam("type") Long type,
                                     @RequestParam(value = "url", required = false) String url, @RequestParam("institutionId") Long institutionId, @RequestParam(value = "attachment", required = false) MultipartFile file) throws IOException {
        if (file != null) {
            byte[] content = file.getBytes();
            String name = file.getOriginalFilename();
            String fileContentType = file.getContentType();
            Long size = file.getSize();
            publicDocumentForYearService.savePublicDocForYear(year, type, url, institutionId, content, name, fileContentType, size);
        } else
            publicDocumentForYearService.savePublicDocForYear(year, type, url, institutionId, null, "", "", 0l);

    }

    @GetMapping("/monitoring")
    public List<MonitoringDto> monitoringOnPublicBodies() {
        return publicDocumentForYearService.monitoring();
    }
}
