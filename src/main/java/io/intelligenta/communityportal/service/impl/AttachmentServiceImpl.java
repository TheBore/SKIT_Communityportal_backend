package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.exceptions.AttachmentNotFoundException;
import io.intelligenta.communityportal.repository.AttachmentRepository;
import io.intelligenta.communityportal.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment findById(Long id) {
        return attachmentRepository.findById(id).orElseThrow(AttachmentNotFoundException::new);
    }

    @Override
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(AttachmentNotFoundException::new);
        attachmentRepository.delete(attachment);
    }

    @Override
    public List<Attachment> findAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public Attachment saveAttachment(String name, String mimeType, Long size, byte[] content) {
        Attachment newAttachment = new Attachment();
        newAttachment.setName(name);
        newAttachment.setMimeType(mimeType);
        newAttachment.setSize(size);
        newAttachment.setContent(content);
        return attachmentRepository.save(newAttachment);
    }

}
