# JavaFX 五子棋 - AI辅助代码生成实验项目

## 项目简介

本项目是《AI辅助程序设计》课程的实验项目，旨在探索使用DeepSeek AI辅助生成JavaFX代码的实践。项目实现了一个完整的五子棋游戏，并集成了DeepSeek API用于代码生成演示。

## 技术栈

- **Java**: 17+
- **JavaFX**: 21
- **Maven**: 项目构建工具
- **DeepSeek API**: AI代码生成
- **OkHttp**: HTTP客户端
- **Gson**: JSON处理

## 项目结构

```
src/main/java/com/gobang/
├── GobangApp.java          # 主程序入口
├── GobangBoard.java        # 棋盘UI和游戏逻辑（核心代码）
└── DeepSeekAPI.java        # DeepSeek API封装类
```

## 环境配置

### 1. 前置要求

- JDK 17 或更高版本
- Maven 3.6+

### 2. DeepSeek API Key配置

创建API Key：
1. 访问 https://platform.deepseek.com/
2. 注册账号并获取API Key
3. 配置环境变量：

```bash
export DEEPSEEK_API_KEY="your-api-key-here"
```

或者在代码中直接设置（不推荐用于生产环境）。

## 编译与运行

### 前置要求

确保使用 Java 11+ 版本。如果系统默认是 Java 8，需要设置 JAVA_HOME：

```bash
# 检查可用Java版本
/usr/libexec/java_home -V

# 设置Java 25（根据你的系统调整）
export JAVA_HOME=/Users/wzx/Library/Java/JavaVirtualMachines/openjdk-25/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
```

### 编译项目

```bash
# 在项目根目录执行
mvn clean compile
```

### 运行程序

**方法1：使用提供的脚本（推荐）**

```bash
# 设置API Key（可选，如果已在环境变量中设置）
export DEEPSEEK_API_KEY="your-api-key"

# 运行脚本
./run-simple.sh
```

**方法2：手动运行**

```bash
# 1. 编译
mvn clean compile

# 2. 运行（需要设置JavaFX模块路径）
java --module-path $HOME/.m2/repository/org/openjfx/javafx-controls/21:$HOME/.m2/repository/org/openjfx/javafx-fxml/21 \
     --add-modules javafx.controls,javafx.fxml \
     -cp "target/classes:$(mvn dependency:build-classpath -DincludeScope=compile -q -Dmdep.outputFile=/dev/stdout)" \
     com.gobang.GobangApp
```

## 功能特性

1. **五子棋游戏核心功能**
   - 15x15标准棋盘
   - 黑白双方轮流下棋
   - 自动判断胜负（五连珠）
   - 重新开始功能

2. **AI代码生成演示**
   - 集成DeepSeek API
   - 通过自然语言提示生成代码
   - 实时显示生成状态

## 代码说明

### 核心代码统计

- `GobangBoard.java`: 约280行（核心游戏逻辑和UI）
- `DeepSeekAPI.java`: 约90行（API封装）
- `GobangApp.java`: 约20行（入口类）

总计核心代码约390行，符合报告要求的150-300行范围（核心部分）。

## 实验报告要点

本项目的实现可用于以下报告章节：

1. **方案设计**: 系统架构图、模块划分、Prompt设计
2. **实现细节**: 关键代码片段、UI截图
3. **实验评估**: 功能测试、API调用成功率、响应时间

## 参考文献示例

1. OpenJFX官方文档. https://openjfx.io/
2. DeepSeek API文档. https://platform.deepseek.com/api-docs/
3. Maven官方文档. https://maven.apache.org/
4. JavaFX Canvas API. Oracle Documentation
5. 五子棋算法研究. 相关学术论文

## 许可证

本项目仅用于教学实验目的。

## 作者

[学生姓名]
[学号]
[完成日期]

