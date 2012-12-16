package mylittleterrorist;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class Spritesheet {

    protected BufferedImage sheet;
    protected int tileW, tileH;
    protected Map<Point, BufferedImage> tileCache;
    
    public Spritesheet(String file, int tileW, int tileH) throws IOException {
        this.tileW = tileW;
        this.tileH = tileH;
        sheet = ImageIO.read(Spritesheet.class.getResource(file));
        tileCache = new TreeMap<Point, BufferedImage>(Util.POINTCMP);
    }
    
    BufferedImage get(int col, int row) {
        Point p = new Point(col, row);
        if (tileCache.get(p) != null) return tileCache.get(p);
        
        int sx = col * tileW, sy = row * tileH;
        
        BufferedImage tile = new BufferedImage(tileW, tileH,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)tile.getGraphics();
        g.drawImage(sheet, 0, 0, tileW, tileH,
                sx, sy, sx+tileW, sy+tileH, null);
        
        tileCache.put(p, tile);
        return tile;
    }

}
