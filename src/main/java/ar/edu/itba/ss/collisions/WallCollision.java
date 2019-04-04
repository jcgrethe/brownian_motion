package ar.edu.itba.ss.collisions;

import ar.edu.itba.ss.Particle;

public class WallCollision extends Collision {
    private Particle particle;

    public WallCollision(Double time, Particle particle) {
        super(time);
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }
}
