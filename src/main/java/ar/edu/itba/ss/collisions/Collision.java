package ar.edu.itba.ss.collisions;

public abstract class Collision {
    private final Double time;

    public Double getTime() {
        return time;
    }

    public Collision(Double time) {
        this.time = time;
    }
}
