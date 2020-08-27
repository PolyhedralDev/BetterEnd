import com.dfsek.betterend.generation.Interpolator;

public class InterpolatorTest {
    public static void main(String[] args) {
        Interpolator i = new Interpolator(1, 2, 3, 4);
        for(byte x = 0; x <= 4; x++) {
            for(byte z = 0; z <= 4; z++) {
                System.out.print(i.bilerp((double)x/4, (double)z/4) + " ");
            }
            System.out.println();
        }
    }
}
