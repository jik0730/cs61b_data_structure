/**
 * Created by Ingyo on 2016. 4. 28..
 */
public class StrangeBitwiseGenerator implements Generator {
    private int state;
    private int period;

    /** constructor */
    public StrangeBitwiseGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    public double next() {
        state += 1;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return (2.0 / (double) period) * ((double) weirdState - ((double) period * (weirdState / period))) - (double) 1;
    }

//    public static void main(String args[]) {
//        Generator generator = new StrangeBitwiseGenerator(512);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);
//    }
}
