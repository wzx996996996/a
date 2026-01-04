#!/bin/bash
# 五子棋项目启动脚本

# 设置Java 25环境
export JAVA_HOME=/Users/wzx/Library/Java/JavaVirtualMachines/openjdk-25/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# 设置DeepSeek API Key
if [ -z "$DEEPSEEK_API_KEY" ]; then
    export DEEPSEEK_API_KEY="sk-ea182b22c22d403f9d093aaf588c6f98"
fi

# 编译项目
echo "正在编译项目..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "编译失败！"
    exit 1
fi

# 获取依赖的classpath
CLASSPATH=$(mvn dependency:build-classpath -DincludeScope=compile -q -Dmdep.outputFile=/dev/stdout)
CLASSPATH="target/classes:$CLASSPATH"

# 查找JavaFX jar文件路径
JAVAFX_PATH=$(echo "$CLASSPATH" | tr ':' '\n' | grep 'javafx-controls' | head -1 | xargs dirname)

# 运行程序
echo "正在启动五子棋游戏..."
java --module-path "$JAVAFX_PATH" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "$CLASSPATH" \
     com.gobang.GobangApp

