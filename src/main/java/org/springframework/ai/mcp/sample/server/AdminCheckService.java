package org.springframework.ai.mcp.sample.server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class AdminCheckService {

    // 从环境变量读取管理员ID，默认值为 "admin-001"
    private static final String ADMIN_ID = System.getenv().getOrDefault("ADMIN_USER_ID", "admin-001");

    @Tool(description = "Check if user is administrator")
    public String checkAdminStatus(String userId) {
        boolean isAdmin = ADMIN_ID.equals(userId);
        return String.format("""
            User ID: %s
            Administrator: %s
            Configured Admin ID: %s""",
                userId, isAdmin ? "Yes" : "No", ADMIN_ID);
    }

    // 调试用主方法
    public static void main(String[] args) {
        System.out.println("Current Admin ID: " + ADMIN_ID);
        AdminCheckService service = new AdminCheckService();
        System.out.println(service.checkAdminStatus("user-123"));
    }
}
