package ar.edu.itba.ss.io;
import ar.edu.itba.ss.Particle;
import sun.security.krb5.internal.PAData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Output {
    private final static String FILENAME = "output.txt";
    private final static String FILENAME2 = "positions.xyz";
    private final static String STATIC_FILE = "sample_input_static.txt";
    private final static String DINAMIC_FILE = "sample_input_dinamic.txt";

    public static void printToFile(BufferedWriter bufferedWriter, Particle particle) throws IOException {
        bufferedWriter.newLine();
        String print = particle.getId()
                + " " + particle.getX()
                + " " + particle.getY()
                + " " + particle.getvX()
                + " " + particle.getvY()
                + " " + particle.getRadius()
                + " " + particle.getMass();
        bufferedWriter.write(print);
    }

    public static void generateXYZFile(List<Particle> particles){
        try{
            FileWriter fileWriter = new FileWriter(STATIC_FILE);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(particles.size()));

            // For each state
            for (Particle particle : particles){
                printToFile(bufferedWriter, particle);
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void generateInputFiles(Long totalParticlesQuantity, int sideLength, List<Particle> particles){
        try{
            FileWriter fileWriter = new FileWriter(STATIC_FILE);
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
            FileWriter fileWriter = new FileWriter(DINAMIC_FILE);
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
