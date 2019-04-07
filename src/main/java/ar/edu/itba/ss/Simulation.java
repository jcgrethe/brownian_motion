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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Simulation {

    private static double EPSILON = 0.001;
    private double size;
    private double time;
    private double dt;
    private Input input;
    public static Double startTime = (double)(System.currentTimeMillis()/1000);
    public static Double simulationCurrentTime = 0.0;
    private TreeSet<Collision> collisions;

    public Simulation(double size, double time, double dt, Input input) {
        this.size = Input.getSystemSideLength();
        this.time = time;
        this.dt = dt;
        this.input = input;
        this.collisions = new TreeSet<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return Double.compare(o1.getTime(), o2.getTime());
            }
        });
    }


    public void start(){
        //Calculate time (Tc) to next collision
        //Evolve particles to Tc
        //Save Tc state
        //Determine new velocities
        getCollisions(input.getParticles());
        while(!collisions.isEmpty() || input.getTime() < startTime - (double)(System.currentTimeMillis()/1000)){
            Collision nextCollision = collisions.pollFirst();
            evolveParticles(input.getParticles(), nextCollision.getTime() - simulationCurrentTime);
            if (nextCollision instanceof ParticleCollision){
                collide((ParticleCollision) nextCollision);
            }else if (nextCollision instanceof WallCollision){
                collide((WallCollision) nextCollision);
            }
            simulationCurrentTime = nextCollision.getTime();
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
        particles.stream().forEach(particle -> particle.evolve(time));
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
                second.getvX() - Jx/second.getMass(),
                second.getvY() - Jy/second.getMass()
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
            case CORNER:
                wallCollision.getParticle().updateMotion(-wallCollision.getParticle().getvX(), -wallCollision.getParticle().getvY());
                break;
            default: throw new IllegalStateException(); //This should not happen.
        }
    }

    private void getCollisions(List<Particle> particles){
        particles.stream().forEach(
                particle -> {
            Collision aux = CollisionValidator.wallCollision(particle,this.size);
            if (aux!=null)
                collisions.add(aux);
        });

        particles.stream().forEach(
                particle -> {
                    this.input.getParticles().stream().forEach(
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
        Supplier<TreeSet<Collision>> supplier = () -> new TreeSet<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return Double.compare(o1.getTime(), o2.getTime());
            }
        });
        collisions = collisions.stream().filter(collision -> !CollisionValidator.hasCommonParticles(currentCollision, collision)).collect(Collectors.toCollection(supplier));
        getCollisions(currentCollisionParticles);
    }

}
