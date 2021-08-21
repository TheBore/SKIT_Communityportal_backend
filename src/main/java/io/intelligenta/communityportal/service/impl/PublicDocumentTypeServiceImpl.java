package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.PublicDocumentType;
import io.intelligenta.communityportal.models.exceptions.PublicDocumentTypeNotFoundException;
import io.intelligenta.communityportal.repository.PublicDocumentTypeRepository;
import io.intelligenta.communityportal.service.PublicDocumentTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class PublicDocumentTypeServiceImpl implements PublicDocumentTypeService {

    PublicDocumentTypeRepository publicDocumentTypeRepository;

    PublicDocumentTypeServiceImpl(PublicDocumentTypeRepository publicDocumentTypeRepository) {
        this.publicDocumentTypeRepository = publicDocumentTypeRepository;
    }

    @Override
    public List<PublicDocumentType> findAll() {
        return publicDocumentTypeRepository.findAllByOrderByIdDesc();
    }

    @Override
    public PublicDocumentType addPublicDocType(String name) {
        PublicDocumentType pdt = new PublicDocumentType();
        pdt.setName(name);
        publicDocumentTypeRepository.save(pdt);
        return pdt;
    }

    @Override
    public PublicDocumentType editPublicDocType(PublicDocumentType pubdoctype) {
        PublicDocumentType pdt = publicDocumentTypeRepository.findById(pubdoctype.getId()).orElseThrow(PublicDocumentTypeNotFoundException::new);
        pdt.setName(pubdoctype.getName());
        publicDocumentTypeRepository.save(pdt);
        return pdt;
    }

}
