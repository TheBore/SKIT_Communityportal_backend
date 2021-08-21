package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.AnnPubWithComments;
import io.intelligenta.communityportal.models.exceptions.AnnouncementPublicationNotFoundException;
import io.intelligenta.communityportal.models.exceptions.AnnouncementNotFoundException;
import io.intelligenta.communityportal.models.exceptions.EmailSendingException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.AnnouncementPublicationService;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class AnnouncementPublicationServiceImpl implements AnnouncementPublicationService {

    AnnouncementRepository announcementRepository;
    InstitutionRepository institutionRepository;
    UserRepository userRepository;
    AnnouncementPublicationUserRepository announcementPublicationUserRepository;
    AnnouncementPublicationRepository announcementPublicationRepository;
    Environment environment;
    TagRepository tagRepository;
    EmailRepository emailRepository;
    AnnouncementAttachmentRepository announcementAttachmentRepository;
    AttachmentRepository attachmentRepository;
    private AnnouncementCommentRepository announcementCommentRepository;


    public AnnouncementPublicationServiceImpl(AnnouncementRepository announcementRepository,
                                              InstitutionRepository institutionRepository,
                                              AnnouncementPublicationRepository announcementPublicationRepository,
                                              AnnouncementPublicationUserRepository announcementPublicationUserRepository,
                                              TagRepository tagRepository,
                                              UserRepository userRepository,
                                              EmailRepository emailRepository,
                                              Environment environment,
                                              AnnouncementAttachmentRepository announcementAttachmentRepository,
                                              AttachmentRepository attachmentRepository, AnnouncementCommentRepository announcementCommentRepository) {
        this.announcementRepository = announcementRepository;
        this.institutionRepository = institutionRepository;
        this.announcementPublicationRepository = announcementPublicationRepository;
        this.announcementPublicationUserRepository = announcementPublicationUserRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.environment = environment;
        this.announcementAttachmentRepository = announcementAttachmentRepository;
        this.attachmentRepository = attachmentRepository;
        this.announcementCommentRepository = announcementCommentRepository;
    }

    @Override
    public List<AnnouncementPublication> findAllByAnnouncementIdOrderByReadAsc(Long annId) {
        return announcementPublicationRepository.findAllByAnnouncementIdOrderByReadAsc(annId);
    }

    @Override
    public void publishToInstitution(Long annId, List<Long> institutionIds) {
        String from = environment.getProperty("spring.mail.username");
        final Announcement announcement = announcementRepository.findById(annId)
                .orElseThrow(AnnouncementNotFoundException::new);

        List<Institution> institutions = institutionRepository.findAllById(institutionIds);

        if(announcement.getAreaOfInterest().getNameMk().equals("Директор")){
            institutions.forEach(institution -> {
                if (announcementPublicationRepository.findByAnnouncementIdAndReceiverId(annId, institution.getId()) == null) {
                    AnnouncementPublication ap = new AnnouncementPublication();
                    ap.setAnnouncement(announcement);
                    ap.setReceiver(institution);
                    ap.setRead(false);
                    announcementPublicationRepository.save(ap);


                    List<String> emails = new ArrayList<>();

                    emails.add(institution.getDirektorEmail());

                    Map<String, String> map = new HashMap<>();
                    map.put("title", announcement.getTitle());
                    map.put("body", announcement.getBody());
                    try {
                        emailRepository.sendHtmlMail(emails, "Објавено соопштение", "PubAnnToDirector.html", map, from);
                    } catch (Exception e) {
                        throw new EmailSendingException();
                    }

                }
            });
        }

        else {
            institutions.forEach(institution -> {
                if (announcementPublicationRepository.findByAnnouncementIdAndReceiverId(annId, institution.getId()) == null) {
                    AnnouncementPublication ap = new AnnouncementPublication();
                    ap.setAnnouncement(announcement);
                    ap.setReceiver(institution);
                    ap.setRead(false);
                    announcementPublicationRepository.save(ap);


                    List<User> users = institution.getUsers();
                    List<String> emails = new ArrayList<>();
                    users.forEach(item -> {
                        emails.add(item.getEmail());
                    });
                    Map<String, String> map = new HashMap<>();
                    try {
                        emailRepository.sendHtmlMail(emails, "Објавено соопштение", "PubAnn.html", map, from);
                    } catch (Exception e) {
                        throw new EmailSendingException();
                    }

                }
            });
        }

    }

    public void publishAnnouncementByInstitutionTags(Long annId, List<Long> tagIds) {
        String from = environment.getProperty("spring.mail.username");

        final Announcement announcement = announcementRepository.findById(annId)
                .orElseThrow(AnnouncementNotFoundException::new);

        List<Tag> tags = tagRepository.findAllById(tagIds);
        List<Institution> institutions = new ArrayList<>();
        tags.forEach((item) -> {
            institutions.addAll(institutionRepository.findAllByTags(item).stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList()));
        });
        institutions.forEach(institution -> {
            if (announcementPublicationRepository.findByAnnouncementIdAndReceiverId(annId, institution.getId()) == null) {
                AnnouncementPublication ap = new AnnouncementPublication();
                ap.setAnnouncement(announcement);
                ap.setReceiver(institution);
                ap.setRead(false);
                announcementPublicationRepository.save(ap);

                List<User> users = institution.getUsers();
                List<String> emails = new ArrayList<>();
                users.forEach(item -> {
                    emails.add(item.getEmail());
                });
                Map<String, String> map = new HashMap<>();
                try {
                    emailRepository.sendHtmlMail(emails, "Објавено соопштение", "PubAnn.html", map, from);
                } catch (Exception e) {
                    throw new EmailSendingException();
                }

            }
        });
    }

    @Override
    public List<AnnouncementPublication> findAllByReceiverId(Long InstitutionId) {
        return announcementPublicationRepository.findAllByReceiverId(InstitutionId);
    }


    @Override
    public AnnouncementPublication findAnnouncementPubById(Long id) {
        return announcementPublicationRepository.findById(id)
                .orElseThrow(AnnouncementPublicationNotFoundException::new);
    }

    @Override
    @Transactional
    public AnnouncementPublication markAnnouncementPublicationRead(Long annPubId) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        AnnouncementPublication announcementPublication = announcementPublicationRepository.findById(annPubId).orElseThrow(AnnouncementPublicationNotFoundException::new);
        if (announcementPublication.getReadAt() == null && announcementPublication.getRead() == false) {
            announcementPublication.setRead(true);
            announcementPublication.setReadAt(LocalDateTime.now());
            announcementPublicationRepository.save(announcementPublication);
        }
        AnnouncementPublicationUser announcementPublicationUser = new AnnouncementPublicationUser();
        announcementPublicationUser.setAnnPubId(annPubId);
        announcementPublicationUser.setUserId(user.getId());
        announcementPublicationUser.setReadAt(LocalDateTime.now());
        announcementPublicationUserRepository.save(announcementPublicationUser);
        return announcementPublication;
    }

    @Override
    public void deleteAnnouncementPublication(Long annPubId) {
        AnnouncementPublication announcementPublication = announcementPublicationRepository.findById(annPubId).orElseThrow(AnnouncementPublicationNotFoundException::new);
        if (announcementPublication.getReadAt() == null && announcementPublication.getRead() == false) {
            announcementPublicationRepository.delete(announcementPublication);
        }
    }

    @Override
    public void reminder(Long apinstitutionId, String message) {
        String from = environment.getProperty("spring.mail.username");

        List<User> usersToSendMail = userRepository.findAllByActiveAndInstitutionId(true, apinstitutionId);
        List<String> emails = new ArrayList<>();
        usersToSendMail.forEach(item -> {
            emails.add(item.getEmail());
        });
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        try {
            emailRepository.sendHtmlMail(emails, "Потсетување", "Reminder.html", map, from);
        } catch (Exception e) {
            throw new EmailSendingException();
        }
    }

    @Override
    public void escalate(String direktorEmail, String message) {
        String from = environment.getProperty("spring.mail.username");

        List<String> emails = new ArrayList<>();
        emails.add(direktorEmail);

        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        try {
            emailRepository.sendHtmlMail(emails, "Ескалирај", "Escalate.html", map, from);
        } catch (Exception e) {
            throw new EmailSendingException();
        }

    }

    @Override
    public List<Optional<Attachment>> getAllAttachmentsByAnnPubId(Long annPubId) {
        AnnouncementPublication announcementPublication = announcementPublicationRepository.findById(annPubId).orElseThrow(AnnouncementPublicationNotFoundException::new);

        Announcement newAnn = announcementPublication.getAnnouncement();

        Long annId = newAnn.getId();

        List<AnnouncementAttachment> AnnouncementAttachments = announcementAttachmentRepository.findAllByAnnouncementId(annId);

        List<Long> annAttachmentIds = new ArrayList<>();

        AnnouncementAttachments.forEach(item -> {
            annAttachmentIds.add(item.getAttachmentId());
        });


        List<Optional<Attachment>> allAttachments = new ArrayList<>();

        annAttachmentIds.forEach(item -> {
            allAttachments.add(attachmentRepository.findById(item));
        });

        return allAttachments;

    }

    @Override
    public Page<AnnPubWithComments> findByReceiverId(Long receiverId, Pageable pageable) {
        List<AnnPubWithComments> dtos = new ArrayList<>();
        List<AnnouncementPublication> annpubs = announcementPublicationRepository.findAllByReceiverId(receiverId);
        annpubs.forEach(item -> {
            Integer numComments = numberOfAnnouncementComments(item.getAnnouncement().getId());
            AnnPubWithComments dto = new AnnPubWithComments();
            dto.setAnnouncementPublication(item);
            dto.setNumberOfComments(numComments);
            dtos.add(dto);
        });

        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > dtos.size() ? dtos.size() : (start + pageable.getPageSize());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

    public Integer numberOfAnnouncementComments(Long announcementId) {
        List<AnnouncementComment> allAnnComment = announcementCommentRepository.findAllByAnnouncementId(announcementId);
        return allAnnComment.size();
    }
}

