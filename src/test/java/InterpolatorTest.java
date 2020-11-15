import org.polydev.gaea.math.Interpolator;

public class InterpolatorTest {
    public static void main(String[] args) {
        Interpolator i = new Interpolator(0, 0.5, 0.5, 1, Interpolator.Type.LINEAR);
        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                System.out.print(i.bilerp((double) x / 3, (double) z / 3) + " ");
            }
            System.out.println();
        }
    }
}
