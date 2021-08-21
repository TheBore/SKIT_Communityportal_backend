package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.service.AttachmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/attachment")
public class AttachmentController {

    private AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/{id}")
    public Attachment findAttachmentById (@PathVariable(value = "id") Long Id){
        return attachmentService.findById(Id);
    }

    @PostMapping("/addAttachment")
    public Attachment addAttachment(@RequestParam("attachment") MultipartFile attachment) throws IOException {
        byte[] content = attachment.getBytes();
        String name = attachment.getOriginalFilename();
        String mimeType = attachment.getContentType();
        Long size = attachment.getSize();
        return attachmentService.saveAttachment(name, mimeType, size, content);
    }

    @GetMapping("/all")
    public List<Attachment> findAllAttachments(){
        return attachmentService.findAll();
    }


    @DeleteMapping("/delete/{id}")
    public void deleteAttachmentById (@PathVariable(value = "id")Long Id){
        attachmentService.deleteAttachment(Id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity downloadFile(@PathVariable(value = "id")Long Id) {

        Attachment attachment = attachmentService.findById(Id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .body(attachment.getContent());
    }

}
