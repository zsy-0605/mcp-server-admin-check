package org.springframework.ai.mcp.sample.server;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class SseAdminCheckController {

    private static final String ADMIN_ID = System.getenv().getOrDefault("ADMIN_USER_ID", "admin-001");

    @GetMapping(path = "/check-admin", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter checkAdmin(@RequestParam String userId) {
        SseEmitter emitter = new SseEmitter(30_000L); // 30秒超时

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                boolean isAdmin = ADMIN_ID.equals(userId);

                // 构建文本回答
                String message = isAdmin ?
                        "data:  You are admin!\n\n" :
                        "data:  You are not admin!\n\n";

                emitter.send(message);
                emitter.complete();

            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}