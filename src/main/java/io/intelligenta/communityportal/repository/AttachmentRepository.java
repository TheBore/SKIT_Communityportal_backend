package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Attachment;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface AttachmentRepository extends JpaSpecificationRepository<Attachment>{
Attachment findAttachmentById(Long Id);
Attachment findAttachmentByNameAndMimeTypeAndSize(String name, String mimeType, Long size );
}
