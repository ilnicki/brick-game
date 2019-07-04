package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;

public class SnakePart extends Entity {
    private static final PixelMatrix sprite = MatrixUtils.fromString("#");

    private SnakePart child = null;

    SnakePart(Point pos) {
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
    void setPos(Point pos) {
        Point prevPos = this.getPos();
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
