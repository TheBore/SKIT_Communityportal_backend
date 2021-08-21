package io.intelligenta.communityportal.web;


import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.service.auth.UserService;
import io.intelligenta.communityportal.web.specifications.UserSpecification;
import io.intelligenta.communityportal.web.specifications.utils.SearchCriteria;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("/rest/user")
@CrossOrigin("*")
public class UsersController extends CrudResource<User, UserService> {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("(isAuthenticated() && (hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')))")
    @GetMapping("/roles")
    public UserRole[] getRoles() {
        return UserRole.values();
    }

    @Override
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    public UserService getService() {
        return service;
    }

    @Override
    public User beforeUpdate(User oldEntity, User newEntity) {
        return oldEntity;
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/inactive")
    public Page<User> findAllInactiveUsers(@RequestParam("instName") String instName, Pageable pageable) {
        if (!instName.equals("")) {
            return service.findUserByInstitutionName(instName, pageable);
        }
        else return service.findInactiveUsers(pageable);

    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PatchMapping("/setactive")
    public User setActiveUser(@RequestParam("userId") Long userId) {
        return service.setActiveUser(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/paged1", method = RequestMethod.GET, produces = "application/json")
    public Page<User> getAllUsers(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String instName, Pageable pageable) throws JSONException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = service.findByEmail(username);

        if (UserRole.ROLE_ADMIN.equals(user.getRole())) {
            if (!firstName.equals("") && lastName.equals("") && instName.equals(""))
                return getService().findAllByFirstName(firstName, pageable);
            else if (!firstName.equals("") && lastName.equals("") && !instName.equals(""))
                return getService().findAllByFirstOrLastNameAndInstution(firstName, instName, pageable);
            else if (!firstName.equals("") && !lastName.equals("") && instName.equals(""))
                return getService().findAllByFirstAndLastName(firstName, lastName, pageable);
            else if (!firstName.equals("") && !lastName.equals("") && !instName.equals(""))
                return getService().findAllByFirstNameAndLastNameAndInstitution(firstName, lastName, instName, pageable);
            else if (firstName.equals("") && lastName.equals("") && !instName.equals(""))
                return getService().findAllByInstitution(instName, pageable);
            else
                return getService().findAllByActive(true, pageable);
        } else {
            Long institutionUserId = user.getInstitution().getId();
            return getService().findAllUserFromLoggedInstitution(institutionUserId, pageable);
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/paged", method = RequestMethod.GET, produces = "application/json")
    public Page<User> getAll(@RequestParam int page, @RequestParam int pageSize, HttpServletRequest request) throws JSONException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = service.findByEmail(username);

        if (UserRole.ROLE_ADMIN.equals(user.getRole())) {
            Specification<User> spec = new UserSpecification(new SearchCriteria("active", ":", true));
            return super.getPaged(page, pageSize, spec, null, request);
        } else {
            Institution institutionUser = user.getInstitution();
            Specification<User> spec = new UserSpecification(new SearchCriteria("active", ":", true));
            Specification<User> secondSpec = new UserSpecification(new SearchCriteria("institution", ":", institutionUser));
            return super.getPaged(page, pageSize, spec, secondSpec, request);
        }
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    @Transient
    public User update(@RequestBody @Valid User obj) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = service.findByEmail(username);


        if (UserRole.ROLE_ADMIN.equals(user.getRole())) {
            return service.updateUser(obj);
        } else {
            return service.updateInstitutionalUser(obj);
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public User create(@RequestBody @Valid User entity, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = service.findByEmail(username);
        if (UserRole.ROLE_ADMIN.equals(user.getRole())) {
            return service.createUser(entity);
        } else {
            return service.createInstitutionalUser(entity, user);
        }
    }

    @Override
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<User> getAll(HttpServletRequest request) {
        return super.getAll(request);
    }

    @Override
    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public User get(@PathVariable Long id, HttpServletResponse response) {
        return super.get(id, response);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history/paged")
    public Page<User> getHistoryPaged(int page, int pageSize, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = service.findByEmail(username);
        if (UserRole.ROLE_ADMIN.equals(user.getRole())) {

            Specification<User> spec = new UserSpecification(new SearchCriteria("active", ":", false));

            return super.getPaged(page, pageSize, spec, null, request);
        } else {
            Institution institutionUser = user.getInstitution();
            Specification<User> spec = new UserSpecification(new SearchCriteria("active", ":", false));
            Specification<User> secondSpec = new UserSpecification(new SearchCriteria("institution", ":", institutionUser));

            return super.getPaged(page, pageSize, spec, secondSpec, request);
        }

    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/history")
    public Page<User> getHistory(int page, int pageSize, HttpServletRequest request) {
        return super.getAll(page, pageSize, request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/userDetails")
    public List<String> userDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = service.findByEmail(username);
        Long userInstitutionId = user.getInstitution() != null ?
                user.getInstitution().getId() : -1L;
        List<String> details = new ArrayList<>();

        List<AreaOfInterest> allAreasOfInterest = new ArrayList<>(user.getAreasOfInterest());
        List<String> namesOfAreasOfInterest = new ArrayList<>();

        for (AreaOfInterest area : allAreasOfInterest) {
            namesOfAreasOfInterest.add(area.getNameMk());
        }

        details.add(user.getUsername());
        details.add(user.getUserRole().getAuthority());
        details.add(userInstitutionId.toString());
        details.add(user.getEmail());
        details.addAll(namesOfAreasOfInterest);

        return details;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePassword")
    public void changePassword(@RequestParam(name = "oldpassword") String oldpassword,
                              @RequestParam(name = "newpassword") String newpassword,
                              @RequestParam(name = "repeatPassword") String repeatPassword) {
        service.changePassword(oldpassword, newpassword, repeatPassword);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable(value = "id") Long Id) {
        getService().deleteById(Id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PatchMapping("/setinactive")
    public User setInactiveUser(@RequestParam("Id") Long Id) {
        return service.setInactiveUser(Id);
    }

    @PostMapping("/forgotPassword/password")
    public void forgotPassword(@RequestHeader String email) {
        service.processForgotPassword(email);
    }

    @PostMapping("/resetPassword/password")
    public void resetPassword(@RequestHeader String token, @RequestHeader String password) {
        service.processResetPassword(token, password);
    }

    @PostMapping("/resetpassword")
    public void resetPassword(@RequestParam("email") String email) {
        service.resetPassword(email);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
//    @PostMapping("/createEvaluator")
//    public void createEvaluator (@RequestBody User user){
//         service.createEvaluator(user);
//    }

}
