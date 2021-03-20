package sample;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Controller extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int body = 6;
    int pointsGot;
    int pointX;
    int pointY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    Controller(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyAdapter());
        startGame();
    }
    public void startGame(){
        newPoints();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            g.setColor(Color.orange);
            g.fillOval(pointX, pointY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < body; i++) {
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Casey", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + pointsGot, (SCREEN_WIDTH - metrics.stringWidth("Score: " + pointsGot))/2,
                    g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newPoints(){
        pointX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        pointY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for(int i = body; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction){
            case 'U':
                 y[0] = y[0] - UNIT_SIZE;
                 break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkPoint(){
        if(x[0] == pointX && y[0] == pointY){
            body++;
            pointsGot++;
            newPoints();
        }
    }
    public void checkCollisions(){
        for (int i = body; i > 0; i--) {
            if(x[0] == x[i] && y[0] == y[i]){
                running = false;
            }
            if (x[0] < 0){
                running = false;
            }
            if (x[0] > SCREEN_WIDTH){
                running = false;
            }
            if (y[0] < 0){
                running = false;
            }
            if (y[0] > SCREEN_HEIGHT){
                running = false;
            }
            if (!running){
                timer.stop();
            }
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Casey", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + pointsGot, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + pointsGot))/2,
                SCREEN_HEIGHT/2);

        g.setColor(Color.red);
        g.setFont(new Font("Casey", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT);

        g.setColor(Color.white);
        g.setFont(new Font("Casey", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Quit and start a game again",
                (SCREEN_WIDTH - metrics3.stringWidth("Quit and start a game again"))/2, SCREEN_HEIGHT/3);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkPoint();
            checkCollisions();
        }
        repaint();
    }
    public class MyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
