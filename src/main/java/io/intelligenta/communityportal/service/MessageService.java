package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService extends BaseEntityCrudService<Message> {

    Message createMessage(Long questionId, String email, Message message);

    Page<Message> findAllByActive(Long questionId, Boolean active, Pageable pageable);

    Page<Message> findAllByInActive(Long questionId, Boolean active, Pageable pageable);

    Page<Message> findAllQuestionsForAdmin(Long questionId, Pageable pageable);

    Message findMessageById(Long id);

    Message updateMessage(Message message);

    Message setInactive(Long id);

    Message setActive(Long id);

    Message createReplyMessage(Long replyMessageId, Long questionId, String email, Message message);
}
