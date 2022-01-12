package states.levels;

import entity.Hero;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import states.GameLevelState;
import states.GameStateManager;

public class Level1 extends GameLevelState {
    
    private BufferedImage bg;
    
    public Level1(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        setLoading(true);
        try {
            bg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/bg.gif"));
        } catch (IOException ex) {}
        clearAll();
        readMap("map1.txt", true, "tiles.png", true);
        hero = new Hero(gsm, areas[0], 0, 0, 0);
        //for(int i = 0; i < 1; i++) {
        //addEnemy(new Dinosaur(area[0], 600, 600, i, gsm));
        //addEnemy(new Gobelin(areas, (200 / 32 + 1) * 32 - 1, 200, 1, gsm));
        //}
        //addObstacle(new Trampoline(, 800, 64, 0));
        //addObstacle(new Scale(tileMap, 32, 32, 600, 1));
        //addObstacle(new Door(tileMap, 400, 88, 2, 1));
        areas[0].setHero(hero);
        setArea(getCheckPoint());
        setMapPositions();
    }
}