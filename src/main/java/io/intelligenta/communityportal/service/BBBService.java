package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.BBBUserMeeting;
import io.intelligenta.communityportal.utils.bbb.api.BBBException;
import io.intelligenta.communityportal.utils.bbb.api.BBBMeeting;

import java.time.LocalDate;
import java.util.List;

public interface BBBService {

    BBBMeeting createMeeting(List<Long> ids, String name, LocalDate meetingDate);

    List<BBBUserMeeting> getMeetingsByInstitution(Long id);

    String getMeetingUrl(String meetingId);

    void closeMeeting(Long meetingId);

    List<BBBUserMeeting> getAllMeetingsForAdmin();

    Integer getNumberOfParticipants(String meetingId, String password) throws BBBException;
}
