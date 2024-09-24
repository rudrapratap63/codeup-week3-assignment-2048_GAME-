/**
 * Rudra Pratap Singh
 * Date - 18/09/2024
 * Java code to create a Game Of 2048 with GUI using swing and awt Toolkit
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;  

public class Game {
    class GameState {
        int[][] board;
        int score;
        GameState(int[][] board, int score) {
            this.board = board;
            this.score = score;
        }
    }
    private int size;
    private int[][] board;
    private boolean isMoved = false;
    private int score = 0;
    private int highScore = 0;
    private GamePanel gamePanel;
    private CustomStack history = new CustomStack(100);
    public static   void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    // Save Previous State in 2048 Game
    private void saveState() {
        int[][] boardCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, size);
        }
        GameState gameState = new GameState(boardCopy, score);
        history.insertIntoStack(gameState);
    }

    // Undo functionality to undo the moves and update the game board
    private void undo() {
        if (!history.isStackEmpty()) {
            GameState lastState = history.removeFromStack();
            if (lastState != null) {  
                board = lastState.board;
                score = lastState.score;
                gamePanel.updateBoard(board, score, highScore);
            }
        }
    }
    
    // Start function for setup JFrame and GUI
    public void start() {
        JFrame frame = new JFrame(Constant.APP_TITLE);
        String inputSize = JOptionPane.showInputDialog(frame, Constant.ENTER_GRID_SIZE, Constant.GRID_SIZE_TITLE,
                JOptionPane.QUESTION_MESSAGE);
                
        // This try catch block handle Number format exception if the user give invalid format        
        try {
            size = Integer.parseInt(inputSize);
            if (size < 2 || size > 8) {
                JOptionPane.showMessageDialog(frame, Constant.INVALID_GRID_SIZE, Constant.INVALID_INPUT,
                        JOptionPane.WARNING_MESSAGE);
                size = 4;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, Constant.INVALID_GRID_SIZE, Constant.INVALID_INPUT,
                    JOptionPane.WARNING_MESSAGE);
            size = 4;
        }
        board = new int[size][size];
        addTile();
        addTile();
        JButton newGameButton = new JButton(Constant.NEW_GAME);
        newGameButton.addActionListener(key -> resetGame()); 
        newGameButton.setFont(new Font(Constant.FONT_NAME, Font.BOLD, 18));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setBackground(new Color(0x8f7a66)); 
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        newGameButton.setOpaque(true);
        newGameButton.setBorderPainted(false);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(0x8f7a66), 2, true), 
        BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        newGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                newGameButton.setBackground(new Color(0xa68b79)); 
            }
            @Override
            public void mouseExited(MouseEvent event) {
                newGameButton.setBackground(new Color(0x8f7a66));
            }
        });
        newGameButton.setFocusable(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        boardLayoutCustomization(frame, buttonPanel);
        frame.addKeyListener(new KeyAdapter() {
            // Invoke Up, Down, Left, Right function based on keys
            // Input - KeyEvent 
            @Override
            public void keyPressed(KeyEvent pressedKey) {
                int key = pressedKey.getKeyCode();
                isMoved = false;
                switch (key) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> moveUp();
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> moveLeft();
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> moveDown();
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> moveRight();
                    case KeyEvent.VK_Z -> undo();
                }
                if (isMoved) {
                    addTile();
                    if (isGameOver()) {
                        highScore = score >= highScore ? score : highScore;
                        SoundPlayer.playSound("./game-over.wav");
                        SoundPlayer.playSound("./game-over1.wav");
                        int response = JOptionPane.showConfirmDialog(frame,
                                Constant.GAME_OVER_WITH_SCORE + score + Constant.WANT_TO_PLAY_AGAIN, Constant.GAME_OVER,
                                JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            resetGame();
                        } else {
                            System.exit(0);
                        }
                    }
                    gamePanel.updateBoard(board, score, highScore);
                }
            }
        });
    }

    // This method for customizing board like setSize, etc
    // Output - JFrame Object, JPanel Object
    public void boardLayoutCustomization(JFrame frame, JPanel buttonPanel){
        frame.add(buttonPanel, BorderLayout.NORTH);
        gamePanel = new GamePanel(board, size, score, highScore);
        frame.add(gamePanel);
        frame.setSize(500, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Constant.IMAGE_ICON));
    }

    // isGameOver function for checking is game over or not
    // return Type - boolean
    private boolean isGameOver() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] == 0 || (i > 0 && board[i][j] == board[i - 1][j])) return false;
                if((i < size - 1 && board[i][j] == board[i + 1][j]) || (j > 0 && board[i][j] == board[i][j - 1])) return false;
                if((j < size - 1 && board[i][j] == board[i][j + 1])) return false;
            }
        }
        return true;
    }

    //moving the tiles to up side
    private void moveUp() {
        saveState();
        SoundPlayer.playSound(Constant.SOUND_FILE_PATH);
        for (int j = 0; j < size; j++) {
            int target = 0; 
            int lastValue = 0; 
            for (int i = 0; i < size; i++) {
                if (board[i][j] != 0) {
                    if (lastValue == 0) {
                        lastValue = board[i][j]; 
                    } else if (lastValue == board[i][j]) {
                        board[target][j] = lastValue * 2; 
                        score += board[target][j];
                        lastValue = 0; 
                        isMoved = true;
                        target++;
                    } else {
                        board[target][j] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != i) {
                            isMoved = true;
                        }
                        target++;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[target][j] = lastValue;
                if (target != size - 1) {
                    isMoved = true;
                }
            }
        }
    }

    //moving the tiles to down side
    private void moveDown() {
        saveState();
        SoundPlayer.playSound(Constant.SOUND_FILE_PATH);
        for (int j = 0; j < size; j++) {
            int target = size - 1; 
            int lastValue = 0; 
            for (int i = size - 1; i >= 0; i--) {
                if (board[i][j] != 0) {
                    if (lastValue == 0) {
                        lastValue = board[i][j]; 
                    } else if (lastValue == board[i][j]) {
                        board[target][j] = lastValue * 2; 
                        score += board[target][j];
                        lastValue = 0; 
                        isMoved = true;
                        target--;
                    } else {
                        board[target][j] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != i) {
                            isMoved = true;
                        }
                        target--;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[target][j] = lastValue;
                if (target != 0) {
                    isMoved = true;
                }
            }
        }
    }

    //moving the tiles to left side
    private void moveLeft() {
        saveState();
        SoundPlayer.playSound(Constant.SOUND_FILE_PATH);
        for (int i = 0; i < size; i++) {
            int target = 0; 
            int lastValue = 0; 

            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    if (lastValue == 0) {
                        lastValue = board[i][j]; 
                    } else if (lastValue == board[i][j]) {
                        board[i][target] = lastValue * 2; 
                        score += board[i][target];
                        lastValue = 0; 
                        isMoved = true;
                        target++;
                    } else {
                        board[i][target] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != j) {
                            isMoved = true;
                        }
                        target++;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[i][target] = lastValue;
                if (target != size - 1) {
                    isMoved = true;
                }
            }
        }
    }

    //moving the tiles to right side
    private void moveRight() {
        saveState();
        SoundPlayer.playSound(Constant.SOUND_FILE_PATH);
        for (int i = 0; i < size; i++) {
            int target = size - 1; 
            int lastValue = 0; 
            for (int j = size - 1; j >= 0; j--) {
                if (board[i][j] != 0) {
                    if (lastValue == 0) {
                        lastValue = board[i][j]; 
                    } else if (lastValue == board[i][j]) {
                        board[i][target] = lastValue * 2; 
                        score += board[i][target];
                        lastValue = 0; 
                        isMoved = true;
                        target--;
                    } else {
                        board[i][target] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != j) {
                            isMoved = true;
                        }
                        target--;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[i][target] = lastValue;
                if (target != 0) {
                    isMoved = true;
                }
            }
        }
    }

    // adding tiles to frame
    private void addTile() {
        int emptyCount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    emptyCount++;
                }
            }
        }
        if (emptyCount == 0) {
            return;
        }
        double randomValue = customRandom();  
        int randomPosition = (int) (randomValue * emptyCount);  
        int emptyIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    if (emptyIndex == randomPosition) {
                        randomValue = customRandom();
                        board[i][j] = (randomValue < 0.9) ? 2 : 4;
                        return;
                    }
                    emptyIndex++;
                }
            }
        }
    }

    // for Reset the game and update the board
    private void resetGame() {
        board = new int[size][size];
        score = 0;
        addTile();
        addTile();
        gamePanel.updateBoard(board, score, highScore);
    }

    // This function is used for generating random number between 0 and 1
    // Return - double
    public static double customRandom() {
        long currentTime = System.nanoTime();  
        return (currentTime % 1000) / 1000.0;  
    }
}
