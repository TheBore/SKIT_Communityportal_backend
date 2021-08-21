package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.FAQ;
import io.intelligenta.communityportal.models.dto.FAQDto;
import io.intelligenta.communityportal.repository.FAQRepository;
import io.intelligenta.communityportal.service.FAQService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/rest/faq")
public class FAQController {

    private FAQService faqService;
    private FAQRepository faqRepository;

    FAQController(FAQService faqService, FAQRepository faqRepository) {
        this.faqService = faqService;
        this.faqRepository = faqRepository;
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public FAQ createFAQ(@ModelAttribute FAQDto faq) throws IOException {
        byte[] content = null;
        String docName = null;
        String mimeType = null;

        if (faq.getAttachment() != null) {
            content = faq.getAttachment().getBytes();
            docName = faq.getAttachment().getOriginalFilename();
            mimeType = faq.getAttachment().getContentType();
        }

        return faqService.createFAQ(faq.getQuestionMK(),
                faq.getQuestionAL(),
                faq.getAnswerMK(),
                faq.getAnswerAL(),
                docName,
                mimeType,
                content);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/update", consumes = "multipart/form-data")
    public FAQ updateFAQ(@ModelAttribute FAQDto faqDto) throws IOException {
        FAQ faq = faqService.findFAQById(faqDto.getId());
        byte[] content = null;
        String docName = null;
        String mimeType = null;

//        MultipartFile attachment = faqDto.getAttachment();
        if (faqDto.getAttachment() == null) {
            content = faq.getContent();
            docName = faq.getDocName();
            mimeType = faq.getMimeType();
        } else {
            content = faqDto.getAttachment().getBytes();
            docName = faqDto.getAttachment().getOriginalFilename();
            mimeType = faqDto.getAttachment().getContentType();
        }

        return faqService.updateFAQ(faqDto.getQuestionMK(),
                faqDto.getQuestionAL(),
                faqDto.getAnswerMK(),
                faqDto.getAnswerAL(),
                docName,
                mimeType,
                content,
                faqDto.getId());
    }


    @GetMapping("/download/{FaqId}")
    public ResponseEntity downloadFile(@PathVariable("FaqId") Long FaqId) {
        FAQ faq = faqService.findFAQById(FaqId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(faq.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + faq.getDocName() + "\"")
                .body(faq.getContent());
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/{faqID}")
    public FAQ findFAQById(@PathVariable("faqID") Long faqID) {
        return faqService.findFAQById(faqID);
    }

    @GetMapping("/all")
    public Page<FAQ> getAllPaged(Pageable pageable) {
        return faqRepository.findAll(pageable);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{faqID}")
    public void deleteFAQ(@PathVariable("faqID") Long faqID) {
        faqService.deleteFAQ(faqID);
    }
}
