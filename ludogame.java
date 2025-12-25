import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ludogame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ludogame::new);
    }

    Board board = new Board();

    public ludogame() {
        setTitle("Simple Ludo - Java Swing");
        setSize(620, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(board, BorderLayout.CENTER);

        JButton diceButton = new JButton("Roll Dice");
        diceButton.setFont(new Font("Arial", Font.BOLD, 18));
        diceButton.addActionListener(e -> board.rollDice());

        add(diceButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}

/* ================= BOARD ================= */

class Board extends JPanel {

    int dice = 1;
    int currentPlayer = 0; // 0 = Red, 1 = Blue

    Token[] tokens = {
            new Token(Color.RED, 0),
            new Token(Color.BLUE, 1)
    };

    Random random = new Random();

    // Simple linear path (for demo)
    Point[] path = new Point[20];

    public Board() {
        setBackground(Color.WHITE);
        initPath();
    }

    void initPath() {
        int x = 50;
        int y = 300;
        for (int i = 0; i < path.length; i++) {
            path[i] = new Point(x + i * 25, y);
        }
    }

    void rollDice() {
        dice = random.nextInt(6) + 1;
        tokens[currentPlayer].move(dice, path.length);
        currentPlayer = (currentPlayer + 1) % tokens.length;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawTokens(g);
        drawInfo(g);
    }

    void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        for (Point p : path) {
            g.drawRect(p.x, p.y, 25, 25);
        }
    }

    void drawTokens(Graphics g) {
        for (Token t : tokens) {
            if (t.position >= 0) {
                Point p = path[t.position];
                g.setColor(t.color);
                g.fillOval(p.x + 5, p.y + 5, 15, 15);
            }
        }
    }

    void drawInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Dice: " + dice, 20, 30);
        g.drawString("Turn: " + (currentPlayer == 0 ? "RED" : "BLUE"), 20, 55);
    }
}

/* ================= TOKEN ================= */

class Token {
    Color color;
    int playerId;
    int position = -1;

    Token(Color color, int playerId) {
        this.color = color;
        this.playerId = playerId;
    }

    void move(int dice, int maxPath) {
        if (position == -1) {
            if (dice == 6)
                position = 0;
        } else {
            position += dice;
            if (position >= maxPath) {
                position = maxPath - 1;
            }
        }
    }
}
