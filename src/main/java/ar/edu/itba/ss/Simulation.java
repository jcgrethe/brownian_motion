package ar.edu.itba.ss;

import ar.edu.itba.ss.collisions.Collision;
import ar.edu.itba.ss.collisions.CollisionValidator;
import ar.edu.itba.ss.collisions.ParticleCollision;
import ar.edu.itba.ss.collisions.WallCollision;
import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;

import java.io.IOException;
import java.util.*;

public class Simulation {

    private static double EPSILON = 0.001;
    private double size;
    private double time;
    private double dt;
    private Input input;
    TreeSet<Collision> collisions;

    public Simulation(double size, double time, double dt, Input input) {
        this.size = Input.getSystemSideLength();
        this.time = time;
        this.dt = dt;
        this.input = input;
        this.collisions = new TreeSet<Collision>();
    }


    public void start(){
        //Calculate time (Tc) to next collision
        //Evolve particles to Tc
        //Save Tc state
        //Determine new velocities
        getCollisions(input.getParticles());
        long start = System.currentTimeMillis();
        while(!collisions.isEmpty() || input.getTime() < System.currentTimeMillis() - start){
            Collision nextCollision = collisions.pollFirst();
            if (nextCollision instanceof ParticleCollision){
                collide((ParticleCollision) nextCollision);
            }else if (nextCollision instanceof WallCollision){
                collide((WallCollision) nextCollision);
            }
            evolveParticles(input.getParticles(), nextCollision.getTime());
            updateCollisions(nextCollision);
            try{
                Output.printToFile(input.getParticles());
                System.out.println(nextCollision.getTime());
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    private void evolveParticles(List<Particle> particles, Double time){
        particles.stream().parallel().forEach(particle -> particle.evolve(time));
    }

    private void collide(ParticleCollision particleCollision){
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
    private void collide(WallCollision wallCollision){
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

    private void getCollisions(List<Particle> particles){
        particles.stream().parallel().forEach(
                particle -> {
            Collision aux = CollisionValidator.wallCollision(particle,this.size);
            if (aux!=null)
                collisions.add(aux);
        });

        particles.stream().parallel().forEach(
                particle -> {
                    particles.stream().parallel().forEach(
                            particle1 -> {
                                Collision aux = CollisionValidator.particleCollision(particle, particle1);
                                if (aux!=null)
                                    collisions.add(aux);
                            }
                    );
                }
        );
    }

    private void updateCollisions(Collision currentCollision){
        List<Particle> currentCollisionParticles = new LinkedList<>();
        if (currentCollision instanceof ParticleCollision){
            currentCollisionParticles.add(((ParticleCollision) currentCollision).getFirst());
            currentCollisionParticles.add(((ParticleCollision) currentCollision).getSecond());
        }else if(currentCollision instanceof WallCollision)
            currentCollisionParticles.add(((WallCollision) currentCollision).getParticle());
        collisions.stream().parallel().filter(collision -> !CollisionValidator.hasCommonParticles(currentCollision, collision));
        getCollisions(currentCollisionParticles);
    }

}
