public abstract class Agent {
    //Position
    int x;
    int y;
    int power;
    ColorAgent color;

    int id;

    static int cpt=0;
    
    public Agent(int x, int y, ColorAgent c) {
        this.x=x;
        this.y=y;
        this.power = 1;
        this.color = c;
        this.id = cpt;
        cpt++;
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public void set_x(int x) {
        this.x = x;
    }

    public void set_y(int y) {
        this.y = y;
    }

    public int get_power() {
        return power;
    }

    public void upgrade() {
        power++;
    }

    public ColorAgent getColor() {
        return color;
    }

}