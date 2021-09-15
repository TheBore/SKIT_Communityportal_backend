package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.NAPAreaType;
import io.intelligenta.communityportal.service.NAPAreaTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/nap-area-type")
public class NAPAreaTypeController {

    private final NAPAreaTypeService napAreaTypeService;

    public NAPAreaTypeController (NAPAreaTypeService napAreaTypeService){
        this.napAreaTypeService = napAreaTypeService;
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PostMapping("/create")
    public NAPAreaType createNAPAreaType (@RequestBody NAPAreaType napAreaType){
        return napAreaTypeService.createNAPAreaType(napAreaType);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/{id}")
    public NAPAreaType getById (@PathVariable Long id){
        return napAreaTypeService.findById(id);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/all")
    public Page<NAPAreaType> getAllPaged (@RequestParam("keyword") String keyword, Pageable pageable){
        return napAreaTypeService.findAllPagedWithKeyword(keyword, pageable);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/allActive")
    public List<NAPAreaType> getAllActive(){
        return napAreaTypeService.findAllActive();
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/update")
    public NAPAreaType updateNAPAreaType (@RequestBody NAPAreaType napAreaType){
        return napAreaTypeService.updateNAPAreaType(napAreaType);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/delete/{id}")
    public NAPAreaType deleteNAPAreaType (@PathVariable Long id){
        return napAreaTypeService.setInactive(id);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/undelete/{id}")
    public NAPAreaType undeleteNAPAreaType (@PathVariable Long id){
        return napAreaTypeService.setActive(id);
    }
}
