package ar.edu.itba.ss.io;
import ar.edu.itba.ss.models.Particle;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Input {


    private Long smallParticlesQuantity;
    private List<Particle> particles;
    private static final Double systemSideLength = 0.5;
    private static final Double maxVelocityModule = 0.1;
    public static Double time = 100.0;
    private static final Double dt = 0.1;
    private static final Double smallParticleRadio = 0.005;
    private static final Double bigParticleRadio = 0.05;
    private static final Double smallParticleMass = 0.0001;
    private static final Double bigParticleMass = 0.1;
    private static final int FRAMES_PER_SECOND = 15;
    private static Double framesFactor;

    // Defined values
//    private static int defaultIterations = 1000;
//    private static Double defaultInteractionRadio = 1.0;
//    private static Double MAX_PARTICLE_RADIO = 0.5;
//    private static Double MIN_PARTICLE_RADIO = 0.2;
//    private static Double defaultVelocityModule = 0.03;
//    private static Double MIN_ANGLE = 0.0;
//    private static Double MAX_ANGLE = Math.PI * 2;
//    private double noise;


    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(Long quantity){
        System.out.print("[Generating Input... ");
        Random random = new Random();
        this.smallParticlesQuantity = quantity;
        this.particles = new ArrayList<>();

//      Setting the big particle
        this.particles.add(new Particle(
                systemSideLength/2,
                systemSideLength/2,
                0,
                0,
                    bigParticleRadio,
                    bigParticleMass

        ));

//      Setting the small particles with random position and velocity (respecting max module)
        for (int p = 0 ; p < smallParticlesQuantity ; p++ ){
            Double x,y,vX,vY;
            do{
                x =  ThreadLocalRandom.current().nextDouble(smallParticleRadio, systemSideLength-smallParticleRadio);;
                y =  ThreadLocalRandom.current().nextDouble(smallParticleRadio, systemSideLength-smallParticleRadio);;
                vX = random.nextDouble() * maxVelocityModule;
                vY = random.nextDouble() * maxVelocityModule;
                vX = random.nextBoolean()?vX:-vX;
                vY = random.nextBoolean()?vY:-vY;
            }while(!noOverlapParticle(x,y) || Math.hypot(vX, vY) > maxVelocityModule);
            this.particles.add(new Particle(
                    x,
                    y,
                    vX,
                    vY,
                    smallParticleRadio,
                    smallParticleMass
            ));
        }
        Double averageFramesPerSecond = 3*Math.exp(-6*Math.pow(smallParticlesQuantity,3)) + 0.0041*Math.pow(smallParticlesQuantity,2) + 0.1057*smallParticlesQuantity + 2.0049;
        this.framesFactor = averageFramesPerSecond / FRAMES_PER_SECOND;
        System.out.println("Done.]");
    }

    private boolean noOverlapParticle(Double x, Double y){
        if (particles.size() == 0) return true;
        for (Particle particle : particles){
                if ( (Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2)) <= Math.pow(particle.getRadius() + smallParticleRadio, 2)){
                    return false;
            }
        }
        return true;
    }

    /**
     * A constructor that generates an {@link Input} instance obtaining parameters from input files.
     *
     * @param staticFileName        The static parameters file, such as side length.
     * @param dinamicFileName       The dinamic parameters file, such as velocity in one state for each particle.
     * @param noise                 The noise param.
     * @throws IOException          e.g. if one of the files cannot be founded.
     */
    public Input(String staticFileName, String dinamicFileName, double noise, int quantity,int length) throws IOException{
//        this.contornCondition = true;
//        this.systemSideLength = length;
//        this.interactionRadio = defaultInteractionRadio;
//        BufferedReader staticFileReader, dinamicFileReader;
//        try{
//            // Static file
//            staticFileReader = new BufferedReader(new FileReader(staticFileName));
//            dinamicFileReader = new BufferedReader(new FileReader(dinamicFileName));
//            this.particlesQuantity = Long.valueOf(quantity);
//            this.noise=noise;
//            this.cellSideQuantity = (int) Math.ceil(systemSideLength/interactionRadio) - 1 ;
//            this.particles = new ArrayList<>();
//            dinamicFileReader.readLine();  //Discard first time notation
//            while(staticFileReader.ready()){    //Only time zero for dinamic file
//                String[] staticLineValues = staticFileReader.readLine().split(" ");
//                String[] dinamicLineValues = dinamicFileReader.readLine().split(" ");
//                Particle p =new Particle(
//                        Double.valueOf(staticLineValues[0]),
//                        staticLineValues[1],
//                        Double.valueOf(dinamicLineValues[0]),
//                        Double.valueOf(dinamicLineValues[1]),
//                        Double.valueOf(dinamicLineValues[2]),
//                        Double.valueOf(dinamicLineValues[3])
//                );
//                particles.add(p);
//            }
//            if (particles.size() != particlesQuantity)
//                throw new IllegalArgumentException();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }

    public Long getSmallParticlesQuantity() {
        return smallParticlesQuantity;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public static Double getSystemSideLength() {
        return systemSideLength;
    }

    public static Double getMaxVelocityModule() {
        return maxVelocityModule;
    }

    public static Double getTime() {
        return time;
    }

    public static Double getDT() {
        return dt;
    }

    public static Double getFramesFactor() {
        return framesFactor;
    }
}
