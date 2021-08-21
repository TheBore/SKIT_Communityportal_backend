package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.AnnouncementAttachment;
import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.repository.AnnouncementAttachmentRepository;
import io.intelligenta.communityportal.repository.AttachmentRepository;
import io.intelligenta.communityportal.service.AnnouncementAttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class AnnouncementAttachmentServiceImpl implements AnnouncementAttachmentService {

    AttachmentRepository attachmentRepository;
    AnnouncementAttachmentRepository announcementAttachmentRepository;

    public AnnouncementAttachmentServiceImpl(AttachmentRepository attachmentRepository,
                                             AnnouncementAttachmentRepository announcementAttachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.announcementAttachmentRepository = announcementAttachmentRepository;
    }

    public List<Attachment> getAnnouncementAttachments(Long annId) {
        List<AnnouncementAttachment> AnnouncementAttachments = announcementAttachmentRepository.findAllByAnnouncementId(annId);
        List<Long> attachmentsIds = new ArrayList<>();
        AnnouncementAttachments.forEach(item -> {
            attachmentsIds.add(item.getAttachmentId());
        });
        List<Attachment> attachments = new ArrayList<>();
        attachmentsIds.forEach(item -> {
            Attachment attachment = attachmentRepository.findAttachmentById(item);
            attachments.add(attachment);
        });
        return attachments;

    }

    @Transactional
    public void removeAttachment(Long announcementId, Long attachmentId) {
        AnnouncementAttachment forDeleting = announcementAttachmentRepository.findByAnnouncementIdAndAttachmentId(announcementId, attachmentId);
        announcementAttachmentRepository.delete(forDeleting);
        Attachment attachment = attachmentRepository.findAttachmentById(attachmentId);
        attachmentRepository.delete(attachment);

    }

    @Transactional
    public Attachment addAttachment(Long announcementId, String name, String mimeType, Long size, byte[] content) {

        Attachment newAttachment = new Attachment();
        newAttachment.setName(name);
        newAttachment.setMimeType(mimeType);
        newAttachment.setSize(size);
        newAttachment.setContent(content);
        attachmentRepository.save(newAttachment);
        Long attachmentId = newAttachment.getId();
        AnnouncementAttachment announcementAttachment = new AnnouncementAttachment();
        announcementAttachment.setAnnouncementId(announcementId);
        announcementAttachment.setAttachmentId(attachmentId);
        announcementAttachmentRepository.save(announcementAttachment);
        return newAttachment;

    }


}
