public class NBody {
    
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]); double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        double radius = readRadius(filename);
        // The Drawing Part
        double curTime = 0;
        while (curTime <= T) {
            StdDraw.enableDoubleBuffering();
            StdDraw.setScale(-radius, radius);
            StdDraw.clear();
            StdDraw.picture(0, 0, "images/starfield.jpg");
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                double xForce = planets[i].calcNetForceExertedByX(planets);
                double yForce = planets[i].calcNetForceExertedByY(planets);
                xForces[i] = xForce;
                yForces[i] = yForce;
            }
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.pause(10);
            StdDraw.show(); 
            curTime += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
        planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
        planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
    }

    /* Read the radius of the simulation universe */
    public static double readRadius(String path) {
        In in = new In(path);
        in.readInt();
        return in.readDouble();
    }
    
    /* Read the planets */
    public static Planet[] readPlanets(String path) {
        In in = new In(path);
        int num = in.readInt();
        in.readDouble();
        Planet[] planets = new Planet[num];
        for (int i = 0; i < num; i++) {
            Planet p = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
            planets[i] = p;
        }
        return planets;
    }
}
