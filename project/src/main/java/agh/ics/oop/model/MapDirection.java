package agh.ics.oop.model;

public enum MapDirection {
    N(0),
    NE(1),
    E(2),
    SE(3),
    S(4),
    SW(5),
    W(6),
    NW(7);
    private final int i;
    MapDirection(int i) { // dziwne to jest ale tak jest na wykładzie i chatgpt tez tak mowi
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public String toString(){
        return switch(this) {
            case N -> "Północ";
            case S -> "Południe";
            case NE -> "Północny-Wschód";
            case E -> "Wschód";
            case W -> "Zachód";
            case SE -> "Południowy-Wschód";
            case SW -> "Południowy-Zachód";
            case NW -> "Północny-Zachód";
        };
    }
    public MapDirection next(){
        return switch(this){
            case N -> NE;
            case NE -> E;
            case E -> SE;
            case SE -> S;
            case S -> SW;
            case SW -> W;
            case W -> NW;
            case NW -> N;
        };
    }

    public MapDirection opposite(){
        return switch(this){
            case N -> S;
            case NE -> SW;
            case E -> W;
            case SE -> NW;
            case S -> N;
            case SW -> NE;
            case W -> E;
            case NW -> SE;
        };
    }

    public Vector2d toUnitVector(){
        return switch(this){
            case N -> new Vector2d(0,1);
            case NE -> new Vector2d(1,1);
            case E -> new Vector2d(1,0);
            case SE -> new Vector2d(1,-1);
            case S -> new Vector2d(0,-1);
            case SW -> new Vector2d(-1,-1);
            case W -> new Vector2d(-1,0);
            case NW -> new Vector2d(-1,1);
        };
    }
}