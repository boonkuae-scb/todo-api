package com.bk.todos.handler;


import com.bk.todos.entity.Todo;
import com.bk.todos.services.TodoService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@LineMessageHandler
public class LineBotHandler {

    @Value("${line.frontend-url}")
    private String frontEndUrl;

    private TodoService todoService;

    @Autowired
    public LineBotHandler(TodoService todoService) {
        this.todoService = todoService;
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        final String userId = event.getSource().getUserId();
        final String originalMessageText = event.getMessage().getText();
        System.out.println("originalMessageText = [" + originalMessageText + "]");

        if (originalMessageText.trim().equalsIgnoreCase("edit")) {
            return new TextMessage(frontEndUrl);
        }

        if (originalMessageText.trim().equalsIgnoreCase("help")) {
            return new TextMessage(
                    "You can create a new to by type format as a below.\n" +
                            "1. task : date/month/year : time e.g Buy milk : 3/5/18 : 13:00\n\n" +
                            "2. task : today : time e.g Finish writing shopping list : today : 15:30 \n\n" +
                            "3. task : tomorrow : time e.g Watch movie : tomorrow : 18:00\n\n" +
                            "To checking all todo list type edit."
            );
        }


        String todoNew = todoService.todoProcessor(userId, originalMessageText);

        return new TextMessage(todoNew);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

}
