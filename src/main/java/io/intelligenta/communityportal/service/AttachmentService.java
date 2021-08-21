package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Attachment;

import java.util.List;

public interface AttachmentService {

    Attachment findById(Long id);

    void deleteAttachment(Long id);

    List<Attachment> findAll();

    Attachment saveAttachment(String name, String mimeType, Long size, byte[] content);
}
