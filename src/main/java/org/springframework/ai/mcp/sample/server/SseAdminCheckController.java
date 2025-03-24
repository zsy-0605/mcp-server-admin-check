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
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // SSE 端点
    @GetMapping(path = "/check-admin", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter checkAdmin(@RequestParam String userId) {
        SseEmitter emitter = new SseEmitter(60_000L); // 60秒超时

        executor.execute(() -> {
            try {
                boolean isAdmin = ADMIN_ID.equals(userId);
                String eventData = String.format("""
                    data: {"userId": "%s", "isAdmin": %s, "adminId": "%s"}
                    
                    """, userId, isAdmin, ADMIN_ID);

                emitter.send(eventData);
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    // 保留原有工具方法（可选）
    @Tool(description = "Check admin status via SSE")
    public String checkAdminStatus(@RequestParam String userId) {
        return "请使用 SSE 端点 /check-admin?userId={id} 进行查询";
    }
}