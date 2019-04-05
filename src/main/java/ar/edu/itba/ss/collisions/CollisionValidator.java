package ar.edu.itba.ss.collisions;

import ar.edu.itba.ss.Simulation;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;

public class CollisionValidator {


    public static Collision wallCollision(Particle particle, double size){
        Double timeY= Double.POSITIVE_INFINITY;
        Double timeX = Double.POSITIVE_INFINITY;

        if (particle.getvY() >0)
            timeY = ( size - particle.getY() - particle.getRadius() ) / particle.getvY();
        else if (particle.getvY() < 0)
            timeY = ( particle.getY() - particle.getRadius() ) / Math.abs(particle.getvY());

        if (particle.getvX()>0)
            timeX = ( size - particle.getX() - particle.getRadius() ) / particle.getvX();
        else if (particle.getvX() < 0)
            timeX = ( 0 + particle.getX() - particle.getRadius() ) / Math.abs(particle.getvX());

        if(timeX < 0)
            timeX = Double.POSITIVE_INFINITY;

        if(timeY < 0)
            timeY = Double.POSITIVE_INFINITY;

        if (timeX.isInfinite() && timeY.isInfinite()) {
            return null;
        }

        if(timeX < timeY){
            return new WallCollision(timeX + Simulation.simulationCurrentTime, particle, ((particle.getvX()>0) ? new Wall(Wall.typeOfWall.RIGHT) : new Wall(Wall.typeOfWall.LEFT)));
        }

        return new WallCollision(timeY  + Simulation.simulationCurrentTime, particle, ((particle.getvY()>0) ? new Wall(Wall.typeOfWall.BOTTOM) : new Wall(Wall.typeOfWall.TOP)));

    }

    public static Collision particleCollision(Particle p1, Particle p2){

        double time;
        double dRadio = p1.getRadius() + p2.getRadius();
        double dX = p2.getX() - p1.getX();
        double dY = p2.getY() - p1.getY();
        double dvX = p2.getvX() - p1.getvX();
        double dvY = p2.getvY() - p1.getvY();
        double dVdR = dvX*dX + dvY*dY;

        if(dVdR >= 0)
            return null;

        double dR2 = Math.pow(dX, 2) + Math.pow(dY, 2);
        double dV2 = Math.pow(dvX, 2) + Math.pow(dvY, 2);
        double d = Math.pow(dVdR, 2) - dV2*(dR2 - Math.pow(dRadio, 2));


        if(d < 0)
            return null;
        time = -(dVdR + Math.sqrt(d)) / dV2;
        if(time > 0)
            return new ParticleCollision(time  + Simulation.simulationCurrentTime, p1, p2);
        return null;
    }

    public static Boolean hasCommonParticles(Collision one, Collision another){
        if (one instanceof ParticleCollision && another instanceof ParticleCollision)
            return (((ParticleCollision) one).getFirst() == ((ParticleCollision) another).getFirst()
                    || ((ParticleCollision) one).getFirst() == ((ParticleCollision) another).getSecond()
                    || ((ParticleCollision) one).getSecond() == ((ParticleCollision) another).getFirst()
                    || ((ParticleCollision) one).getSecond() == ((ParticleCollision) another).getSecond());
        if (one instanceof WallCollision && another instanceof ParticleCollision)
            return ((WallCollision) one).getParticle() == ((ParticleCollision) another).getFirst() ||
                    ((WallCollision) one).getParticle() == ((ParticleCollision) another).getSecond();
        if (one instanceof ParticleCollision && another instanceof WallCollision)
            return ((WallCollision) another).getParticle() == ((ParticleCollision) one).getFirst() ||
                    ((WallCollision) another).getParticle() == ((ParticleCollision) one).getSecond();
        if (one instanceof WallCollision && another instanceof  WallCollision)
            return ((WallCollision) another).getParticle() == ((WallCollision) another).getParticle();
        return false;
    }
}
