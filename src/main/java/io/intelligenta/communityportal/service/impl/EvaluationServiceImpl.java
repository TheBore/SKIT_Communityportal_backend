package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.dto.EvaluationDto;
import io.intelligenta.communityportal.models.exceptions.EmailSendingException;
import io.intelligenta.communityportal.models.exceptions.EvaluationNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NAPNotFoundException;
import io.intelligenta.communityportal.repository.ActivityRepository;
import io.intelligenta.communityportal.repository.EvaluationRepository;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.EvaluationService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImpl extends BaseEntityCrudServiceImpl<Evaluation, EvaluationRepository> implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final NAPRepository napRepository;
    private final ActivityRepository activityRepository;
    private final EmailRepository emailRepository;
    private final Environment environment;

    public EvaluationServiceImpl(EvaluationRepository evaluationRepository, NAPRepository napRepository, ActivityRepository activityRepository, EmailRepository emailRepository, Environment environment) {
        this.evaluationRepository = evaluationRepository;
        this.napRepository = napRepository;
        this.activityRepository = activityRepository;
        this.emailRepository = emailRepository;
        this.environment = environment;
    }

    @Override
    protected EvaluationRepository getRepository() {
        return evaluationRepository;
    }

    @Override
    public Evaluation createEvaluation(EvaluationDto evaluation) {
        String from = environment.getProperty("spring.mail.username");

        Evaluation newEvaluation = new Evaluation();

        newEvaluation.setDateCreated(LocalDateTime.now());

        newEvaluation.setDescriptionMk(evaluation.getDescriptionMk());
        newEvaluation.setDescriptionAl(evaluation.getDescriptionAl());
        newEvaluation.setDescriptionEn(evaluation.getDescriptionEn());

        newEvaluation.setOpen(true);

        NAP nap = napRepository.findById(evaluation.getNap()).orElseThrow(NAPNotFoundException::new);
        nap.setOpenForEvaluation(true);

        newEvaluation.setNap(nap);

        List<Activity> activities = activityRepository.
                findAll().
                stream().
                filter(activity -> activity.getStatus().getId().equals(Integer.toUnsignedLong(34))).
                collect(Collectors.toList());

        HashSet<Institution> institutions = new HashSet<>();

        for (Activity a: activities) {
            if(a.getCompetentInstitution() != null && a.getCompetentInstitution().getInstitution() != null)
                institutions.add(a.getCompetentInstitution().getInstitution());
        }

        institutions.forEach(institution -> {

            List<User> users = institution.getUsers();
            List<String> emails = new ArrayList<>();

            users.forEach(user -> {
                if(user.getEmail() != null && !user.getEmail().equals("") && user.getActive() && user.getRole().equals(UserRole.ROLE_EVALUATOR)){
                    emails.add(user.getEmail());
                }
            });

            Map<String, String> map = new HashMap<>();
            map.put("name", newEvaluation.getDescriptionMk());

            try {
                emailRepository.sendHtmlMail(emails, "Нова отворена евалуација", "OpenEvaluation.html", map, from);
            }
            catch (Exception e){
                throw new EmailSendingException();
            }
        });

        napRepository.save(nap);
        evaluationRepository.save(newEvaluation);

        return newEvaluation;
    }

    @Override
    public Evaluation updateEvaluation(Long id, EvaluationDto evaluation) {
        Evaluation updatedEvaluation = evaluationRepository.findById(id).orElseThrow(EvaluationNotFoundException::new);

        updatedEvaluation.setDescriptionMk(evaluation.getDescriptionMk());
        updatedEvaluation.setDescriptionAl(evaluation.getDescriptionAl());
        updatedEvaluation.setDescriptionEn(evaluation.getDescriptionEn());

        evaluationRepository.save(updatedEvaluation);

        return updatedEvaluation;
    }

    @Override
    public Evaluation getEvaluationByNapId(Long napId) {
        NAP nap = napRepository.findById(napId).orElseThrow(NAPNotFoundException::new);

        return evaluationRepository.findByNapAndOpen(nap, true);
    }

    @Override
    public List<Evaluation> getAllEvaluations(Long napId) {
        NAP nap = napRepository.findById(napId).orElseThrow(NAPNotFoundException::new);
        return evaluationRepository.findAllByNap(nap);
    }

    @Override
    public void openOldEvaluation(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(EvaluationNotFoundException::new);
        evaluation.setOpen(true);
        evaluationRepository.save(evaluation);

        NAP nap = napRepository.findById(evaluation.getNap().getId()).orElseThrow(NAPNotFoundException::new);
        nap.setOpenForEvaluation(true);
        napRepository.save(nap);
    }
}
