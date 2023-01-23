public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    private static final double G = 6.67e-11;

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
    }
    /* Construcor Function */
    public Planet(double xP, double yP, double xV,
                double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet b) {
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    }

    /* Calculate the distance */
    public double calcDistance(Planet p) {
        double xxDiff = p.xxPos - xxPos;
        double yyDiff = p.yyPos - yyPos;
        return Math.sqrt(xxDiff * xxDiff + yyDiff * yyDiff);
    }
    /* Calculate the force */
    public double calcForceExertedBy(Planet p) {
        double distanceSquare = calcDistance(p) * calcDistance(p);
        return G * mass * p.mass / distanceSquare;
    }
    /* Calculate the force (on x and y dimensions) */
    public double calcForceExertedByX(Planet p) {
        double cos = (p.xxPos - xxPos) / calcDistance(p);
        return cos * calcForceExertedBy(p);
    }
    public double calcForceExertedByY(Planet p) {
        double sin = (p.yyPos - yyPos) / calcDistance(p);
        return sin * calcForceExertedBy(p);
    }

    /* Calculate the total force (on x and y dimensions) */
    public double calcNetForceExertedByX(Planet[] planets) {
        double netForceX = 0;
        if (planets == null || planets.length <= 1) {
            return netForceX;
        }
        for (Planet p : planets) {
            if (p.equals(this)) {
                continue;
            }
            netForceX += calcForceExertedByX(p);
        }
        return netForceX;
    }
    public double calcNetForceExertedByY(Planet[] planets) {
        double netForceY = 0;
        if (planets == null || planets.length <= 1) {
            return netForceY;
        }
        for (Planet p : planets) {
            if (p.equals(this)) {
                continue;
            }
            netForceY += calcForceExertedByY(p);
        }
        return netForceY;
   }

   /* Calculate the position */
   public void update(double dt, double Fx, double Fy) {
        double accX = Fx / mass; double accY = Fy / mass;
        xxVel += accX * dt; yyVel += accY * dt;
        xxPos += xxVel * dt; yyPos += yyVel * dt;
   }
}
