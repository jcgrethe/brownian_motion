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
    private Map<Integer,Integer> collisionsPerUnitOfTime;
    private static Integer collisionCounter = 0;

    public Simulation(double size, double time, double dt, Input input) {
        this.size = Input.getSystemSideLength();
        this.time = time;
        this.dt = dt;
        this.input = input;
        this.collisionsPerUnitOfTime  = new HashMap<>();
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
        System.out.println("Starting Simulation");
        while(!collisions.isEmpty() && simulationCurrentTime < Input.getTime() ){
            Collision nextCollision = collisions.first();
            collisions.remove(nextCollision);
            evolveParticles(input.getParticles(), nextCollision.getTime() - simulationCurrentTime);
            if (nextCollision instanceof ParticleCollision){
                collide((ParticleCollision) nextCollision);
            }else if (nextCollision instanceof WallCollision){
                collide((WallCollision) nextCollision);
            }
            if (collisionsPerUnitOfTime.get((int)Math.floor(simulationCurrentTime)) == null){
                collisionsPerUnitOfTime.put((int)Math.floor(simulationCurrentTime),1);
            }else{
                collisionsPerUnitOfTime.put((int)Math.floor(simulationCurrentTime),collisionsPerUnitOfTime.get((int)Math.floor(simulationCurrentTime))+1);
            }
            simulationCurrentTime = nextCollision.getTime();
            updateCollisions(nextCollision);
            if (Math.floor(collisionCounter%input.getFramesFactor())==0){
                try{
                    Output.printToFile(input.getParticles());
                }catch (IOException e){
                    System.out.println(e);
                }
            }
            collisionCounter++;
        }
        System.out.println("Simulation Finished");
        Output.generateStatistics(collisionsPerUnitOfTime);

    }

    private void evolveParticles(List<Particle> particles, Double time){
        particles.stream().forEach(particle -> particle.evolve(time));
    }

    private void collide(ParticleCollision particleCollision){
        // Different Mass - Elastic Collision

        Particle first = particleCollision.getFirst();
        Particle second = particleCollision.getSecond();

        Double dX = second.getX() - first.getX();
        Double dY = second.getY() - first.getY();
        Double dVX = second.getvX() - first.getvX();
        Double dVY = second.getvY() - first.getvY();
        Double dVdR = dVX*dX + dVY*dY;
        Double sigma = first.getRadius() + second.getRadius();

        Double J = (2*first.getMass()*second.getMass()*dVdR) / (sigma*(first.getMass() + second.getMass()));
        Double Jx = (J*dX) / sigma;
        Double Jy = (J*dY) / sigma;

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

    private void getCollisions(List<Particle> particles, ParticleCollision c){
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
                                ParticleCollision aux = (ParticleCollision) CollisionValidator.particleCollision(particle, particle1);
                                if (aux!=null && !aux.sameCollision(c) )
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
        if (currentCollision instanceof ParticleCollision)
            getCollisions(currentCollisionParticles, (ParticleCollision) currentCollision);
        else
            getCollisions(currentCollisionParticles);
    }

}
