package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.BBBUserMeeting;
import io.intelligenta.communityportal.service.BBBService;
import io.intelligenta.communityportal.utils.bbb.api.BBBException;
import io.intelligenta.communityportal.utils.bbb.api.BBBMeeting;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rest/bbb")
public class BBBController {

    private final BBBService bbbService;

    public BBBController(BBBService bbbService) {
        this.bbbService = bbbService;
    }

    @PostMapping("/create/{ids}")
    public BBBMeeting createMeeting(@PathVariable List<Long> ids, @RequestParam String name,
                                    @RequestParam("meetingDate") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate meetingDate){
        return bbbService.createMeeting(ids, name, meetingDate);
    }

    @GetMapping("/all-by-institution/{id}")
    public List<BBBUserMeeting> getMeetingsByInstitution(@PathVariable Long id) {
        return bbbService.getMeetingsByInstitution(id);
    }

    @GetMapping("/url/{meetingId}")
    public String getMeetingUrl(@PathVariable String meetingId){
        return bbbService.getMeetingUrl(meetingId);
    }

    @PutMapping("/close-session/{id}")
    public void closeMeetingSession(@PathVariable Long id){
        bbbService.closeMeeting(id);
    }

    @GetMapping("/getAllForAdmin")
    public List<BBBUserMeeting> getAllMeetingsForAdmin(){
        return bbbService.getAllMeetingsForAdmin();
    }

    @GetMapping("/getNumberOfParticipants/{meetingId}/{password}")
    public Integer getNumberOfParticipants(@PathVariable String meetingId, @PathVariable String password) throws BBBException {
        return bbbService.getNumberOfParticipants(meetingId, password);
    }
}
