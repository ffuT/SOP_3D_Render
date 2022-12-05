
public class Screen extends Render {
    private Render3D render;

    public Screen(int width, int height) {
        super(width, height);
        render = new Render3D(width, height);
    }

    public void render(Game game) {
        for(int i = 0 ; i < width * height ; i++){
            pixels[i]=0;
        }

        render.floor(game);

        for (double i = 0; i <= 5; i+=0.5){
            renderbox(1,i,0);
            renderbox(1,i,0.5);
        }
        for (double i = 1; i <= 6; i+=0.5){
            render.renderWall(i,i+0.5,7.5,7.5,0);
            render.renderWall(i,i+0.5,7.5,7.5,0.5);
        }
        for (int i = 0; i <= 6; i++){
            renderbox(10,i,0);
            renderbox(10,i,0.5);
            renderbox(10,i,1);
            renderbox(10,i,1.5);
            renderbox(10,i,2);
            renderbox(10,i,2.5);
        }
        for (double j = 0;j <= 5;j += 0.5){
            for (double i = -1; i >= -2.5; i-=0.5){
                renderbox(j,i,0);
                renderbox(j,i,0.5);
                renderbox(j,i,1);
            }
        }
        for (double j = 0;j <= 5;j += 0.5){
            for (double i = -4; i >= -5.5; i-=0.5){
                renderbox(j,i,0);
                renderbox(j,i,0.5);
                renderbox(j,i,1);
            }
        }
        renderbox(4,-3,0);
        renderbox(4,-3.5,0);
        renderbox(4,-3,0.5);
        renderbox(4,-3.5,0.5);

        renderbox(2,-3,0);
        renderbox(2,-3.5,0);
        renderbox(2,-3,0.5);
        renderbox(2,-3.5,0.5);

        for (int j = -2; j >= -5;j--){
            for (int i = 0; i <= 6; i++){
                renderbox(j,i,0);
                renderbox(j,i,0.5);
                renderbox(j,i,1);
                renderbox(j,i,1.5);
                renderbox(j,i,2);
                renderbox(j,i,2.5);
            }
        }

        render.RenderDistanceLimit();
        draw(render, 0,0);

    }
    private void renderbox(double x,double z,double y){
        render.renderWall(x+0.5, x, z+0.5, z+0.5, y);
        render.renderWall(x, x, z+0.5, z, y);
        render.renderWall(x, x+0.5, z, z, y);
        render.renderWall(x+0.5, x+0.5, z, z+0.5, y);
    }



}
