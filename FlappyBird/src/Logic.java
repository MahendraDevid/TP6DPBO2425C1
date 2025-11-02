import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerstartPosY = frameHeight / 2;
    int playerWidth = 44;
    int playerHeight = 28;

    //tambahkan atribut posisi dan ukuran pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    //atribut untuk jika game over
    boolean gameOver = false;

    boolean gameStarted = false;

    SoundManager sound;

    View view;
    Image birdImage;
    Player player;

    //tambahkan list pipa, dan gambar pipa
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -2;

    // SCORE
    int score = 0;
    JLabel scoreLabel;
    int highScore = 0;


    public void setScoreLabel(JLabel label){
        this.scoreLabel = label;
    }

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("/assets/nsx.png")).getImage();
        player = new Player(playerStartPosX, playerstartPosY, playerWidth, playerHeight, birdImage);
        sound = new SoundManager();


        //pipa rucica
        lowerPipeImage = new ImageIcon(getClass().getResource("/assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("/assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pipa rucica muncul");
                placePipes();
            }
        });

        gameLoop = new Timer(1000 / 60, this);
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    //untuk mengembalikkan nilai GameOver
    public boolean isGameOver() {
        return gameOver;
    }

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + pipeHeight + openingSpace), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void move() {
        if(gameOver) return;

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // jatuh menyentuh bawah frame
        if(player.getPosY() + playerHeight >= frameHeight) {
            gameOver = true;
            gameLoop.stop();
            pipesCooldown.stop();
            System.out.println("Game Over: Jatuh");
        }

        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // cek tabrakan pipa
            if(player.getPosX() < pipe.getPosX() + pipeWidth &&
                    player.getPosX() + playerWidth > pipe.getPosX() &&
                    player.getPosY() < pipe.getPosY() + pipeHeight &&
                    player.getPosY() + playerHeight > pipe.getPosY()) {

                gameOver = true;
                gameLoop.stop();
                pipesCooldown.stop();
                sound.stopBackground();
                System.out.println("Game Over: Menabrak pipa");

            }

            // scoring
            if ((i % 2 == 0) && !pipe.isScored() && pipe.getPosX() + pipeWidth < player.getPosX()) {
                pipe.setScored(true);
                score++;

                if(score > highScore){
                    highScore = score;
                }

                if (scoreLabel != null) scoreLabel.setText("Score: " + score + "  |  Best: " + highScore);
            }
        }
    }

    //method reset game
    public void resetGame() {
        // reset player
        player.setPosX(playerStartPosX);
        player.setPosY(playerstartPosY);
        player.setVelocityY(0);

        score = 0;
        if(scoreLabel != null) scoreLabel.setText("Score: 0  |  Best: " + highScore);

        // reset pipa
        pipes.clear();

        // reset game state
        gameOver = false;

        // mulai ulang timer
        gameLoop.start();
        pipesCooldown.start();
        sound.playBackground();

        System.out.println("Game Restarted");
    }

    public void startGame() {
        gameStarted = true;
        gameLoop.start();
        pipesCooldown.start();
        sound.playBackground();
        scoreLabel.setText("Score: 0  |  Best: " + highScore);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
            if (view != null) view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        // jika game belum over, tombol space untuk lompat
        if (!gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
            sound.playJump(); // suara loncat
        }

        // jika game over, tombol R untuk restart
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
        }
    }


    public void keyReleased(KeyEvent e) {}


}