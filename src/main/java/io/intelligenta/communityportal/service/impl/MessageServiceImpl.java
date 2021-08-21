package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Message;
import io.intelligenta.communityportal.models.Question;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.exceptions.MessageNotFoundException;
import io.intelligenta.communityportal.models.exceptions.QuestionNotFoundException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.repository.MessageRepository;
import io.intelligenta.communityportal.repository.QuestionRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl extends BaseEntityCrudServiceImpl<Message, MessageRepository> implements MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final QuestionRepository questionRepository;

    public MessageServiceImpl(UserRepository userRepository, MessageRepository messageRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.questionRepository = questionRepository;
    }


    @Override
    public Message createMessage(Long questionId, String email, Message message) {
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
        Question question = this.questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        message.setActive(true);
        message.setDateCreated(LocalDateTime.now());
        message.setDateUpdated(LocalDateTime.now());
        message.setAuthor(user);
        message.setQuestion(question);

        List<Message> messages = question.getMessages();
        messages.add(message);
        question.setMessages(messages);
        questionRepository.save(question);

        return messageRepository.save(message);
    }

    @Override
    public Page<Message> findAllByActive(Long questionId, Boolean active, Pageable pageable) {
        return messageRepository.findAllByQuestionIdAndActiveOrderByDateCreatedAsc(questionId, true, pageable);
    }

    @Override
    public Page<Message> findAllByInActive(Long questionId, Boolean active, Pageable pageable) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        return messageRepository.findAllByQuestionIdAndActiveAndAuthorIdOrderByDateCreatedDesc(questionId, false, user.getId(), pageable);
    }

    @Override
    public Page<Message> findAllQuestionsForAdmin(Long questionId, Pageable pageable) {
        return this.messageRepository.findAllByQuestionIdOrderByDateCreatedDesc(questionId, pageable);
    }

    @Override
    public Message findMessageById(Long id) {
        return messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public Message updateMessage(Message message) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Message updatedMessage = messageRepository.findById(message.getId()).orElseThrow(MessageNotFoundException::new);

        updatedMessage.setBody(message.getBody());
        updatedMessage.setActive(true);
        updatedMessage.setDateUpdated(LocalDateTime.now());
        updatedMessage.setAuthor(user);

        return messageRepository.save(updatedMessage);
    }


    @Override
    public Message setInactive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Message updateMessage = this.messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        List<Message> childMessages = updateMessage.getChildMessages();

        if (childMessages.size() == 0) {
            updateMessage.setActive(false);
            updateMessage.setDateUpdated(LocalDateTime.now());
//            updateMessage.setDateCreated(LocalDateTime.now()); // to be pushed to bottom when message is not active
            updateMessage.setDeletedByUser(user);
            updateMessage.setDeletedByUserEmail(user.getEmail());
        } else {
            updateMessage.setActive(false);
            updateMessage.setDateUpdated(LocalDateTime.now());
//            updateMessage.setDateCreated(LocalDateTime.now()); // to be pushed to bottom when message is not active
            updateMessage.setDeletedByUser(user);
            updateMessage.setDeletedByUserEmail(user.getEmail());

            childMessages.forEach(msg -> {
                msg.setActive(false);
                msg.setDateUpdated(LocalDateTime.now());
//                msg.setDateCreated(LocalDateTime.now()); // to be pushed to bottom when message is not active
                msg.setDeletedByUser(user);
                msg.setDeletedByUserEmail(user.getEmail());
                List<Message> childOfChild = msg.getChildMessages();
                if (childOfChild.size() > 0) {
                    childOfChild.forEach(child -> {
                        child.setActive(false);
                        child.setDateUpdated(LocalDateTime.now());
                        child.setDeletedByUser(user);
                        child.setDeletedByUserEmail(user.getEmail());
                        this.messageRepository.save(child);
                    });
                }
                this.messageRepository.save(msg);
            });
        }

        return messageRepository.save(updateMessage);
    }

    @Override
    public Message setActive(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);

        Message updateMessage = this.messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        List<Message> childMessages = updateMessage.getChildMessages();

        if (childMessages.size() == 0) {
            updateMessage.setActive(true);
//            if (updateMessage.getParentMessage() != null) {
//                updateMessage.setDateUpdated(updateMessage.getParentMessage().getDateUpdated());
//            } else {
//                updateMessage.setDateUpdated(LocalDateTime.now());
//            }
            updateMessage.setDateUpdated(LocalDateTime.now());
            updateMessage.setDeletedByUser(null);
            updateMessage.setDeletedByUserEmail(null);
        } else {
            updateMessage.setActive(true);
//            if (updateMessage.getParentMessage() != null) {
//                updateMessage.setDateUpdated(updateMessage.getParentMessage().getDateUpdated());
//            } else {
//                updateMessage.setDateUpdated(LocalDateTime.now());
//            }
            updateMessage.setDateUpdated(LocalDateTime.now());
            updateMessage.setDeletedByUser(null);
            updateMessage.setDeletedByUserEmail(null);

            childMessages.forEach(msg -> {
                msg.setActive(true);
//                msg.setDateUpdated(updateMessage.getDateUpdated());
                msg.setDateUpdated(LocalDateTime.now());
                msg.setDeletedByUser(null);
                msg.setDeletedByUserEmail(null);
                List<Message> childOfChild = msg.getChildMessages();
                if (childOfChild.size() > 0) {
                    childOfChild.forEach(child -> {
                        child.setActive(true);
//                        child.setDateUpdated(msg.getDateUpdated());
                        child.setDateUpdated(LocalDateTime.now());
                        child.setDeletedByUser(null);
                        child.setDeletedByUserEmail(null);
                        this.messageRepository.save(child);
                    });
                }
                this.messageRepository.save(msg);
            });
        }

        return messageRepository.save(updateMessage);
    }

    @Override
    public Message createReplyMessage(Long replyMessageId, Long questionId, String email, Message message) {
        User user = userRepository.findByEmailAndActive(email, true).orElseThrow(UserNotFoundException::new);
        Question question = this.questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        Message parentMessage = this.messageRepository.findById(replyMessageId).orElseThrow(MessageNotFoundException::new);

        message.setActive(true);
        message.setDateCreated(parentMessage.getDateCreated().minusSeconds(5));
        message.setDateUpdated(LocalDateTime.now());
        message.setAuthor(user);
        message.setQuestion(question);
        message.setParentMessage(parentMessage);

        List<Message> messages = question.getMessages();
        messages.add(message);
        question.setMessages(messages);
        questionRepository.save(question);

        return messageRepository.save(message);
    }

    @Override
    protected MessageRepository getRepository() {
        return messageRepository;
    }
}
