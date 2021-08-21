package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.StrategyGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StrategyGoalService {

    StrategyGoal createStrategyGoal (StrategyGoal strategyGoal);

    StrategyGoal findById (Long id);

    Page<StrategyGoal> findAllPaged (String keyword, Pageable pageable);

    StrategyGoal updateStrategyGoal (StrategyGoal strategyGoal);

    StrategyGoal setInactive (Long id);

    StrategyGoal setActive (Long id);

    List<StrategyGoal> findAllActive();


}
