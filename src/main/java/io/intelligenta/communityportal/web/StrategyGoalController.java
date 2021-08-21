package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.StrategyGoal;
import io.intelligenta.communityportal.service.StrategyGoalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/strategy-goal")
public class StrategyGoalController {

    private final StrategyGoalService strategyGoalService;

    public StrategyGoalController (StrategyGoalService strategyGoalService){
        this.strategyGoalService = strategyGoalService;
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PostMapping("/create")
    public StrategyGoal createStrategyGoal (@RequestBody StrategyGoal strategyGoal){
        return strategyGoalService.createStrategyGoal(strategyGoal);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/{id}")
    public StrategyGoal getStrategyGoalById (@PathVariable Long id){
        return strategyGoalService.findById(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/all")
    public Page<StrategyGoal> getAllStrategyGoals (@RequestParam("keyword") String keyword, Pageable pageable){
        return strategyGoalService.findAllPaged(keyword, pageable);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/update")
    public StrategyGoal updateStrategyGoal (@RequestBody StrategyGoal strategyGoal){
        return strategyGoalService.updateStrategyGoal(strategyGoal);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/delete/{id}")
    public StrategyGoal deleteStrategyGoal (@PathVariable Long id){
        return strategyGoalService.setInactive(id);
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/undelete/{id}")
    public StrategyGoal undeleteStrategyGoal (@PathVariable Long id){
        return strategyGoalService.setActive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-listed")
    public List<StrategyGoal> findAllActive() {
        return strategyGoalService.findAllActive();
    }

}
