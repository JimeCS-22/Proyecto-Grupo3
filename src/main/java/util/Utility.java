package util;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Random;

public class Utility {

    private static final Random random = new Random();

    public static String format(long value) {

        return new DecimalFormat("#,###,###").format(value);
    }

    public static int[] generatedSorted(int size, int maxValue) {
        int[] arr = new Random().ints(size,1,maxValue+1)
                .distinct().limit(size).sorted().toArray();

        return arr;

    }

    public static int random(int min, int max) {   //min y max incluyentes
        return min + random.nextInt((max - min) + 1);
    }

    public static String formatBig(String number) {

        number = number.replaceAll("\\s+", ""); // limpiar espacios

        BigInteger big = new BigInteger(number);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(big);
    }

    public static String instanceOf(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) return "Integer";
        if (a instanceof String && b instanceof String) return "String";
        if (a instanceof Character && b instanceof Character) return "Character";
        return "Unknown";
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)) {
            case "Integer":
                Integer int1 = (Integer) a;
                Integer int2 = (Integer) b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0;

            case "String":
                String str1 = (String) a;
                String str2 = (String) b;
                return str1.compareTo(str2) < 0 ? -1 : str1.compareTo(str2) > 0 ? 1 : 0;

            case "Character":
                Character ch1 = (Character) a;
                Character ch2 = (Character) b;
                return ch1.compareTo(ch2) < 0 ? -1 : ch1.compareTo(ch2) > 0 ? 1 : 0;


        }
        return 2; //Unknown
    }
}
