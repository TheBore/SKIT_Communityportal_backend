package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.NAPAreaType;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.NapAreaDto;
import io.intelligenta.communityportal.models.exceptions.NAPAreaNotFoundException;

import io.intelligenta.communityportal.models.exceptions.NAPAreaWithoutNAPAreaTypeException;
import io.intelligenta.communityportal.models.exceptions.NAPAreaTypeNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NapAreaCanNotBeDeletedException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.NAPAreaRepository;
import io.intelligenta.communityportal.repository.NAPAreaTypeRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.NAPAreaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NAPAreaServiceImpl implements NAPAreaService {

    private final NAPAreaRepository napAreaRepository;
    private final UserRepository userRepository;
    private final NAPAreaTypeRepository napAreaTypeRepository;

    public NAPAreaServiceImpl(NAPAreaRepository napAreaRepository, UserRepository userRepository, NAPAreaTypeRepository napAreaTypeRepository){
        this.napAreaRepository = napAreaRepository;
        this.userRepository = userRepository;
        this.napAreaTypeRepository = napAreaTypeRepository;
    }

    @Override
    public NAPArea createNAPArea(NapAreaDto napAreaDto) {
        NAPArea napArea = new NAPArea();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null)
        {
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
            napArea.setCreatedByUser(user);
        }

        napArea.setNameMk(napAreaDto.getNameMk());
        napArea.setNameAl(napAreaDto.getNameAl());
        napArea.setNameEn(napAreaDto.getNameEn());
        napArea.setDescriptionMk(napAreaDto.getDescriptionMk());
        napArea.setDescriptionAl(napAreaDto.getDescriptionAl());
        napArea.setDescriptionEn(napAreaDto.getDescriptionEn());
        napArea.setCode(napAreaDto.getCode());

        NAPAreaType napAreaType = napAreaTypeRepository.findById(napAreaDto.getNapAreaTypeId()).orElseThrow(NAPAreaTypeNotFoundException::new);
        napArea.setNapAreaType(napAreaType);
        napArea.setDateCreated(LocalDateTime.now());
        napArea.setActive(true);

        return napAreaRepository.save(napArea);
    }


    @Override
    public NAPArea findById(Long id) {
        return napAreaRepository.findById(id).orElseThrow(NAPAreaNotFoundException::new);
    }

    @Override
    public Page<NAPArea> findAllPaged(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase();
        if(!keyword.equals("") && !keyword.equals("null") && !keyword.equals("undefined")){
            return napAreaRepository.findAllByActiveWithKeyword(keyword, pageable);
        }
        else{
            return napAreaRepository.findAllByActiveWithoutKeyword(pageable);
        }

    }


    @Override
    public NAPArea updateNAPArea(NapAreaDto napAreaDto) {
        NAPArea updatedNAPArea = napAreaRepository.findById(napAreaDto.getId())
                .orElseThrow(NAPAreaNotFoundException::new);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null){
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
            updatedNAPArea.setUpdatedByUser(user);
        }
       updatedNAPArea.setDateUpdated(LocalDateTime.now());
       updatedNAPArea.setNameMk(napAreaDto.getNameMk());
       updatedNAPArea.setNameAl(napAreaDto.getNameAl());
       updatedNAPArea.setNameEn(napAreaDto.getNameEn());

       updatedNAPArea.setDescriptionMk(napAreaDto.getDescriptionMk());
       updatedNAPArea.setDescriptionAl(napAreaDto.getDescriptionAl());
       updatedNAPArea.setDescriptionEn(napAreaDto.getDescriptionEn());
       updatedNAPArea.setCode(napAreaDto.getCode());

       if(napAreaDto.getNapAreaTypeId() != null){
           NAPAreaType napAreaType = napAreaTypeRepository.findById(napAreaDto.getNapAreaTypeId()).orElseThrow(NAPAreaTypeNotFoundException::new);
           updatedNAPArea.setNapAreaType(napAreaType);
       }
       else{
           updatedNAPArea.setNapAreaType(updatedNAPArea.getNapAreaType());
       }

        return napAreaRepository.save(updatedNAPArea);
    }

    @Override
    public NAPArea setInactive(Long id) {
        NAPArea napArea = napAreaRepository.findById(id).orElseThrow(NAPAreaNotFoundException::new);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(napArea.getProblems().size() > 0){
            throw new NapAreaCanNotBeDeletedException();
        }
        else{
            napArea.setDateUpdated(LocalDateTime.now());
            napArea.setActive(false);
            napArea.setNapAreaType(null);
            if(authentication != null)
            {
                String email = authentication.getPrincipal().toString();
                User user = userRepository.findByEmailAndActive(email, true)
                        .orElseThrow(UserNotFoundException::new);
                napArea.setDeletedByUser(user);
            }
            return napAreaRepository.save(napArea);
        }
    }

    @Override
    public NAPArea setActive(Long id) {
        NAPArea napArea = napAreaRepository.findById(id).orElseThrow(NAPAreaNotFoundException::new);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null)
        {
            String email = authentication.getPrincipal().toString();
            User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
            napArea.setUpdatedByUser(user);
        }
        napArea.setDateUpdated(LocalDateTime.now());
        napArea.setActive(true);

        return napAreaRepository.save(napArea);
    }


    @Override
    public List<NAPArea> findAllList() {
        return napAreaRepository.findAll().stream().filter(NAPArea::getActive).collect(Collectors.toList());
    }

}
