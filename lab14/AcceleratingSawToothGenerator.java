/**
 * Created by Ingyo on 2016. 4. 28..
 */
public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.state = 0;
        this.period = period;
        this.factor = factor;
    }

    public double next() {
        state += 1;
        int temp = state % period;
        state = state % period;
        if (temp == 0) {
            period *= factor;
        }
        return (2.0 / (double) period) * ((double) state - (double) period) + (double) 1;
    }

//    public static void main(String[] args) {
//        Generator generator = new AcceleratingSawToothGenerator(200, 1.1);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);
//    }
}
