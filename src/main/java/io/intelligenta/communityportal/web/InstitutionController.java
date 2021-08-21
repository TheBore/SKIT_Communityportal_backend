package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.dto.InstitutionPage;
import io.intelligenta.communityportal.models.dto.InstitutionWithModerators;
import io.intelligenta.communityportal.service.InstitutionService;
import io.intelligenta.communityportal.service.TagService;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("/rest/institution")
public class InstitutionController extends CrudResource<Institution, InstitutionService> {
    private InstitutionService institutionService;
    private TagService tagService;

    public InstitutionController(InstitutionService institutionService, TagService tagService) {
        this.institutionService = institutionService;
        this.tagService = tagService;
    }


    @GetMapping("/all")
    public List<Institution> getListOfAllInstitutions() {
        return institutionService.findAll();
    }

    @GetMapping("/allEdited")
    public List<Institution> getListOfAllEditedInstitutions() {
        return institutionService.findAll().stream().filter(institution -> !institution.isEdited()).collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Institution findInstitutionById(@PathVariable(value = "id") Long Id) {
        return institutionService.findById(Id);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/paged", method = RequestMethod.GET, produces = "application/json")
    public Page<InstitutionPage> getAllSluzbeno(@RequestParam("keyword") String keyword, Pageable pageable) {
        return getService().findAllWithModerator(keyword, pageable);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/paged1", method = RequestMethod.GET, produces = "application/json")
    public Page<Institution> getAll(@RequestParam int page, @RequestParam int pageSize, HttpServletRequest request) throws JSONException {
        return super.getAll(page, pageSize, request);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public InstitutionPage create(@RequestBody @Valid InstitutionPage entity, HttpServletRequest request, HttpServletResponse response) {
        return institutionService.createInstitution(entity);
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public InstitutionPage update(@RequestBody InstitutionPage obj) {
        return institutionService.updateInstitution(obj);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteInst/{id}")
    public void deleteInstitution(@PathVariable(value = "id") Long Id) {
        institutionService.deleteInstitutionById(Id);
    }


    @GetMapping("/all/moderators")
    public List<InstitutionWithModerators> getAllWithModerators() {
        return this.getService().extractInstitutionsWithModerators();
    }

    @Override
    public InstitutionService getService() {
        return institutionService;
    }

    @Override
    public Institution beforeUpdate(Institution oldEntity, Institution newEntity) {
        return oldEntity;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/selectedTags/{id}")
    public List<Tag> selectedTags(@PathVariable("id") Long id) {
        Institution institution = institutionService.findById(id);
        return institution.getTags();
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PatchMapping("/deleteInst/{id}")
    public Institution setInactive(@PathVariable(value = "id") Long Id) {
        return institutionService.setInactive(Id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/selectedInstitution")
    public Institution getInstitutionByUserEmail() {
        return institutionService.findInstitutionByUserEmail();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sendSelected/{institutionId}/{categoryId}")
    public Institution sendSelectedInstitution(@PathVariable Long institutionId, @RequestBody InstitutionPage institution, @PathVariable Long categoryId) {
        return institutionService.sendSelectedInstitution(institutionId, institution, categoryId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sendSelectedWithParent/{institutionId}/{parentInstitutionId}")
    public Institution sendSelectedInstitutionWithParent(@PathVariable Long institutionId, @RequestBody InstitutionPage institution, @PathVariable Long parentInstitutionId) {
        return institutionService.sendSelectedInstitutionWithParent(institutionId, institution, parentInstitutionId);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllEdited")
    public Page<InstitutionPage> getAllEdited(Pageable pageable) {
        return institutionService.findAllEdited(pageable);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/recreateInstitution")
    public Institution recreateInstitution(@RequestBody InstitutionPage institution) {
        return institutionService.recreateInstitution(institution);
    }

    @GetMapping("/allActiveInstitutions")
    public List<Institution> allActiveInstitutions(){
        return institutionService.findAll().stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList());
    }

    @GetMapping("/allByParentRecursively/{ids}")
    public List<Institution> allByParentRecursively(@PathVariable List<Long> ids){
        return institutionService.allByParentRecursively(ids);
    }

    @GetMapping("/allByTags/{ids}")
    public List<Institution> allByTags(@PathVariable List<Long> ids){
        return institutionService.allByTags(ids);
    }

    @GetMapping("/allByCategoriesRecursively/{ids}")
    public List<Institution> allByCategoriesRecursively(@PathVariable List<Long> ids){
        return institutionService.allByCategoriesRecursively(ids);
    }

    @GetMapping("/getDirectorEmails/{institutionId}")
    public List<String> getDirectorEmailsForInstitution(@PathVariable Long institutionId){
        return institutionService.getDirectorEmailsForInstitution(institutionId);
    }

}
