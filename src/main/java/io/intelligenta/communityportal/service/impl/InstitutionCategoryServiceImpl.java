package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.dto.InstitutionCategoryDto;
import io.intelligenta.communityportal.models.dto.InstitutionPage;
import io.intelligenta.communityportal.models.exceptions.InstitutionCategoryCannotBeDeletedException;
import io.intelligenta.communityportal.models.exceptions.InstitutionCategoryHeadingNotFoundException;
import io.intelligenta.communityportal.models.exceptions.InstitutionCategoryNotFoundException;
import io.intelligenta.communityportal.models.exceptions.InstitutionCategoryParentChildException;
import io.intelligenta.communityportal.repository.InstitutionCategoryRepository;
import io.intelligenta.communityportal.service.InstitutionCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstitutionCategoryServiceImpl extends BaseEntityCrudServiceImpl<InstitutionCategory, InstitutionCategoryRepository> implements InstitutionCategoryService {

    InstitutionCategoryRepository institutionCategoryRepository;

    public InstitutionCategoryServiceImpl(InstitutionCategoryRepository institutionCategoryRepository) {
        this.institutionCategoryRepository = institutionCategoryRepository;

    }

    @Override
    public InstitutionCategory findInstitutionCategoryById(Long Id) {
        return institutionCategoryRepository.findById(Id).orElseThrow(InstitutionCategoryNotFoundException::new);
    }

    @Override
    public InstitutionCategory addInstitutionCategory(InstitutionCategoryDto institutionCategory) {
        InstitutionCategory newInstCat = new InstitutionCategory();
        if (institutionCategory.getParentCategory() != null) {
            InstitutionCategory cat = institutionCategoryRepository.findById(institutionCategory.getParentCategory()).orElseThrow(InstitutionCategoryNotFoundException::new);
            newInstCat.setParentCategory(cat);
        }
        newInstCat.setNameMk(institutionCategory.getNameMk());
        newInstCat.setNameAl(institutionCategory.getNameAl());
        newInstCat.setNameEn(institutionCategory.getNameEn());
        newInstCat.setDateCreated(LocalDateTime.now());
        newInstCat.setDateUpdated(LocalDateTime.now());
        institutionCategoryRepository.save(newInstCat);
        return newInstCat;
    }


    @Override
    public InstitutionCategory updateInstitutionCategory(InstitutionCategoryDto institutionCategory) {
        InstitutionCategory updateInstitutionCategory = institutionCategoryRepository.findById(institutionCategory.getId()).orElseThrow(InstitutionCategoryNotFoundException::new);

        if(institutionCategory.getParentCategory() != null){
            InstitutionCategory parentCategory = institutionCategoryRepository.findById(institutionCategory.getParentCategory()).orElse(null);
            if (parentCategory.equals(updateInstitutionCategory)) {
                throw new InstitutionCategoryParentChildException();
            } else if (parentCategory.getParentCategory() == null && !institutionCategory.equals(updateInstitutionCategory)) {
                updateInstitutionCategory.setParentCategory(parentCategory);
            } else if (!parentCategory.getParentCategory().equals(updateInstitutionCategory)) {
                updateInstitutionCategory.setParentCategory(parentCategory);
            } else if (parentCategory.getParentCategory().equals(updateInstitutionCategory)) {
                throw new InstitutionCategoryParentChildException();
            }
        }
        else{
                updateInstitutionCategory.setParentCategory(null);
        }

        updateInstitutionCategory.setNameMk(institutionCategory.getNameMk());
        updateInstitutionCategory.setNameAl(institutionCategory.getNameAl());
        updateInstitutionCategory.setNameEn(institutionCategory.getNameEn());
        updateInstitutionCategory.setDateUpdated(LocalDateTime.now());
        institutionCategoryRepository.save(updateInstitutionCategory);

        return updateInstitutionCategory;

    }

    @Override
    public List<InstitutionCategory> findAllCat() {
        return institutionCategoryRepository.findAll();
    }

    @Override
    public Page<InstitutionCategory> listAll(String keyword, Pageable pageable) {
        Long size = 0l;
        keyword = keyword.toLowerCase();
        List<InstitutionCategory> institutionCategories = keyword.equals("") ? getRepository().findAll(pageable).getContent() : getRepository().search(keyword, pageable).getContent();

        if (keyword.equals("")) {
            size = (long) getRepository().findAll().size();
        } else {
            size = (long) getRepository().searchSize(keyword).size();
        }
        return new PageImpl<>(institutionCategories, pageable, size);
    }

    @Override
    public void deleteById(Long institutionCategoryId) {

        InstitutionCategory institutionCategory = this.institutionCategoryRepository.findById(institutionCategoryId).orElseThrow(InstitutionCategoryNotFoundException::new);
        List<Institution> institutionList = institutionCategory.getInstitutionList();

        List<InstitutionCategory> childCategories = institutionCategory.getChildCategories();

        if(institutionList.size()==0 && childCategories.size()==0){
            this.institutionCategoryRepository.deleteById(institutionCategoryId);
        }
        else{
            throw new InstitutionCategoryCannotBeDeletedException();
        }

    }

    @Override
    public List<InstitutionCategory> changeCategoriesOrder(List<Long> categoryIds) {

        for(int i = 0; i < categoryIds.size(); i++){
            InstitutionCategory institutionCategory = institutionCategoryRepository.findById(categoryIds.get(i)).orElseThrow(InstitutionCategoryHeadingNotFoundException::new);
            institutionCategory.setOrderId((long) i);
            institutionCategoryRepository.save(institutionCategory);
        }

        return institutionCategoryRepository.findAll();
    }

    @Override
    protected InstitutionCategoryRepository getRepository() {
        return institutionCategoryRepository;
    }
}
