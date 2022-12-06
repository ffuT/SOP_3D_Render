
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Game {
    public int time;
    private Robot r;
    public Controller control;
    public Game(){
        try {
            r = new Robot();
        } catch (Exception e){
            System.out.println("robot doesnt work (game.java)");
        }
        control = new Controller();
    } 
    
    public void tick(boolean[] Key){
        time++;
        boolean forward = Key[KeyEvent.VK_W];
        boolean back = Key[KeyEvent.VK_S];
        boolean left = Key[KeyEvent.VK_A];
        boolean right = Key[KeyEvent.VK_D];
        boolean turnleft = Key[KeyEvent.VK_LEFT];
        boolean turnright = Key[KeyEvent.VK_RIGHT];
        boolean jump = Key[KeyEvent.VK_SPACE];
        boolean sprint = Key[KeyEvent.VK_SHIFT];
        boolean crouch = Key[KeyEvent.VK_CONTROL];

        control.tick(forward, back, left, right, turnleft, turnright, jump, sprint, crouch);
    }

    public void benchmark(ArrayList<Integer> FPS){
        //enhanced switch case
        switch (time-1) {
            case 0 -> {        //press n' hold w
                r.keyPress(KeyEvent.VK_W);
                }
            case 240*2, 1558*2, 940*2 -> {      //release w turn right
                r.keyRelease(KeyEvent.VK_W);
                r.keyPress(KeyEvent.VK_RIGHT);
                }
            case 320*2, 1640*2, 1015*2 -> {      //release turn right press w
                r.keyRelease(KeyEvent.VK_RIGHT);
                r.keyPress(KeyEvent.VK_W);
                }
            case 590*2 -> {      //turn right
                r.keyPress(KeyEvent.VK_RIGHT);
                }
            case 668*2 -> {      //stop turn
                r.keyRelease(KeyEvent.VK_RIGHT);
                }
            case 1800*2 ->{       //stop press w, terminate program print avg frames and 10% low after 30 secs (without frame zero)
                r.keyRelease(KeyEvent.VK_W);

                Collections.sort(FPS);

                int TotalFPS=0;
                int TotalLowFPS=0;
                int k=0;

                for (Integer n: FPS) {
                    TotalFPS +=n;
                }

                for (int i = 0 ;i<=FPS.size()/10-1;i++){
                    TotalLowFPS += FPS.get(i);
                    k = i+1;
                }

                int AVGFPS = TotalFPS/FPS.size();
                int LOWFPS = TotalLowFPS/k;

                System.out.println("Average Fps: "+AVGFPS);
                System.out.println("10% low Fps: "+LOWFPS);

                System.exit(0);
            }
        }
    }
}
