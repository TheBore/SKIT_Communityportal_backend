package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Message;
import io.intelligenta.communityportal.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/message")
public class MessageController extends CrudResource<Message, MessageService> {
    private final MessageService messageService;

    public MessageController (MessageService messageService){
        this.messageService = messageService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{questionId}")
    public Message createMessage(@PathVariable Long questionId, @RequestParam String email, @RequestBody Message message) {
        return messageService.createMessage(questionId, email, message);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Message findById(@PathVariable(value = "id") Long id) {
        return messageService.findMessageById(id);
    }

    @GetMapping("/allMessages/{questionId}")
    public Page<Message> findAllMessages(@PathVariable Long questionId, Pageable pageable) {
        return messageService.findAllQuestionsForAdmin(questionId, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allActive/{questionId}")
    public Page<Message> findAllActiveMessages(@PathVariable Long questionId, Boolean active, Pageable pageable) {
        return messageService.findAllByActive(questionId, true, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allInActive/{questionId}")
    public Page<Message> findAllInActiveMessages(@PathVariable Long questionId, Boolean active, Pageable pageable) {
        return messageService.findAllByInActive(questionId, active, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update")
    public Message updateMessage(@RequestBody Message message) {
        return messageService.updateMessage(message);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public Message deleteMessage(@PathVariable(value = "id") Long id) {
        return messageService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unDelete/{id}")
    public Message unDeleteMessage(@PathVariable(value = "id") Long id) {
        return messageService.setActive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createReplyMessage/{replyMessageId}/{questionId}")
    public Message createReply(@PathVariable Long replyMessageId, @PathVariable Long questionId, @RequestParam String email, @RequestBody Message message) {
        return messageService.createReplyMessage(replyMessageId, questionId, email, message);
    }

    @Override
    public MessageService getService() {
        return messageService;
    }

    @Override
    public Message beforeUpdate(Message oldEntity, Message newEntity) {
        return oldEntity;
    }
}
