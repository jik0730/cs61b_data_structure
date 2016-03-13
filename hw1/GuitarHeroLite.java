/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHeroLite {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        synthesizer.GuitarString stringA = new synthesizer.GuitarString(CONCERT_A);
        synthesizer.GuitarString stringC = new synthesizer.GuitarString(CONCERT_C);
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        synthesizer.GuitarString[] string = new synthesizer.GuitarString[37];
        for(int i =0; i < 37; i++) {
            string[i] = new synthesizer.GuitarString(CONCERT_A * Math.pow(2, ((double) i - 24.0) / 12.0));
        }
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int intkey = keyboard.indexOf(key);
                if(intkey >= 0 && intkey <=36){
                    string[intkey].pluck();
                }
            }

        /* compute the superposition of samples */
            double sample = 0;
            for(int i = 0; i < 37; i++) {
                sample = sample + string[i].sample();
            }

        /* play the sample on standard audio */
            StdAudio.play(sample);

        /* advance the simulation of each guitar string by one step */
            for(int i = 0; i < 37; i++) {
                string[i].tic();
            }
        }
    }
}

