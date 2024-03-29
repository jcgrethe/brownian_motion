package ar.edu.itba.ss.collisions;

import ar.edu.itba.ss.models.Particle;

public class ParticleCollision extends Collision {
    private final Particle first;
    private final Particle second;

    public ParticleCollision(Double time, Particle first, Particle second) {
        super(time);
        this.first = first;
        this.second = second;
    }

    public Particle getFirst() {
        return first;
    }

    public Particle getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ParticleCollision){
            ParticleCollision other = (ParticleCollision) obj;
            if (
                    (getFirst() == other.getFirst() && getSecond() == other.getSecond() && getTime().equals(other.getTime()))
                ||  (getFirst() == other.getSecond() && getSecond() == other.getFirst() && getTime().equals(other.getTime()))
            )
                return true;
            else
                return false;
        }
        return false;
    }

    public boolean sameCollision(Object obj) {
        if (obj instanceof ParticleCollision){
            ParticleCollision other = (ParticleCollision) obj;
            if (
                    (getFirst() == other.getFirst() && getSecond() == other.getSecond() )
                            ||  (getFirst() == other.getSecond() && getSecond() == other.getFirst() )
                    )
                return true;
            else
                return false;
        }
        return false;
    }


}
