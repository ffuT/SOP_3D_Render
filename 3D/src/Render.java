
public class Render {
    public final int width;
    public final int height;
    public final int[] pixels;

public Render(int width, int height){
    this.width = width;
    this.height = height;
    pixels = new int[width * height];
    }      

    public void draw(Render render, int xOffset, int yOffset){
        for(int y=0;y<render.height;y++){
            int ypix = y + yOffset;
            if(ypix<0 || ypix>=height){
                continue;
            }

            for(int x=0;x<render.width;x++){
                int xpix = x + xOffset;
                if(xpix<0 || xpix>=width){
                    continue;
                }

                int alpha = render.pixels[x+y*width];
                if(alpha>0){
                    pixels[xpix+ypix*width] = alpha;
                }
            }
        }
    }

}

