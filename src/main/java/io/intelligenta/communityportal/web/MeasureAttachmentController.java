package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.service.MeasureAttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/measureAttach")
public class MeasureAttachmentController {

    private final MeasureAttachmentService measureAttachmentService;

    public MeasureAttachmentController (MeasureAttachmentService measureAttachmentService){
        this.measureAttachmentService = measureAttachmentService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/attachments/{measureId}")
    public List<Attachment> getMeasureAttachments(@PathVariable(value = "measureId")Long measureId){
        return measureAttachmentService.getMeasureAttachments(measureId);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public void deleteAttachment(@RequestParam("measureId")Long measureId, @RequestParam("attachmentId")Long attachmentId){
        measureAttachmentService.removeAttachment(measureId, attachmentId);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/addAttachment")
    public Attachment addAttachment(@RequestParam("measureId")Long measureId, @RequestParam("attachment") MultipartFile attachment) throws IOException{
        byte[] content = attachment.getBytes();
        String name = attachment.getOriginalFilename();
        String mimeType = attachment.getContentType();
        Long size = attachment.getSize();

        return measureAttachmentService.addAttachment(measureId, name, mimeType, size, content);
    }
}