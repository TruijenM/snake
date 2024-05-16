import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int unitSize = 25;
    static final int gameUnits = (screenWidth * screenHeight) / unitSize;
    static final int delay = 75;
    final int[] x = new int[gameUnits];
    final int[] y = new int[gameUnits];
    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char currentDirection = 'R';
    private LinkedList<Character> directions = new LinkedList<>();
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
//            for (int i = 0; i < screenHeight / unitSize; i++) {
//                g.drawLine(i * unitSize, 0, i * unitSize, screenHeight);
//                g.drawLine(0, i * unitSize, screenWidth, i * unitSize);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, unitSize, unitSize);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(43, 145, 11));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
                g.setColor(Color.red);
                g.setFont(new Font("LinkedList Free", Font.PLAIN, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + appleEaten, (screenWidth - metrics.stringWidth("Score: " + appleEaten)) / 2, g.getFont().getSize());
            }
        } else gameOver(g);
    }

    public void newApple() {
        appleX = random.nextInt((int) (screenWidth / unitSize)) * unitSize;
        appleY = random.nextInt((int) (screenWidth / unitSize)) * unitSize;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
            if (!directions.isEmpty()) {
                currentDirection = directions.pop();
            }
        }
        switch (currentDirection) {
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case 'D':
                y[0] = y[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'R':
                x[0] = x[0] + unitSize;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkColissions() {
        //check if had collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0]) == x[i] && (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if (x[0] > screenWidth) {
            running = false;
        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if (y[0] > screenHeight) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("LinkedList Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screenWidth - metrics.stringWidth("Game Over")) / 2, screenHeight / 2);

        g.setColor(Color.red);
        g.setFont(new Font("LinkedList Free", Font.PLAIN, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + appleEaten, (screenWidth - metrics2.stringWidth("Score: " + appleEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkColissions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (currentDirection != 'R') {
                        directions.add('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (currentDirection != 'L') {
                        directions.add('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (currentDirection != 'D') {
                        directions.add('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (currentDirection != 'U') {
                        directions.add('D');
                    }
                    break;
            }
        }
    }
}
