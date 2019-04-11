package ar.edu.itba.ss.io;
import ar.edu.itba.ss.models.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Output {
    private final static String FILENAME = "output.txt";
    private final static String SIMULATION_FILENAME = "positions.xyz";
    private final static String STATIC_FILENAME = "sample_input_static.txt";
    private final static String DINAMIC_FILENAME = "sample_input_dinamic.txt";
    private final static String STATISTICS_FILENAME = "statistics_collisions_per_unit_of_time.csv";
    public final static String STATISTICS_V_0_FILENAME = "statistics_v_0.csv";
    public final static String STATISTICS_V_F_FILENAME = "statistics_v_f.csv";

    private static BufferedWriter simulationBufferedWriter;


    public static void printToFile(List<Particle> particles) throws IOException {
        simulationBufferedWriter.write(String.valueOf(particles.size()));
        simulationBufferedWriter.newLine();
        simulationBufferedWriter.newLine();
        particles.stream().forEach(particle -> {
            try{
                simulationBufferedWriter.write(
                        particle.getId()
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
        simulationBufferedWriter.flush();
    }

    public static void generateXYZFile(){
        try{
            FileWriter fileWriter = new FileWriter(SIMULATION_FILENAME);
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void generateCollisionsStatistics(Map<Integer, Integer> collisionsPerUnitOfTime){
        try{
            FileWriter statisticsFileWriter = new FileWriter(STATISTICS_FILENAME);
            BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);

            statisticsBuffererWriter.write(
                    "time,collisions"
            );
            statisticsBuffererWriter.newLine();
            collisionsPerUnitOfTime.entrySet().forEach(entry -> {
                try{
                    statisticsBuffererWriter.write(
                            entry.getKey() + "," + entry.getValue()
                            );
                    statisticsBuffererWriter.newLine();
                }catch (IOException e){
                    System.out.println(e);
                }
            });
            statisticsBuffererWriter.flush();

        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void generateVelocitiesFile(String filename, List<Particle> particles){
        try{
            FileWriter statisticsFileWriter = new FileWriter(filename);
            BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);

            statisticsBuffererWriter.write(
                    "particle,velocity"
            );
            statisticsBuffererWriter.newLine();
            particles.forEach(particle -> {try{
                statisticsBuffererWriter.write(
                        particle.getId() + "," + particle.getvModule()
                );
                statisticsBuffererWriter.newLine();
            }catch (IOException e){
                System.out.println(e);
            }});
            statisticsBuffererWriter.flush();

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
