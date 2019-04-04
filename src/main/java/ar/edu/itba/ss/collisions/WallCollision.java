package ar.edu.itba.ss.collisions;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;

public class WallCollision extends Collision {
    private Particle particle;
    private Wall wall;

    public WallCollision(Double time, Particle particle, Wall wall) {
        super(time);
        this.particle = particle;
        this.wall = wall;
    }

    public Particle getParticle() {
        return particle;
    }

    public Wall getWall() {
        return wall;
    }
}
