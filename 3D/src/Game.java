
import java.awt.event.KeyEvent;

public class Game {
    public int time;
    public Controller control;

    private long currentime;

    public Game(){
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
}
