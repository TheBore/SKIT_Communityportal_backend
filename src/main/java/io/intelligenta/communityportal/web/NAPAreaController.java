package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.dto.NapAreaDto;
import io.intelligenta.communityportal.service.NAPAreaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/nap-area")
public class NAPAreaController {

    private final NAPAreaService napAreaService;

    public NAPAreaController(NAPAreaService napAreaService){
        this.napAreaService = napAreaService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PostMapping("/create")
    public NAPArea createNapArea (@RequestBody NapAreaDto napArea){
        return napAreaService.createNAPArea(napArea);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public NAPArea findById (@PathVariable Long id){
        return napAreaService.findById(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/all")
    public Page<NAPArea> findAllPaged (@RequestParam("keyword") String keyword, Pageable pageable){
        return napAreaService.findAllPaged(keyword, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allActive")
    public List<NAPArea> findAllActive () { return napAreaService.findAllList(); }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/update")
    public NAPArea updateNAPArea (@RequestBody NapAreaDto napArea){
        return napAreaService.updateNAPArea(napArea);
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/delete/{id}")
    public NAPArea deleteNAPArea (@PathVariable Long id ){
        return napAreaService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/undelete/{id}")
    public NAPArea undeleteNAPArea (@PathVariable Long id){
        return napAreaService.setActive(id);
    }

}
