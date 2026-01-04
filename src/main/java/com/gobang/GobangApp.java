package com.gobang;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 五子棋游戏主程序
 * 使用JavaFX构建图形界面
 */
public class GobangApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        GobangBoard board = new GobangBoard();
        // 确保窗口足够大以显示所有组件
        Scene scene = new Scene(board, 750, 850);
        
        primaryStage.setTitle("五子棋 - AI辅助代码生成实验");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);  // 允许调整大小
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(800);
        primaryStage.show();
        
        System.out.println("窗口已显示 - 大小: " + scene.getWidth() + "x" + scene.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

