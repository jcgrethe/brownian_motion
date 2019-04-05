package ar.edu.itba.ss.models;

public class Wall {
    public enum typeOfWall{
        TOP, BOTTOM, RIGHT, LEFT
    }



    private final typeOfWall typeOfWall;

    public Wall(typeOfWall type) {
        this.typeOfWall = type;
    }

    public Wall.typeOfWall getTypeOfWall() {
        return typeOfWall;
    }
}
