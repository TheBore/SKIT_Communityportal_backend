package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.AnnouncementPublication;
import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.dto.AnnPubWithComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */

public interface AnnouncementPublicationService {

    List<AnnouncementPublication> findAllByAnnouncementIdOrderByReadAsc(Long annId);

    void publishToInstitution(Long annId, List<Long> institutionId);

    void publishAnnouncementByInstitutionTags(Long annId, List<Long> tagIds);

    List<AnnouncementPublication> findAllByReceiverId(Long InstitutionId);

    AnnouncementPublication findAnnouncementPubById(Long id);

    AnnouncementPublication markAnnouncementPublicationRead(Long annPubId);

    void deleteAnnouncementPublication(Long annPubId);

    void reminder(Long apinstitutionId, String message);

    void escalate(String direktorEmail, String message);

    List<Optional<Attachment>> getAllAttachmentsByAnnPubId(Long annPubId);

    Page<AnnPubWithComments> findByReceiverId(Long receiverId, Pageable pageable);
}
