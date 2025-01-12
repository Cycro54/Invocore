package invoker54.invocore.common;

public class MathUtil {

    public static float lerp(double value, double min, double max) {
        return (float) (min + value * (max - min));
    }

    public static int randomInt(int min, int max){
        return Math.round(lerp(Math.random(), min, max));
    }

    public static float randomFloat(float min, float max){
        return lerp(Math.random(), min, max);
    }
}
