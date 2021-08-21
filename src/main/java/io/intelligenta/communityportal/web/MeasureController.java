package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Measure;
import io.intelligenta.communityportal.models.dto.MeasureDto;
import io.intelligenta.communityportal.service.MeasureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/measure")
public class MeasureController extends CrudResource<Measure, MeasureService> {

    private final MeasureService measureService;

    public MeasureController (MeasureService measureService){
        this.measureService = measureService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create" /*, consumes = "multipart/form-data"*/ )
    public Measure createMeasure (@RequestBody MeasureDto measureDto){
        return measureService.createMeasure(measureDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Measure findMeasureById(@PathVariable (value = "id") Long id){
        return measureService.findMeasureById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public Page<Measure> findAllMeasures (Pageable pageable){
        return measureService.findAllMeasures(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-list")
    public List<Measure> findAllMeasuresList (){
        return measureService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-problem/{id}")
    public List<Measure> findAllMeasuresByProblemId (@PathVariable Long id){
        return measureService.findAllMeasuresByProblemId(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PutMapping("/update" /*, consumes = "multipart/form-data"*/)
    public Measure updateMeasure (@RequestBody MeasureDto measureDto) throws IOException{
        return measureService.updateMeasure(measureDto);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public Measure deleteMeasure (@PathVariable (value = "id")Long id){
        return measureService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/undelete/{id}")
    public Measure unDeleteMeasure (@PathVariable(value = "id")Long id){
        return measureService.setActive(id);
    }


/*    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-strategy-goal/{id}")
    public List<Measure> findAllMeasuresByStrategyGoalId(@PathVariable Long id){
        return measureService.findAllMeasuresByStrategyGoalId(id);
    }*/

    @Override
    public MeasureService getService() {
        return measureService;
    }

    @Override
    public Measure beforeUpdate(Measure oldEntity, Measure newEntity) {
        return oldEntity;
    }
}