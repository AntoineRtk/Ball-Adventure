package states;

public class State {
    
    private double x, y;
    private double jumpStart, moveSpeed, maxSpeed, fallingSpeed, maxFallingSpeed, waterMoveSpeed, waterJumpSpeed, waterFallingSpeed;
    
    public State(double x, double y, double jumpStart, double moveSpeed, double maxSpeed, double fallingSpeed, double maxFallingSpeed, double waterMoveSpeed, double waterJumpSpeed, double waterFallingSpeed) {
        this.x = x;
        this.y = y;
        this.jumpStart = jumpStart;
        this.moveSpeed = moveSpeed;
        this.maxSpeed = maxSpeed;
        this.fallingSpeed = fallingSpeed;
        this.maxFallingSpeed = maxFallingSpeed;
        this.waterMoveSpeed = waterMoveSpeed;
        this.waterJumpSpeed = waterJumpSpeed;
        this.waterFallingSpeed = waterFallingSpeed;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getJumpStart() {
        return jumpStart;
    }
    
    public double getMoveSpeed() {
        return moveSpeed;
    }
    
    public double getMaxSpeed() {
        return maxSpeed;
    }
    
    public double getFallingSpeed() {
        return fallingSpeed;
    }
    
    public double getMaxFallingSpeed() {
        return maxFallingSpeed;
    }
    
    public double getWaterMoveSpeed() {
        return waterMoveSpeed;
    }
    
    public double getWaterJumpSpeed() {
        return waterJumpSpeed;
    }
    
    public double getWaterFallingSpeed() {
        return waterFallingSpeed;
    }
}