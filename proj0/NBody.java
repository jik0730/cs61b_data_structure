/**
 * Created by Ingyo on 2016. 1. 31..
 */
public class NBody {
    public static double readRadius(String txtfile) {
        In in = new In(txtfile);
        int temp = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String txtfile) {
        In in = new In(txtfile);
        int planetsNum = in.readInt();
        Planet[] ps = new Planet[planetsNum];
        double temp = in.readDouble();
        for (int i = 0; i < planetsNum; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double mass = in.readDouble();
            String img = in.readString();
            ps[i] = new Planet(xP, yP, xV, yV, mass, img);
        }
        return ps;
    }

    public static void main(String[] args) {
        String T = args[0];
        String dt = args[1];
        Planet[] planets;
        double uniRadius;
        String txtfile = args[2];
        In in = new In(txtfile);
        uniRadius = readRadius(txtfile);
        planets = readPlanets(txtfile);
        StdAudio.play("./audio/2001.mid");
        // Start drawing!!
        // Draw background.
        StdDraw.setXscale(-uniRadius, uniRadius);
        StdDraw.setYscale(-uniRadius, uniRadius);
        StdDraw.picture(0, 0, "./images/starfield.jpg");
        // Draw planets.
        for (int i = 0; i < planets.length; i++) {
            planets[i].draw();
        }
        // Create an Animation.
        double realT = Double.parseDouble(T);
        double realdt = Double.parseDouble(dt);
        double unitdt = realdt;
        while (realdt <= realT) {
            double xForces[] = new double[planets.length];
            double yForces[] = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(unitdt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "./images/starfield.jpg");
            for (int i = 0; i < planets.length; i++) {
                planets[i].draw();
            }
            StdDraw.show(10);
            realdt = realdt + unitdt;
        }
        // Print the Universe.
        System.out.println(planets.length);
        System.out.println(uniRadius);
        for (int i = 0; i < planets.length; i++) {
            System.out.println(planets[i].xxPos + "	" + planets[i].yyPos + "	" + planets[i].xxVel + "	" + planets[i].yyVel + "	" + planets[i].mass + "	" + planets[i].imgFileName);
        }
    }
}
