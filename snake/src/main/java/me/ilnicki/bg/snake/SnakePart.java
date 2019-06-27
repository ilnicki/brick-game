package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class SnakePart extends Entity {

    private static final PixelMatrix sprite = MatrixUtils.fromString("#");

    private SnakePart childPart = null;

    protected SnakePart(int posX, int posY) {
        super(posX, posY);
    }

    public SnakePart(int posX, int posY, SnakePart parentPart) {
        super(posX, posY);
        parentPart.setChildPart(this);
    }

    public SnakePart getChildPart() {
        return childPart;
    }

    public void setChildPart(SnakePart childPart) {
        this.childPart = childPart;
    }

    @Override
    public void setPos(int posX, int posY) {
        if (posX != this.getPosX() || posY != this.getPosY()) {
            int oldX = this.getPosX();
            int oldY = this.getPosY();

            super.setPos(posX, posY);

            if (this.childPart != null) {
                this.childPart.setPos(oldX, oldY);
            }
        }
    }

    @Override
    public void setPosY(int posY) {
        this.setPos(this.getPosX(), posY);
    }

    @Override
    public void setPosX(int posX) {
        this.setPos(posX, this.getPosY());
    }

    @Override
    public PixelMatrix getSprite() {
        return sprite;
    }
}
