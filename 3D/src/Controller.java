
public class Controller {
    public double x , y, z , rotation , xa , za , rotationa;
    public static boolean walk = false;
    public static boolean isSprinting=false;
    public static double rotationspeed = 0.01;
    public static double walkspeed = 0.75;

    public void tick(boolean forward, Boolean back,Boolean left,Boolean right,Boolean turnleft,Boolean turnright, boolean jump, boolean sprint, boolean crouch){
        rotationspeed = Display.distance;
        walkspeed = 0.75;
        if(!walk)
            walkspeed=0;
        double jumpheight = 1;
        double crouchheight = 0.6;
        double xmove = 0;
        double zmove = 0;
        isSprinting=false;

        if(forward){
            zmove++;
            walk = true;
        }
        if(back){
            zmove--;
            walk = true;
        }
        if(left){
            xmove-=0.5;
            walk = true;
        }
        if(right){
            xmove+=0.5;
            walk = true;
        }
        if(turnleft){
            rotationspeed=0.01;
            rotationa -= rotationspeed;
        }
        if(turnright){
            rotationspeed=0.01;
            rotationa += rotationspeed;
        }
        if(jump){
            //y += jumpheight;
        }
        if(crouch){
            y -=  crouchheight;
            walkspeed = 0.5;
            sprint=false;
        }
        if(sprint){
            walkspeed = 1.5;
            isSprinting=true;
        }
        //only bobbing when walking
        if(xmove==0 && zmove==0)
            walk=false;

        xa += (xmove * Math.cos(rotation) + zmove*Math.sin(rotation)) * walkspeed;
        za += (zmove * Math.cos(rotation) - xmove*Math.sin(rotation)) * walkspeed;

        x += xa;
        y *= 0.9;
        z += za;
        xa *= 0.1;
        za *= 0.1;
        rotation += rotationa;
        rotationa *= 0.5;
    }
}
