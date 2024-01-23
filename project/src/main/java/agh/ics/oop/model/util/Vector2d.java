package agh.ics.oop.model.util;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x,int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(other.getX() + this.x,other.getY() + this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return getX() == vector2d.getX() && getY() == vector2d.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}