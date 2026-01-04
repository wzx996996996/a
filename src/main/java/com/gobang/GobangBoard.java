package com.gobang;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * 五子棋棋盘和游戏逻辑
 * 核心代码：棋盘绘制、下棋逻辑、胜负判断
 */
public class GobangBoard extends BorderPane {
    private static final int BOARD_SIZE = 15;  // 15x15棋盘
    private static final int CELL_SIZE = 40;   // 每个格子40像素
    private static final int OFFSET = 30;      // 边距
    
    private Canvas canvas;
    private GraphicsContext gc;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];  // 0=空, 1=黑, 2=白
    private boolean isBlackTurn = true;  // true=黑棋, false=白棋
    private boolean gameOver = false;
    private Label statusLabel;
    private Label aiStatusLabel;
    
    public GobangBoard() {
        initializeUI();
        drawBoard();
    }
    
    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        // 创建画布
        canvas = new Canvas(BOARD_SIZE * CELL_SIZE + OFFSET * 2, 
                           BOARD_SIZE * CELL_SIZE + OFFSET * 2);
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(this::handleMouseClick);
        
        // 状态标签
        statusLabel = new Label("当前回合: 黑棋");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        aiStatusLabel = new Label("AI状态: 未使用");
        aiStatusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        // 控制按钮
        Button resetButton = new Button("重新开始");
        resetButton.setOnAction(e -> resetGame());
        resetButton.setPrefWidth(130);
        resetButton.setPrefHeight(35);
        
        Button aiButton = new Button("🤖 使用AI生成代码");
        aiButton.setOnAction(e -> demonstrateAICodeGeneration());
        aiButton.setPrefWidth(180);
        aiButton.setPrefHeight(35);
        aiButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // 布局
        VBox topBox = new VBox(10, statusLabel, aiStatusLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #ffffff;");
        
        // 按钮区域 - 使用更明显的样式
        HBox buttonBox = new HBox(20, resetButton, aiButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 2;");
        
        VBox centerBox = new VBox(10, canvas);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(10));
        
        // 设置BorderPane布局
        this.setTop(topBox);
        this.setCenter(centerBox);
        this.setBottom(buttonBox);
        
        // 添加调试输出
        System.out.println("UI初始化完成 - 按钮区域已添加到底部");
        System.out.println("重置按钮: " + resetButton.getText());
        System.out.println("AI按钮: " + aiButton.getText());
    }
    
    /**
     * 绘制棋盘
     */
    private void drawBoard() {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        
        // 绘制网格线
        for (int i = 0; i < BOARD_SIZE; i++) {
            double pos = OFFSET + i * CELL_SIZE;
            gc.strokeLine(OFFSET, pos, OFFSET + (BOARD_SIZE - 1) * CELL_SIZE, pos);
            gc.strokeLine(pos, OFFSET, pos, OFFSET + (BOARD_SIZE - 1) * CELL_SIZE);
        }
        
        // 绘制天元和星位
        int[] starPositions = {3, 7, 11};
        gc.setFill(Color.BLACK);
        for (int x : starPositions) {
            for (int y : starPositions) {
                double px = OFFSET + x * CELL_SIZE;
                double py = OFFSET + y * CELL_SIZE;
                gc.fillOval(px - 3, py - 3, 6, 6);
            }
        }
        
        // 绘制已下的棋子
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != 0) {
                    drawPiece(i, j, board[i][j] == 1);
                }
            }
        }
    }
    
    /**
     * 绘制棋子
     */
    private void drawPiece(int row, int col, boolean isBlack) {
        double x = OFFSET + col * CELL_SIZE;
        double y = OFFSET + row * CELL_SIZE;
        double radius = CELL_SIZE * 0.4;
        
        if (isBlack) {
            gc.setFill(Color.BLACK);
        } else {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
        }
        
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        if (!isBlack) {
            gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }
    
    /**
     * 处理鼠标点击事件
     */
    private void handleMouseClick(MouseEvent event) {
        if (gameOver) return;
        
        double x = event.getX();
        double y = event.getY();
        
        // 计算点击的格子坐标
        int col = (int) Math.round((x - OFFSET) / CELL_SIZE);
        int row = (int) Math.round((y - OFFSET) / CELL_SIZE);
        
        if (col >= 0 && col < BOARD_SIZE && row >= 0 && row < BOARD_SIZE) {
            if (board[row][col] == 0) {
                board[row][col] = isBlackTurn ? 1 : 2;
                drawBoard();
                
                // 检查胜负
                if (checkWin(row, col)) {
                    gameOver = true;
                    statusLabel.setText("游戏结束！" + (isBlackTurn ? "黑棋" : "白棋") + "获胜！");
                    statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");
                } else {
                    isBlackTurn = !isBlackTurn;
                    statusLabel.setText("当前回合: " + (isBlackTurn ? "黑棋" : "白棋"));
                }
            }
        }
    }
    
    /**
     * 检查是否获胜（五连珠）
     */
    private boolean checkWin(int row, int col) {
        int player = board[row][col];
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};  // 横、竖、左斜、右斜
        
        for (int[] dir : directions) {
            int count = 1;  // 包含当前棋子
            
            // 正向检查
            for (int i = 1; i < 5; i++) {
                int newRow = row + dir[0] * i;
                int newCol = col + dir[1] * i;
                if (newRow >= 0 && newRow < BOARD_SIZE && 
                    newCol >= 0 && newCol < BOARD_SIZE && 
                    board[newRow][newCol] == player) {
                    count++;
                } else {
                    break;
                }
            }
            
            // 反向检查
            for (int i = 1; i < 5; i++) {
                int newRow = row - dir[0] * i;
                int newCol = col - dir[1] * i;
                if (newRow >= 0 && newRow < BOARD_SIZE && 
                    newCol >= 0 && newCol < BOARD_SIZE && 
                    board[newRow][newCol] == player) {
                    count++;
                } else {
                    break;
                }
            }
            
            if (count >= 5) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 重置游戏
     */
    private void resetGame() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        isBlackTurn = true;
        gameOver = false;
        statusLabel.setText("当前回合: 黑棋");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        drawBoard();
    }
    
    /**
     * 演示AI代码生成功能
     */
    private void demonstrateAICodeGeneration() {
        System.out.println("=== AI代码生成功能被触发 ===");
        aiStatusLabel.setText("AI状态: 正在调用DeepSeek API...");
        
        // 使用DeepSeek API生成一个简单的辅助方法
        DeepSeekAPI deepSeek = new DeepSeekAPI();
        
        // 如果环境变量中有API Key，确保使用它
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        System.out.println("API Key检查: " + (apiKey != null && !apiKey.isEmpty() ? "已设置（长度: " + apiKey.length() + "）" : "未设置"));
        if (apiKey != null && !apiKey.isEmpty()) {
            deepSeek.setApiKey(apiKey);
            System.out.println("API Key已设置到DeepSeekAPI对象");
        } else {
            System.out.println("警告: API Key未设置！");
            javafx.application.Platform.runLater(() -> {
                aiStatusLabel.setText("AI状态: 错误 - API Key未设置");
                aiStatusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d32f2f;");
            });
            return;
        }
        
        String prompt = "请生成一个Java方法，用于计算五子棋棋盘上某个位置周围的空格数量（上下左右四个方向）。" +
                       "方法签名：public int countEmptyNeighbors(int[][] board, int row, int col, int boardSize)" +
                       "要求：返回值为该位置周围（上下左右）空格的数量，board为0表示空格，非0表示已有棋子。";
        
        System.out.println("正在发送API请求...");
        new Thread(() -> {
            try {
                String generatedCode = deepSeek.generateCode(prompt);
                System.out.println("API调用成功，收到响应");
                javafx.application.Platform.runLater(() -> {
                    aiStatusLabel.setText("AI状态: 代码生成成功！查看控制台");
                    aiStatusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32;");
                    System.out.println("========== DeepSeek生成的代码 ==========");
                    System.out.println(generatedCode);
                    System.out.println("========================================");
                });
            } catch (Exception e) {
                System.err.println("API调用异常:");
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    String errorMsg = e.getMessage();
                    if (errorMsg == null) {
                        errorMsg = e.getClass().getSimpleName();
                    }
                    System.out.println("错误信息: " + errorMsg);
                    if (errorMsg.length() > 60) {
                        errorMsg = errorMsg.substring(0, 60) + "...";
                    }
                    aiStatusLabel.setText("AI状态: 失败 - " + errorMsg);
                    aiStatusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d32f2f;");
                });
            }
        }).start();
    }
}

