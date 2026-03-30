package invoker54.invocore.common.util;

import invoker54.invocore.client.util.InvoZone;
import org.joml.Vector2f;

public class MathUtil {

    public static float lerp(double value, double min, double max) {
        return (float) (min + value * (max - min));
    }

    public static double clampLoop(double value, double min, double max) {
        if (min == max) return max;
        if (min > max) {
            double holder = min;
            min = max;
            max = holder;
        }

        double difference = min - max;

        if (value > max) return clampLoop(value + difference, min, max);

        if (value < min) return clampLoop(value - difference, min, max);

        return value;
    }

    public static double clampPingPong(double value, double min, double max) {
        if (min == max) return max;
        if (min > max) {
            double holder = min;
            min = max;
            max = holder;
        }

        double difference = min - max;

        if (value > max) return clampLoop(-(value + difference), min, max);

        if (value < min) return clampLoop(-(value - difference), min, max);

        return value;
    }

    public static Vector2f rotateAroundPoint(Vector2f originPos, Vector2f endPos, double rotateAmount){

        // "Liberated" from: https://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
        Vector2f rotatedPoint = new Vector2f(
                (float) (Math.cos(rotateAmount) * (endPos.x - originPos.x) - Math.sin(rotateAmount) * (endPos.y - originPos.y) + originPos.x),
                (float) (Math.sin(rotateAmount) * (endPos.x - originPos.x) + Math.cos(rotateAmount) * (endPos.y - originPos.y) + originPos.y)
        );
        return rotatedPoint;
    }

    public static double lookRotation(Vector2f startPos, Vector2f endPos){
        return lookRotation(0, startPos, endPos);
    }

    public static double lookRotation(double startRotation, Vector2f startPos, Vector2f endPos){
        //North is 90 degrees
        //East is 0 degrees
        //South is 270 degrees
        //West is 180 degrees

        double resultRotation = (Math.toDegrees(Math.atan2(endPos.y() - startPos.y(), endPos.x() - startPos.x())));
        if (resultRotation < 0) resultRotation = 360 + resultRotation;

//        LOGGER.warn("Rotation: " + "start:"+(startRotation)+", result:"+resultRotation+", final:"+( resultRotation - startRotation));

        return resultRotation - startRotation;
    }

    public static InvoZone zoneLerp(double percentage, InvoZone beginZone, InvoZone endZone){
        InvoZone lerpZone = beginZone.copy();

        //First middle
        double middleX = MathUtil.lerp(percentage, beginZone.middleX(), endZone.middleX());
        double middleY = MathUtil.lerp(percentage, beginZone.middleY(), endZone.middleY());
        double width = MathUtil.lerp(percentage, beginZone.width(), endZone.width());
        double height = MathUtil.lerp(percentage, beginZone.height(), endZone.height());

        lerpZone.setWidth((float) width).setHeight((float) height).centerX((float) middleX).centerY((float) middleY);
        return lerpZone;
    }

    public static double percentageLerp(double value, double begin, double end){
        double maxDistance = end - begin;
        double valueDistance = value - begin;

        if (maxDistance == 0) maxDistance = 1;

        return valueDistance/maxDistance;
    }

    public static int randomInt(int min, int max){
        return Math.round(lerp(Math.random(), min, max));
    }

    public static float randomFloat(float min, float max){
        return lerp(Math.random(), min, max);
    }

    public static boolean isInRange(double value, double start, double end){
        double min = Math.min(start, end);
        double max = Math.max(start, end);

        return min <= value && value <= max;
    }

    public enum EaseType{
        EASEINSINE{
            @Override
            public double getEase(double x) {
                return 1 - Math.cos((x * Math.PI) / 2);
            }
        },
        EASEOUTSINE{
            @Override
            public double getEase(double x) {
                return Math.sin((x * Math.PI) / 2);
            }
        },
        EASEINOUTSINE{
            @Override
            public double getEase(double x) {
                return -(Math.cos(Math.PI * x) - 1) / 2;
            }
        },

        EASEINQUAD{
            @Override
            public double getEase(double x) {
                return x * x;
            }
        },
        EASEOUTQUAD{
            @Override
            public double getEase(double x) {
                return 1 - (1 - x) * (1 - x);
            }
        },
        EASEINOUTQUAD{
            @Override
            public double getEase(double x) {
                return x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2;
            }
        },

        EASEINCUBIC{
            @Override
            public double getEase(double x) {
                return x * x * x;
            }
        },
        EASEOUTCUBIC{
            @Override
            public double getEase(double x) {
                return 1 - Math.pow(1 - x, 3);
            }
        },
        EASEINOUTCUBIC{
            @Override
            public double getEase(double x) {
                return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
            }
        },

        EASEINQUART{
            @Override
            public double getEase(double x) {
                return x * x * x * x;
            }
        },
        EASEOUTQUART{
            @Override
            public double getEase(double x) {
                return 1 - Math.pow(1 - x, 4);
            }
        },
        EASEINOUTQUART{
            @Override
            public double getEase(double x) {
                return x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2;
            }
        },

        EASEINQUINT{
            @Override
            public double getEase(double x) {
                return x * x * x * x * x;
            }
        },
        EASEOUTQUINT{
            @Override
            public double getEase(double x) {
                return 1 - Math.pow(1 - x, 5);
            }
        },
        EASEINOUTQUINT{
            @Override
            public double getEase(double x) {
                return x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2;
            }
        },

        EASEINEXPO{
            @Override
            public double getEase(double x) {
                return x == 0 ? 0 : Math.pow(2, 10 * x - 10);
            }
        },
        EASEOUTEXPO{
            @Override
            public double getEase(double x) {
                return x == 1 ? 1 : 1 - Math.pow(2, -10 * x);
            }
        },
        EASEINOUTEXPO{
            @Override
            public double getEase(double x) {
                return x == 0
                        ? 0
                        : x == 1
                        ? 1
                        : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                        : (2 - Math.pow(2, -20 * x + 10)) / 2;
            }
        },

        EASEINCIRC{
            @Override
            public double getEase(double x) {
                return 1 - Math.sqrt(1 - Math.pow(x, 2));
            }
        },
        EASEOUTCIRC{
            @Override
            public double getEase(double x) {
                return Math.sqrt(1 - Math.pow(x - 1, 2));
            }
        },
        EASEINOUTCIRC{
            @Override
            public double getEase(double x) {
                return x < 0.5
                        ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
                        : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2;
            }
        },

        EASEINBACK{
            @Override
            public double getEase(double x) {
                double c1 = 1.70158;
                double c3 = c1 + 1;

                return c3 * x * x * x - c1 * x * x;
            }
        },
        EASEOUTBACK{
            @Override
            public double getEase(double x) {
                double c1 = 1.70158;
                double c3 = c1 + 1;

                return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
            }
        },
        EASEINOUTBACK{
            @Override
            public double getEase(double x) {
                double c1 = 1.70158;
                double c2 = c1 * 1.525;

                return x < 0.5
                        ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
                        : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
            }
        },

        EASEINELASTIC{
            @Override
            public double getEase(double x) {
                double c4 = (2 * Math.PI) / 3;

                return x == 0
                        ? 0
                        : x == 1
                        ? 1
                        : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4);
            }
        },
        EASEOUTELASTIC{
            @Override
            public double getEase(double x) {
                double c4 = (2 * Math.PI) / 3;

                return x == 0
                        ? 0
                        : x == 1
                        ? 1
                        : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
            }
        },
        EASEINOUTELASTIC{
            @Override
            public double getEase(double x) {
                double c5 = (2 * Math.PI) / 4.5;

                double sin = Math.sin((20 * x - 11.125) * c5);
                return x == 0
                        ? 0
                        : x == 1
                        ? 1
                        : x < 0.5
                        ? -(Math.pow(2, 20 * x - 10) * sin) / 2
                        : (Math.pow(2, -20 * x + 10) * sin) / 2 + 1;
            }
        },

        EASEINBOUNCE{
            @Override
            public double getEase(double x) {
                return 1 - EASEOUTBOUNCE.getEase(1 - x);
            }
        },
        EASEOUTBOUNCE{
            @Override
            public double getEase(double x) {
                double n1 = 7.5625;
                double d1 = 2.75;

                if (x < 1 / d1) {
                    return n1 * x * x;
                } else if (x < 2 / d1) {
                    return n1 * (x -= 1.5 / d1) * x + 0.75;
                } else if (x < 2.5 / d1) {
                    return n1 * (x -= 2.25 / d1) * x + 0.9375;
                } else {
                    return n1 * (x -= 2.625 / d1) * x + 0.984375;
                }
            }
        },
        EASEINOUTBOUNCE{
            @Override
            public double getEase(double x) {
                return x < 0.5
                        ? (1 - EASEOUTBOUNCE.getEase(1 - 2 * x)) / 2
                        : (1 + EASEOUTBOUNCE.getEase(2 * x - 1)) / 2;
            }
        },
        EASESTEP {
            @Override
            public double getEase(double x) {
                return getEaseWithSteps(x, 1);
            }

            public double getEaseWithSteps(double x, int steps){
                if (steps == 0) return 1;

                int stepsTaken = (int) (x * steps);
                return ((double) stepsTaken /steps);
            }
        };

        public abstract double getEase(double x);
    }
}
