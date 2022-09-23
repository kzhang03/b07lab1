public class Polynomial {

    double [] function;

    public Polynomial() {
        function = new double[1];
        function[0] = 0;
    }

    public Polynomial(double[] co) {

        function = new double[co.length];
        for (int i = 0; i < co.length; i++)
            function[i] = co[i];
    }

    public Polynomial add(Polynomial p) {

        int temp;
        Polynomial arr;
        if (p.function.length > this.function.length) {
            temp = this.function.length;
            arr = new Polynomial(p.function);
        }
        else {
            temp = p.function.length;
            arr = new Polynomial(this.function);
        }

        for (int i = 0; i < temp; i++)
            arr.function[i] = p.function[i] + this.function[i];

        return arr;
    }

    public double evaluate(double sub) {

        double soln = 0;

        for (int i = 0; i < this.function.length; i++)
            soln += Math.pow(sub, i)*this.function[i];

        return soln;
    }

    public boolean hasRoot(double root) {

        double soln = this.evaluate(root);

        if (soln <= 0.01 && soln >= -0.01)
            return true;
        else
            return false;
    }
}
