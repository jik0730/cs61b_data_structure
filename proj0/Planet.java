public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx;
        double dy;
        double r;
        dx = xxPos - p.xxPos;
        dy = yyPos - p.yyPos;
        r = Math.sqrt(dx * dx + dy * dy);
        return r;
    }

    public double calcForceExertedBy(Planet p) {
        double F;
        double r = calcDistance(p);
        F = 6.67 * 0.00000000001 * mass * p.mass / (r * r);
        return F;
    }

    public double calcForceExertedByX(Planet p) {
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        double Fx;
        Fx = F * (p.xxPos - xxPos) / r;
        return Fx;
    }

    public double calcForceExertedByY(Planet p) {
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        double Fy;
        Fy = F * (p.yyPos - yyPos) / r;
        return Fy;
    }

    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double NetFx = 0;
        for (Planet p : allPlanets) {
            if (!this.equals(p)) {
                NetFx = NetFx + calcForceExertedByX(p);
            }
        }
        return NetFx;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double NetFy = 0;
        for (Planet p : allPlanets) {
            if (!this.equals(p)) {
                NetFy = NetFy + calcForceExertedByY(p);
            }
        }
        return NetFy;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX / mass;
        double aY = fY / mass;
        xxVel = xxVel + dt * aX;
        yyVel = yyVel + dt * aY;
        xxPos = xxPos + dt * xxVel;
        yyPos = yyPos + dt * yyVel;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "./images/" + imgFileName);
    }
}
