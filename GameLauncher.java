import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

public class GameLauncher {
    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame("🎮 Game Zone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel title = new JLabel("Select a Game to Play!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        frame.add(title, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // Create Buttons
        JButton button1 = createButton("🐍 Snake");
        JButton button2 = createButton("👻 Pac-Man");
        JButton button3 = createButton("🃏 Blackjack");
        JButton button4 = createButton("🐦 Flappy Bird");
        JButton button5 = createButton("🏓 Ping Pong");

        // Add action listeners
        button1.addActionListener(e -> launchSnake());
        button2.addActionListener(e -> launchPacMan());
        button3.addActionListener(e -> new BlackJack());
        button4.addActionListener(e -> launchFlappyBird());
        button5.addActionListener(e -> new pongGame());

        // Add buttons to panel
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setToolTipText("Click to launch " + text.trim());
        return button;
    }

    private static void launchSnake() {
        JFrame frame = new JFrame("Snake");
        SnakeGame snakeGame = new SnakeGame(600, 600);
        frame.add(snakeGame);
        frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        snakeGame.requestFocus();
    }

    private static void launchPacMan() {
        int tileSize = 32;
        int width = 19 * tileSize;
        int height = 21 * tileSize;

        JFrame frame = new JFrame("Pac-Man");
        PacMan pacman = new PacMan();
        frame.add(pacman);
        frame.pack();
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        pacman.requestFocus();
    }

    private static void launchFlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird fb = new FlappyBird();
        frame.add(fb);
        frame.pack();
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        fb.requestFocus();
    }
}
