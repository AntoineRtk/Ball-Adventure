package unused;


import unused.DinosaurFighter;
import unused.Fighter;
import java.util.ArrayList;


public class EnemyList {
    
    private static ArrayList<Fighter> fighters = new ArrayList<>();
    
    public static void init() {
        DinosaurFighter dino = new DinosaurFighter();
        dino.setName("Dinosaure");
        fighters.add(dino);
    }
    
    public static Fighter get(int monsterID) {
        return fighters.get(monsterID);
    }
}