// GamePanel class for all GUI related components

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;  

public class GamePanel extends JPanel {
    private int[][] board;
    private final int size;
    private int score;
    private int highScore;
    Constant constant = new Constant();

    // Constructor to initialize the GamePanel with 2D array 
    // input - 2D Integer Array, Integer
    public GamePanel(int[][] board, int size, int score, int highScore) {
        this.board = board;
        this.size = size;
        this.score = score;
        this.highScore = highScore; 
    }

    // paintComponent of drawing board and score
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBoard(graphics2D);
        drawScore(graphics2D);
    }

    // Drawing Board and setColor and font size 
    private void drawBoard(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0xfaf8ef));
        graphics2D.fillRect(0, 100, getWidth(), getHeight() - 50); 
        int padding = 15;
        int tileSize = (getWidth() - (padding * (size + 1))) / size; 
        graphics2D.setFont(new Font(constant.FONT_NAME, Font.BOLD, tileSize / 3));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                drawTile(graphics2D, i, j, tileSize, padding);
            }
        }
    }

    // Drawing Tile component 
    // Input - Graphics2d class, Integer
    private void drawTile(Graphics2D graphics2D, int row, int column, int tileSize, int padding) {
        int value = board[row][column];
        int x = column * (tileSize + padding) + padding; 
        int y = row * (tileSize + padding) + padding; 
        GradientPaint gradient = new GradientPaint(x, y, getTileColor(value), x + tileSize, y + tileSize, getTileColor(value).brighter());
        graphics2D.setPaint(gradient);
        graphics2D.fill(new RoundRectangle2D.Float(x, y, tileSize, tileSize, 15, 15));
        
        graphics2D.setColor(getTileBorderColor());
        graphics2D.draw(new RoundRectangle2D.Float(x, y, tileSize, tileSize, 15, 15));
        if (value != 0) {
            graphics2D.setColor(getTileTextColor(value));
            graphics2D.setFont(new Font(constant.FONT_NAME, Font.BOLD, tileSize / 3));
            String text = String.valueOf(value);
            int textWidth = graphics2D.getFontMetrics().stringWidth(text);
            int textHeight = graphics2D.getFontMetrics().getAscent();
            graphics2D.drawString(text, x + (tileSize - textWidth) / 2, y + (tileSize + textHeight) / 2 - 4);
        }
    }

    // Drawing Score in 2048 Game
    // Input - Graphics2D Class
    private void drawScore(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0xbbada0));
        graphics2D.fillRect(0, getHeight() - 50, getWidth(), 50); 
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(new Font(constant.FONT_NAME, Font.BOLD, 28));
        graphics2D.drawString("Score: " + score, 10, getHeight() - 15);
        graphics2D.drawString(constant.BEST + highScore, getWidth() - graphics2D.getFontMetrics().stringWidth(constant.BEST + highScore) - 10, getHeight() - 15);
    }

    private Color getTileColor(int value) {
        return switch (value) {
            case 2 -> new Color(0xeee4da);
            case 4 -> new Color(0xede0c8);
            case 8 -> new Color(0xf2b179);
            case 16 -> new Color(0xf59563);
            case 32 -> new Color(0xf67c5f);
            case 64 -> new Color(0xf65e3b);
            case 128 -> new Color(0xedcf72);
            case 256 -> new Color(0xedcc61);
            case 512 -> new Color(0xedc850);
            case 1024 -> new Color(0xedc53f);
            case 2048 -> new Color(0xedc22e);
            default -> new Color(0xcdc1b4);
        };
    }

    private Color getTileBorderColor() {
        return new Color(0xbbada0); 
    }

    // get Tile Text Color
    // Input - Integer
    private Color getTileTextColor(int value) {
        return value <= 4 ? new Color(0x776e65) : Color.WHITE; 
    }

    // UpdateBoard in 2048 game and repaint the frame
    // Input - 2D Integer Array, Integer
    public void updateBoard(int[][] board, int score, int highScore) {
        this.board = board;
        this.score = score;
        this.highScore = highScore;
        repaint();
    }
}
