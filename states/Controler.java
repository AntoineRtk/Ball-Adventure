
package states;

public class Controler {
    
    private static boolean leftPressed, rightPressed, upPressed, downPressed, ctrlPressed, spacePressed, enterPressed;
    
    public static void setLeft(boolean left) {
        Controler.leftPressed = left;
    }
    
    public static void setRight(boolean right) {
        Controler.rightPressed = right;
    }
    
    public static void setUp(boolean up) {
        Controler.upPressed = up;
    }
    
    public static void setDown(boolean down) {
        Controler.downPressed = down;
    }
    
    public static void setSpace(boolean space) {
        Controler.spacePressed = space;
    }
    
    public static void setControl(boolean ctrl) {
        Controler.ctrlPressed = ctrl;
    }
    
    public static void setEnter(boolean enter) {
        Controler.enterPressed = enter;
    }
    
    public static boolean isLeft() {
        return Controler.leftPressed;
    }
    
    public static boolean isRight() {
        return Controler.rightPressed;
    }
    
    public static boolean isUp() {
        return Controler.upPressed;
    }
    
    public static boolean isDown() {
        return Controler.downPressed;
    }
    
    public static boolean isSpace() {
        return Controler.spacePressed;
    }
    
    public static boolean isControl() {
        return Controler.ctrlPressed;
    }
    
    public static boolean isEnter() {
        return Controler.enterPressed;
    }
}