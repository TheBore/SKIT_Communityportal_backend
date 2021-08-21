package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Attachment;

import java.util.List;

public interface MeasureAttachmentService {

    List<Attachment> getMeasureAttachments (Long measureId);

    void removeAttachment (Long measureId, Long attachmentId);

    Attachment addAttachment (Long measureId, String name, String mimeType, Long size, byte[] content);

}
