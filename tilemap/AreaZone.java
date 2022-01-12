
package tilemap;

import entity.Enemy;
import entity.Hero;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import obstacles.Obstacle;

public class AreaZone {
    
    public TileMap tileMap = new TileMap(32);
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Obstacle> objects = new ArrayList<>();
    public Hero hero;
    private int startX, startY, start, end;
    private String musicPath = "";
    private int id;
    
    public AreaZone(String map, int id) {
        tileMap.loadMap(map);
        this.id = id;
    }
    
    public int getID() {
        return id;
    }
    
    public void setMusicPath(String path, int start, int end) {
        this.musicPath = path;
        this.start = start;
        this.end = end;
    }
    
    public String getMusicPath() {
        return musicPath;
    }
    
    public int getStartMusicLoop() {
        return start;
    }
    
    public int getEndMusicLoop() {
        return end;
    }
    
    public void setHero(Hero hero) {
        this.hero = hero;
    }
    
    public Hero getHero() {
        return hero;
    }
    
    public void setStartX(int x) {
        this.startX = x;
    }
    
    public void setStartY(int y) {
        this.startY = y;
    }
    
    public int getStartX() {
        return startX;
    }
    
    public int getStartY() {
        return startY;
    }
    
    public void update() {
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).update();
        }
        for(int i = 0; i < enemies.size(); i++) {
            if(enemies.get(i).onScreen()) {
                enemies.get(i).update();
            }
        }
    }
    
    public void paint(Graphics2D g) {
        tileMap.paint(g);
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).paint(g);
            if(i == 8) {
                //g.setColor(Color.blue);
                //g.fillRect((int) objects.get(i).getX() + tileMap.getX(), (int) objects.get(i).getY() + tileMap.getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
            }
        }
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).paint(g);
        }
    }
    
    public void removeEnemy(int id) {
        for(int i = 0; i < enemies.size(); i++) {
            if(i == id) {
                enemies.remove(i);
                break;
            }
        }
    }
    
    public void removeEnemy(Enemy e) {
        for(int i = 0; i < enemies.size(); i++) {
            if(enemies.get(i) == e) {
                enemies.remove(i);
                break;
            }
        }
    }
    
    public static int getNumberOfZone(String path, boolean in) {
        try {
            BufferedReader br = null;
            if(in) {
                InputStream stream = TileMap.class.getClassLoader().getResourceAsStream("maps/"+path);
                InputStreamReader reader = new InputStreamReader(stream);
                br = new BufferedReader(reader);
            }
            else {
                br = new BufferedReader(new FileReader(new File(path)));
            }
            String append = "";
            String text = "";
            while((append = br.readLine()) != null) {
                text += append+"\n";
            }
            System.out.println(text.trim().split("-").length);
            return text.trim().split("-").length;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Le fichier carte n'est pas valide.", "Ball Adventure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Le fichier carte n'est pas valide.", "Ball Adventure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return -1;
    }
    
    public static String[] getText(String path, boolean in) {
        try {
            BufferedReader br = null;
            if(in) {
                InputStream stream = TileMap.class.getClassLoader().getResourceAsStream("maps/"+path);
                InputStreamReader reader = new InputStreamReader(stream);
                br = new BufferedReader(reader);
            }
            else {
                br = new BufferedReader(new FileReader(new File(path)));
            }
            String append = "";
            String text = "";
            while((append = br.readLine()) != null) {
                text += append+"\n";
            }
            return text.trim().split("-");
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Le fichier carte n'est pas valide.", "Ball Adventure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Le fichier carte n'est pas valide.", "Ball Adventure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        return null;
    }
    
    public static String[] getObjects(String[] text) {
        String[] general = new String[text.length];
        for(int i = 0; i < text.length; i++) {
            String append = text[i].split("_")[1];
            general[i] = append.trim()+"\n";
        }
        return general;
    }
    
    public void addObjects(Obstacle[] objs) {
        for(int i = 0; i < objs.length; i++) {
            objects.add(objs[i]);
        }
    }
    
    public void event(int id) {
        for(int i = 0; i < objects.size(); i++) {
            if(objects.get(i).getIndexEvent() == id) {
                objects.get(i).event();
            }
        }
    }
}