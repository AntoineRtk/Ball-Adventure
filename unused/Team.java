package unused;


import java.util.ArrayList;

public class Team {
    
    ArrayList<Fighter> fighters = new ArrayList<>();
    
    public void add(Fighter f) {
        fighters.add(f);
    }
    
    public void clear() {
        fighters.clear();
    }
    
    public String getName() {
        return new String("é").toUpperCase()+"quipe";
    }

    public int size() {
        return fighters.size();
    }
    
    public Fighter get(int i) {
        return fighters.get(i);
    }
}
