package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.ProblemDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.NAPAreaRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.ProblemRepository;
import io.intelligenta.communityportal.repository.StrategyGoalRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final NAPAreaRepository napAreaRepository;
    private final NAPRepository napRepository;
    private final StrategyGoalRepository strategyGoalRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository, UserRepository userRepository, NAPAreaRepository napAreaRepository, NAPRepository napRepository, StrategyGoalRepository strategyGoalRepository){
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.napAreaRepository = napAreaRepository;
        this.napRepository = napRepository;
        this.strategyGoalRepository = strategyGoalRepository;
    }

    @Override
    public Problem createProblem(ProblemDto problem) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Problem newProblem = new Problem();

        List<StrategyGoal> strategyGoalList = new ArrayList<>();
        for( int i = 0; i < problem.getStrategyGoals().size(); i++){
            StrategyGoal includedInstitution = strategyGoalRepository.
                    findById(problem.getStrategyGoals().get(i)).orElseThrow(InstitutionNotFoundException::new);
            strategyGoalList.add(includedInstitution);
        }
        newProblem.setStrategyGoals(strategyGoalList);

        newProblem.setNameMk(problem.getNameMk());
        newProblem.setNameAl(problem.getNameAl());
        newProblem.setNameEn(problem.getNameEn());

        newProblem.setDescriptionMk(problem.getDescriptionMk());
        newProblem.setDescriptionAl(problem.getDescriptionAl());
        newProblem.setDescriptionEn(problem.getDescriptionEn());

        NAP nap = napRepository.findById(problem.getNap()).orElseThrow(NAPNotFoundException::new);

        newProblem.setNap(nap);

        NAPArea napArea = napAreaRepository.findById(problem.getNapArea()).orElseThrow(NAPAreaNotFoundException::new);

        newProblem.setNapArea(napArea);

        newProblem.setDateCreated(LocalDateTime.now());
        newProblem.setCreatedByUser(user);
        newProblem.setActive(true);

        return problemRepository.save(newProblem);
    }

    @Override
    public Problem findById(Long id) {
        return problemRepository.findById(id).orElseThrow(ProblemNotFoundException::new);
    }

    @Override
    public Page<Problem> findAllPaged(Pageable pageable) {
        return problemRepository.findAll(pageable);
    }

    @Override
    public List<Problem> findAllList() {
        return problemRepository.findAll();
    }

    @Override
    public Problem updateProblem(ProblemDto problem) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Problem updatedProblem = problemRepository.findById(problem.getId()).orElseThrow(ProblemNotFoundException::new);

        List<StrategyGoal> strategyGoalList = new ArrayList<>();
        if(problem.getStrategyGoals().size() != 0){
            for( int i = 0; i < problem.getStrategyGoals().size(); i++){
                StrategyGoal includedInstitution = strategyGoalRepository.
                        findById(problem.getStrategyGoals().get(i)).orElseThrow(InstitutionNotFoundException::new);
                strategyGoalList.add(includedInstitution);
            }
            updatedProblem.setStrategyGoals(strategyGoalList);
        }

        updatedProblem.setUpdatedByUser(user);
        updatedProblem.setDateUpdated(LocalDateTime.now());

        updatedProblem.setNameMk(problem.getNameMk());
        updatedProblem.setNameAl(problem.getNameAl());
        updatedProblem.setNameEn(problem.getNameEn());

        updatedProblem.setDescriptionMk(problem.getDescriptionMk());
        updatedProblem.setDescriptionAl(problem.getDescriptionAl());
        updatedProblem.setDescriptionEn(problem.getDescriptionEn());

        return problemRepository.save(updatedProblem);
    }

    @Override
    public Problem setInactive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Problem problem = problemRepository.findById(id).orElseThrow(ProblemNotFoundException::new);
        problem.setDateUpdated(LocalDateTime.now());
        problem.setDeletedByUser(user);
        problem.setNapArea(null);
        problem.setActive(false);

        return problemRepository.save(problem);
    }

    @Override
    public Problem setActive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Problem problem = problemRepository.findById(id).orElseThrow(ProblemNotFoundException::new);
        problem.setDateUpdated(LocalDateTime.now());
        problem.setUpdatedByUser(user);
        problem.setActive(true);

        return problemRepository.save(problem);
    }

    @Override
    public List<Problem> findAllByNapAreaId(Long id) {
        NAPArea napArea = napAreaRepository.findById(id).orElseThrow(NAPAreaNotFoundException::new);

        return problemRepository.findAllByNapArea(napArea);
    }
}
