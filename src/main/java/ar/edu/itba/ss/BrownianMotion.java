package ar.edu.itba.ss;

/**
 * Hello world!
 */
public final class BrownianMotion {
    private BrownianMotion() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Input input = new Input(Long.valueOf(100));
        Double currentTime = 0.0;

        //Calculate time (Tc) to next collision
        //Evolve particles to Tc
        //Save Tc state
        //Determine new velocities
        while(currentTime < input.getTime()){


        }

    }
}
