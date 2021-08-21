package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AnnouncementAttachment;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AnnouncementAttachmentRepository extends JpaSpecificationRepository<AnnouncementAttachment> {

    List<AnnouncementAttachment> findAllByAnnouncementId(Long annId);
    AnnouncementAttachment findByAnnouncementIdAndAttachmentId(Long announcementId,Long attachmentId);

}
