package ar.edu.itba.ss;

import ar.edu.itba.ss.collisions.Collision;
import ar.edu.itba.ss.collisions.ParticleCollision;
import ar.edu.itba.ss.collisions.WallCollision;
import ar.edu.itba.ss.io.Input;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * Hello world!
 */
public final class BrownianMotion {
    private BrownianMotion() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Input input = new Input(Long.valueOf(100));
        Double currentTime = 0.0;

        //Calculate time (Tc) to next collision
        //Evolve particles to Tc
        //Save Tc state
        //Determine new velocities
        while(currentTime < input.getTime()){
            Collision nextCollision = getNextCollision(input.getParticles());
            if (nextCollision instanceof ParticleCollision){
                collide((ParticleCollision) nextCollision);
            }else if (nextCollision instanceof WallCollision){
                collide((WallCollision) nextCollision);
            }
        }

    }

    private static void collide(ParticleCollision particleCollision){

    }
    private static void collide(WallCollision particleCollision){

    }

    private static Collision getNextCollision(List<Particle> particles){
        TreeSet<Collision> collisions = new TreeSet<Collision>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return (int) Math.ceil(o1.getTime() - o2.getTime());
            }
        });

        for (Particle particle : particles){
            //Simulate and add to set.
        }
        return collisions.first();
    }
}
