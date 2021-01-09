import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	private static final String GAME_OVER = "Game Over";
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 600;
	public static final int UNIT_SIZE = 25;
	public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	public static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEatten;
	int applesX;
	int applesY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MykeyAdapter());
		startGame();

	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}

	public void draw(Graphics graphics) {
		if (running) {
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			graphics.setColor(Color.red);
			graphics.fillOval(applesX, applesY, UNIT_SIZE, UNIT_SIZE);
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					graphics.setColor(Color.green);
					graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					graphics.setColor(new Color(45, 180, 0));
					graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			graphics.setColor(Color.red);
			graphics.setFont(new Font("Family", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Score: " + applesEatten,
					(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEatten)) / 2, graphics.getFont().getSize());
		} else {
			gameOver(graphics);
		}
	}

	public void newApple() {
		applesX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		applesY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
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
		default:
			break;
		}
	}

	public void checkApple() {
		if (x[0] == applesX && (y[0] == applesY)) {
			bodyParts++;
			applesEatten++;
			newApple();
		}
	}

	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i == 0; i--) {
			if (x[0] == x[i] && (y[0] == y[i])) {
				running = false;
			}
		}
		// check if head collides left border
		if (x[0] < 0) {
			running = false;
		}
		// check if head touches right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// check if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics graphics) {
		// Game over text
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Family", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString(GAME_OVER, (SCREEN_WIDTH - metrics.stringWidth(GAME_OVER)) / 2, SCREEN_HEIGHT / 2);
		// display the score
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Family", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: " + applesEatten,
				(SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEatten)) / 2, graphics.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();

	}

	// inner class
	public class MykeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			default:
				break;
			}
		}
	}

}
