package ar.edu.itba.ss;

public class Particle {
    private static Long serial_id = Long.valueOf(0);

    private final Long id;
    private double x;
    private double y;
    private double vX;
    private double vY;
    private double vAngle;
    private double vModule;
    private final double radius;
    private final double mass;
    private int collisionsCount;

    public Particle(double x, double y, double vX, double vY, double radius, double mass) {
        this.id = serial_id++;
        this.x = x;
        this.y = y;
        this.vX = vX;
        this.vY = vY;
        this.radius = radius;
        this.mass = mass;
        this.collisionsCount = 0;
        this.vModule = Math.hypot(vX, vY);
        this.vAngle = Math.atan(vY/vX);
    }

    public Long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getvX() {
        return vX;
    }

    public double getvY() {
        return vY;
    }

    public double getvAngle() {
        return vAngle;
    }

    public double getvModule() {
        return vModule;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public int getCollisionsCount() {
        return collisionsCount;
    }
}
