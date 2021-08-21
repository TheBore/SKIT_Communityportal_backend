package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.MeasureAttachment;
import io.intelligenta.communityportal.repository.AttachmentRepository;
import io.intelligenta.communityportal.repository.MeasureAttachmentRepository;
import io.intelligenta.communityportal.service.MeasureAttachmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeasureAttachmentServiceImpl implements MeasureAttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final MeasureAttachmentRepository measureAttachmentRepository;

    public MeasureAttachmentServiceImpl (AttachmentRepository attachmentRepository, MeasureAttachmentRepository measureAttachmentRepository){
        this.attachmentRepository = attachmentRepository;
        this.measureAttachmentRepository = measureAttachmentRepository;
    }

    @Override
    public List<Attachment> getMeasureAttachments(Long measureId) {
        List<MeasureAttachment> measureAttachments = measureAttachmentRepository.findAllByMeasureId(measureId);
        List<Long> attachmentIds = new ArrayList<>();
        measureAttachments.forEach(item -> attachmentIds.add(item.getId()));

        List<Attachment> attachments = new ArrayList<>();
        attachmentIds.forEach(item ->{
            Attachment attachment = attachmentRepository.findAttachmentById(item);
            attachments.add(attachment);
        });
        return attachments;
    }

    @Override
    public void removeAttachment(Long measureId, Long attachmentId) {
        MeasureAttachment measureAttachmentToDelete = measureAttachmentRepository.findByMeasureIdAndAttachmentId(measureId, attachmentId);
        measureAttachmentRepository.delete(measureAttachmentToDelete);
        Attachment attachment = attachmentRepository.findAttachmentById(attachmentId);
        attachmentRepository.delete(attachment);
    }

    @Override
    public Attachment addAttachment(Long measureId, String name, String mimeType, Long size, byte[] content) {

        Attachment newAttachment = new Attachment();
        newAttachment.setName(name);
        newAttachment.setMimeType(mimeType);
        newAttachment.setSize(size);
        newAttachment.setContent(content);
        attachmentRepository.save(newAttachment);

        Long attachmentId = newAttachment.getId();
        MeasureAttachment measureAttachment = new MeasureAttachment();
        measureAttachment.setMeasureId(measureId);
        measureAttachment.setAttachmentId(attachmentId);
        measureAttachmentRepository.save(measureAttachment);
        return newAttachment;
    }

}
