package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.NAPAreaType;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.exceptions.NAPAreaTypeNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NapAreaTypeCanNotBeDeletedException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.NAPAreaTypeRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.NAPAreaTypeService;
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
public class NAPAreaTypeServiceImpl implements NAPAreaTypeService {

    private final NAPAreaTypeRepository napAreaTypeRepository;
    private final UserRepository userRepository;


    public NAPAreaTypeServiceImpl (NAPAreaTypeRepository napAreaTypeRepository, UserRepository userRepository){
        this.napAreaTypeRepository = napAreaTypeRepository;
        this.userRepository = userRepository;
    }


    @Override
    public NAPAreaType createNAPAreaType(NAPAreaType napAreaType) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        napAreaType.setDateCreated(LocalDateTime.now());
        napAreaType.setCreatedByUser(user);
        napAreaType.setActive(true);

        return napAreaTypeRepository.save(napAreaType);
    }

    @Override
    public NAPAreaType findById(Long id) {
        return napAreaTypeRepository.findById(id).orElseThrow(NAPAreaTypeNotFoundException::new);
    }

    @Override
    public Page<NAPAreaType> findAllPagedWithKeyword(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase();
        if(!keyword.equals("null") && !keyword.equals("undefined") && !keyword.equals("")){
            return napAreaTypeRepository.findAllByActivePaged(keyword,pageable);
        }
        else{
            return napAreaTypeRepository.findAllByActiveNotPaged(pageable);
        }
    }

    @Override
    public NAPAreaType updateNAPAreaType(NAPAreaType napAreaType) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        NAPAreaType updatedNAPAreaType = napAreaTypeRepository.findById(napAreaType.getId()).orElseThrow(NAPAreaTypeNotFoundException::new);

        updatedNAPAreaType.setUpdatedByUser(user);
        updatedNAPAreaType.setDateUpdated(LocalDateTime.now());

        updatedNAPAreaType.setNameMk(napAreaType.getNameMk());
        updatedNAPAreaType.setNameAl(napAreaType.getNameAl());
        updatedNAPAreaType.setNameEn(napAreaType.getNameEn());

        updatedNAPAreaType.setDescriptionMk(napAreaType.getDescriptionMk());
        updatedNAPAreaType.setDescriptionAl(napAreaType.getDescriptionAl());
        updatedNAPAreaType.setDescriptionEn(napAreaType.getDescriptionEn());

        return napAreaTypeRepository.save(updatedNAPAreaType);
    }

    @Override
    public NAPAreaType setInactive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        NAPAreaType napAreaType = napAreaTypeRepository.findById(id).orElseThrow(NAPAreaTypeNotFoundException::new);

        if(napAreaType.getNapAreas().size() > 0){
            throw new NapAreaTypeCanNotBeDeletedException();
        }
        else{
            napAreaType.setDateCreated(LocalDateTime.now());
            napAreaType.setDeletedByUser(user);
            napAreaType.setActive(false);

            return napAreaTypeRepository.save(napAreaType);
        }
    }

    @Override
    public NAPAreaType setActive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        NAPAreaType napAreaType = napAreaTypeRepository.findById(id).orElseThrow(NAPAreaTypeNotFoundException::new);

        napAreaType.setDateUpdated(LocalDateTime.now());
        napAreaType.setUpdatedByUser(user);
        napAreaType.setActive(true);

        return napAreaTypeRepository.save(napAreaType);
    }

    @Override
    public List<NAPAreaType> findAllActive() {
        return napAreaTypeRepository.findAll().stream().filter(NAPAreaType::getActive).collect(Collectors.toList());
    }


}
