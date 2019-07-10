package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public class SnakePart extends Entity {
    private static final PixelMatrix sprite = Matrices.fromString("#");

    private SnakePart child = null;

    SnakePart(Vector pos) {
        super(pos);
    }

    SnakePart getChild() {
        return child;
    }

    void append(SnakePart childPart) {
        this.child = childPart;
    }

    SnakePart tail() {
        SnakePart tail = this;

        while(tail.getChild() != null) {
            tail = tail.getChild();
        }

        return tail;
    }

    int size() {
        int size = 1;
        SnakePart tail = this;

        while(tail.getChild() != null) {
            tail = tail.getChild();
            size++;
        }

        return size;
    }

    @Override
    void setPos(Vector pos) {
        Vector prevPos = this.getPos();
        if (!prevPos.equals(pos)) {
            super.setPos(pos);

            if (this.child != null) {
                this.child.setPos(prevPos);
            }
        }
    }

    @Override
    public PixelMatrix getSprite() {
        return sprite;
    }
}
