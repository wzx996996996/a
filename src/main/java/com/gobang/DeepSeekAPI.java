package com.gobang;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek API 封装类
 * 用于调用DeepSeek API进行代码生成
 */
public class DeepSeekAPI {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private String apiKey;
    private OkHttpClient client;
    private Gson gson;
    
    public DeepSeekAPI() {
        // 从环境变量或配置文件读取API Key
        this.apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            // 如果环境变量未设置，使用默认值（实际使用时应配置）
            this.apiKey = "your-api-key-here";
        }
        
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        
        this.gson = new Gson();
    }
    
    /**
     * 生成代码
     * @param prompt 提示词
     * @return 生成的代码
     */
    public String generateCode(String prompt) throws IOException {
        // 构建请求体
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "deepseek-chat");
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 1000);
        
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        
        JsonArray messages = new JsonArray();
        messages.add(message);
        requestBody.add("messages", messages);
        
        // 创建HTTP请求
        RequestBody body = RequestBody.create(
                gson.toJson(requestBody),
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();
        
        // 发送请求并解析响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API调用失败: " + response.code() + " " + response.message());
            }
            
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            // 提取生成的代码
            if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                JsonObject choice = jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject();
                JsonObject messageObj = choice.getAsJsonObject("message");
                return messageObj.get("content").getAsString();
            } else {
                throw new IOException("API响应格式错误");
            }
        }
    }
    
    /**
     * 设置API Key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

