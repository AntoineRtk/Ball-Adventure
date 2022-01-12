package states;

import obstacles.Flower;
import entity.Dinosaur;
import entity.Enemy;
import entity.Fish;
import entity.Gobelin;
import entity.Hero;
import entity.RockPeak;
import entity.Tronar;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import jukebox.JukeBox;
import obstacles.Apple;
import obstacles.Canon;
import obstacles.Door;
import obstacles.EventDoor;
import obstacles.ExplosionPower;
import obstacles.GiantPower;
import obstacles.LevelDoor;
import obstacles.MiniPower;
import obstacles.Obstacle;
import obstacles.Scale;
import obstacles.SwitchEvent;
import obstacles.Trampoline;
import obstacles.UpGenerator;
import tilemap.AreaZone;

public abstract class GameLevelState extends GameState {
    
    public AreaZone[] areas;
    public Hero hero;
    private boolean loading = false;
    private int currentArea = 0, checkPoint = 0;
    private long currentTime = System.currentTimeMillis();
    private String music;
    private Color cb;
    private float fb;
    private Color color = Color.WHITE;
    public int lastLife = 0;
    
    public GameLevelState(GameStateManager gsm) {
        super(gsm);
    }
    
    public void setMapPositions() {
        for(int i1 = 0; i1 < areas.length; i1++) {
            for(int i = 0; i < areas[i1].objects.size(); i++) {
                areas[i1].objects.get(i).setPosition((int) areas[i1].objects.get(i).getX() / 32 * 32, (int) areas[i1].objects.get(i).getY() / 32 * 32);
            }
            hero.setPosition(areas[currentArea].getStartX(), areas[currentArea].getStartY());
        }
    }
    
    public void clearAll() {
        areas = null;
    }
    
    public abstract void init();
    
    public void update() {
        if(!loading) {
            areas[currentArea].update();
            hero.update();
            areas[currentArea].tileMap.update();
        }
    }
    
    public void addObstacle(Obstacle o, int area) {
        areas[area].objects.add(o);
    }
    
    public void addEnemy(Enemy e, int area) {
        areas[area].enemies.add(e);
    }
    
    public int getCheckPoint() {
        return checkPoint;
    }
    
    public void setArea(int index) {
        lastLife = (int) hero.getLife();
        checkPoint = index;
        currentArea = index;
        hero.setArea(areas[currentArea]);
        hero.init();
        hero.setLife(lastLife);
        hero.setPosition(areas[currentArea].getStartX(), areas[currentArea].getStartY());
        initMusic();
        setBackground(null, 0);
    }
    
    public void informSpecialTheme() {
        music = "Kirby's Return to Dreamland : Super Copy Ability / Super Copie";
        currentTime = System.currentTimeMillis();
    }
    
    public void initMusic() {
        if(!areas[currentArea].getMusicPath().isEmpty()) {
            if(JukeBox.clip != null) {
                if(JukeBox.getName().equals(areas[currentArea].getMusicPath())) return;
            }
            JukeBox.play(areas[currentArea].getMusicPath());
            if(areas[currentArea].getStartMusicLoop() != -1) {
                JukeBox.setLoopPoints(areas[currentArea].getStartMusicLoop(), areas[currentArea].getEndMusicLoop());
            }
            setMusicText();
        }
    }
    
    public void setMusicText() {
        switch(areas[currentArea].getMusicPath()) {
            case "plains":
                music = "Super Paper Mario : Lineland Road / Chapitre 1-1";
                break;
            case "underground":
                music = "Dragonica : City Hall Sewers / Egouts de la ville du port des quatre vents";
                break;
            case "mount":
                music = "Dragonica : Traitor's Ridge / Crête du fourbe - Canyon de l'oubli";
                break;
            case "caverns":
                music = "Dragonica : Candescent Caverns / Caverne des dangers";
                break;
            case "boss":
                music = "Super Mario Galaxy : Dino Piranha";
                break;
            case "clouds":
                music = "Super Mario 3D World : Double Cherry Pass / Gorge aux doubles cerises";
                break;
        }
        currentTime = System.currentTimeMillis();
    }
    
    public void paint(Graphics2D g) {
        if(isLoading()) {
            g.setColor(color);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Chargement...", getWidth() / 3, getHeight() / 2);
            return;
        }
        Composite c = g.getComposite();
        areas[currentArea].paint(g);
        if(hero != null) {
            hero.paint(g);
        }
        if(cb != null) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fb));
            g.setColor(cb);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setComposite(c);
        if(System.currentTimeMillis() - currentTime < 5000 && music != null) {
            g.setFont(new Font("Tekton Pro Ext", Font.ITALIC, 15));
            TextLayout text = new TextLayout(music, g.getFont(), g.getFontRenderContext());
            g.setPaint(new GradientPaint(5, 0, new Color(255, 128, 0), 10, 10, new Color(6, 249, 37), true));
            g.drawString(music, getWidth() - (int) text.getBounds().getWidth() - 10, getHeight() - (int) text.getBounds().getHeight() - 20);
        }
    }
    public void readMap(String path, boolean inmap, String tiles, boolean intiles) {
        color = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
        currentArea = 0;
        areas = new AreaZone[AreaZone.getNumberOfZone(path, inmap)];
        String[] splitted = AreaZone.getText(path, inmap);
        for(int i = 0; i < areas.length; i++) {
            System.out.println("ID : "+i);
            areas[i] = new AreaZone(splitted[i].trim(), i);
            System.out.println("ID : "+i);
            areas[i].tileMap.loadTiles(tiles, intiles);
            System.out.println("ID : "+i);
        }
        String[] splittedObjects = AreaZone.getObjects(splitted);
        for(int i = 0; i < splittedObjects.length; i++) {
            System.out.println("ID splitted "+i+" : "+splittedObjects[i]);
            String[] objects = splittedObjects[i].split("\n");
            readObjects(objects, i);
        }
        initMusic();
        setLoading(false);
    }
    
    public void readObjects(String[] objects, int a) {
        int m = Integer.parseInt(objects[0]);
        switch(m) {
            case 1:
                areas[a].setMusicPath("plains", 123555, -1);
                break;
            case 2:
                areas[a].setMusicPath("caverns", 276093, -1);
                break;
            case 3:
                areas[a].setMusicPath("underground", 0, -1);
                break;
            case 4:
                areas[a].setMusicPath("clouds", 85400, -1);
                break;
            case 5:
                areas[a].setMusicPath("boss", 51319, -1);
                break;
        }
        boolean xySet = false;
        for(int i = 1; i < objects.length; i++) {
            int id = Integer.parseInt(objects[i].substring(0, objects[i].indexOf(":") - 1));
            double[] arguments = new double[objects[i].substring(objects[i].indexOf(":") + 2, objects[i].length()).split(" ").length];
            for(int i1 = 0; i1 < arguments.length; i1++) {
                arguments[i1] = Double.parseDouble(objects[i].substring(objects[i].indexOf(":") + 2, objects[i].length()).split(" ")[i1]);
            }
            switch(id) {
                case 0:
                    addObstacle(new Trampoline(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 1:
                    addObstacle(new Scale(areas[a], (int) arguments[0], (int) arguments[1], id, gsm), a);
                    break;
                case 2:
                    addObstacle(new Door(areas[a], (int) arguments[0], (int) arguments[1], (int) arguments[2], 0, gsm), a);
                    break;
                case 3:
                    addObstacle(new Canon(areas[a], (int) arguments[0], (int) arguments[1], arguments[2], arguments[3], 0, gsm), a);
                    break;
                case 4:
                    addEnemy(new Dinosaur(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 5:
                    addEnemy(new Gobelin(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 6:
                    if(!xySet) {
                        areas[a].setStartX((int) arguments[0]);
                        areas[a].setStartY((int) arguments[1]);
                        xySet = true;
                    }
                    break;
                case 7:
                    addObstacle(new LevelDoor(areas[a], (int) arguments[0], (int) arguments[1], (int) arguments[2], (int) arguments[3], 0, gsm), a);
                    break;
                case 8:
                    addObstacle(new MiniPower(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 9:
                    addObstacle(new GiantPower(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 10:
                    addObstacle(new ExplosionPower(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 11:
                    addObstacle(new Flower(areas[a], (int) arguments[0], (int) arguments[1], 0, gsm), a);
                    break;
                case 12:
                    addObstacle(new Apple(areas[a], arguments[0], arguments[1], 0, gsm), a);
                    break;
                case 13:
                    addObstacle(new UpGenerator(areas[a], arguments[0], arguments[1], (int) arguments[2], 0, gsm), a);
                    break;
                case 14:
                    addObstacle(new EventDoor(areas[a], arguments[0], arguments[1], (int) arguments[2], 0, gsm), a);
                    break;
                case 15:
                    addObstacle(new SwitchEvent(areas[a], arguments[0], arguments[1], (int) arguments[2], 0, gsm), a);
                    break;
                case 16:
                    addEnemy(new RockPeak(areas[a], arguments[0], arguments[1], 0, gsm), a);
                    break;
                case 17:
                    addEnemy(new Fish(areas[a], arguments[0], arguments[1], 0, gsm), a);
                    break;
                case 18:
                    addEnemy(new Tronar(areas[a], arguments[0], arguments[1], (int) arguments[2], 0, gsm), a);
                    break;
            }
        }
        if(!xySet) {
            areas[a].setStartX(64);
            areas[a].setStartY(64);
        }
    }
    
    public void loadTiles(String path, boolean in) {
        for(int i = 0; i < areas.length; i++) {
            areas[i].tileMap.loadTiles(path, in);
        }
    }
    
    public void loadMap(String text, int area) {
        areas[area].tileMap.loadMap(text);
    }
    
    public void removeEnemies(int[] monstersID) {
        //for(int m : monstersID) {
            //for(int i = 0; i < enemies.size(); i++) {
                //if(enemies.get(i).getInstantID() == m) {
                    //enemies.remove(i);
                //}
            //}
        //}
    }
    
    public void removeEnemy(Enemy e) {
        areas[currentArea].removeEnemy(e);
    }
    
    public void removeEnemy(int instantID) {
        areas[currentArea].removeEnemy(instantID);
    }
    
    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    
    public boolean isLoading() {
        return loading;
    }
    
    public void setBackground(Color cb, float fb) {
        this.cb = cb;
        this.fb = fb;
    }
    
    public void informEndTheme() {
        music = "Super Paper Mario : End of chapter / Fin de chapitre";
        currentTime = System.currentTimeMillis();
    }
    
    public void setCheckPoint(int c) {
        this.checkPoint = c;
    }
    
    public void informBossBeatenTheme() {
        music = "Dragonica : You beat the boss! / Boss F5 battu";
        currentTime = System.currentTimeMillis();
    }
}