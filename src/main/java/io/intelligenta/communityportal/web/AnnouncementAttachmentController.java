package io.intelligenta.communityportal.web;


import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.service.AnnouncementAttachmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping(value = "/rest")
public class AnnouncementAttachmentController {
    AnnouncementAttachmentService announcementAttachmentService;

    public AnnouncementAttachmentController(AnnouncementAttachmentService announcementAttachmentService) {
        this.announcementAttachmentService = announcementAttachmentService;
    }

    @GetMapping("/attachments/{annId}")
    @Transactional
    public List<Attachment> getAnnouncementAttachments(@PathVariable("annId") Long annId) {
        return announcementAttachmentService.getAnnouncementAttachments(annId);

    }

    @DeleteMapping("/deleteAnnAtt")
    public void removeAttachment(@RequestParam("announcementId") Long announcementId, @RequestParam("attachmentId") Long attachmentId) {
        announcementAttachmentService.removeAttachment(announcementId, attachmentId);
    }

    @PostMapping("/addAttachment")
    public Attachment addAttachment(@RequestParam("announcementId") Long announcementId, @RequestParam("attachment") MultipartFile attachment) throws IOException {
        byte[] content = attachment.getBytes();
        String name = attachment.getOriginalFilename();
        String mimeType = attachment.getContentType();
        Long size = attachment.getSize();
        return announcementAttachmentService.addAttachment(announcementId, name, mimeType, size, content);
    }

}
