package ar.edu.itba.ss;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Input {


    private Long smallParticlesQuantity;
    private List<Particle> particles;
    private static final Double systemSideLength = 0.5;
    private static final Double maxVelocityModule = 0.1;


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
                0.05,
                100
        ));

//      Setting the small particles with random position and velocity (respecting max module)
        for (int p = 0 ; p < smallParticlesQuantity ; p++ ){
            Double vX = random.nextDouble() * (double) maxVelocityModule;
            Double vY = random.nextDouble() * (maxVelocityModule*maxVelocityModule - vX*vX);
            this.particles.add(new Particle(
                    random.nextDouble() * systemSideLength,
                    random.nextDouble() * systemSideLength,
                    vX,
                    vY,
                    0.005,
                    0.1
            ));
        }
        System.out.println("Done.]");
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
}
