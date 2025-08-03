import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class KeyInput extends KeyAdapter {
	
	private Paddle p1;
	private boolean up1 = false;
	private boolean down1 = false;
	
	private Paddle p2;
	private boolean up2 = false;
	private boolean down2 = false;

	public KeyInput(Paddle p1, Paddle p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP) {
			p2.switchDirection(-1);
			up2 = true;
		}
		if(key == KeyEvent.VK_DOWN) {
			p2.switchDirection(1);
			down2 = true;
		}
		if(key == KeyEvent.VK_W) {
			p1.switchDirection(-1);
			up1 = true;
		}
		if(key == KeyEvent.VK_S) {
			p1.switchDirection(1);
			down1 = true;
		}
		
		if(key == KeyEvent.VK_ENTER) {
			pongGame.running = true;
		}
	}
	
public void keyReleased(KeyEvent e) {
		
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP) {
			up2 = false;
		}
		if(key == KeyEvent.VK_DOWN) {
			down2 = false;
		}
		if(key == KeyEvent.VK_W) {
			up1 = false;
		}
		if(key == KeyEvent.VK_S) {
			down1 = false;
		}
		
		if(!up1 && !down1) {
			p1.stop();
		}
		if(!up2 && !down2) {
			p2.stop();
		}
		
	}
}


class Ball {
	
	public static final int SIZE = 16;
	
	private int x, y;
	private int xVel, yVel;
	private int speed = 5;
	
	public Ball() {
		
		reset();
		
	}

	private void reset() {
		
		x = pongGame.WIDTH/2 - SIZE/2;
		y = pongGame.HEIGHT/2 - SIZE/2;
	
		xVel = pongGame.sign(Math.random()*2 -1);
		yVel = pongGame.sign(Math.random()*2 -1);
	
       // xVel = (Math.random() < 0.5) ? -1 : 1;
        // yVel = (Math.random() < 0.5) ? -1 : 1;
	}
	
	public void changeXDir() {
		xVel *= -1;
	}
	
	public void changeYDir() {
		yVel *= -1;
	}

	public void draw(Graphics g) {
		
		g.setColor(Color.white);
		g.fillOval(x, y, SIZE, SIZE);
		
	}

	public void update(Paddle paddle1, Paddle paddle2) {
		// ball movement
		x += xVel * speed;
		y += yVel * speed;
		
		// for collisions with upper and lower walls
		if(y >= pongGame.HEIGHT - SIZE || y<= 0) {
			changeYDir();
		}
		
		// for paddle miss
		if(x >= pongGame.WIDTH - SIZE) {
			paddle1.addPoint();
			reset();
		}
		if(x <= 0) {
			paddle2.addPoint();
			reset();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}

class Paddle {
	
	private int x, y;
	private int vel = 0;
	private int speed = 10;
	private int width = 25, height = 100;
	private int score;
	private Color color;
	private boolean left;
	
	public Paddle(Color c, boolean left) {
		color = c;
		this.left = left;
		if (this.left) {
			x = 0;
		}
		else {
			x = pongGame.WIDTH - width;
		}
		y = pongGame.HEIGHT/2 - height/2;
	}
	
	public void addPoint() {
		score++;
	}

	public void draw(Graphics g) {
		
		// draw paddle
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		// draw score
		String scoreText = Integer.toString(score);
		int sx;
		Font font = new Font("ROBOTO", Font.PLAIN, 50);
		int strWidth = g.getFontMetrics(font).stringWidth(scoreText);
		int padding = 25;
		
		if(left) {
			sx = pongGame.WIDTH/2 - strWidth - padding;
		}
		else {
			sx = pongGame.WIDTH/2 + padding;
		}
		
		g.setFont(font);
		g.drawString(scoreText, sx, 50);
		
	}

	public void update(Ball ball) {
		// paddle movement
		if(y > pongGame.HEIGHT - height) { y = pongGame.HEIGHT - height; }
		else if(y <0) { y = 0; }
		else { y += vel; }
		
		int ballX = ball.getX();
		int ballY = ball.getY();
		
		// paddle hits
		if(left) {
			if(ballX <= width && ballY >= y-Ball.SIZE && ballY <= y+height) {
				ball.changeXDir();
			}
		}
		else {
			if(ballX >= x - Ball.SIZE && ballY >= y-Ball.SIZE && ballY <= y+height) {
				ball.changeXDir();
			}
		}
		
	}
	
	public void switchDirection(int dir) {
		
		vel = speed * dir;
		
	}
	
	public void stop() {
		
		vel = 0;
		
	}

}

class Window extends JFrame {

    private static final long serialVersionUID = 1L;

    public Window(int width, int height, String title, pongGame game) {
        super(title);
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        add(game);
        pack();
        setVisible(true);
    }
}
public class pongGame extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = WIDTH * 9/16;
	
	Ball ball;
	Paddle paddle1;
	Paddle paddle2;
	
	public static boolean running = false;
	private Thread gameThread;


	
	public pongGame() {
		
		canvasSetup();
		initialize();
		this.addKeyListener(new KeyInput(paddle1, paddle2));
		this.setFocusable(true);
        new Window(WIDTH, HEIGHT, "ping Pong", this);
        start();
	}

	private void initialize() {
		// initialize ball
		ball = new Ball();
		
		// initialize paddles
		paddle1 = new Paddle(Color.CYAN, true);
		paddle2 = new Paddle(Color.ORANGE, false);
		
	}

	private void canvasSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
	}

	@Override
	public void run() {
		
		this.requestFocus();
		
		//game timer
		long lastTime = System.nanoTime();
		double  maxFPS = 60.0;
		double frameTime = 1000000000/maxFPS;
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime)/frameTime;
			lastTime = now;
			if(delta>=1) {
				update();
				delta--;
				draw();
			}
		}
		stop();
		
		
	}

	private void draw() {
		// initialize drawing tools
		BufferStrategy buffer = this.getBufferStrategy();
		if (buffer == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = buffer.getDrawGraphics();
		
		// draw background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// draw ball
		ball.draw(g);
		
		// draw paddles
		paddle1.draw(g);
		paddle2.draw(g);
		
		// dispose all on screen
		g.dispose();
		buffer.show();
		
	}

	private void update() {
		// update ball
		ball.update(paddle1, paddle2);
		
		// update paddles
		paddle1.update(ball);
		paddle2.update(ball);
		
	}

	public void start() {
    running = true;
    gameThread = new Thread(this);
    gameThread.start();
}
	
	public void stop() {
		
		try {
			gameThread.join();
			running = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int sign(double d) {
		
		if (d>=0) {return 1;}
		else {return -1;}
	}

}
