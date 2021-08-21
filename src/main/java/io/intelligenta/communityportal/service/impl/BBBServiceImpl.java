package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.BBBUserMeeting;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.exceptions.EmailSendingException;
import io.intelligenta.communityportal.models.exceptions.InstitutionNotFoundException;
import io.intelligenta.communityportal.models.exceptions.MeetingNotFoundException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.BBBUserMeetingRepository;
import io.intelligenta.communityportal.repository.InstitutionRepository;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.BBBService;
import io.intelligenta.communityportal.utils.bbb.api.BBBException;
import io.intelligenta.communityportal.utils.bbb.api.BBBMeeting;
import io.intelligenta.communityportal.utils.bbb.impl.BaseBBBAPI;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BBBServiceImpl implements BBBService {

    private static final String server = "noserver";
    private static final String salt = "nosalt";

    private final BaseBBBAPI bbbapi;
    private final BBBUserMeetingRepository bbbUserMeetingRepository;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final Environment environment;

//    @Value("${bbb.server}")
//    private String server;
//
//    @Value("${bbb.salt}")
//    private String salt;

    public BBBServiceImpl(BBBUserMeetingRepository bbbUserMeetingRepository, InstitutionRepository institutionRepository, UserRepository userRepository, EmailRepository emailRepository, Environment environment) {
        this.bbbUserMeetingRepository = bbbUserMeetingRepository;
        this.institutionRepository = institutionRepository;
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.environment = environment;
        this.bbbapi = new BaseBBBAPI(server,salt);
    }

    @Override
    public BBBMeeting createMeeting(List<Long> ids, String name, LocalDate meetingDate) {
        String from = environment.getProperty("spring.mail.username");
        try{
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();

            BBBMeeting bbbMeeting = this.bbbapi.createMeeting(uuidAsString);

            List<Institution> institutions = new ArrayList<>();

            //  BBBUserMeeting is created for the purpose of listing meetings for Users in a given Institutions
            BBBUserMeeting bbbUserMeeting = new BBBUserMeeting();

            for (Long id : ids) {
                institutions.add(institutionRepository.findById(id).orElseThrow(InstitutionNotFoundException::new));
            }

            bbbUserMeeting.setName(name);
            bbbUserMeeting.setMeetingId(uuidAsString);
            bbbUserMeeting.setInstitutions(institutions);
            bbbUserMeeting.setOpenSession(true);
            bbbUserMeeting.setDateCreated(LocalDateTime.now());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            bbbUserMeeting.setMeetingDate(meetingDate);

            bbbUserMeeting.setModeratorPw(bbbMeeting.getModeratorPW());
            bbbUserMeeting.setAttendeePw(bbbMeeting.getAttendeePW());
            bbbUserMeetingRepository.save(bbbUserMeeting);

            institutions.forEach(institution -> {

                List<User> users = institution.getUsers();
                List<String> emails = new ArrayList<>();

                users.forEach(user -> {
                    if(user.getEmail() != null && !user.getEmail().equals("") && user.getActive()){
                        emails.add(user.getEmail());
                    }
                });

                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("date", meetingDate.toString());

                try {
                    emailRepository.sendHtmlMail(emails, "New meeting", "Meeting.html", map, from);
                }
                catch (Exception e){
                    throw new EmailSendingException();
                }
            });

            return bbbMeeting;
        }
        catch (BBBException e){
            return null;
        }
    }

    @Override
    public List<BBBUserMeeting> getMeetingsByInstitution(Long id) {
        List<BBBUserMeeting> bbbUserMeetings = bbbUserMeetingRepository.findAll();
        List<BBBUserMeeting> bbbUserMeetingsList = new ArrayList<>();

        for(BBBUserMeeting bbbUserMeeting : bbbUserMeetings){
            if(bbbUserMeeting.getOpenSession()){
                for(Institution institution: bbbUserMeeting.getInstitutions()){
                    if (institution.getId().equals(id)){
                        bbbUserMeetingsList.add(bbbUserMeeting);
                    }
                }
            }
        }

//        for (BBBUserMeeting userMeeting : bbbUserMeetingsList){
//            try {
//                bbbapi.getMeetingInfo(userMeeting.getMeetingId(), userMeeting.getModeratorPw());
//                userMeeting.setParticipants(Integer.parseInt(bbbapi.getMeetingInfo(userMeeting.getMeetingId(), userMeeting.getModeratorPw()).get("participantCount").toString()));
//            }catch (BBBException e){
//                if(e.getMessageKey().equals("notFound")){
//                    userMeeting.setParticipants(0);
//                }
//            }
//        }

        return bbbUserMeetingsList;
    }

    @Override
    public String getMeetingUrl(String meetingId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        String password;

        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            password = bbbUserMeetingRepository.findByMeetingId(meetingId).getModeratorPw();
        }
        else{
            password = bbbUserMeetingRepository.findByMeetingId(meetingId).getAttendeePw();
        }
        String url = "";

        try {
            this.bbbapi.getMeetingInfo(meetingId, password);
            url = this.bbbapi.getJoinMeetingURL(meetingId,password, user.getFirstName() + " " + user.getLastName(), user.getId().toString());

        }catch (BBBException e){
            if(e.getMessageKey().equals("notFound")){
                try {
                    BBBMeeting bbbMeeting = this.bbbapi.createMeeting(meetingId);

                    BBBUserMeeting bbbUserMeeting = this.bbbUserMeetingRepository.findByMeetingId(meetingId);
                    bbbUserMeeting.setModeratorPw(bbbMeeting.getModeratorPW());
                    bbbUserMeeting.setAttendeePw(bbbMeeting.getAttendeePW());
                    this.bbbUserMeetingRepository.save(bbbUserMeeting);

                    if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
                        password = bbbMeeting.getModeratorPW();
                    }
                    else{
                        password = bbbMeeting.getAttendeePW();
                    }
                    url = this.bbbapi.getJoinMeetingURL(meetingId,password, user.getFirstName() + " " + user.getLastName(), user.getId().toString());
                }catch (BBBException ignored){

                }
            }
        }
        return url;
    }

    @Override
    public void closeMeeting(Long id) {
        try{
            BBBUserMeeting userMeeting = bbbUserMeetingRepository.findById(id).orElseThrow(MeetingNotFoundException::new);

            userMeeting.setOpenSession(false);
            bbbUserMeetingRepository.save(userMeeting);

            this.bbbapi.endMeeting(userMeeting.getMeetingId(), userMeeting.getModeratorPw());
        }
        catch (BBBException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<BBBUserMeeting> getAllMeetingsForAdmin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        if(user.getRole().equals(UserRole.ROLE_ADMIN)){
            return bbbUserMeetingRepository.findAll().stream().filter(BBBUserMeeting::getOpenSession).collect(Collectors.toList());
//            for (BBBUserMeeting userMeeting : meetings){
//                try {
//                    bbbapi.getMeetingInfo(userMeeting.getMeetingId(), userMeeting.getModeratorPw());
//                    userMeeting.setParticipants(Integer.parseInt(bbbapi.getMeetingInfo(userMeeting.getMeetingId(), userMeeting.getModeratorPw()).get("participantCount").toString()));
//                }catch (BBBException e){
//                    if(e.getMessageKey().equals("notFound")){
//                        userMeeting.setParticipants(0);
//                    }
//                }
//            }

        }
        return null;
    }

    @Override
    public Integer getNumberOfParticipants(String meetingId, String password) {
        try{
            return Integer.parseInt(bbbapi.getMeetingInfo(meetingId, password).get("participantCount").toString());
        }
        catch (BBBException ignored){
            return 0;
        }
    }

}
