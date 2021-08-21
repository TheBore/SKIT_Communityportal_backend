package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.Message;
import io.intelligenta.communityportal.models.Question;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.QuestionDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.AreaOfInterestRepository;
import io.intelligenta.communityportal.repository.MessageRepository;
import io.intelligenta.communityportal.repository.QuestionRepository;

import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl extends BaseEntityCrudServiceImpl<Question, QuestionRepository> implements QuestionService {


    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final AreaOfInterestRepository areaOfInterestRepository;

    public QuestionServiceImpl (QuestionRepository questionRepository, UserRepository userRepository, MessageRepository messageRepository, AreaOfInterestRepository areaOfInterestRepository){
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.areaOfInterestRepository = areaOfInterestRepository;
    }

    @Override
    public Question createQuestion(QuestionDto questionDto) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
        Question question = new Question();

        question.setAuthor(user);
        question.setActive(true);
        question.setTitle(questionDto.getTitle());

        if(questionDto.getAreaOfInterestId() != null){
            AreaOfInterest areaOfInterest = areaOfInterestRepository.findById(questionDto.getAreaOfInterestId()).orElseThrow(AreaOfInterestNotFoundException::new);
            question.setAreaOfInterest(areaOfInterest);
        }
        else{
            throw new QuestionWithoutAreaOfInterest();
        }

        question.setDateCreated(LocalDateTime.now());
        question.setDateUpdated(LocalDateTime.now());

        return questionRepository.save(question);
    }

    @Override
    public Page<Question> findAllByActive(Boolean active, Pageable pageable) {
        return questionRepository.findAllByActiveOrderByDateUpdatedDesc(true, pageable);
    }

    @Override
    public Page<Question> findAll(Pageable pageable) {
        return questionRepository.findAllOrderByDateUpdatedDesc(pageable);
    }

    @Override
    public Question findQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Override
    public Question updateQuestion(QuestionDto questionDto) {
        Question updatedQuestion = questionRepository.findById(questionDto.getId()).orElseThrow(QuestionNotFoundException::new);

        updatedQuestion.setTitle(questionDto.getTitle());
        updatedQuestion.setDateUpdated(LocalDateTime.now());

        if(questionDto.getAreaOfInterestId() != null){
            AreaOfInterest areaOfInterest = areaOfInterestRepository.findById(questionDto.getAreaOfInterestId()).orElseThrow(AreaOfInterestNotFoundException::new);
            updatedQuestion.setAreaOfInterest(areaOfInterest);
        }
        else{
            updatedQuestion.setAreaOfInterest(updatedQuestion.getAreaOfInterest());
        }
        questionRepository.save(updatedQuestion);
        return updatedQuestion;
    }

    @Override
    public Question setInactive(Long id) {
        Question updateQuestion = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        List<Message> messages = updateQuestion.getMessages();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        messages.forEach(msg -> {
            msg.setActive(false);
            msg.setDateUpdated(LocalDateTime.now());
            this.messageRepository.save(msg);
        });

        updateQuestion.setDateUpdated(LocalDateTime.now().minusDays(30));
        updateQuestion.setActive(false);
        updateQuestion.setAreaOfInterest(null);
        updateQuestion.setDeletedByUser(user);

        return questionRepository.save(updateQuestion);
    }

    @Override
    public Question setActive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Question updateQuestion = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        List<Message> messages = updateQuestion.getMessages();

        messages.forEach(msg -> {
            msg.setActive(true);
            msg.setDateUpdated(LocalDateTime.now());
            this.messageRepository.save(msg);
        });

        updateQuestion.setDateUpdated(LocalDateTime.now());
        updateQuestion.setActive(true);
        updateQuestion.setDeletedByUser(null);

        return questionRepository.save(updateQuestion);
    }

    @Override
    public Page<Question> findAllByActiveAndByAuthor(Boolean active, Pageable pageable) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        return questionRepository.findAllByActiveAndAuthorId(false, user.getId(), pageable);

    }

    @Override
    public Page<Question> getAllQuestionsByUserAreasOfInterest(List<String> areas, Pageable pageable) {
        if(areas.isEmpty()){
            throw new NoAreasOfInterestForTheUser();
        }

        List<AreaOfInterest> allAreasOfInterest = new ArrayList<>(areaOfInterestRepository.getAreasOfInterestByName(areas));

        try{
            return questionRepository.getAllByUserAreasOfInterest(allAreasOfInterest, pageable);
        }
        catch (Exception e){
            throw new AreaOfInterestNotFoundException();
        }
    }

    @Override
    public Integer getNumberOfCommentsForQuestion(Long questionId) {
        Question question = this.questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        return question.getMessages().size();
    }


    @Override
    protected QuestionRepository getRepository() {
        return questionRepository;
    }
}
