import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    private Logic logic;// tambahkan atribut logic
    Font pixelFont;
    Image bgImage;


    int width = 360;
    int height = 640;

    //constructor
    public View(Logic logic){
        bgImage = new ImageIcon(getClass().getResource("/assets/city.jpg")).getImage();

        this.logic = logic; // inisialisasi atribut logic
        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        addKeyListener(logic);

        // load font pixel sekali
        try {
            pixelFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/assets/fonts/PressStart2P-Regular.ttf")
            ).deriveFont(32f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (Exception e) {
            pixelFont = new Font("Monospaced", Font.BOLD, 32);
            System.out.println("Gagal load font pixel, fallback default");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // gambar background fullscreen
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);

        Player player = logic.getPlayer();
        if (player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }

        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null){
            for (int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // tampilkan teks Game Over jika permainan selesai
        if (logic.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(pixelFont);
            String text = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = getHeight() / 2;
            g.drawString(text, x, y);

            // teks kecil untuk restart
            g.setFont(pixelFont.deriveFont(16f));
            String restart = "Press R to Restart";
            int x2 = (getWidth() - g.getFontMetrics().stringWidth(restart)) / 2;
            int y2 = y + 50;
            g.drawString(restart, x2, y2);
        }
    }
}
