package ar.edu.itba.ss;

import ar.edu.itba.ss.collisions.Collision;
import ar.edu.itba.ss.collisions.ParticleCollision;
import ar.edu.itba.ss.collisions.WallCollision;
import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import ar.edu.itba.ss.models.Particle;

import java.io.IOException;
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
        //Calculate time (Tc) to next collision
        //Evolve particles to Tc
        //Save Tc state
        //Determine new velocities
        Collision nextCollision = nextCollision = getNextCollision(input.getParticles());
        for (double currentTime = 0.0 ; currentTime < input.getTime() ; currentTime+=input.getDT()){
            if (nextCollision != null && nextCollision.getTime() < currentTime + input.getDT()){
                //Collision next to happen in this dt
                if (nextCollision instanceof ParticleCollision){
                    collide((ParticleCollision) nextCollision);
                }else if (nextCollision instanceof WallCollision){
                    collide((WallCollision) nextCollision);
                }
                nextCollision = getNextCollision(input.getParticles());
            }
            try{
                Output.printToFile(input.getParticles());
            }catch (IOException e){
                System.out.println(e);
            }
            evolveParticles(input.getParticles(), input.getDT());
        }

    }

    private static void evolveParticles(List<Particle> particles, Double time){
        particles.stream().parallel().forEach(particle -> particle.evolve(time));
    }

    private static void collide(ParticleCollision particleCollision){
        // Different Mass - Elastic Collision

        Particle first = particleCollision.getFirst();
        Particle second = particleCollision.getSecond();

        Double sigma = first.getRadius() + second.getRadius(); //TODO: Re-Check if sigma is R1 + R2
        Double J = (2*first.getMass()*second.getMass()*Math.abs(first.getvModule()-second.getvModule()*
                Math.abs(first.getRadius() - second.getRadius())))/
                ( sigma*(first.getMass() + second.getMass()));
        Double Jx = J*Math.abs(first.getX()-second.getX())/sigma;
        Double Jy = J*Math.abs(first.getY()-second.getY())/sigma;
        first.updateMotion(
                first.getvX() + Jx/first.getMass(),
                first.getvY() + Jy/first.getMass()
        );
        second.updateMotion(
                second.getvX() + Jx/second.getMass(),
                second.getvY() + Jy/second.getMass()
        );

    }
    private static void collide(WallCollision wallCollision){
        switch (wallCollision.getWall().getTypeOfWall()){
            case TOP:
            case BOTTOM:
                wallCollision.getParticle().updateMotion(wallCollision.getParticle().getvX(), -wallCollision.getParticle().getvY());
                break;
            case LEFT:
            case RIGHT:
                wallCollision.getParticle().updateMotion(-wallCollision.getParticle().getvX(), wallCollision.getParticle().getvY());
                break;
            default: throw new IllegalStateException(); //This should not happen.
        }
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
