/**
 * Created by Ingyo on 2016. 4. 28..
 */
public class SawToothGenerator implements Generator {
    private int state;
    private int period;

    /** constructor */
    public SawToothGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    public double next() {
        state += 1;
        state = state % period;
        return (2.0 / (double) period) * ((double) state - (double) period) + (double) 1;
    }

//    public static void main(String args[]) {
//        Generator generator = new SawToothGenerator(512);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);
//    }

}
