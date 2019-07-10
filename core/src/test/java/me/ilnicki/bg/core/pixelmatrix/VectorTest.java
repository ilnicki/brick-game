package me.ilnicki.bg.core.pixelmatrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void getX() {
        int x = 1;
        int y = 2;
        Vector vector = new Vector(x, y);

        assertEquals(x, vector.getX());
    }

    @Test
    void getY() {
        int x = 1;
        int y = 2;
        Vector vector = new Vector(x, y);

        assertEquals(y, vector.getY());
    }

    @Test
    void add() {
        int x1 = 1;
        int y1 = 2;
        int x2 = 3;
        int y2 = 4;

        Vector vector = new Vector(x1, y1).add(new Vector(x2, y2));

        assertEquals(x1 + x2, vector.getX());
        assertEquals(y1 + y2, vector.getY());
    }

    @Test
    void sub() {
        int x1 = 1;
        int y1 = 2;
        int x2 = 3;
        int y2 = 4;

        Vector vector = new Vector(x1, y1).sub(new Vector(x2, y2));

        assertEquals(x1 - x2, vector.getX());
        assertEquals(y1 - y2, vector.getY());
    }

    @Test
    void withX() {
        int x1 = 1;
        int y1 = 2;
        int x2 = 3;
        Vector vector = new Vector(x1, y1).withX(x2);

        assertEquals(x2, vector.getX());
    }

    @Test
    void withY() {
        int x1 = 1;
        int y1 = 2;
        int y2 = 3;
        Vector vector = new Vector(x1, y1).withY(y2);

        assertEquals(y2, vector.getY());
    }

    @Test
    void equality() {
        int x = 1;
        int y = 2;
        Vector vector1 = new Vector(x, y);
        Vector vector2 = new Vector(x, y);

        assertEquals(vector1, vector2);
    }

    @Test
    void stringifying() {
        int x = 1;
        int y = 2;
        Vector vector = new Vector(x, y);

        assertEquals("[" + x + ", " + y + "]", vector.toString());
    }

    @Test
    void cloning() {
        int x = 1;
        int y = 2;
        Vector vector = new Vector(x, y);
        Vector clone = (Vector) vector.clone();

        assertEquals(vector, clone);
        assertSame(vector, clone);
    }
}