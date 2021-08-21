package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.dto.InstitutionCategoryDto;
import io.intelligenta.communityportal.repository.InstitutionCategoryRepository;
import io.intelligenta.communityportal.service.InstitutionCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/institutionCategory")
public class InstitutionCategoryController {
    InstitutionCategoryService institutionCategoryService;
    InstitutionCategoryRepository institutionCategoryRepository;

    public InstitutionCategoryController (InstitutionCategoryService institutionCategoryService,
                                          InstitutionCategoryRepository institutionCategoryRepository){
        this.institutionCategoryService = institutionCategoryService;
        this.institutionCategoryRepository = institutionCategoryRepository;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/{institutionCategoryId}")
    InstitutionCategory getInstitutionCategoryById (@PathVariable ("institutionCategoryId") Long institutionCategoryId){
        return institutionCategoryService.findInstitutionCategoryById(institutionCategoryId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/all")
    List<InstitutionCategory> getListOfCategories(){
        return institutionCategoryService.findAllCat();
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public InstitutionCategory addInstitutionCategory (@RequestBody InstitutionCategoryDto institutionCategory){
        return  institutionCategoryService.addInstitutionCategory(institutionCategory);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PatchMapping("/update")
    public InstitutionCategory updateInstitutionCategory (@RequestBody InstitutionCategoryDto institutionCategory){
        return institutionCategoryService.updateInstitutionCategory(institutionCategory);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/paged")
    public Page <InstitutionCategory> listCategories (@RequestParam("keyword") String keyword, @PageableDefault Pageable pageable){
        return institutionCategoryService.listAll(keyword, pageable);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteCategory/{institutionCategoryId}")
    public void deleteInstitutionCategory(@PathVariable ("institutionCategoryId") Long institutionCategoryId){
        this.institutionCategoryService.deleteById(institutionCategoryId);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PutMapping("/changeCategoriesOrder/{categoryIds}")
    public List<InstitutionCategory> changeCategoriesOrder(@PathVariable List<Long> categoryIds){
        return this.institutionCategoryService.changeCategoriesOrder(categoryIds);
    }

}
