
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RacingGame extends JPanel implements ActionListener, KeyListener {

    
    final int WIDTH = 500;
    final int HEIGHT = 600;

    
    int carX = 225;
    int carY = 450;
    int carWidth = 50;
    int carHeight = 90;

    
    int enemyX = 225;
    int enemyY = -100;
    int enemyWidth = 50;
    int enemyHeight = 90;

    
    int score = 0;
    int speed = 5;
    int roadOffset = 0;
    boolean gameOver = false;

    Random random = new Random();
    Timer timer;

    public RacingGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);

        setFocusable(true);
        addKeyListener(this);

        // Game updates approximately every 16 milliseconds (~60 FPS)
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        g.setColor(new Color(30, 130, 50));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        
        g.setColor(Color.GRAY);
        g.fillRect(100, 0, 300, HEIGHT);

        
        g.setColor(Color.WHITE);
        g.fillRect(100, 0, 5, HEIGHT);
        g.fillRect(395, 0, 5, HEIGHT);

        
        g.setColor(Color.WHITE);

        for (int y = -80 + roadOffset; y < HEIGHT; y += 100) {
            g.fillRect(200, y, 5, 50);
            g.fillRect(300, y, 5, 50);
        }

       
        g.setColor(Color.RED);
        g.fillRoundRect(carX, carY, carWidth, carHeight, 10, 10);

        
        g.setColor(Color.CYAN);
        g.fillRect(carX + 10, carY + 15, 30, 20);
        g.fillRect(carX + 10, carY + 55, 30, 15);

        
        g.setColor(Color.BLUE);
        g.fillRoundRect(enemyX, enemyY, enemyWidth, enemyHeight, 10, 10);

        
        g.setColor(Color.BLACK);
        g.fillRect(enemyX + 10, enemyY + 15, 30, 20);

        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Score: " + score, 15, 30);

        g.drawString("Speed: " + speed, 15, 60);

        
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 190));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 45));
            g.drawString("GAME OVER", 95, 250);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Final Score: " + score, 155, 310);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press R to Restart", 160, 360);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {

            
            // Move the road lines downwards to create a moving effect
            roadOffset += speed;

            if (roadOffset >= 100) {
                roadOffset = 0;
            }

            
            enemyY += speed;

            
            if (enemyY > HEIGHT) {
                enemyY = -100;

                
                int lane = random.nextInt(3);

                if (lane == 0) {
                    enemyX = 125;
                } else if (lane == 1) {
                    enemyX = 225;
                } else {
                    enemyX = 325;
                }

                score++;

                
                if (score % 5 == 0) {
                    speed++;
                }
            }

            
            Rectangle player = new Rectangle(
                carX, carY, carWidth, carHeight
            );

            Rectangle enemy = new Rectangle(
                enemyX, enemyY, enemyWidth, enemyHeight
            );

            if (player.intersects(enemy)) {
                gameOver = true;
                timer.stop();
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (!gameOver) {

            if (key == KeyEvent.VK_LEFT) {
                carX -= 25;
            }

            if (key == KeyEvent.VK_RIGHT) {
                carX += 25;
            }

            // Road boundaries to prevent the car from going off the road
            if (carX < 105) {
                carX = 105;
            }

            if (carX > 345) {
                carX = 345;
            }
        }

        // Restart the game when 'R' is pressed after game over
        if (key == KeyEvent.VK_R && gameOver) {
            restartGame();
        }

        repaint();
    }

    public void restartGame() {

        carX = 225;
        enemyX = 225;
        enemyY = -100;

        score = 0;
        speed = 5;
        roadOffset = 0;
        gameOver = false;

        timer.start();
        requestFocusInWindow();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Java Racing Game");

            RacingGame game = new RacingGame();

            frame.add(game);
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

            game.requestFocusInWindow();
        });
    }
}
