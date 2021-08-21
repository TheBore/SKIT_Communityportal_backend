package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.PublicDocumentType;
import io.intelligenta.communityportal.service.PublicDocumentTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("publicdocument/type")
@CrossOrigin("*")
public class PublicDocumentTypeController {

    PublicDocumentTypeService publicDocumentTypeService;

    PublicDocumentTypeController(PublicDocumentTypeService publicDocumentTypeService) {
        this.publicDocumentTypeService = publicDocumentTypeService;
    }

    @GetMapping("/all")
    List<PublicDocumentType> getAllTypes() {
        return publicDocumentTypeService.findAll();
    }

    @PostMapping("/add")
    PublicDocumentType addPublicDocType(@RequestParam("name") String name) {
        return publicDocumentTypeService.addPublicDocType(name);
    }

    @PatchMapping("/edit")
    PublicDocumentType editPublicDocType(@RequestBody PublicDocumentType type) {
        return publicDocumentTypeService.editPublicDocType(type);
    }

}
