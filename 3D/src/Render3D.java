import java.util.Arrays;

public class Render3D extends Render {

    public double[] zBuffer;
    public double[] zBufferWall;
    private final double renderDistance=20000;
    private final double floorPosition = 16;
    private final double ceilingPosition = 100;
    private double forward, rightward, upward, cosine, sine, walking;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width*height];
        zBufferWall =  new double[width];
    }

    public void floor(Game game){
        for(int i =0;i <width;i++){
            zBufferWall[i] = 0;
        }

        forward = game.control.z;
        rightward = game.control.x;
        upward = game.control.y;
        walking=0;
       
        double rotation = game.control.rotation;
        cosine = Math.cos(rotation);
        sine = Math.sin(rotation);

        for(int y=0;y<height;y++){

            double ceiling= (y - height / 2.0) / height;
            
            double z = (floorPosition+upward) / ceiling;
            if(Controller.walk){
                walking = Math.sin((game.time/3.0)*0.5)/3;
                z = (floorPosition+upward+walking) / ceiling;
            }

            if(Controller.isSprinting && Controller.walk){
                walking = Math.sin((game.time/3.0)*0.5)/1.5;
                z = (floorPosition+upward+walking) / ceiling;
            }

            if(ceiling < 0){
                z = (ceilingPosition-upward) / - ceiling;
                /* if i want roof to move as well
                if(Controller.walk)
                    z = (ceilingPosition-upward - walking) / -ceiling;
                    */
            }

            for(int x=0;x<width;x++){
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + rightward);
                int yPix = (int) (yy + forward);
                zBuffer[x+y*width]=z;
                
                //for different roof and floor
                if(y>height/2){
                    pixels[x + y * width] = Texture.MCgrass.pixels[(xPix & 15) + (yPix & 15) * 16];
                } else{
                    pixels[x + y * width] = Texture.sky.pixels[(xPix & 15) + (yPix & 15) * 16];
                }
            }
        }
    }

    public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight){
        double correction = 1/(floorPosition*2);

        //transformation matrix
        double xcLeft =  ((xLeft) - (rightward*correction))*2;
        double zcLeft = ((zDistanceLeft) - forward*correction)*2;
        
        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTL = ((-yHeight)-((-upward)*correction-(walking*correction)))*2;
        double yCornerBL = ((+0.5 - yHeight)-((-upward)*correction-(walking*correction)))*2;
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight =  ((xRight) - rightward*correction)*2;
        double zcRight = ((zDistanceRight) - forward*correction)*2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTR = ((-yHeight)-(-upward*correction-(walking*correction)))*2;
        double yCornerBR = ((+ 0.5 - yHeight) - (-upward*correction-(walking*correction)))*2;
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double tex20 = 0;
        double tex30 = 16;
        double clip = 0.05;

        //clip if you walk into the wall it stops render, (removes visual bugs)
        if(rotLeftSideZ < clip && rotRightSideZ < clip){
            return;
        }

        if(rotLeftSideZ < clip){
            double clip0 = ((clip-rotLeftSideZ)/(rotRightSideZ-rotLeftSideZ));
            rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            tex20 = tex20 + (tex30-tex20) * clip0;
        }

        if(rotRightSideZ < clip) {
            double clip0 = ((clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ));
            rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            tex30 = tex20 + (tex30 - tex20) * clip0;
        }

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2.0);
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2.0);

        if(xPixelLeft>=xPixelRight){
            return;
        }

        int xPixelLeftInt = (int) (xPixelLeft);
        int xPixelRightInt = (int) (xPixelRight);

        if(xPixelLeftInt < 0){
            xPixelLeftInt = 0;
        }
        if(xPixelRightInt > width){
            xPixelRightInt = width;
        }
        
        double yPixelLeftTop =  (yCornerTL/rotLeftSideZ*height+height/2.0);
        double yPixelLeftBot =  (yCornerBL/rotLeftSideZ*height+height/2.0);
        double yPixelRightTop =  (yCornerTR/rotRightSideZ*height+height/2.0);
        double yPixelRightBot =  (yCornerBR/rotRightSideZ*height+height/2.0);

        double tex0 = 1 / rotLeftSideZ;
        double tex1 = 1 / rotRightSideZ;
        double tex2 = tex20 / rotLeftSideZ;
        double tex3 = tex30 / rotRightSideZ-tex2;

        for(int x=xPixelLeftInt;x<xPixelRightInt;x++){
            double pixelrotation = (x-xPixelLeft)/(xPixelRight-xPixelLeft);
            double zWall = (tex0+(tex1-tex0)*pixelrotation);

            if(zBufferWall[x] > zWall)
                continue;
            zBufferWall[x] = zWall;

            int xTexture = (int) ((tex2+tex3*pixelrotation)/zWall);

            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelrotation;
            double yPixelBot = yPixelLeftBot + (yPixelRightBot - yPixelLeftBot) * pixelrotation;

            int yPixelTopInt = (int) yPixelTop;
            int yPixelBotInt = (int) yPixelBot;

            if (yPixelTopInt<0){
                yPixelTopInt=0;
            }
            if (yPixelBotInt>height){
                yPixelBotInt=height;
            }
            
            for(int y = yPixelTopInt;y<yPixelBotInt;y++){
                    double pixelrotationY = (y - yPixelTop) / (yPixelBot - yPixelTop);
                    int yTexture = (int) (16*pixelrotationY);
                    pixels[x+y*width] = Texture.bricks.pixels[(xTexture & 15)+(yTexture & 15)*16];
                    //pixels[x+y*width] =xTexture*80 + yTexture*80;
                    zBuffer[x+y*width]= 1/(tex0+(tex1-tex0)*pixelrotation)*16;
            }
        }
    }


    //fade away using light aka render distance
    public void RenderDistanceLimit(){
        for(int i=0;i<width*height;i++){
            int color = pixels[i];

            int brightness = (int) (renderDistance/(zBuffer[i]));

            if(brightness < 0)
                brightness = 0;

            if(brightness>255)
                brightness=255;

            int r=(color>>16)&0xff;
            int g=(color>>8)&0xff;
            int b=(color)&0xff;

            // >>> 8 or >> 8 idk signed or unsigned fuck...  it is the same as /255
            r=r*brightness >> 8;
            g=g*brightness >> 8;
            b=b*brightness >> 8;

            pixels[i]=r<<16 | g<<8 | b;
        }
    }
}
