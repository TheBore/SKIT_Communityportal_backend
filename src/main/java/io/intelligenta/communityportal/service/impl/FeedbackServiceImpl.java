package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.FeedbackAnalysisDto;
import io.intelligenta.communityportal.models.dto.FeedbackDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.models.feedback.Feedback;
import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackItemAnswer;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    FeedbackRepository feedbackRepository;
    FeedbackItemRepository feedbackItemRepository;
    FeedbackItemAnswerRepository feedbackItemAnswerRepository;
    UserRepository userRepository;
    private final AreaOfInterestRepository areaOfInterestRepository;
    private final FeedbackPublicationRepository feedbackPublicationRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackItemRepository feedbackItemRepository,
                               FeedbackItemAnswerRepository feedbackItemAnswerRepository,
                               UserRepository userRepository, AreaOfInterestRepository areaOfInterestRepository, FeedbackPublicationRepository feedbackPublicationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackItemRepository = feedbackItemRepository;
        this.feedbackItemAnswerRepository = feedbackItemAnswerRepository;
        this.userRepository = userRepository;
        this.areaOfInterestRepository = areaOfInterestRepository;
        this.feedbackPublicationRepository = feedbackPublicationRepository;
    }


    @Override
    public Feedback findFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(FeedbackNotFoundException::new);
    }

    @Override
    public List<Feedback> getAll() {
        return feedbackRepository.findAllByOrderByDateCreatedDesc();
    }


    @Override
    public Feedback updateFeedback(FeedbackDto feedbackDto) {
        Feedback updatedFeedback = feedbackRepository.findById(feedbackDto.getId()).orElseThrow(FeedbackNotFoundException::new);
        updatedFeedback.setName(feedbackDto.getName());
        updatedFeedback.setDescription(feedbackDto.getDescription());
        updatedFeedback.setDueDate(feedbackDto.getDueDate());
        updatedFeedback.setDateUpdated(LocalDateTime.now());

        if(feedbackDto.getAreaOfInterestId() != null){
            AreaOfInterest areaOfInterest = this.areaOfInterestRepository.findById(feedbackDto.getAreaOfInterestId()).orElseThrow(AreaOfInterestNotFoundException::new);
            updatedFeedback.setAreaOfInterest(areaOfInterest);
        }
        else{
            updatedFeedback.setAreaOfInterest(updatedFeedback.getAreaOfInterest());
        }

        feedbackRepository.save(updatedFeedback);
        return updatedFeedback;
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(FeedbackNotFoundException::new);
        List<FeedbackItem> items = feedback.getFeedbackItems();
        for (FeedbackItem feedbackItem : items) {
            feedbackItem.setFeedback(null);

            List<FeedbackItemAnswer> feedbackItemAnswers = feedbackItemAnswerRepository.findByItemId(feedbackItem.getId());
            for (FeedbackItemAnswer answer : feedbackItemAnswers) {
                answer.setItem(null);
                answer.setPublication(null);
                feedbackItemAnswerRepository.delete(answer);
            }

            feedbackItemRepository.delete(feedbackItem);
        }

        List<FeedbackPublication> feedbackPublications = feedbackPublicationRepository.findFeedbackPublicationByFeedbackId(id);
        for (FeedbackPublication feedbackPublication : feedbackPublications) {
            feedbackPublication.setFeedback(null);
            feedbackPublication.setInstitution(null);
            feedbackPublicationRepository.delete(feedbackPublication);
        }
        feedback.setAreaOfInterest(null);
        feedbackRepository.delete(feedback);
    }


    @Override
    public Feedback addFeedback(String name, String description, Long areaOfInterestId, LocalDate dueDate) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Feedback newFeedback = new Feedback();
        newFeedback.setName(name);
        newFeedback.setDescription(description);
        if (!dueDate.isBefore(LocalDate.now())) {
            newFeedback.setDueDate(dueDate);
        } else
            throw new FeedbackDueDateIsBeforeNowException();

        if(areaOfInterestId != null){
            AreaOfInterest areaOfInterest = this.areaOfInterestRepository.findById(areaOfInterestId).orElseThrow(AreaOfInterestNotFoundException::new);
            newFeedback.setAreaOfInterest(areaOfInterest);
        }
        else {
            throw new FeedbackWithoutAreaOfInterest();
        }

        newFeedback.setDateUpdated(LocalDateTime.now());
        newFeedback.setDateCreated(LocalDateTime.now());
        newFeedback.setCreator(user);
        newFeedback.setIsPublished(false);
        feedbackRepository.save(newFeedback);
        return newFeedback;
    }

    public FeedbackAnalysisDto getAllFeedbackItemAnswersByFeedbackId(Long id) {
        List<FeedbackItemAnswer> result = this.feedbackItemAnswerRepository.findByItemFeedbackId(id);
        FeedbackAnalysisDto dto = new FeedbackAnalysisDto();

        for (FeedbackItemAnswer answer : result) {
            dto.registerAnswer(answer);
        }

        return dto;
    }

    @Override
    public byte[] analyseFileToExport(Long feedbackId) {
        List<FeedbackItemAnswer> result = this.feedbackItemAnswerRepository.findByItemFeedbackId(feedbackId);
        FeedbackAnalysisDto dto = new FeedbackAnalysisDto();

        for (FeedbackItemAnswer answer : result) {
            dto.registerAnswer(answer);
        }
        File export = new File("JSON.json");
        try {
            FileWriter fileWriter = new FileWriter("JSON.json");
            fileWriter.write(dto.toString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] exportJson = readFileToByteArray(export);

        return exportJson;
    }

    @Override
    public Page<Feedback> getAllFeedbacksByUserAreasOfInterest(List<String> areas, Pageable pageable) {
        if(areas.isEmpty()){
            throw new NoAreasOfInterestForTheUser();
        }

        List<AreaOfInterest> allAreasOfInterest = new ArrayList<>(areaOfInterestRepository.getAreasOfInterestByName(areas));


       try{
           return feedbackRepository.getAllByUserAreasOfInterest(allAreasOfInterest, pageable);
       }
       catch (Exception e){
           throw new AreaOfInterestNotFoundException();
       }

    }

    @Override
    public Integer getNumberOfFeedbackPublishes(Long id) {
        return feedbackRepository.findById(id).orElseThrow(FeedbackNotFoundException::new).getFeedbackPublications().size();
    }


    private static byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
            ioExp.printStackTrace();
        }
        return bArray;
    }
}