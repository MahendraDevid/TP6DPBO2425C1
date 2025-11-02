import javax.swing.*;
import java.awt.*;

public class App {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Logic & View (game panel)
        Logic logic = new Logic();
        View view = new View(logic);
        logic.setView(view);

        // Score label
        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBackground(new Color(0,0,0,150)); // transparan
        scoreLabel.setOpaque(true);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5,8,5,8)); // padding
        logic.setScoreLabel(scoreLabel);

        // Game layer
        JLayeredPane gameLayer = new JLayeredPane();
        gameLayer.setPreferredSize(new Dimension(360, 640));

        // posisi view (game canvas)
        view.setBounds(0, 0, 360, 640);

        // posisi score label
        scoreLabel.setBounds(10, 10, 180, 28);

        // tambahkan dalam layer
        gameLayer.add(view, Integer.valueOf(0));       // layer bawah (game)
        gameLayer.add(scoreLabel, Integer.valueOf(1)); // layer atas (score)


        // ===== MENU TRANSPARAN =====
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setOpaque(false); // transparan

        JPanel box = new JPanel();
        box.setOpaque(true);
        box.setBackground(new Color(0,0,0,160)); // semi transparan hitam
        box.setLayout(new GridLayout(3,1,5,5));
        box.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        ImageIcon icon = new ImageIcon(App.class.getResource("/assets/logo.png"));
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel title = new JLabel(new ImageIcon(img));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        JButton btnPlay = new JButton("Play");
        JButton btnExit = new JButton("Exit");
        btnPlay.setBackground(new Color(0, 200, 0));   // hijau
        btnPlay.setForeground(Color.WHITE);            // warna teks
        btnPlay.setFont(new Font("Arial", Font.BOLD, 10));
        btnPlay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnPlay.setFocusPainted(false);                // hilangkan garis fokus biru

        btnExit.setBackground(new Color(200, 0, 0));   // merah
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Arial", Font.BOLD, 10));
        btnExit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnExit.setFocusPainted(false);

        btnPlay.setFocusable(false);
        btnExit.setFocusable(false);

        // tombol kecil
        Dimension btnSize = new Dimension(200, 35);
        btnPlay.setPreferredSize(btnSize);
        btnExit.setPreferredSize(btnSize);

        box.add(title);
        box.add(btnPlay);
        box.add(btnExit);
        menuPanel.add(box);

        // Layering
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(360, 640));

        gameLayer.setBounds(0,0,360,640);
        menuPanel.setBounds(0,0,360,640);

        layeredPane.add(gameLayer, Integer.valueOf(0));
        layeredPane.add(menuPanel, Integer.valueOf(1));


        frame.setContentPane(layeredPane);
        frame.setVisible(true);

        // Events
        btnPlay.addActionListener(e -> {
            menuPanel.setVisible(false);
            logic.startGame();
            view.requestFocusInWindow();
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}
