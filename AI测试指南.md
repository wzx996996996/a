# DeepSeek API 功能测试指南

## 测试步骤

### 1. 确保API Key已设置

在运行程序前，确保设置了环境变量：

```bash
export DEEPSEEK_API_KEY="sk-ea182b22c22d403f9d093aaf588c6f98"
```

### 2. 运行程序

```bash
./run-simple.sh
```

### 3. 测试AI代码生成功能

1. **启动程序后**，你会看到五子棋游戏界面
2. **点击"使用AI生成代码"按钮**
3. **观察界面状态**：
   - AI状态标签会显示："AI状态: 正在调用DeepSeek API..."
   - 等待几秒钟（API调用需要时间）
   - 成功后显示："AI状态: 代码生成成功！查看控制台输出"

4. **查看生成的代码**：
   - 打开终端/控制台
   - 查看程序运行的终端窗口
   - 你会看到类似以下输出：

```
========== DeepSeek生成的代码 ==========
[这里会显示AI生成的Java代码]
========================================
```

### 4. 预期的AI生成代码示例

AI应该生成类似这样的代码：

```java
public int countEmptyNeighbors(int[][] board, int row, int col, int boardSize) {
    int count = 0;
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右
    
    for (int[] dir : directions) {
        int newRow = row + dir[0];
        int newCol = col + dir[1];
        if (newRow >= 0 && newRow < boardSize && 
            newCol >= 0 && newCol < boardSize && 
            board[newRow][newCol] == 0) {
            count++;
        }
    }
    return count;
}
```

## 故障排查

### 如果AI调用失败：

1. **检查API Key是否正确**：
   ```bash
   echo $DEEPSEEK_API_KEY
   ```

2. **检查网络连接**：确保能访问 `https://api.deepseek.com`

3. **查看错误信息**：
   - 界面上的AI状态标签会显示错误信息
   - 终端中可能也会有详细的错误堆栈

4. **测试API连接**（可选）：
   ```bash
   ./test-api.sh
   ```

## 功能说明

- **异步调用**：AI API调用在后台线程进行，不会阻塞UI
- **错误处理**：如果API调用失败，会在UI上显示错误信息
- **代码输出**：生成的代码会打印到控制台，方便查看和使用

## 报告用途

这个功能可以用于实验报告的以下部分：

1. **第5章 实现细节**：展示AI API集成的代码
2. **第6章 实验与评估**：测试AI代码生成的成功率和质量
3. **第7章 讨论**：分析AI生成代码的准确性和局限性



