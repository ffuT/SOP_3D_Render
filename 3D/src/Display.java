import java.awt.Canvas;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.*;


public class Display extends Canvas implements Runnable {
    public static final int WIDTH = 1080, HEIGHT = 720;
    public static final String TITLE = "3D World";
    public static double distance=0;
    public static boolean robotmove=false;

    private static Robot r;
    private double sensetivity = 1000.0;
    private int FPS=0;
    private Thread thread;
    private boolean running = false;
    private Screen screen;
    private Game game;
    private BufferedImage img;
    private int[] pixels;
    private InputHandler input;
    private int newX=0;
    private int oldX=0;
    private int newY=0;


    public static void main(String[] args) {
        try {
            r = new Robot();
        } catch (Exception e) {
            System.out.println("robot blyat");
            System.exit(1);
        }
        BufferedImage cursor = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB); 
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor,new Point(0,0),"blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.pack();
        //frame.getContentPane().setCursor(blank);
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        
        game.start();
    }

    public Display(){
        Dimension size = new Dimension(WIDTH,HEIGHT);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);

        screen = new Screen(WIDTH,HEIGHT);
        game = new Game();
        img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    private void start(){
        if(running)
            return;
        running=true;
        thread = new Thread(this);
        thread.start(); 
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1_000_000_000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        requestFocus();
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running)
                render();
            frames++;
            /*
            newX=InputHandler.mouseX;
            if(newX>oldX && !robotmove){
                distance = (newX-oldX)/sensetivity;
                //System.out.println(distance);
                Controller.rotRight=true;
            } else {
                Controller.rotRight=false;
            }
            if(newX<oldX && !robotmove){
                distance = Math.abs(newX-oldX)/sensetivity;
                //System.out.println(distance);
                Controller.rotLeft=true;
            }else {
                Controller.rotLeft=false;
            }

            if(newX>1000 || newX<80){
                robotmove=true;
                oldX=540;
                r.mouseMove((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
                oldX=540;
                robotmove=false;
                newX=oldX;
            }

            
            oldX=newX;
            */
            //newY=InputHandler.mouseY;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
                FPS=frames;
                frames = 0;
            }
        }
        stop();
    }

    public synchronized void stop() {
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void tick(){
        game.tick(input.key);
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null){
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for(int i=0;i<WIDTH*HEIGHT;i++){
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(img, 0, 0, WIDTH,HEIGHT,null);
        g.drawString("FPS: " + FPS, 5, 15);
        g.drawString("WalkSpeed: "+Controller.walkspeed , 5, 30);
        g.dispose();
        bs.show();
    }

}
