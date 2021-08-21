package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.dto.TagWithInstitutionDto;
import io.intelligenta.communityportal.service.TagService;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest/tag")
public class TagController extends CrudResource<Tag, TagService> {

    private TagService tagService;


    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public Tag create(@RequestBody @Valid Tag entity, HttpServletRequest request, HttpServletResponse response) {
        return tagService.createTag(entity);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Tag findTagById(@PathVariable(value = "id") Long Id) {
        return tagService.findById(Id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<Tag> getAllTags() {
        return tagService.findAllTags();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/withinstitutions")
    public List<TagWithInstitutionDto> getAllTagsWithInstituions() {
        return tagService.getAllTagsWithInstituions();
    }


    @Override
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/paged", method = RequestMethod.GET, produces = "application/json")
    public Page<Tag> getAll(@RequestParam int page, @RequestParam int pageSize, HttpServletRequest request) throws JSONException {
        return super.getAll(page, pageSize, request);
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public Tag update(@RequestBody TagWithInstitutionDto obj) {
        return tagService.updateTag(obj);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteTagById(@PathVariable(value = "id") Long Id) {
        tagService.deleteTagById(Id);
    }

    @Override
    public TagService getService() {
        return tagService;
    }

    @Override
    public Tag beforeUpdate(Tag oldEntity, Tag newEntity) {
        return oldEntity;
    }
}
