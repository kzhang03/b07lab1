import java.util.Arrays;
import java.io.File;
import java.io.*;

public class Polynomial {

    double [] function;
    int [] exponent;

    public Polynomial() {
        function = null;
        exponent = null;
    }

    public Polynomial(double[] co, int[] exp){
        this.function = co;
        this.exponent = exp;
    }

    public boolean present(String str, char key) {
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == key)
                return true;
        return false;
    }

    public Polynomial(File f) {
        try{
            BufferedReader contents = new BufferedReader(new FileReader(f));
            String curr = contents.readLine();
            String[] arr;

            int temp = 1;
            if (curr.charAt(0) == '-') {
                arr = curr.substring(1).split("-|\\+");
                temp = -1;
            }
            else
                arr = curr.split("-|\\+");
            
            int length = arr.length;
            double[] new_co = new double[length];
            int[] new_exp = new int[length];

            for (int i = 0; i < length; i++) {
                if (!present(arr[i], 'x')) {
                    new_co[i] = temp*Double.parseDouble(arr[i]);
                    new_exp[i] = 0;
                }
                else {
                    int n;
                    for (n = 0; n < arr[i].length(); n++) 
                        if (arr[i].charAt(n) == 'x')
                            break;
                    new_co[i] = temp*Double.parseDouble(arr[i].substring(0, n));
                    new_exp[i] = Integer.parseInt(arr[i].substring(n + 1, arr[i].length()));

                    if (i != 0 && curr.charAt(curr.indexOf(arr[i].substring(0, n)) - 1) == '-')
                        new_co[i] = new_co[i]*-1;
                }
                temp = 1;
            }

            this.function = new_co;
            this.exponent = new_exp;
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public double[] flip_co(double[] co) {
        var j = co.length;
        double[] b = new double[j];
        for (var i : co) {
            b[--j] = i;
        }
        return b;
    }

    public int[] flip_exp(int[] exp) {
        var j = exp.length;
        int[] b = new int[j];
        for (var i : exp) {
            b[--j] = i;
        }
        return b;
    }

    public Polynomial add(Polynomial p) {

        int[] new_arr;
        double[] coefficients;
        int length = 0;

        double[] temp_co1;
        int[] temp_exp1;
        double[] temp_co2;
        int[] temp_exp2;

        if (this.exponent[this.exponent.length - 1] < this.exponent[0]) {
            temp_co1 = flip_co(this.function);
            temp_exp1 = flip_exp(this.exponent);
        }
        else {
            temp_co1 = this.function;
            temp_exp1 = this.exponent;
        }

        if (p.exponent[p.exponent.length - 1] < p.exponent[0]) {
            temp_co2 = flip_co(p.function);
            temp_exp2 = flip_exp(p.exponent);
        }
        else {
            temp_co2 = p.function;
            temp_exp2 = p.exponent;
        }

        int[] longer;
        int[] shorter;

        if (temp_exp1[temp_exp1.length - 1] > temp_exp2[temp_exp2.length - 1]) {
            longer = new int[temp_exp1.length];
            for (int i = 0; i < temp_exp1.length; i++)
                longer[i] = temp_exp1[i];
                            
            shorter = new int[temp_exp2.length];
            for (int i = 0; i < temp_exp2.length; i++)
                shorter[i] = temp_exp2[i];
        }
        else {
            longer = new int[temp_exp2.length];
            for (int i = 0; i < temp_exp2.length; i++)
                longer[i] = temp_exp2[i];
            
            shorter = new int[temp_exp1.length];
            for (int i = 0; i < temp_exp1.length; i++)
                shorter[i] = temp_exp1[i];
        }

        length = longer.length + shorter.length; 
        for (int i = 0; i < longer.length; i++) {
            for (int j = 0; j < shorter.length; j++) {
                if (shorter[j] == longer[i]) {
                    length--;
                    break;
                }
            }
        }

        new_arr = new int[length];

        int curr = longer.length;
        for (int i = 0; i < longer.length; i++)
            new_arr[i] = longer[i];

        for (int i = 0; i < shorter.length; i++) {
            if (Arrays.binarySearch(longer, shorter[i]) <= -1){
                new_arr[curr] = shorter[i];
                curr++;
            }
        }

        Arrays.sort(new_arr);

        coefficients = new double[length];
        int pos;

        for (int i = 0; i < temp_co1.length; i++) {
             pos = Arrays.binarySearch(new_arr, temp_exp1[i]);
             coefficients[pos] += temp_co1[i];
        }
        
        for (int i = 0; i < temp_co2.length; i++) {
             pos = Arrays.binarySearch(new_arr, temp_exp2[i]);
             coefficients[pos] += temp_co2[i];
        }

        Polynomial arr = new Polynomial(coefficients, new_arr);

        return arr;
    }

    public double evaluate(double sub) {

        double soln = 0;

        for (int i = 0; i < this.function.length; i++)
            soln += Math.pow(sub, this.exponent[i])*this.function[i];

        return soln;
    }

    public boolean hasRoot(double root) {
        return this.evaluate(root) == 0;
    }

    public Polynomial multiply(Polynomial p) {

        double[] temp_co1;
        int[] temp_exp1;
        double[] temp_co2;
        int[] temp_exp2;
    
        if (this.exponent[this.exponent.length - 1] < this.exponent[0]) {
            temp_co1 = flip_co(this.function);
            temp_exp1 = flip_exp(this.exponent);
        }
        else {
            temp_co1 = this.function;
            temp_exp1 = this.exponent;
        }
    
        if (p.exponent[p.exponent.length - 1] < p.exponent[0]) {
            temp_co2 = flip_co(p.function);
            temp_exp2 = flip_exp(p.exponent);
        }
        else {
            temp_co2 = p.function;
            temp_exp2 = p.exponent;
        }

        double[] length_co1 = new double[temp_exp1[temp_exp1.length - 1] + 1];
        double[] length_co2 = new double[temp_exp2[temp_exp2.length - 1] + 1];

        for (int i = 0; i < temp_co1.length; i++) {
            length_co1[temp_exp1[i]] = temp_co1[i];
        }
        for (int j = 0; j < temp_co2.length; j++) {
            length_co2[temp_exp2[j]] = temp_co2[j];
        }

        int count = 0;
        double[] m_co = new double[length_co1.length + length_co2.length - 1];

        for (int i = 0; i < length_co1.length; i++) {
            for (int j = 0; j < length_co2.length; j++) {
                m_co[i + j] += length_co1[i]*length_co2[j];
            }
        }
        for (int j = 0; j < m_co.length; j++) {
            if (m_co[j] != 0) {
                count++;
            }
        }

        int pos = 0;
        double[] final_co = new double[count];
        int[] final_exp = new int[count];

        for (int i = 0; i < m_co.length; i++) {
            if (m_co[i] != 0) {
                final_co[pos] = m_co[i];
                final_exp[pos] = i;
                pos++;
            }
        }
    
        Polynomial arr = new Polynomial(final_co, final_exp);
    
        return arr;
        }

    public void saveToFile(String s) {
        try {
            File f = new File(s);
            PrintWriter pw;
            if (f.exists()) {
                pw = new PrintWriter(f);
            }
            else {
                pw = new PrintWriter(f, "UTF-8");
            }

            pw.print(""); // Empties text file?

            for (int i = 0; i < this.exponent.length; i++) {
                if (this.function[i] >= 0 && this.exponent[i] != 0) {
                    pw.write("+");
                }
                pw.write(Double.toString(this.function[i]));
                if (this.exponent[i] != 0) {
                    pw.write("x" + Integer.toString(this.exponent[i]));
                }
            }
            pw.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}