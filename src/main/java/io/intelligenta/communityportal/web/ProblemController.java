package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Problem;
import io.intelligenta.communityportal.models.dto.ProblemDto;
import io.intelligenta.communityportal.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/problem")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController (ProblemService problemService){
        this.problemService = problemService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PostMapping("/create")
    public Problem createProblem (@RequestBody ProblemDto problem){
        return problemService.createProblem(problem);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Problem getById (@PathVariable(value = "id") Long id){
        return problemService.findById(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/all")
    public Page<Problem> getAll (Pageable pageable){
        return problemService.findAllPaged(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-list")
    public List<Problem> getAllList (){
        return problemService.findAllList();
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/update")
    public Problem updateProblem (@RequestBody ProblemDto problem)  {
        return problemService.updateProblem(problem);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/delete/{id}")
    public Problem deleteProblem (@PathVariable(value = "id") Long id){
        return problemService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/undelete/{id}")
    public Problem undeleteProblem (@PathVariable(value = "id") Long id){
        return problemService.setActive(id);
    }
}
