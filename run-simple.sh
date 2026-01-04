#!/bin/bash
# 简单的运行脚本

export JAVA_HOME=/Users/wzx/Library/Java/JavaVirtualMachines/openjdk-25/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

if [ -z "$DEEPSEEK_API_KEY" ]; then
    export DEEPSEEK_API_KEY="sk-ea182b22c22d403f9d093aaf588c6f98"
fi

cd "$(dirname "$0")"

# 编译
mvn clean compile -q || exit 1

# 获取完整的classpath
FULL_CLASSPATH=$(mvn dependency:build-classpath -DincludeScope=compile -q -Dmdep.outputFile=/dev/stdout)
CLASSPATH="target/classes:$FULL_CLASSPATH"

# 从classpath中提取JavaFX jar文件（平台特定版本）
JAVAFX_MODULES=$(echo "$FULL_CLASSPATH" | tr ':' '\n' | grep 'javafx.*mac-aarch64\.jar' | tr '\n' ':')
JAVAFX_MODULES="${JAVAFX_MODULES%:}"  # 移除末尾的冒号

if [ -z "$JAVAFX_MODULES" ]; then
    echo "错误：找不到JavaFX模块，请确保Maven依赖已下载"
    exit 1
fi

# 运行
echo "正在启动五子棋游戏..."
java --module-path "$JAVAFX_MODULES" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "$CLASSPATH" \
     com.gobang.GobangApp

