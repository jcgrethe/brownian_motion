package ar.edu.itba.ss;

import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import org.apache.commons.cli.*;
import java.io.IOException;

public final class BrownianMotion {
    private BrownianMotion() {
    }



    public static void main(String[] args) {

        CommandLine cmd = getOptions(args);
        long particlesQuantity = 100;
        if(cmd.getOptionValue('N')!= null){
            particlesQuantity = Long.parseLong(cmd.getOptionValue('N'));
            System.out.println("Particles quantity: "+ particlesQuantity);
        }else {
            System.out.println("Default particles quantity: "+ particlesQuantity);
        }

        Input input = new Input(Long.valueOf(particlesQuantity));

        if(cmd.getOptionValue('t')!= null){
            Input.time = Double.parseDouble(cmd.getOptionValue('t'));
            System.out.println("Time: "+ Input.time);
        }else {
            System.out.println("Default time: "+ Input.time);
        }

        Output.generateXYZFile();
        try {
            // Save first state
            Output.printToFile(input.getParticles());
        }catch (IOException e){
            System.out.println(e);
        }
        Simulation simulation = new Simulation(10, 10, 0.1, input);
        simulation.start();
    }

    private static CommandLine getOptions(String[] args){


        Options options = new Options();

        Option iterations = new Option("t", "time", true, "iterations");
        iterations.setRequired(false);
        options.addOption(iterations);

        Option quantity = new Option("N", "quantity", true, "quantity(N)");
        quantity.setRequired(false);
        options.addOption(quantity);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd=null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
        return cmd;
    }

}
