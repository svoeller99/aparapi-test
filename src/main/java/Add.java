import com.aparapi.Kernel;
import com.aparapi.Range;

public class Add {
    private static final boolean debug = false;

    public static void main(String[] _args) {

        final int size = 1_000_000;

        final float[] a = new float[size];
        final float[] b = new float[size];

        for (int i = 0; i < size; i++) {
            a[i] = (float) (Math.random() * 100);
            b[i] = (float) (Math.random() * 100);
        }

        final float[] sum = new float[size];

        try (AutoStopWatch sw = new AutoStopWatch("withKernel")) {
            withKernel(size, a, b, sum);
        }

        try (AutoStopWatch sw = new AutoStopWatch("withoutKernel")) {
            withoutKernel(size, a, b, sum);
        }
    }

    private static void withoutKernel(int size, float[] a, float[] b, float[] sum) {
        for(int i = 0; i < size; i++) {
            sum[i] = a[i] + b[i];
        }

        printResult(size, a, b, sum);
    }

    private static void withKernel(int size, float[] a, float[] b, float[] sum) {
        Kernel kernel = new Kernel(){
            @Override public void run() {
                int gid = getGlobalId();
                sum[gid] = a[gid] + b[gid];
            }
        };

        kernel.execute(Range.create(size));

        printResult(size, a, b, sum);

        kernel.dispose();
    }

    private static void printResult(int size, float[] a, float[] b, float[] sum) {
        if (!debug) return;

        for (int i = 0; i < size; i++) {
            System.out.printf("%6.2f + %6.2f = %8.2f\n", a[i], b[i], sum[i]);
        }
    }
}
