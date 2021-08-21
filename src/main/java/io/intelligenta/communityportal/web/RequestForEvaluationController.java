package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.RequestForEvaluation;
import io.intelligenta.communityportal.service.RequestForEvaluationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/sendRequest")
public class RequestForEvaluationController {

    private final RequestForEvaluationService requestForEvaluationService;

    public RequestForEvaluationController(RequestForEvaluationService requestForEvaluationService) {
        this.requestForEvaluationService = requestForEvaluationService;
    }

    @GetMapping("/paged")
    public Page<RequestForEvaluation> findAllPaged(Pageable pageable) {
        return requestForEvaluationService.findAllPaged(pageable);
    }

    @PostMapping("/institutions/{instIds}")
    public void sendRequestForEvaluation(@PathVariable("instIds") List<Long> instIds, @RequestParam String title, @RequestParam String body) {
        requestForEvaluationService.sendRequestsForEvaluation(instIds, title, body);
    }

    @PostMapping("/tags/{tagsIds}")
    public void sendRequestForEvaluationTags(@PathVariable("tagsIds") List<Long> tagsIds, @RequestParam String title, @RequestParam String body) {
        requestForEvaluationService.sendRequestsForEvaluationTags(tagsIds, title, body);
    }

    @PostMapping("/notify/{emails}")
    public void notifyDirectors(@PathVariable("emails") List<String> emails, @RequestParam String message){
        requestForEvaluationService.notifyDirectors(emails, message);
    }

}

