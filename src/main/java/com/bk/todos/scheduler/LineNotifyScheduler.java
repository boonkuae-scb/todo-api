package com.bk.todos.scheduler;

import com.bk.todos.services.TodoService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class LineNotifyScheduler {
    @Value("${line.bot.channel-token}")
    private String accessToken;

    private final TodoService todoService;

    @Autowired
    public LineNotifyScheduler(TodoService todoService) {
        this.todoService = todoService;
    }


    @Scheduled(cron = "* * 12 * * *")
    public void pushPendingTodoMessage() {
        System.out.println("pushPendingTodoMessage - " + System.currentTimeMillis() / 1000);
        Map<String, TextMessage> messageList = todoService.processPendingTodo();
        for (Map.Entry<String, TextMessage> message : messageList.entrySet()) {
            this.pushLineMessage(message.getKey(), message.getValue());
        }
    }

    @Scheduled(cron = "* * 18 * * *")
    public void pushSummaryTodoMessage() {
        System.out.println("pushSummaryTodoMessage- " + System.currentTimeMillis() / 1000);
        Map<String, TextMessage> messageList = todoService.processSummaryTodo();
        for (Map.Entry<String, TextMessage> message : messageList.entrySet()) {
            this.pushLineMessage(message.getKey(), message.getValue());
        }
    }

    private void pushLineMessage(String userId, TextMessage textMessage) {
        final LineMessagingClient client = LineMessagingClient
                .builder(accessToken)
                .build();

        final PushMessage pushMessage = new PushMessage(
                userId,
                textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(botApiResponse);
    }
}
