package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Announcement;
import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.dto.AnnouncementDto;
import io.intelligenta.communityportal.service.AnnouncementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class AnnouncementController extends CrudResource<Announcement, AnnouncementService> {

    AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/createAnn", consumes = "multipart/form-data")
    public Announcement addAnnouncement(@RequestParam("title") String title, @RequestParam("body") String body, @RequestParam("areaOfInterestId") Long areaOfInterestId, @RequestParam(value = "attachment",required = false) MultipartFile attachment) throws IOException {
        if (attachment != null) {
            byte[] content = attachment.getBytes();
            String name = attachment.getOriginalFilename();
            String mimeType = attachment.getContentType();
            Long size = attachment.getSize();
            return announcementService.addAnnouncement(title, body, areaOfInterestId, name, mimeType, size, content);
        }else
            return announcementService.addAnnouncement(title, body, areaOfInterestId,null,null,null,null);

    }


//    @PreAuthorize("isAuthenticated()")
//    @RequestMapping(value = "/allannouncements/paged",method = RequestMethod.GET,produces = "application/json")
//    public Page<Announcement> getAllAnnouncement(int page, int pageSize, HttpServletRequest request){
//        return getAll(page,pageSize,request);
//    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/allannouncements/paged",method = RequestMethod.GET,produces = "application/json")
    public Page<Announcement> getAllAnnouncements(Pageable pageable){
        return announcementService.findAllAnnouncements(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/announcement/{id}")
    public Announcement getAnnouncementById(@PathVariable("id")Long id){
        return announcementService.findAnnouncementById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/allannouncements/{annId}")
    public void deleteAnnouncement(@PathVariable("annId") Long annId) {
        announcementService.deleteAnnouncement(annId);
    }
    
    @PatchMapping("/updateAnn")
    Announcement updateAnnouncement (@RequestBody AnnouncementDto announcement){
        return announcementService.updateAnnouncement(announcement);
    }

    @GetMapping("/announcements-by-areas")
    public Page<Announcement> getAllAnnouncementsByAreasOfInterest (@RequestParam (value = "areas") List<String> areas, Pageable pageable){
        return announcementService.getAllAnnouncementsByUserAreasOfInterest(areas, pageable);
    }

    @GetMapping("/announcement/number-publishes/{id}")
    public Integer getNumberOfAnnouncementPublishes(@PathVariable Long id){
        return announcementService.getNumberOfAnnouncementPublishes(id);
    }

    @Override
    public AnnouncementService getService() {
        return announcementService;
    }

    @Override
    public Announcement beforeUpdate(Announcement oldEntity, Announcement newEntity) {
        return oldEntity;
    }
}
