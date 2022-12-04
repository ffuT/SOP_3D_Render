
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

        //renders a box with 4walls
        render.renderWall(0.5, 0, 1.5, 1.5, 0);
        render.renderWall(0, 0, 1.5, 1.0, 0);
        render.renderWall(0.0, 0.5, 1.0, 1.0, 0);
        render.renderWall(.5, .5, 1.0, 1.5, 0);
        //another
        render.renderWall(1.5, 1, 2.5, 2.5, 0);
        render.renderWall(1, 1, 2.5, 2.0, 0);
        render.renderWall(1.0, 1.5, 2.0, 2.0, 0);
        render.renderWall(1.5, 1.5, 2.0, 2.5, 0);

        //renderbox(0.5,1.5,0.5);

        render.RenderDistanceLimit();
        draw(render, 0,0);
    }
    //this renderbox is wrong...
    private void renderbox(double x,double z,double y){
        render.renderWall(x, 0, z, z, y);
        render.renderWall(x-0.5, 0, z, z-0.5, y);
        render.renderWall(x-0.5, 0.5, z-0.5, z-0.5,y);
        render.renderWall(x, .5, z-0.5, z, y);
    }


}
