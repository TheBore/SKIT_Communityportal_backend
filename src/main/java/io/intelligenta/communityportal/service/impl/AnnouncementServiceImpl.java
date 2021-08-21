package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.AnnouncementDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.AnnouncementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementServiceImpl extends BaseEntityCrudServiceImpl<Announcement, AnnouncementRepository> implements AnnouncementService {

    private UserRepository userRepository;
    private AnnouncementRepository announcementRepository;
    private AnnouncementCommentRepository announcementCommentRepository;
    private AnnouncementPublicationRepository announcementPublicationRepository;
    private AttachmentRepository attachmentRepository;
    private AnnouncementAttachmentRepository announcementAttachmentRepository;
    private final AreaOfInterestRepository areaOfInterestRepository;

    public AnnouncementServiceImpl(UserRepository userRepository,
                                   AnnouncementRepository announcementRepository,
                                   AnnouncementCommentRepository announcementCommentRepository,
                                   AnnouncementPublicationRepository announcementPublicationRepository,
                                   AttachmentRepository attachmentRepository,
                                   AnnouncementAttachmentRepository announcementAttachmentRepository,
                                   AreaOfInterestRepository areaOfInterestRepository) {
        this.announcementRepository = announcementRepository;
        this.announcementCommentRepository = announcementCommentRepository;
        this.announcementPublicationRepository = announcementPublicationRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.announcementAttachmentRepository = announcementAttachmentRepository;
        this.areaOfInterestRepository = areaOfInterestRepository;
    }

    @Override
    public Announcement findAnnouncementById(Long Id) {
        return announcementRepository.findById(Id).orElseThrow(AnnouncementNotFoundException::new);
    }


    @Override
    @Transactional
    public Announcement addAnnouncement(String title, String body, Long areaOfInterestId, String name, String mimeType, Long size, byte[] content) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User creator = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
        Announcement newAnn = new Announcement();
        if(areaOfInterestId == null){
            throw new AnnouncementWithoutAreaOfInterest();
        }
        else {
            newAnn.setAreaOfInterest(areaOfInterestRepository.findById(areaOfInterestId).orElseThrow(AreaOfInterestNotFoundException::new));
        }
        newAnn.setDateCreated(LocalDateTime.now());
        newAnn.setTitle(title);
        newAnn.setCreator(creator);
        newAnn.setBody(body);

        announcementRepository.save(newAnn);

        if(name != null && mimeType != null && content != null && size != null) {
            Attachment newAttachment = new Attachment();
            newAttachment.setName(name);
            newAttachment.setMimeType(mimeType);
            newAttachment.setSize(size);
            newAttachment.setContent(content);
            attachmentRepository.save(newAttachment);

            Long attachmentId = newAttachment.getId();
            AnnouncementAttachment announcementAttachment = new AnnouncementAttachment();
            Long announcementId = newAnn.getId();
            announcementAttachment.setAnnouncementId(announcementId);
            announcementAttachment.setAttachmentId(attachmentId);
            announcementAttachmentRepository.save(announcementAttachment);
        }
        return newAnn;
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long annId) {
        Announcement announcement = announcementRepository.findById(annId).orElseThrow(AnnouncementNotFoundException::new);
        announcement.setAreaOfInterest(null);
        List<AnnouncementComment> comments = announcementCommentRepository.findAllByAnnouncementId(annId);
        announcementCommentRepository.deleteAll(comments);
        List<AnnouncementPublication> publications = announcementPublicationRepository.findAllByAnnouncementId(annId);
        announcementPublicationRepository.deleteAll(publications);
        announcementRepository.deleteById(annId);
    }
    
    public Announcement updateAnnouncement(AnnouncementDto announcement) {
        Announcement newAnnouncement = announcementRepository.findById(announcement.getId()).orElseThrow(AnnouncementNotFoundException::new);

        newAnnouncement.setTitle(announcement.getTitle());
        newAnnouncement.setBody(announcement.getBody());
        newAnnouncement.setDateUpdated(LocalDateTime.now());
        if(announcement.getAreaOfInterestId() != null){
            AreaOfInterest areaOfInterest = areaOfInterestRepository.findById(announcement.getAreaOfInterestId()).orElseThrow(AreaOfInterestNotFoundException::new);
            newAnnouncement.setAreaOfInterest(areaOfInterest);
        }
        else{
            newAnnouncement.setAreaOfInterest(newAnnouncement.getAreaOfInterest());
        }

        announcementRepository.save(newAnnouncement);
        return newAnnouncement;
    }

    @Override
    public Page<Announcement> findAllAnnouncements(Pageable pageable) {
        return getRepository().findAllAnnouncements(pageable);
    }

    @Override
    public Page<Announcement> getAllAnnouncementsByUserAreasOfInterest(List<String> areas, Pageable pageable) {
        if(areas.isEmpty()){
            throw new NoAreasOfInterestForTheUser();
        }

        List<AreaOfInterest> allAreasOfInterest = new ArrayList<>(areaOfInterestRepository.getAreasOfInterestByName(areas));

        try{
            return announcementRepository.getAllByUserAreasOfInterest(allAreasOfInterest, pageable);
        }
        catch (Exception e){
            throw new AreaOfInterestNotFoundException();
        }
    }

    @Override
    public Integer getNumberOfAnnouncementPublishes(Long id) {
        return announcementRepository.findById(id).orElseThrow(AnnouncementNotFoundException::new).getAnnouncementPublications().size();
    }

    @Override
    protected AnnouncementRepository getRepository() {
        return announcementRepository;
    }

}
