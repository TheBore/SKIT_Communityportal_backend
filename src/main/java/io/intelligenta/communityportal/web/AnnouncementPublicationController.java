package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.AnnouncementPublication;
import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.dto.AnnPubUserDto;
import io.intelligenta.communityportal.models.dto.AnnPubWithComments;
import io.intelligenta.communityportal.repository.AnnouncementPublicationRepository;
import io.intelligenta.communityportal.service.AnnouncementPublicationService;
import io.intelligenta.communityportal.service.AnnouncementPublicationUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class AnnouncementPublicationController {

    AnnouncementPublicationUserService announcementPublicationUserService;
    AnnouncementPublicationService announcementPublicationService;
    AnnouncementPublicationRepository announcementPublicationRepository;

    AnnouncementPublicationController(AnnouncementPublicationService announcementPublicationService,
                                      AnnouncementPublicationUserService announcementPublicationUserService,
                                      AnnouncementPublicationRepository announcementPublicationRepository) {
        this.announcementPublicationService = announcementPublicationService;
        this.announcementPublicationUserService = announcementPublicationUserService;
        this.announcementPublicationRepository = announcementPublicationRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAnnouncementPub")
    public AnnouncementPublication getAnnouncementPubById(@RequestParam(value = "announcementPubId") Long announcementPubID) {
        return announcementPublicationService.findAnnouncementPubById(announcementPubID);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/annPub/report")                                   
    public Page<AnnPubUserDto> findAll(@PageableDefault Pageable pageable) {
        return announcementPublicationUserService.findAllWithUsers(pageable);
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/annPub/reportbyann")                              
    public Page<AnnPubUserDto> findAllByAnnouncement(@RequestParam("title")String title,@PageableDefault Pageable pageable) {
        return announcementPublicationUserService.findAllWithUsersByAnnouncement(title,pageable);
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/annPub/reportbyuser")                             
    public Page<AnnPubUserDto> findAllByUser(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName ,@PageableDefault Pageable pageable) {
        return announcementPublicationUserService.findAllWithUsersByUser(firstName,lastName,pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/annpub/paged")                                    
    public Page<AnnPubWithComments> getAnnouncementPublicationPaged(@RequestParam("receiverId") Long receiverId, Pageable pageable) {
        return announcementPublicationService.findByReceiverId(receiverId, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/ann/{annId}/institution/{insIds}")     
    public void publishAnnouncementToInstitution(@PathVariable("annId") Long annId, @PathVariable("insIds") List<Long> insId) {

        announcementPublicationService.publishToInstitution(annId, insId);

    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/ann/{annId}/publishByInstitutionTags/{tagIds}")  
    public void publishAnnouncementByInstitutionTags(@PathVariable("annId") Long annId, @PathVariable("tagIds") List<Long> tagIds) {

        announcementPublicationService.publishAnnouncementByInstitutionTags(annId, tagIds);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/myannouncements/{id}")           
    public AnnouncementPublication markRead(@PathVariable(value = "id") Long announcementPubID) {
        return announcementPublicationService.markAnnouncementPublicationRead(announcementPubID);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/annpub/{id}")              
    public void deletePublication(@PathVariable(value = "id") Long announcementPubID) {
        announcementPublicationService.deleteAnnouncementPublication(announcementPubID);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/annpub/{annId}")  
    public Page<AnnouncementPublication> getAllPublicationsForAnnouncement(@PathVariable(value = "annId") Long annId,Pageable pageable) {
        return announcementPublicationRepository.findAllByAnnouncementIdOrderByReadAsc(annId,pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attachIds/{annPubId}")
    public List<Optional<Attachment>> getAllAttachmentIds (@PathVariable("annPubId") Long annPubId){
        return announcementPublicationService.getAllAttachmentsByAnnPubId(annPubId);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mail/reminder")
    public void reminder(@RequestParam("apinstitutionId") Long apinstitutionId, @RequestParam("message") String message){
         announcementPublicationService.reminder(apinstitutionId, message);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mail/escalate")    
    public void escalate(@RequestParam("direktorEmail") String direktorEmail, @RequestParam("message") String message) {
        announcementPublicationService.escalate(direktorEmail, message);
    }
}
