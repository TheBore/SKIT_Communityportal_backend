package io.intelligenta.communityportal.service.impl.auth;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.repository.AnnouncementRepository;
import io.intelligenta.communityportal.repository.AreaOfInterestRepository;
import io.intelligenta.communityportal.repository.FeedbackRepository;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.InstitutionService;
import io.intelligenta.communityportal.service.auth.UserService;
import io.intelligenta.communityportal.service.impl.BaseEntityCrudServiceImpl;
import io.intelligenta.communityportal.web.specifications.utils.SpecificationUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
public class UserServiceImpl extends BaseEntityCrudServiceImpl<User, UserRepository> implements UserService {

    private UserRepository repo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Logger logger = Logger.getLogger(UserService.class.getName());

    private final Environment environment;
    private InstitutionService institutionService;
    private FeedbackRepository feedbackRepository;
    private AnnouncementRepository announcementRepository;
    private final EmailRepository emailRepository;
    private final AreaOfInterestRepository areaOfInterestRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository repo, BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment, InstitutionService institutionService, FeedbackRepository feedbackRepository, AnnouncementRepository announcementRepository, EmailRepository emailRepository, AreaOfInterestRepository areaOfInterestRepository, UserRepository userRepository) {
        this.repo = repo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.institutionService = institutionService;
        this.feedbackRepository = feedbackRepository;
        this.announcementRepository = announcementRepository;
        this.emailRepository = emailRepository;
        this.areaOfInterestRepository = areaOfInterestRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected UserRepository getRepository() {
        return repo;
    }


    @Override
    public User createUser(User user) {

        Institution institution = institutionService.findById(user.getInstitution_id());
        User userExist = getRepository().findByEmailAndActive(user.getEmail(), true).orElse(null);
        if (userExist == null) {
            User newUser = new User();
            newUser.setEmail(user.getEmail().toLowerCase());
            if(user.getAlternativeEmail() != null) {
                newUser.setAlternativeEmail(user.getAlternativeEmail().toLowerCase());
            }
            if(user.getAlternativeSecondEmail() != null) {
                newUser.setAlternativeSecondEmail(user.getAlternativeSecondEmail().toLowerCase());
            }
            newUser.setUsername(user.getEmail());
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            if(user.getPhone() != null){
                newUser.setPhone(institutionService.formatPhone(user.getPhone()));
                if(user.getLocales() != null){
                    newUser.setLocales(user.getLocales());
                }
            }

            if(user.getAlternativePhone() != null){
                newUser.setAlternativePhone(institutionService.formatPhone(user.getAlternativePhone()));
                if(user.getAlternativeLocales() != null){
                    newUser.setAlternativeLocales(user.getAlternativeLocales());
                }
            }

            if(user.getAlternativeSecondPhone() != null) {
                newUser.setAlternativeSecondPhone(institutionService.formatPhone(user.getAlternativeSecondPhone()));
                if(user.getAlternativeSecondLocales() != null){
                    newUser.setAlternativeSecondLocales(user.getAlternativeSecondLocales());
                }
            }

            if(user.getAreasOfInterest_ids() == null || user.getAreasOfInterest_ids().size() == 0){
                throw new UserWithoutAreaOfInterest();
            }
//            else{
//                newUser.setAreasOfInterest(user.getAreasOfInterest());
//            }

            List<AreaOfInterest> areasOfInterests = new ArrayList<>();
            for( int i = 0; i < user.getAreasOfInterest_ids().size() ; i++){
                AreaOfInterest areaOfInterest = areaOfInterestRepository.findById(user.getAreasOfInterest_ids().get(i)).orElseThrow(AreaOfInterestNotFoundException::new);
                areasOfInterests.add(areaOfInterest);
            }
            newUser.setAreasOfInterest(areasOfInterests);

            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newUser.setDateRegistrationCompleted(LocalDateTime.now());
            newUser.setDateCreated(LocalDateTime.now());
            newUser.setDateUpdated(LocalDateTime.now());
            newUser.setActive(true);
            newUser.setUserRole(user.getRole());
            newUser.setRole(user.getRole());
            newUser.setInstitution(institution);
            return userRepository.save(newUser);
        } else throw new UserAlreadyExistsException();

    }

    public User createInstitutionalUser(User user, User creator) {
        Institution creatorInstitution = creator.getInstitution();
        if (validateCreate(user)) {
            User newUser = new User();
            newUser.setInstitution(creatorInstitution);
            newUser.setEmail(user.getEmail().toLowerCase());
            if(user.getAlternativeEmail() != null) {
                newUser.setAlternativeEmail(user.getAlternativeEmail().toLowerCase());
            }
            if(user.getAlternativeSecondEmail() != null){
                newUser.setAlternativeSecondEmail(user.getAlternativeSecondEmail().toLowerCase());

            }
            newUser.setUsername(user.getEmail());
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            if(user.getPhone() != null) {
                newUser.setPhone(institutionService.formatPhone(user.getPhone()));
                if(user.getLocales() != null){
                    newUser.setLocales(user.getLocales());
                }
            }
            if(user.getAlternativePhone() != null){
                newUser.setAlternativePhone(institutionService.formatPhone(user.getAlternativePhone()));
                if(user.getAlternativeLocales() != null){
                    newUser.setAlternativeLocales(user.getAlternativeLocales());
                }
            }

            if(user.getAlternativeSecondPhone() != null) {
                newUser.setAlternativeSecondPhone(institutionService.formatPhone(user.getAlternativeSecondPhone()));
                if(user.getAlternativeSecondLocales() != null){
                    newUser.setAlternativeSecondLocales(user.getAlternativeSecondLocales());
                }
            }

//            if(user.getAreasOfInterest().isEmpty()){
//                throw new UserWithoutAreaOfInterest();
//            }
//            else{
//                newUser.setAreasOfInterest(user.getAreasOfInterest());
//            }

            if(user.getAreasOfInterest_ids() == null || user.getAreasOfInterest_ids().size() == 0){
                throw new UserWithoutAreaOfInterest();
            }

            List<AreaOfInterest> areasOfInterests = new ArrayList<>();
            for( int i = 0; i < user.getAreasOfInterest_ids().size() ; i++){
                AreaOfInterest areaOfInterest = areaOfInterestRepository.findById(user.getAreasOfInterest_ids().get(i)).orElseThrow(AreaOfInterestNotFoundException::new);
                areasOfInterests.add(areaOfInterest);
            }
            newUser.setAreasOfInterest(areasOfInterests);

            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newUser.setDateRegistrationCompleted(LocalDateTime.now());
            newUser.setDateCreated(LocalDateTime.now());
            newUser.setDateUpdated(LocalDateTime.now());
            newUser.setActive(false);
            newUser.setRole(UserRole.ROLE_INSTITUTIONAL_MODERATOR);
            newUser.setUserRole(UserRole.ROLE_INSTITUTIONAL_MODERATOR);
            return userRepository.save(newUser);
        }
        throw new UserAlreadyExistsException();
    }

    @Override
    public User updateInstitutionalUser(User user) {
        User prev = this.getRepository()
                .findById(user.getId())
                .orElseThrow(UserNotFoundException::new);

        prev.setFirstName(user.getFirstName());
        prev.setLastName(user.getLastName());
        prev.setEmail(user.getEmail().toLowerCase());
        if(user.getAlternativeEmail() != null) {
            prev.setAlternativeEmail(user.getAlternativeEmail().toLowerCase());
        }

        if(!user.getAreasOfInterest().isEmpty()){
            List<AreaOfInterest> oldAreasOfInterest = prev.getAreasOfInterest();
            List<AreaOfInterest> newAreasOfInterest = user.getAreasOfInterest();


            List<AreaOfInterest> allAreasOfInterest = new ArrayList<>();
            allAreasOfInterest.addAll(oldAreasOfInterest);
            allAreasOfInterest.addAll(newAreasOfInterest);


            prev.setAreasOfInterest(allAreasOfInterest);
        }

        if(user.getAlternativeSecondEmail() != null) {
            prev.setAlternativeSecondEmail(user.getAlternativeSecondEmail().toLowerCase());
        }
        prev.setUsername(user.getEmail());
        prev.setDateUpdated(LocalDateTime.now());
        if(user.getPhone() != null) {
            prev.setPhone(institutionService.formatPhone(user.getPhone()));
        }
        prev.setLocales(user.getLocales());

        if(user.getAlternativePhone() != null) {
            prev.setAlternativePhone(institutionService.formatPhone(user.getAlternativePhone()));
        }
        prev.setAlternativeLocales(user.getAlternativeLocales());

        if (user.getAlternativeSecondPhone() != null) {
            prev.setAlternativeSecondPhone(institutionService.formatPhone(user.getAlternativeSecondPhone()));
        }
        prev.setAlternativeSecondLocales(user.getAlternativeSecondLocales());

        prev.setActive(user.getActive());

        if (user.getInstitution_id() != null) {
            Institution institution = this.institutionService.findById(user.getInstitution_id());
            prev.setInstitution(institution);
        } else {
            prev.setInstitution(null);
        }

        getRepository().save(prev);
        return prev;
    }

    @Override
    public User updateUser(User updateUser) {

        User prev = getRepository().findById(updateUser.getId()).orElseThrow(UserNotFoundException::new);

        if (updateUser.getInstitution_id() != null) {
            Institution institution = institutionService.findById(updateUser.getInstitution_id());
            prev.setInstitution(institution);
        }
        if (updateUser.getRole() != null) {
            prev.setRole(updateUser.getRole());
            prev.setUserRole(updateUser.getRole());
        }
        prev.setEmail(updateUser.getEmail().toLowerCase());
        if(updateUser.getAlternativeEmail() != null) {
            prev.setAlternativeEmail(updateUser.getAlternativeEmail().toLowerCase());
        }

//        if(!updateUser.getAreasOfInterest().isEmpty()){
//            List<AreaOfInterest> oldAreasOfInterest = prev.getAreasOfInterest();
//            List<AreaOfInterest> newAreasOfInterest = updateUser.getAreasOfInterest();
//
//
//            List<AreaOfInterest> allAreasOfInterest = new ArrayList<>();
//            allAreasOfInterest.addAll(oldAreasOfInterest);
//            allAreasOfInterest.addAll(newAreasOfInterest);
//
//
//            prev.setAreasOfInterest(allAreasOfInterest);
//        }

        List<AreaOfInterest> areasOfInterests = new ArrayList<>();
        if(updateUser.getAreasOfInterest_ids() != null && updateUser.getAreasOfInterest_ids().size() != 0){
            for( int i = 0; i < updateUser.getAreasOfInterest_ids().size() ; i++){
                AreaOfInterest areaOfInterest = areaOfInterestRepository.findById(updateUser.getAreasOfInterest_ids().get(i)).orElseThrow(AreaOfInterestNotFoundException::new);
                areasOfInterests.add(areaOfInterest);
            }
            prev.setAreasOfInterest(areasOfInterests);
        }
        else{
            prev.setAreasOfInterest(updateUser.getAreasOfInterest());
        }


        if(updateUser.getAlternativeSecondEmail() != null) {
            prev.setAlternativeSecondEmail(updateUser.getAlternativeSecondEmail().toLowerCase());
        }
        prev.setFirstName(updateUser.getFirstName());
        prev.setLastName(updateUser.getLastName());
        prev.setUsername(updateUser.getEmail());
        prev.setDateUpdated(LocalDateTime.now());
        if(updateUser.getPhone() != null){
            prev.setPhone(institutionService.formatPhone(updateUser.getPhone()));
        }
        prev.setLocales(updateUser.getLocales());

        if(updateUser.getAlternativePhone() != null) {
            prev.setAlternativePhone(institutionService.formatPhone(updateUser.getAlternativePhone()));
        }
        prev.setAlternativeLocales(updateUser.getAlternativeLocales());

        if(updateUser.getAlternativeSecondPhone() != null){
            prev.setAlternativeSecondPhone(institutionService.formatPhone(updateUser.getAlternativeSecondPhone()));
        }
        prev.setAlternativeSecondLocales(updateUser.getAlternativeSecondLocales());

        if (updateUser.getActive() == false) {
            prev.setActive(updateUser.getActive());

        } else {
            prev.setActive(updateUser.getActive());
        }
        if (updateUser.getInstitution_id() != null) {
            Institution institution = this.institutionService.findById(updateUser.getInstitution_id());
            prev.setInstitution(institution);
        } else {
            prev.setInstitution(updateUser.getInstitution());
        }

        getRepository().save(prev);
        return prev;

    }

    public boolean validUpdateUser(User user1, User user2) {
        return user1 != null && !user1.equals(user2);
    }


    @Override
    public Page<User> findAllByFirstName(String firstName, Pageable pageable) {
        firstName = firstName.toLowerCase();
        return getRepository().findAllByFirstName(firstName, pageable);
    }

    @Override
    public Page<User> findAllByFirstOrLastNameAndInstution(String firstName, String instName, Pageable pageable) {
        firstName = firstName.toLowerCase();
        instName = instName.toLowerCase();
        return getRepository().findAllByFirstOrLastNameAndInstution(firstName, instName, pageable);
    }

    @Override
    public Page<User> findAllByFirstAndLastName(String firstName, String lastName, Pageable pageable) {
        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();
        return getRepository().findAllByFirstAndLastName(firstName, lastName, pageable);
    }

    @Override
    public Page<User> findAllByInstitution(String instName, Pageable pageable) {
        instName = instName.toLowerCase();
        return getRepository().findAllByInstitution(instName, pageable);
    }

    @Override
    public Page<User> findAllByFirstNameAndLastNameAndInstitution(String firstName, String lastName, String instName, Pageable pageable) {
        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();
        instName = instName.toLowerCase();
        return getRepository().findAllByFirstNameAndLastNameAndInstitution(firstName, lastName, instName, pageable);
    }

    @Override
    public Page<User> findAllByActive(Boolean active, Pageable pageable) {
        return getRepository().findAllByActive(active, pageable);
    }

    @Override
    public Page<User> findAllUserFromLoggedInstitution(Long institutionId, Pageable pageable) {
        return getRepository().findAllByInstitutionId(institutionId, pageable);
    }

    @Override
    public boolean passwordMatches(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }


    @Override
    public void changePassword(String current, String password, String repeatPassword) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = getRepository().findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        String userPassword = user.getPassword();

        if (bCryptPasswordEncoder.matches(current, userPassword)) {
            if (password.equals(repeatPassword)) {
                user.setPassword(bCryptPasswordEncoder.encode(password));
                user.setPasswordResetActiveUntil(null);
                user.setPasswordResetString(null);
                getRepository().save(user);
            } else {
                throw new PasswordsNotTheSameException();
            }
        } else throw new InvalidCurrentPassword();
    }


    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return getRepository().findOne(SpecificationUtils.<User>equal("email", email)).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByEmailAndActive(String email, boolean active) throws UserNotFoundException {
        return getRepository().findByEmailAndActive(email, active).orElseThrow(UserNotFoundException::new);
    }


    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getRepository().findByEmail(s).orElseThrow(UserNotFoundException::new);
    }

    public boolean validateCreate(User user) {
        return !getRepository().existsByUsername(user.getUsername()) &&
                !getRepository().existsByEmail(user.getEmail());
    }


    @Transactional
    public void createAdmin() {

        User user = new User();
        user.setUsername("admin");
        user.setPassword(this.bCryptPasswordEncoder.encode("admin"));
        user.setActive(true);
        user.setPhone("033 444 555");
        user.setUserRole(UserRole.ROLE_ADMIN);
        user.setRole(UserRole.ROLE_ADMIN);

        user.setDateRegistrationCompleted(LocalDateTime.now());
        user.setDateCreated(LocalDateTime.now());
        user.setDateUpdated(LocalDateTime.now());
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setEmail("admin@gmail.com");

        AreaOfInterest areaOfInterest = new AreaOfInterest();
        areaOfInterest.setNameMk("Администратор");
        areaOfInterest.setActive(false);
        areaOfInterestRepository.save(areaOfInterest);
        user.setAreasOfInterest(Collections.singletonList(areaOfInterest));

        this.repo.save(user);

    }

    @Override
    public User setActiveUser(Long userId) {
        User updateUser = getRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        updateUser.setDateUpdated(LocalDateTime.now());
        updateUser.setActive(true);
        return getRepository().save(updateUser);
    }

    @Override
    public Page<User> findInactiveUsers(Pageable pageable) {
        return getRepository().findAllByActiveOrderByDateCreatedDesc(false, pageable);
    }

    @Override
    public Page<User> findUserByInstitutionName(String instName, Pageable pageable) {
        instName = instName.toLowerCase();
        return getRepository().findInactiveUsers(instName, pageable);
    }

    @Override
    public void deleteById(Long userId) {
        User user = this.getRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        this.getRepository().delete(user);

//        List<Feedback> feedbackList = this.feedbackRepository.findAllByCreatorId(userId);
//        List<Announcement> announcementList = this.announcementRepository.findAllByCreatorId(userId);
//        try{
//            this.announcementRepository.deleteAll(announcementList);
//
//            this.feedbackRepository.deleteAll(feedbackList);
//
//            this.getRepository().deleteById(userId);
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public User setInactiveUser(Long userId) {
        User updateUser = getRepository().findById(userId).orElseThrow(UserNotFoundException::new);
        updateUser.setDateUpdated(LocalDateTime.now());
        updateUser.setActive(false);
        return getRepository().save(updateUser);
    }

    @Override
    public void processForgotPassword(String email) {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();

        String from = environment.getProperty("spring.mail.username");

        List<String> emails = new ArrayList<>();
        emails.add(email);

        String tokenLink = "https://portal.aspi.mk/rest/user/resetPassword?token=" + token;
        Map<String, String> map = new HashMap<>();
        map.put("message", tokenLink);

        try {
            this.updateResetPasswordToken(token, email);
            emailRepository.sendHtmlMail(emails, "CommunityPortal - Reset password", "ResetPassword.html", map, from);
        } catch (Exception e) {
            throw new EmailSendingException();
        }
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        User user = repo.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (user != null && user.getActive()) {
            user.setPasswordResetString(token);
            user.setPasswordResetActiveUntil(LocalDateTime.now().plusMinutes(30));
            repo.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void processResetPassword(String token, String password) {
        User user = repo.getByPasswordResetString(token);
        if(user == null){
            throw new PasswordResetTokenHasExpired();
        }
        else {
            if(user.getPasswordResetActiveUntil().compareTo(LocalDateTime.now()) >= 0) {
                if(!user.getActive()){
                    user.setActive(true);
                    this.updateUser(user);
                }
                this.updatePassword(user, password);
            }
            else{
                throw new PasswordResetTokenHasExpired();
            }
        }
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setPasswordResetString(null);
        user.setPasswordResetActiveUntil(null);

        repo.save(user);
    }

    private String generateRandomString() {
        //Change the number 15 to the size desired
        return RandomStringUtils.random(15, true, true);
    }

    @Override
    public void resetPassword(String email) {
        email = email.toLowerCase();
        User user = getRepository().findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
        String tempPass = generateRandomString();
        user.setPasswordResetString(user.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(tempPass));
        user.setPasswordResetActiveUntil(LocalDateTime.now().plusMinutes(30));
        getRepository().save(user);

        Map<String, String> map = new HashMap<>();
        map.put("user", user.getFirstName());
        map.put("temp", tempPass);
        String sender = environment.getProperty("spring.mail.from");
        try {
            emailRepository.sendHtmlMail(Collections.singletonList(email), "Reset password", "newPass.html", map, sender);
        } catch (Exception e) {
            throw new EmailSendingException();
        }
    }

//    @Override
//    public void createEvaluator(User user) {
//
//        User userExists = getRepository().findByEmailAndActive(user.getEmail(), true).orElse(null);
//
//        if (userExists != null) {
//            if(userExists.getRole() == UserRole.ROLE_INSTITUTIONAL_MODERATOR && userExists.getUserRole() == UserRole.ROLE_INSTITUTIONAL_MODERATOR) {
//
//                userExists.setUserRole(UserRole.ROLE_MODERATOR_EVALUATOR);
//                userExists.setRole(UserRole.ROLE_MODERATOR_EVALUATOR);
//                userExists.setDateUpdated(LocalDateTime.now());
//
//                getRepository().save(userExists);
//            }
//
//            else if (userExists.getRole() == UserRole.ROLE_ADMIN && userExists.getUserRole() == UserRole.ROLE_ADMIN){
//                throw new IllegalStateException("This user is Administrator, so it can not be Evaluator!");
//            }
//
//            else if (userExists.getRole() == UserRole.ROLE_EVALUATOR && userExists.getUserRole() == UserRole.ROLE_EVALUATOR){
//                throw  new IllegalStateException("This user exists and it is Evaluator!");
//            }
//
//            else if (userExists.getRole() == UserRole.ROLE_MODERATOR_EVALUATOR && userExists.getUserRole() == UserRole.ROLE_MODERATOR_EVALUATOR){
//                throw  new IllegalStateException("This user exists and it is Moderator-Evaluator!");
//            }
//        }
//
//        else {
//            Institution institution = institutionService.findById(user.getInstitution_id());
//            User newUser = new User();
//            newUser.setEmail(user.getEmail().toLowerCase());
//            newUser.setUsername(user.getEmail());
//            newUser.setFirstName(user.getFirstName());
//            newUser.setLastName(user.getLastName());
//            newUser.setPhone(institutionService.formatPhone(user.getPhone()));
//            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//            newUser.setDateRegistrationCompleted(LocalDateTime.now());
//            newUser.setActive(true);
//            newUser.setUserRole(UserRole.ROLE_EVALUATOR);
//            newUser.setRole(UserRole.ROLE_EVALUATOR);
//            newUser.setInstitution(institution);
//
//            getRepository().save(newUser);
//        }
//
//    }
}
