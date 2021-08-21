package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Status;
import io.intelligenta.communityportal.models.StatusType;
import io.intelligenta.communityportal.service.StatusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController (StatusService statusService){
        this.statusService = statusService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public Status createStatus (@RequestBody Status status){
        return statusService.createStatus(status);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Status findStatusById (@PathVariable Long id){
        return statusService.findStatusById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<Status> findAllWithKeyword (@RequestParam("keyword") String keyword){
        return statusService.findAllWithKeyword(keyword);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public Status updateStatus (@RequestBody Status status){
        return statusService.updateStatus(status);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public void deleteStatusById (@PathVariable Long id){
        statusService.deleteStatusById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getStatusesByType")
    public List<Status> getAllByType(@RequestParam String statusType){
        return statusService.findAll().stream().filter(status -> status.getStatusType().toString().equals(statusType)).collect(Collectors.toList());
    }
}
