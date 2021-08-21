package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Attachment;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AnnouncementAttachmentService {
    List<Attachment> getAnnouncementAttachments(Long annId);

    void removeAttachment(Long announcementId, Long attachmentId);

    Attachment addAttachment(Long announcementId, String name, String mimeType, Long size, byte[] content);
}
