package unused;


import states.LevelChoice;
import states.GameLevelState;
import states.GameState;
import states.GameStateManager;
import entity.Enemy;
import unused.Fighter;
import unused.Team;
import entity.Hero;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Fight extends GameState {
    
    private ArrayList<Fighter> fighters = new ArrayList<>();
    private int currentIndex = 0, minx = 0, miny = 0;
    private Team heroTeam = new Team(), enemyTeam = new Team();
    private int[] monstersID;
    
    public Fight(GameStateManager gsm) {
        super(gsm);
        EnemyList.init();
    }
    
    public void update() {
        for(int i = 0; i < fighters.size(); i++) {
            fighters.get(i).update();
        }
        fighters.get(currentIndex).updateTurn();
    }
    
    public void paint(Graphics2D g) {
        minx = getWidth() - 684;
        miny = getHeight() - 662;
        g.setColor(Color.cyan);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setPaint(new GradientPaint(0, 0, Color.cyan, 0, getHeight() / 2 - 40, Color.white));
        g.fillRect(0, 0, getWidth(), getHeight() / 2 - 100);
        g.setColor(Color.green);
        g.fillRect(0, getHeight() - getHeight() / 2 - 100, getWidth(), getHeight() / 2 + 100);
        for(int i = 0; i < heroTeam.size(); i++) {
            if(heroTeam.get(i).getX() == 0) {
                if(i == 0) {
                    heroTeam.get(i).setX(100);
                }
                else {
                    heroTeam.get(i).setX(100 + 30 + heroTeam.get(i - 1).getWidth());
                }
            }
            if(heroTeam.get(i).getY() == 0) {
                heroTeam.get(i).setY(getHeight() / 2);
            }
            g.drawImage(heroTeam.get(i).getImage(), heroTeam.get(i).getX(), heroTeam.get(i).getY() + miny, heroTeam.get(i).getWidth() + 35, heroTeam.get(i).getHeight() + 35, null);
        }
        for(int i = 0; i < enemyTeam.size(); i++) {
            if(enemyTeam.get(i).getX() == 0) {
                if(i == 0) {
                    enemyTeam.get(i).setX(getWidth() - 100 - enemyTeam.get(i).getWidth());
                }
                else {
                    enemyTeam.get(i).setX(100 + 30 + enemyTeam.get(i - 1).getWidth());
                }
            }
            if(enemyTeam.get(i).getY() == 0) {
                enemyTeam.get(i).setY(getHeight() / 2);
            }
            g.drawImage(enemyTeam.get(i).getImage(), enemyTeam.get(i).getX() + minx, enemyTeam.get(i).getY() + miny, enemyTeam.get(i).getWidth() + 35, enemyTeam.get(i).getHeight() + 35, null);
        }
        fighters.get(currentIndex).paint(g);
        if(currentIndex == 0) {
            Font font = new Font("Arial", Font.BOLD, 40);
            FontMetrics f = g.getFontMetrics(font);
            g.setColor(Color.red);
            g.setFont(font);
            g.drawString("C'est ton tour", (getWidth() - f.stringWidth("C'est ton tour")) / 2, 100);
        }
        if(currentIndex >= heroTeam.size()) {
            Font font = new Font("Arial", Font.BOLD, 40);
            FontMetrics f = g.getFontMetrics(font);
            g.setColor(Color.red);
            g.setFont(font);
            g.drawString("C'est pas ton tour #Connard", (getWidth() - f.stringWidth("C'est ton tour #Connard")) / 2, 100);
        }
    }
    
    public void keyPressed(int k) {
        fighters.get(currentIndex).keyPressed(k);
    }
    
    public void keyReleased(int k) {
        fighters.get(currentIndex).keyReleased(k);
        if(k == KeyEvent.VK_F10) {
            finishFight(true);
        }
    }
    
    public void finishFight(boolean win) {
        gsm.setState(GameStateManager.LEVEL);
        LevelChoice l = (LevelChoice) gsm.getState();
        GameLevelState g = l.getLevel();
        g.removeEnemies(monstersID);
    }
    
    public void init(Hero hero, Enemy[] ennemies) {
        HeroFighter heroFighter = new HeroFighter();
        heroFighter.setBot(false);
        heroTeam.add(heroFighter);
        fighters.add(heroFighter);
        monstersID = new int[ennemies.length];
        int i = 0;
        for(Enemy en : ennemies) {
            //Fighter f = EnemyList.get(en.getMonsterID());
            //f.setBot(true);
            monstersID[i] = en.getInstantID();
            //enemyTeam.add(f);
            //fighters.add(f);
            i++;
        }
    }
    
    public void init() {
        heroTeam.clear();
        enemyTeam.clear();
        fighters.clear();
    }
    
    public void nextTurn() {
        System.out.println(currentIndex);
        currentIndex += 1;
        if(currentIndex == fighters.size()) {
            currentIndex = 0;
        }
    }
}