package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.MeasureAttachment;

import java.util.List;

public interface MeasureAttachmentRepository extends JpaSpecificationRepository<MeasureAttachment> {

    List<MeasureAttachment> findAllByMeasureId(Long id);
    MeasureAttachment findByMeasureIdAndAttachmentId (Long measureId, Long attachmentId);

}
