/**
 * Rudra Pratap Singh
 * Date - 18/09/2024
 * Java code to create a Game Of 2048 with GUI using swing Toolkit
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game2048 {
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
    private boolean moved = false;
    private int score = 0;
    private int highScore = 0;
    private GamePanel gamePanel;
    private CustomStack history = new CustomStack(100);
    Constant constant = new Constant();
    public static void main(String[] args) {
        Game2048 game = new Game2048();
        game.start();
    }

    // Save Previous State in 2048 Game
    private void saveState() {
        int[][] boardCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, size);
        }
        history.push(new GameState(boardCopy, score));
    }

    // Undo functionality to undo the moves
    private void undo() {
        if (!history.isEmpty()) {
            GameState lastState = history.pop();
            if (lastState != null) {  
                board = lastState.board;
                score = lastState.score;
                gamePanel.updateBoard(board, score, highScore);
            }
        }
    }
    
    // Start function for setup JFrame and GUI
    public void start() {
        JFrame frame = new JFrame(constant.APP_TITLE);
        String inputSize = JOptionPane.showInputDialog(frame, constant.ENTER_GRID_SIZE, constant.GRID_SIZE_TITLE,
                JOptionPane.QUESTION_MESSAGE);
        try {
            size = Integer.parseInt(inputSize);
            if (size < 2 || size > 8) {
                JOptionPane.showMessageDialog(frame, constant.INVALID_GRID_SIZE, constant.INVALID_INPUT,
                        JOptionPane.WARNING_MESSAGE);
                size = 4;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, constant.INVALID_GRID_SIZE, constant.INVALID_INPUT,
                    JOptionPane.WARNING_MESSAGE);
            size = 4;
        }
        board = new int[size][size];
        addTile();
        addTile();
        JButton newGameButton = new JButton(constant.NEW_GAME);
        newGameButton.addActionListener(key -> resetGame()); 
        newGameButton.setFont(new Font(constant.FONT_NAME, Font.BOLD, 18));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setBackground(new Color(0x8f7a66)); 
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        newGameButton.setOpaque(true);
        newGameButton.setBorderPainted(false);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(0x8f7a66), 2, true), 
        BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        newGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newGameButton.setBackground(new Color(0xa68b79)); 
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newGameButton.setBackground(new Color(0x8f7a66));
            }
        });
        newGameButton.setFocusable(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        frame.add(buttonPanel, BorderLayout.NORTH);
        gamePanel = new GamePanel(board, size, score, highScore);
        frame.add(gamePanel);
        frame.setSize(500, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(constant.IMAGE_ICON));
        frame.addKeyListener(new KeyAdapter() {
            
            // Invoke Up, Down, Left, Right function based on keys
            // Input - KeyEvent 
            @Override
            public void keyPressed(KeyEvent pressedKey) {
                int key = pressedKey.getKeyCode();
                moved = false;
                switch (key) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> moveUp();
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> moveLeft();
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> moveDown();
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> moveRight();
                    case KeyEvent.VK_Z -> undo();
                }
                if (moved) {
                    addTile();
                    if (isGameOver()) {
                        highScore = Math.max(score, highScore);
                        SoundPlayer.playSound("./game-over.wav");
                        SoundPlayer.playSound("./game-over1.wav");
                        int response = JOptionPane.showConfirmDialog(frame,
                                constant.GAME_OVER_WITH_SCORE + score + constant.WANT_TO_PLAY_AGAIN, constant.GAME_OVER,
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

    // isGameOver function for checking is game over or not
    // return Type - boolean
    private boolean isGameOver() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0 ||
                        (i > 0 && board[i][j] == board[i - 1][j]) ||
                        (i < size - 1 && board[i][j] == board[i + 1][j]) ||
                        (j > 0 && board[i][j] == board[i][j - 1]) ||
                        (j < size - 1 && board[i][j] == board[i][j + 1])) {
                    return false;
                }
            }
        }
        return true;
    }

    //moving the tiles to up side
    private void moveUp() {
        saveState();
        SoundPlayer.playSound(constant.SOUND_FILE_PATH);
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
                        moved = true;
                        target++;
                    } else {
                        board[target][j] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != i) {
                            moved = true;
                        }
                        target++;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[target][j] = lastValue;
                if (target != size - 1) {
                    moved = true;
                }
            }
        }
    }

    //moving the tiles to down side
    private void moveDown() {
        saveState();
        SoundPlayer.playSound(constant.SOUND_FILE_PATH);
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
                        moved = true;
                        target--;
                    } else {
                        board[target][j] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != i) {
                            moved = true;
                        }
                        target--;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[target][j] = lastValue;
                if (target != 0) {
                    moved = true;
                }
            }
        }
    }

    //moving the tiles to left side
    private void moveLeft() {
        saveState();
        SoundPlayer.playSound(constant.SOUND_FILE_PATH);
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
                        moved = true;
                        target++;
                    } else {
                        board[i][target] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != j) {
                            moved = true;
                        }
                        target++;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[i][target] = lastValue;
                if (target != size - 1) {
                    moved = true;
                }
            }
        }
    }

    //moving the tiles to right side
    private void moveRight() {
        saveState();
        SoundPlayer.playSound(constant.SOUND_FILE_PATH);
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
                        moved = true;
                        target--;
                    } else {
                        board[i][target] = lastValue; 
                        lastValue = board[i][j]; 
                        if (target != j) {
                            moved = true;
                        }
                        target--;
                    }
                    board[i][j] = 0; 
                }
            }
            if (lastValue != 0) {
                board[i][target] = lastValue;
                if (target != 0) {
                    moved = true;
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
        int randomPosition = (int) (Math.random() * emptyCount);
        int emptyIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    if (emptyIndex == randomPosition) {
                        board[i][j] = (Math.random() < 0.9) ? 2 : 4; 
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
}
