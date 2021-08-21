package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.RequestForEvaluation;
import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.exceptions.EmailSendingException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.InstitutionCategoryRepository;
import io.intelligenta.communityportal.repository.InstitutionRepository;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.RequestForEvaluationRepository;
import io.intelligenta.communityportal.repository.TagRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.RequestForEvaluationService;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestForEvaluationServiceImpl implements RequestForEvaluationService {

    private final RequestForEvaluationRepository requestForEvaluationRepository;
    private final Environment environment;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final EmailRepository emailRepository;
    private final TagRepository tagRepository;
    private final InstitutionCategoryRepository institutionCategoryRepository;

    public RequestForEvaluationServiceImpl(RequestForEvaluationRepository requestForEvaluationRepository, Environment environment, UserRepository userRepository, InstitutionRepository institutionRepository, EmailRepository emailRepository, TagRepository tagRepository, InstitutionCategoryRepository institutionCategoryRepository) {
        this.requestForEvaluationRepository = requestForEvaluationRepository;
        this.environment = environment;
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
        this.emailRepository = emailRepository;
        this.tagRepository = tagRepository;
        this.institutionCategoryRepository = institutionCategoryRepository;
    }

    @Override
    public Page<RequestForEvaluation> findAllPaged(Pageable pageable) {
        return requestForEvaluationRepository.findAllPaged(pageable);
    }

    @Override
    public void sendRequestsForEvaluation(List<Long> institutionIds, String title, String body) {
        String from = environment.getProperty("spring.mail.username");

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        List<Institution> institutions = institutionRepository.findAllById(institutionIds);
        institutions.forEach(institution -> {
            RequestForEvaluation requestForEvaluation = new RequestForEvaluation();
            requestForEvaluation.setSender(user);
            requestForEvaluation.setReceiver(institution);
            requestForEvaluation.setDateCreated(LocalDateTime.now());
            requestForEvaluation.setTitle(title);
            requestForEvaluation.setBody(body);
            this.requestForEvaluationRepository.save(requestForEvaluation);

            List<User> users = institution.getUsers().stream().filter(User::getActive).collect(Collectors.toList());
            List<String> emails = new ArrayList<>();

            users.forEach(item -> {
                if (item.getEmail() != null && !item.getEmail().equals("")) {
                    emails.add(item.getEmail());
                }
            });

            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("body", body);

            try {
                emailRepository.sendHtmlMail(emails, title, "Evaluation.html", map, from);
            } catch (Exception e) {
                throw new EmailSendingException();
            }

        });
    }

    @Override
    public void sendRequestsForEvaluationTags(List<Long> tagsIds, String title, String body) {
        String from = environment.getProperty("spring.mail.username");

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        List<Tag> tags = tagRepository.findAllById(tagsIds);
        List<Institution> institutions = new ArrayList<>();

        tags.forEach((tag) -> {
            institutions.addAll(institutionRepository.findAllByTags(tag).stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList()));
        });

        institutions.forEach(institution -> {
            RequestForEvaluation requestForEvaluation = new RequestForEvaluation();
            requestForEvaluation.setSender(user);
            requestForEvaluation.setReceiver(institution);
            requestForEvaluation.setDateCreated(LocalDateTime.now());
            requestForEvaluation.setTitle(title);
            requestForEvaluation.setBody(body);
            this.requestForEvaluationRepository.save(requestForEvaluation);

            List<User> users = institution.getUsers().stream().filter(User::getActive).collect(Collectors.toList());
            List<String> emails = new ArrayList<>();

            users.forEach(item -> {
                if (item.getEmail() != null && !item.getEmail().equals("")) {
                    emails.add(item.getEmail());
                }
            });

            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("body", body);

            try {
                emailRepository.sendHtmlMail(emails, title, "Evaluation.html", map, from);
            } catch (Exception e) {
                throw new EmailSendingException();
            }
        });
    }

    @Override
    public void notifyDirectors(List<String> emails, String message) {
        String from = environment.getProperty("spring.mail.username");
        Map<String, String> map = new HashMap<>();
        map.put("message", message);

        try {
            emailRepository.sendHtmlMail(emails, "Notification", "Notification.html", map, from);
        } catch (Exception e){
            throw new EmailSendingException();
        }
    }

}

