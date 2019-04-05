package ar.edu.itba.ss.io;
import ar.edu.itba.ss.models.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Output {
    private final static String FILENAME = "output.txt";
    private final static String SIMULATION_FILENAME = "positions.xyz";
    private final static String STATIC_FILENAME = "sample_input_static.txt";
    private final static String DINAMIC_FILENAME = "sample_input_dinamic.txt";
    private static BufferedWriter simulationBufferedWriter;


    public static void printToFile(List<Particle> particles) throws IOException {
        simulationBufferedWriter.write(particles.size());
        simulationBufferedWriter.newLine();
        simulationBufferedWriter.newLine();
        particles.stream().parallel().forEach(particle -> {
            try{
                simulationBufferedWriter.write(particle.getId()
                        + " " + particle.getX()
                        + " " + particle.getY()
                        + " " + particle.getvX()
                        + " " + particle.getvY()
                        + " " + particle.getRadius()
                        + " " + particle.getMass());
                simulationBufferedWriter.newLine();
            }catch (IOException e){
                System.out.println(e);
            }
        });
    }

    public static void generateXYZFile(List<Particle> particles){
        try{
            FileWriter fileWriter = new FileWriter(SIMULATION_FILENAME);
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void generateInputFiles(Long totalParticlesQuantity, int sideLength, List<Particle> particles){
        try{
            FileWriter fileWriter = new FileWriter(STATIC_FILENAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(totalParticlesQuantity));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(sideLength));
            bufferedWriter.newLine();
            for (int i = 0 ; i < particles.size() ; i++){
                bufferedWriter.write(particles.get(i).getRadius() + " " + particles.get(i).getMass());
                if (i != particles.size() - 1 ){
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch(IOException e){
            System.out.println(e);
        }

        try{
            FileWriter fileWriter = new FileWriter(DINAMIC_FILENAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            for (int i = 0 ; i < particles.get(0).getStates().size() ; i++){
                bufferedWriter.write(String.valueOf(0));
                bufferedWriter.newLine();
                for (int p = 0 ; p < particles.size() ; p++){
                    bufferedWriter.write(
                        particles.get(p).getX() + " " +
                            particles.get(p).getY() + " " +
                            particles.get(p).getvX() + " " +
                            particles.get(p).getvY()
                        );
                    if (p != particles.size() - 1){
                        bufferedWriter.newLine();
                    }
                }
//            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
