package me.ilnicki.bg.iotesttool;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;

import javax.swing.*;
import java.awt.*;

public class CellBox extends JCheckBox {
    private static final Icon ICON_ON = new ImageIcon(CellBox.class.getResource("/icons/cell-box-on.png"));
    private static final Icon ICON_OFF = new ImageIcon(CellBox.class.getResource("/icons/cell-box-off.png"));

    protected CellBox(PixelMatrix source, Vector point) {
        super(null, ICON_OFF, source.getPixel(point) == Pixel.BLACK);
        setSelectedIcon(ICON_ON);
        setMargin(new Insets(0, 0, 0, 0));
        setIconTextGap(0);
    }
}
