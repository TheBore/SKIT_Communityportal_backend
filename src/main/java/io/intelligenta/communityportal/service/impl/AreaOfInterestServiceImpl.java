package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.AreaOfInterestDto;
import io.intelligenta.communityportal.models.exceptions.AreaOfInterestCanNotBeDeletedException;
import io.intelligenta.communityportal.models.exceptions.AreaOfInterestNotFoundException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.AreaOfInterestRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.AreaOfInterestService;
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
public class AreaOfInterestServiceImpl extends BaseEntityCrudServiceImpl<AreaOfInterest, AreaOfInterestRepository> implements AreaOfInterestService {

    private final AreaOfInterestRepository areaOfInterestRepository;
    private final UserRepository userRepository;

    public AreaOfInterestServiceImpl (AreaOfInterestRepository areaOfInterestRepository, UserRepository userRepository){
        this.areaOfInterestRepository = areaOfInterestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AreaOfInterest createAreaOfInterest(AreaOfInterestDto areaOfInterestDto) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        AreaOfInterest newAreaOfInterest = new AreaOfInterest();
        newAreaOfInterest.setDateCreated(LocalDateTime.now());
        newAreaOfInterest.setNameMk(areaOfInterestDto.getNameMk());
        newAreaOfInterest.setNameAl(areaOfInterestDto.getNameAl());
        newAreaOfInterest.setNameEn(areaOfInterestDto.getNameEn());

        newAreaOfInterest.setDescriptionMk(areaOfInterestDto.getDescriptionMk());
        newAreaOfInterest.setDescriptionAl(areaOfInterestDto.getDescriptionAl());
        newAreaOfInterest.setDescriptionEn(areaOfInterestDto.getDescriptionEn());

        newAreaOfInterest.setCreatedByUser(user);
        newAreaOfInterest.setActive(true);
        return getRepository().save(newAreaOfInterest);
    }

    @Override
    public AreaOfInterest updateAreaOfInterest(AreaOfInterestDto areaOfInterestDto) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        AreaOfInterest updatedAreaOfInterest = getRepository().findById(areaOfInterestDto.getId()).orElseThrow(AreaOfInterestNotFoundException::new);

        updatedAreaOfInterest.setDateUpdated(LocalDateTime.now());
        updatedAreaOfInterest.setUpdatedByUser(user);

        updatedAreaOfInterest.setNameMk(areaOfInterestDto.getNameMk());
        updatedAreaOfInterest.setNameAl(areaOfInterestDto.getNameAl());
        updatedAreaOfInterest.setNameEn(areaOfInterestDto.getNameEn());

        updatedAreaOfInterest.setDescriptionMk(areaOfInterestDto.getDescriptionMk());
        updatedAreaOfInterest.setDescriptionAl(areaOfInterestDto.getDescriptionAl());
        updatedAreaOfInterest.setDescriptionEn(areaOfInterestDto.getDescriptionEn());

        return getRepository().save(updatedAreaOfInterest);
    }

    @Override
    public AreaOfInterest findById(Long id) {
        return getRepository().findById(id).orElseThrow(AreaOfInterestNotFoundException::new);
    }


    @Override
    public Page<AreaOfInterest> findAllAreasOfInterest(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase();
        if(!keyword.equals("null") && !keyword.equals("") && !keyword.equals("undefined")){
            return getRepository().findAreaOfInterestOrderByDateCreatedWithKeyword(keyword, pageable);
        }
        else{
            return getRepository().findAreaOfInterestOrderByDateCreated(pageable);
        }
    }

    @Override
    public AreaOfInterest setInactive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        AreaOfInterest areaOfInterest = getRepository().findById(id).orElseThrow(AreaOfInterestNotFoundException::new);

        if(areaOfInterest.getFeedbacks().size() > 0 || areaOfInterest.getAnnouncements().size() > 0 || areaOfInterest.getQuestions().size() > 0 || areaOfInterest.getUsers().size() > 0){
            throw new AreaOfInterestCanNotBeDeletedException();
        }
        else{
            areaOfInterest.setDateUpdated(LocalDateTime.now());
            areaOfInterest.setDeletedByUser(user);
            areaOfInterest.setActive(false);

            return getRepository().save(areaOfInterest);
        }
    }

    @Override
    public AreaOfInterest setActive(Long id) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        AreaOfInterest areaOfInterest = getRepository().findById(id).orElseThrow(AreaOfInterestNotFoundException::new);
        areaOfInterest.setDateUpdated(LocalDateTime.now());
        areaOfInterest.setUpdatedByUser(user);
        areaOfInterest.setActive(true);
        return getRepository().save(areaOfInterest);
    }

    @Override
    public List<AreaOfInterest> findAll(){
        return areaOfInterestRepository.findAll().stream().filter(AreaOfInterest::getActive).collect(Collectors.toList());
    }

    @Override
    public List<AreaOfInterest> findAreasOfInterestForUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        return user.getAreasOfInterest().stream().filter(AreaOfInterest::getActive).collect(Collectors.toList());
    }

    @Override
    protected AreaOfInterestRepository getRepository() {
        return areaOfInterestRepository;
    }
}
