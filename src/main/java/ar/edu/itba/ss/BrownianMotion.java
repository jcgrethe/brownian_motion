package ar.edu.itba.ss;

import ar.edu.itba.ss.collisions.Collision;
import ar.edu.itba.ss.collisions.CollisionValidator;
import ar.edu.itba.ss.collisions.ParticleCollision;
import ar.edu.itba.ss.collisions.WallCollision;
import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import ar.edu.itba.ss.models.Particle;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public final class BrownianMotion {
    private BrownianMotion() {
    }



    public static void main(String[] args) {
        Input input = new Input(Long.valueOf(100));
        Output.generateXYZFile();
        Simulation simulation = new Simulation(10, 10, 0.1, input);
        simulation.start();
    }

}
