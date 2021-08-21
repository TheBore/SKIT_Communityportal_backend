package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.PublicDocumentForYear;
import io.intelligenta.communityportal.models.PublicDocumentType;
import io.intelligenta.communityportal.models.dto.MonitoringDto;
import io.intelligenta.communityportal.models.exceptions.InstitutionNotFoundException;
import io.intelligenta.communityportal.models.exceptions.PublicDocumentTypeNotFoundException;
import io.intelligenta.communityportal.repository.AttachmentRepository;
import io.intelligenta.communityportal.repository.InstitutionRepository;
import io.intelligenta.communityportal.repository.PublicDocumentForYearRepository;
import io.intelligenta.communityportal.repository.PublicDocumentTypeRepository;
import io.intelligenta.communityportal.service.PublicDocumentForYearService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class PublicDocumentForYearServiceImpl implements PublicDocumentForYearService {

    PublicDocumentForYearRepository publicDocumentForYearRepository;
    InstitutionRepository institutionRepository;
    PublicDocumentTypeRepository publicDocumentTypeRepository;
    AttachmentRepository attachmentRepository;

    PublicDocumentForYearServiceImpl(PublicDocumentForYearRepository publicDocumentForYearRepository,
                                     InstitutionRepository institutionRepository,
                                     AttachmentRepository attachmentRepository,
                                     PublicDocumentTypeRepository publicDocumentTypeRepository) {
        this.publicDocumentForYearRepository = publicDocumentForYearRepository;
        this.institutionRepository = institutionRepository;
        this.publicDocumentTypeRepository = publicDocumentTypeRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    @Transactional
    public void savePublicDocForYear(Integer year, Long type, String url, Long institutionId, byte[] content, String name, String fileContentType, Long size) {
        PublicDocumentForYear doc = publicDocumentForYearRepository.findByYearAndTypeId(year, type).orElse(null);
        if (doc == null) {
            Institution institution = institutionRepository.findById(institutionId).orElseThrow(InstitutionNotFoundException::new);
            PublicDocumentType type1 = publicDocumentTypeRepository.findById(type).orElseThrow(PublicDocumentTypeNotFoundException::new);
            PublicDocumentForYear pdy = new PublicDocumentForYear();
            pdy.setYear(year);
            pdy.setType(type1);
            if (url != null)
                pdy.setUrl(url);
            pdy.setInstitution(institution);
            if (content != null && fileContentType != "" && size != 0 && name != "") {
                Attachment attachment = new Attachment();
                attachment.setContent(content);
                attachment.setMimeType(fileContentType);
                attachment.setName(name);
                attachment.setSize(size);
                attachmentRepository.save(attachment);
                pdy.setAttachment(attachment);
            }
            publicDocumentForYearRepository.save(pdy);
        } else {
            Institution institution = institutionRepository.findById(institutionId).orElseThrow(InstitutionNotFoundException::new);
            PublicDocumentType type1 = publicDocumentTypeRepository.findById(type).orElseThrow(PublicDocumentTypeNotFoundException::new);
            doc.setYear(year);
            doc.setType(type1);
            if (url != null)
                doc.setUrl(url);
            doc.setInstitution(institution);
            if (content != null && fileContentType != "" && size != 0 && name != "") {
                if (doc.getAttachment() != null)
                    attachmentRepository.delete(doc.getAttachment());
                Attachment attachment = new Attachment();
                attachment.setContent(content);
                attachment.setMimeType(fileContentType);
                attachment.setName(name);
                attachment.setSize(size);
                attachmentRepository.save(attachment);
                doc.setAttachment(attachment);
            }
            publicDocumentForYearRepository.save(doc);
        }
    }

    @Override
    public List<PublicDocumentForYear> getUrlForYear(Integer year, Long institutionId) {
        return publicDocumentForYearRepository.findByYearAndInstitutionId(year, institutionId);
    }

    @Override
    public List<Integer> findAllYears() {
        return publicDocumentForYearRepository.findAllYears();
    }

    @Override
    @Transactional
    public List<MonitoringDto> monitoring() {
        List<MonitoringDto> dtos = new ArrayList<>();
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 3; i++) {
            dtos.add(populateDto(year - i));
        }
        return dtos;
    }

    @Override
    @Transactional
    public PublicDocumentForYear getPubDocByTypeInstYear(Integer year, Long institutionId, Long typeId) {
        return publicDocumentForYearRepository.findByYearAndInstitutionIdAndTypeId(year, institutionId, typeId);
    }

    @Transactional
    public MonitoringDto populateDto(Integer year) {
        List<PublicDocumentForYear> list = publicDocumentForYearRepository.findByYear(year);
        MonitoringDto dto = new MonitoringDto();
        dto.setYear(year);
        HashMap<String, Integer> map = new HashMap<>();
        Integer i = 0;
        Integer j = 0;
        if (list.size() > 0) {
            for (PublicDocumentForYear item : list) {
                if (item.getUrl() != null || item.getAttachment() != null) {
                    map.put("yes", ++i);
                    map.put("no", j);
                } else {
                    map.put("yes", i);
                    map.put("no", ++j);
                }
            }
        }
        dto.setCount(map);

        return dto;

    }
}
    
    
    