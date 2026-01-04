#!/bin/bash
# DeepSeek API 测试脚本

export JAVA_HOME=/Users/wzx/Library/Java/JavaVirtualMachines/openjdk-25/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

if [ -z "$DEEPSEEK_API_KEY" ]; then
    echo "错误：请设置 DEEPSEEK_API_KEY 环境变量"
    echo "例如：export DEEPSEEK_API_KEY=\"your-api-key\""
    exit 1
fi

echo "测试DeepSeek API连接..."
echo "API Key: ${DEEPSEEK_API_KEY:0:10}..." # 只显示前10个字符

# 使用curl测试API
curl -X POST https://api.deepseek.com/v1/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $DEEPSEEK_API_KEY" \
  -d '{
    "model": "deepseek-chat",
    "messages": [
      {
        "role": "user",
        "content": "请用一句话回复：你好"
      }
    ],
    "max_tokens": 100
  }' 2>/dev/null | python3 -m json.tool 2>/dev/null || echo "API调用完成（如果看到JSON输出说明连接成功）"



