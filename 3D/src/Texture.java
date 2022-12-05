
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class Texture {
    public static Render floor = LoadBitmap("resources/textures/floor.png");
    public static Render grass = LoadBitmap("resources/textures/grass.png");
    public static Render bricks = LoadBitmap("resources/textures/bricks.png");
    public static Render piller = LoadBitmap("resources/textures/pillar.png");
    public static Render MCgrass = LoadBitmap("resources/textures/MCgrass.png");
    public static Render test = LoadBitmap("resources/textures/test.png");
    public static Render test32 = LoadBitmap("resources/textures/test32.png");
    public static Render sky = LoadBitmap("resources/textures/sky.png");

    public static Render LoadBitmap(String fileName){
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(fileName));
            int width = image.getWidth();
            int height = image.getHeight();
            Render result = new Render(width, height);
            image.getRGB(0, 0, width, height, result.pixels, 0, width);
            return result;
        } catch(Exception e){
            System.out.println("textures not loading");
            throw new RuntimeException(e);

        }
    }
}
