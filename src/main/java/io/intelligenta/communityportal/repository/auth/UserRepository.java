package io.intelligenta.communityportal.repository.auth;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.repository.JpaSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaSpecificationRepository<User> {

    @Query("select u from User u where u.active=true and (u.firstName LIKE %:firstName% or u.lastName LIKE %:firstName% or LOWER(u.firstName) LIKE %:firstName% or LOWER(u.lastName) LIKE %:firstName%) ORDER BY u.firstName, u.lastName ASC")
    Page<User> findAllByFirstName(@Param("firstName") String firstName, Pageable pageable); // samo ime

    @Query("select u from User u where u.active=true and (u.firstName LIKE %:firstName% or u.lastName LIKE %:firstName% or LOWER(u.firstName) LIKE %:firstName% or LOWER(u.lastName) LIKE %:firstName%) and (u.institution.nameMk LIKE %:instName% or u.institution.nameAl LIKE %:instName% or LOWER(u.institution.nameMk) LIKE %:instName% or LOWER(u.institution.nameAl) LIKE %:instName%) ORDER BY u.firstName, u.lastName ASC")
    Page<User> findAllByFirstOrLastNameAndInstution(@Param("firstName") String firstName, @Param("instName") String instName, Pageable pageable); // samo ime ili prezime i institucija

    @Query("select u from User u where u.active=true and (u.firstName LIKE %:firstName% or u.lastName LIKE %:firstName% or LOWER(u.firstName) LIKE %:firstName% or LOWER(u.lastName) LIKE %:firstName%) and (u.lastName LIKE %:lastName% or u.firstName LIKE %:lastName% OR LOWER(u.lastName) LIKE %:lastName% or LOWER(u.firstName) LIKE %:lastName%) ORDER BY u.firstName, u.lastName ASC")
    Page<User> findAllByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName, Pageable pageable); // po ime i prezime

    @Query("select u from User u where u.active=true and (u.institution.nameMk LIKE %:instName% or LOWER(u.institution.nameMk) LIKE %:instName% or LOWER(u.institution.nameAl) LIKE %:instName% or u.institution.nameAl LIKE %:instName% ) ORDER BY u.firstName, u.lastName ASC")
    Page<User> findAllByInstitution(@Param("instName") String instName, Pageable pageable); // po institucija

    @Query("select u from User u where u.active=true and ((u.firstName LIKE %:firstName% or u.lastName LIKE %:firstName% or LOWER(u.firstName) LIKE %:firstName% or LOWER(u.lastName) LIKE %:firstName%)and (u.lastName LIKE %:lastName% or u.firstName LIKE %:lastName% or LOWER(u.lastName) LIKE %:lastName% or LOWER(u.firstName) LIKE %:lastName%) and(u.institution.nameMk LIKE %:instName% or u.institution.nameAl LIKE %:instName% or LOWER(u.institution.nameMk) LIKE %:instName% or LOWER(u.institution.nameAl) LIKE %:instName%)) ORDER BY u.firstName, u.lastName ASC ")
    Page<User> findAllByFirstNameAndLastNameAndInstitution(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("instName") String instName, Pageable pageable); // po ime, prezime, institucija

    @Query("select u from User u where u.active=true ORDER BY u.firstName, u.lastName ASC")
    Page<User> findAllByActive(Boolean active, Pageable pageable); // site aktivni ili neaktivni

    Page<User> findAllByInstitutionId(Long id, Pageable pageable);

    Optional<User> findByEmail(String email);

    List<User> findAllByActiveAndInstitutionId(boolean active, Long institutionId);

    Optional<User> findByEmailAndActive(String email, boolean active);

    List<User> findAllByActiveAndInstitution(boolean active, Institution institution);

    List<User> findAllByActive(boolean active);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByActive(boolean active);

    Long countAllByRole(UserRole role);

    List<User> findByRoleAndActive(UserRole role,Boolean active);

    List<User> findByRoleAndInstitutionIdAndActive(UserRole role , Long id,Boolean active);

    Page<User> findAllByActiveOrderByDateCreatedDesc(boolean active, Pageable pageable);

    @Query("select u from User u where u.active=false and (u.institution.nameMk like %:name% or u.institution.nameAl like %:name% or LOWER(u.institution.nameMk) like %:name% or LOWER(u.institution.nameAl) like %:name%) order by u.dateCreated desc, u.firstName, u.lastName ASC")
    Page<User> findInactiveUsers(@Param("name")String instName,Pageable pageable);

    List<User> findAllByInstitutionId(Long institutionId);

    User getByPasswordResetString (String token);


}
