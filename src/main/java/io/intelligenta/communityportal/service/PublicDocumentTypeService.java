package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.PublicDocumentType;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface PublicDocumentTypeService {

    List<PublicDocumentType> findAll();

    PublicDocumentType addPublicDocType(String name);

    PublicDocumentType editPublicDocType(PublicDocumentType pubdoctype);

}
