package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.StrategyGoal;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.exceptions.StrategyGoalNotFoundException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.StrategyGoalRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.StrategyGoalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StrategyGoalServiceImpl implements StrategyGoalService {

    private final StrategyGoalRepository strategyGoalRepository;
    private final UserRepository userRepository;

    public StrategyGoalServiceImpl (StrategyGoalRepository strategyGoalRepository, UserRepository userRepository){
        this.strategyGoalRepository = strategyGoalRepository;
        this.userRepository = userRepository;
    }


    @Override
    public StrategyGoal createStrategyGoal(StrategyGoal strategyGoal) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        strategyGoal.setDateCreated(LocalDateTime.now());
        strategyGoal.setCreatedByUser(user);
        strategyGoal.setActive(true);

        return strategyGoalRepository.save(strategyGoal);
    }

    @Override
    public StrategyGoal findById(Long id) {
        return strategyGoalRepository.findById(id).orElseThrow(StrategyGoalNotFoundException::new);
    }

    @Override
    public Page<StrategyGoal> findAllPaged(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase();
        if(!keyword.equals("") && !keyword.equals("null") && !keyword.equals("undefined")){
            return strategyGoalRepository.findAllWithKeyword(keyword, pageable);
        }
        else{
            return strategyGoalRepository.findAllWithoutKeyword(pageable);
        }
    }

    @Override
    public StrategyGoal updateStrategyGoal(StrategyGoal strategyGoal) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        StrategyGoal updatedStrategyGoal = strategyGoalRepository.findById(strategyGoal.getId()).orElseThrow(StrategyGoalNotFoundException::new);

        updatedStrategyGoal.setDateUpdated(LocalDateTime.now());
        updatedStrategyGoal.setUpdatedByUser(user);

        updatedStrategyGoal.setNameMk(strategyGoal.getNameMk());
        updatedStrategyGoal.setNameAl(strategyGoal.getNameAl());
        updatedStrategyGoal.setNameEn(strategyGoal.getNameEn());

        updatedStrategyGoal.setDescriptionMk(strategyGoal.getDescriptionMk());
        updatedStrategyGoal.setDescriptionAl(strategyGoal.getDescriptionAl());
        updatedStrategyGoal.setDescriptionEn(strategyGoal.getDescriptionEn());

        return strategyGoalRepository.save(updatedStrategyGoal);
    }

    @Override
    public StrategyGoal setInactive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        StrategyGoal strategyGoal = strategyGoalRepository.findById(id).orElseThrow(StrategyGoalNotFoundException::new);
        strategyGoal.setDateUpdated(LocalDateTime.now());
        strategyGoal.setDeletedByUser(user);
        strategyGoal.setActive(false);

        return strategyGoalRepository.save(strategyGoal);
    }

    @Override
    public StrategyGoal setActive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        StrategyGoal strategyGoal = strategyGoalRepository.findById(id).orElseThrow(StrategyGoalNotFoundException::new);
        strategyGoal.setUpdatedByUser(user);
        strategyGoal.setDateUpdated(LocalDateTime.now());
        strategyGoal.setActive(true);

        return strategyGoalRepository.save(strategyGoal);
    }

    @Override
    public List<StrategyGoal> findAllActive() {
        return strategyGoalRepository.findAll().stream().filter(StrategyGoal::getActive).collect(Collectors.toList());
    }
}
