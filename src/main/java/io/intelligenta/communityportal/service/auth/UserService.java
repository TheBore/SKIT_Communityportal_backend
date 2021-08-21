package io.intelligenta.communityportal.service.auth;


import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.service.BaseEntityCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends BaseEntityCrudService<User> {
    Page<User> findAllByFirstName(String firstName, Pageable pageable); // samo ime

    Page<User> findAllByFirstOrLastNameAndInstution(String firstName, String instName, Pageable pageable); // samo ime ili prezime i institucija

    Page<User> findAllByFirstAndLastName(String firstName, String lastName, Pageable pageable); // samo prezime

    Page<User> findAllByInstitution(String instName, Pageable pageable); // po institucija

    Page<User> findAllByFirstNameAndLastNameAndInstitution(String firstName, String lastName, String instName, Pageable pageable); // po ime, prezime, institucija

    Page<User> findAllByActive(Boolean active, Pageable pageable);

    Page<User> findAllUserFromLoggedInstitution(Long institutionId, Pageable pageable); // all users from logged institution

    boolean passwordMatches(User user, String password);

    void changePassword(String oldpassword, String newpassword, String repeatPassword);

    User findByEmail(String email) throws UserNotFoundException;

    User findByEmailAndActive(String email, boolean active) throws UserNotFoundException;

    User createInstitutionalUser(User user, User creator);

    User updateInstitutionalUser(User user);

    User updateUser(User user);

    User createUser(User user);

    void createAdmin();

    User setActiveUser(Long userId);

    Page<User> findInactiveUsers(Pageable pageable);

    Page<User> findUserByInstitutionName(String instName, Pageable pageable);

    void deleteById(Long userId);

    User setInactiveUser(Long userId);

    void processForgotPassword(String email);

    void updateResetPasswordToken(String token, String email);

    void processResetPassword(String token, String password);

    void updatePassword(User user, String newPassword);

    void resetPassword(String email);

//    User createEvaluator(User user, User creator);
}
