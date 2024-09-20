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

    public GamePanel(int[][] board, int size, int score, int highScore) {
        this.board = board;
        this.size = size;
        this.score = score;
        this.highScore = highScore; 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBoard(g2d);
        drawScore(g2d);
    }

    private void drawBoard(Graphics2D g2d) {
        g2d.setColor(new Color(0xfaf8ef));
        g2d.fillRect(0, 100, getWidth(), getHeight() - 50); 
        int padding = 15;
        int tileSize = (getWidth() - (padding * (size + 1))) / size; 
        g2d.setFont(new Font(constant.FONT_NAME, Font.BOLD, 50));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                drawTile(g2d, i, j, tileSize, padding);
            }
        }
    }

    private void drawTile(Graphics2D g2d, int row, int column, int tileSize, int padding) {
        int value = board[row][column];
        int x = column * (tileSize + padding) + padding; 
        int y = row * (tileSize + padding) + padding; 
        GradientPaint gradient = new GradientPaint(x, y, getTileColor(value), x + tileSize, y + tileSize, getTileColor(value).brighter());
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(x, y, tileSize, tileSize, 15, 15));
        
        g2d.setColor(getTileBorderColor());
        g2d.draw(new RoundRectangle2D.Float(x, y, tileSize, tileSize, 15, 15));
        if (value != 0) {
            g2d.setColor(getTileTextColor(value));
            g2d.setFont(new Font(constant.FONT_NAME, Font.BOLD, 50));
            String text = String.valueOf(value);
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textHeight = g2d.getFontMetrics().getAscent();
            g2d.drawString(text, x + (tileSize - textWidth) / 2, y + (tileSize + textHeight) / 2 - 4);
        }
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(new Color(0xbbada0));
        g2d.fillRect(0, getHeight() - 50, getWidth(), 50); 
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(constant.FONT_NAME, Font.BOLD, 28));
        g2d.drawString("Score: " + score, 10, getHeight() - 15);
        g2d.drawString(constant.BEST + highScore, getWidth() - g2d.getFontMetrics().stringWidth(constant.BEST + highScore) - 10, getHeight() - 15);
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

    private Color getTileTextColor(int value) {
        return value <= 4 ? new Color(0x776e65) : Color.WHITE; 
    }

    public void updateBoard(int[][] board, int score, int highScore) {
        this.board = board;
        this.score = score;
        this.highScore = highScore;
        repaint();
    }
}
