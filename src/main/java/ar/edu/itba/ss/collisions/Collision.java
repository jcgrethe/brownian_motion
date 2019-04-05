package ar.edu.itba.ss.collisions;

public abstract class Collision implements Comparable<Collision> {
    private final Double time;

    public Double getTime() {
        return time;
    }

    public Collision(Double time) {
        this.time = time;
    }

    @Override
    public int compareTo(Collision o) {
        return (int) Math.ceil(this.getTime() - o.getTime());
    }
}
