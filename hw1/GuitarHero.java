/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHero {
    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        synthesizer.GuitarString[] guitarStrings = new synthesizer.GuitarString[keyboard.length()];
        for (int i = 0; i < keyboard.length(); i++) {
            guitarStrings[i] = new synthesizer.GuitarString(440 * Math.pow(2, (i - 24.0) / 12.0));
        }
        /* create two guitar strings, for concert A and C */

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                for (int i = 0; i < keyboard.length(); i++) {
                    if (keyboard.charAt(i) == key) {
                        guitarStrings[i].pluck();
                    }
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < keyboard.length(); i++) {
                sample += guitarStrings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < keyboard.length(); i++) {
                guitarStrings[i].tic();
            }
        }
    }
}

