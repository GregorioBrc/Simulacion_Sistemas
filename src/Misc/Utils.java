package Misc;

import java.text.DecimalFormat;

public class Utils {

    public static double Parse_Dou(String St) {
        St = St.replace(",", ".");
        try {
            return Double.parseDouble(St);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int[] String_To_Array(String Ar) {
        Ar = Ar.substring(1, Ar.length() - 1);

        if (Ar.equals("")) {
            return new int[]{};
        }

        String[] Ax = Ar.split(",");
        int[] Ax_int = new int[Ax.length];

        for (int i = 0; i < Ax.length; i++) {
            Ax_int[i] = Integer.parseInt(Ax[i]);
        }

        return Ax_int;
    }

    public static String Formatear_Num(double Num){
        if (Num > 1e5) {
            return String.format("%.2e", Num);
        } else {
            DecimalFormat df = new DecimalFormat("#,###.###");
            df.setGroupingUsed(true);
            df.setGroupingSize(3);
            return df.format(Num);
        }
    }
}
