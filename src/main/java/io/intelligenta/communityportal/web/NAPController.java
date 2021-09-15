package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.NAP;
import io.intelligenta.communityportal.models.dto.NAPDto;
import io.intelligenta.communityportal.service.NAPService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/nap")
public class NAPController extends CrudResource<NAP, NAPService> {

    private final NAPService napService;

    public NAPController (NAPService napService){
        this.napService = napService;
    }

//    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR'))")
    @PostMapping("/create")
    public NAP createNAP (@RequestBody NAPDto nap){
        return napService.createNAP(nap);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public NAP findById (@PathVariable(value = "id") Long id){//
        return napService.findById(id);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public Page<NAP> findAll (Pageable pageable){
        return napService.findAllByDateCreatedDesc(pageable);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-list")
    public List<NAP> findAllList() {
        return napService.findAllByDateCreatedDescAndActiveList(); }//

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-recent")
    public List<NAP> findAllUnpaged() {
        return napService.findAllByDateCreatedDescAndActiveList().stream().limit(2).collect(Collectors.toList()); //
    }

//    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR'))")
    @PutMapping("/update/{id}")
    public NAP updateNAP (@RequestBody NAPDto nap, @PathVariable Long id){
        return napService.updateNAP(nap, id);//
    }

//    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR'))")
    @PostMapping("/delete/{id}")
    public NAP deleteNAP (@PathVariable (value = "id")Long id){
        return napService.setInactive(id);//
    }

//    @PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR'))")
    @PostMapping("/unDelete/{id}")
    public NAP unDeleteNAP (@PathVariable(value = "id")Long id){
        return napService.setActive(id);
    }


//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PutMapping("/evaluation-status/{id}/{checked}")
    public void changeEvaluationStatus (@PathVariable Long id, @PathVariable String checked){
        napService.changeEvaluationStatus(checked.equals("true"), id);
    }

    @Override
    public NAPService getService() {
        return napService;
    }

    @Override
    public NAP beforeUpdate(NAP oldEntity, NAP newEntity) {
        return oldEntity;
    }
}