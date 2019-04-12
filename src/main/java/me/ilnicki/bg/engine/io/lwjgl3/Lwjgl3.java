package me.ilnicki.bg.engine.io.lwjgl3;

import me.ilnicki.bg.engine.io.Drawer;
import me.ilnicki.bg.engine.io.KeyReader;
import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.machine.Screen;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.SysKey;
import me.ilnicki.bg.engine.machine.keyboard.UpdatableKeyMap;
import me.ilnicki.bg.engine.pixelmatrix.Pixel;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.system.Kernel;
import me.ilnicki.bg.engine.system.container.Inject;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static me.ilnicki.bg.engine.pixelmatrix.Pixel.BLACK;
import static me.ilnicki.bg.engine.pixelmatrix.Pixel.WHITE;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Lwjgl3 implements Drawer, KeyReader {
    // The window handle
    private long window;

    private final float pixelSize = 24.0f;
    private final float pixelDecorSize = pixelSize - pixelSize / 6;
    private final float pixelInnerSize = pixelDecorSize - pixelSize / 6;
    private final float pixelDistance = pixelSize / 8;
    private final float borderSize = pixelSize;
    private final float borderLineWidth = borderSize / 5.0f;

    private final float segmentSize = pixelSize;
    private final float segmentHeight = segmentSize / 2;
    private final float segmentWidth = segmentSize / 4;
    private final float segmentThick = segmentSize / 15;
    private final float segmentIndent = segmentThick / 2;

    private final float volumeIconSize = pixelSize / 2;

    private Color bgColor = new Color(0x6D, 0x77, 0x5C);
    private Color disColor = new Color(0x60, 0x6F, 0x5C);
    private Color fgColor = new Color(0x0, 0x0, 0x0);

    @Inject
    private Kernel kernel;

    @Inject
    private Screen screen;

    @Inject
    private Machine machine;

    private int width;
    private int height;

    @Override
    public void load() {
        width = (int) (borderSize * 3
                + pixelSize * screen.getWidth()
                + pixelDistance * (screen.getWidth() - 1)
                + (pixelSize * machine.getHelper().getWidth()
                + pixelDistance * (machine.getHelper().getWidth() - 1)));

        height = (int) (borderSize * 2
                + pixelSize * screen.getHeight()
                + pixelDistance * (screen.getHeight() - 1));


        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        // glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Brick Game", NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(bgColor.getFloatR(), bgColor.getFloatG(), bgColor.getFloatB(), 0.0f);
    }

    @Override
    public void update(long tick) {
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();

        if (glfwWindowShouldClose(window)) {
            kernel.stop();
        }

        updateKeys();
        draw();

        glfwSwapBuffers(window); // swap the color buffers
    }

    @Override
    public void stop() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null);
    }

    private void updateKeys() {
        UpdatableKeyMap<CtrlKey> ctrlKeys = this.machine.getKeyboard().getCtrlKeyMap();
        ctrlKeys.update(CtrlKey.UP, glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS);
        ctrlKeys.update(CtrlKey.DOWN, glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS);
        ctrlKeys.update(CtrlKey.LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
        ctrlKeys.update(CtrlKey.RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
        ctrlKeys.update(CtrlKey.ROTATE, glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS);

        UpdatableKeyMap<SysKey> sysKeys = this.machine.getKeyboard().getSysKeyMap();
        sysKeys.update(SysKey.ONOFF, glfwGetKey(window, GLFW_KEY_DELETE) == GLFW_PRESS);
        sysKeys.update(SysKey.RESET, glfwGetKey(window, GLFW_KEY_END) == GLFW_PRESS);
        sysKeys.update(SysKey.START, glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS);
        sysKeys.update(SysKey.SOUND, glfwGetKey(window, GLFW_KEY_PAGE_DOWN) == GLFW_PRESS);
    }

    private void draw() {
        //glViewport(0, 0, width, height);
        //glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0, width, -1, height, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glClear(GL_COLOR_BUFFER_BIT);

        drawBorder();
        drawField();
        drawScoreAndHiscore();
        drawHelper();
        drawSpeedAndLevel();
        drawVolume();
        drawPause();
    }

    private void drawBorder() {
        glLineWidth(borderLineWidth);

        glColor3ub(fgColor.getR(), fgColor.getG(), fgColor.getB());

        float borderX = borderSize / 2.0f;
        float borderY = borderSize / 2.0f;
        float borderWidth = pixelSize * screen.getWidth()
                + pixelDistance * (screen.getWidth() - 1) + borderSize;
        float borderHeight = pixelSize * screen.getHeight()
                + pixelDistance * (screen.getHeight() - 1) + borderSize;

        //Top line
        glBegin(GL_LINES);
        glVertex2f(borderX - borderLineWidth / 2, borderY + borderHeight);
        glVertex2f(borderX + borderWidth + borderLineWidth / 2, borderY + borderHeight);
        glEnd();

        //Bottom line
        glBegin(GL_LINES);
        glVertex2f(borderX - borderLineWidth / 2, borderY);
        glVertex2f(borderX + borderWidth + borderLineWidth / 2, borderY);
        glEnd();

        //Left line
        glBegin(GL_LINES);
        glVertex2f(borderX, borderY);
        glVertex2f(borderX, borderY + borderHeight);
        glEnd();

        //Right line
        glBegin(GL_LINES);
        glVertex2f(borderX + borderWidth, borderY + borderHeight);
        glVertex2f(borderX + borderWidth, borderY);
        glEnd();
    }

    private void drawField() {
        for (int i = 0; i < screen.getHeight(); i++) {
            for (int j = 0; j < screen.getWidth(); j++) {
                Pixel pixel = screen.getPixel(j, i);

                drawPixel(borderSize + (pixelSize + pixelDistance) * j,
                        borderSize + (pixelSize + pixelDistance) * i, pixel);
            }
        }
    }

    private void drawScoreAndHiscore() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * screen.getWidth() + borderSize - borderLineWidth + segmentWidth;
        float posY = borderSize * 2
                + (pixelSize + pixelDistance) * screen.getHeight()
                - (borderSize * 1.5f);

        float factor = 1.25f;

        drawString(posX, posY, "HI-SCORE");
        drawNumber(posX, posY - segmentSize * factor,
                machine.getParameters().hiscore.get(), 6);

        drawString(posX, posY - segmentSize * factor * 2, "SCORE");
        drawNumber(posX, posY - segmentSize * factor * 3,
                machine.getParameters().score.get(), 6);
    }

    private void drawHelper() {
        PixelMatrix helper = machine.getHelper();

        float posX = borderSize + (pixelSize + pixelDistance)
                * screen.getWidth() + borderSize - borderLineWidth;
        float posY = borderSize + (pixelSize + pixelDistance)
                * screen.getHeight() + borderSize
                - ((pixelSize + pixelDistance) * helper.getHeight() * 2 + borderSize);

        for (int i = 0; i < helper.getHeight(); i++) {
            for (int j = 0; j < helper.getWidth(); j++) {
                Pixel pixelColor = helper.getPixel(j, i);

                float pixelX = posX + (pixelSize + pixelDistance) * j;
                float pixelY = posY + (pixelSize + pixelDistance) * i;

                drawPixel(pixelX, pixelY, pixelColor);
            }
        }
    }

    private void drawSpeedAndLevel() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * screen.getWidth() + borderSize - borderLineWidth + segmentWidth;
        float posY = borderSize + (pixelSize + pixelDistance)
                * screen.getHeight() + borderSize
                - ((pixelSize + pixelDistance) * machine.getHelper().getHeight() * 2.3f + borderSize);

        drawNumber(posX, posY,
                machine.getParameters().speed.get(), 2);
        drawString(posX, posY - segmentSize * 1.5f, "SPEED");

        drawNumber(posX + (segmentWidth * 10), posY,
                machine.getParameters().level.get(), 2);
        drawString(posX + (segmentWidth * 10), posY - segmentSize * 1.5f, "LEVEL");
    }

    private void drawPixel(float x, float y, Pixel pixel) {
        setColor(pixel);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + pixelSize, y);
        glVertex2f(x + pixelSize, y + pixelSize);
        glVertex2f(x, y + pixelSize);
        glEnd();

        glColor3ub(bgColor.getR(), bgColor.getG(), bgColor.getB());
        glBegin(GL_QUADS);
        glVertex2f(x + pixelSize - pixelDecorSize, y + pixelSize - pixelDecorSize);
        glVertex2f(x + pixelDecorSize, y + pixelSize - pixelDecorSize);
        glVertex2f(x + pixelDecorSize, y + pixelDecorSize);
        glVertex2f(x + pixelSize - pixelDecorSize, y + pixelDecorSize);
        glEnd();

        setColor(pixel);
        glBegin(GL_QUADS);
        glVertex2f(x + pixelSize - pixelInnerSize, y + pixelSize - pixelInnerSize);
        glVertex2f(x + pixelInnerSize, y + pixelSize - pixelInnerSize);
        glVertex2f(x + pixelInnerSize, y + pixelInnerSize);
        glVertex2f(x + pixelSize - pixelInnerSize, y + pixelInnerSize);
        glEnd();
    }

    private void drawVolume() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * screen.getWidth() + borderSize - borderLineWidth
                + (segmentWidth * 1.5f) * 4;
        float posY = borderSize + segmentSize * 2;

        setColor(true);

        glBegin(GL_QUADS);
        glVertex2f(posX, posY);
        glVertex2f(posX + volumeIconSize / 2, posY);
        glVertex2f(posX + volumeIconSize / 2, posY + volumeIconSize);
        glVertex2f(posX, posY + volumeIconSize);
        glEnd();

        glBegin(GL_TRIANGLES);
        glVertex2f(posX, posY + volumeIconSize / 2);
        glVertex2f(posX + volumeIconSize, posY + volumeIconSize * 1.5f);
        glVertex2f(posX + volumeIconSize, posY - volumeIconSize * 0.5f);
        glEnd();

        int volume = machine.volume.get();

        glLineWidth(volumeIconSize / 4);

        if (volume == 0) {
            glBegin(GL_LINES);
            glVertex2f(posX + volumeIconSize * 1.5f, posY);
            glVertex2f(posX + volumeIconSize * 1.5f + volumeIconSize / 2, posY + volumeIconSize);
            glEnd();

            glBegin(GL_LINES);
            glVertex2f(posX + volumeIconSize * 1.5f, posY + volumeIconSize);
            glVertex2f(posX + volumeIconSize * 1.5f + volumeIconSize / 2, posY);
            glEnd();
        } else {
            for (int i = 0; i < volume; i++) {
                glBegin(GL_LINES);
                float x = posX + volumeIconSize * 1.5f + (volumeIconSize / 2 * i);
                glVertex2f(x, posY + (volumeIconSize / 4 * i) + volumeIconSize);
                glVertex2f(x, posY - (volumeIconSize / 4 * i));
                glEnd();
            }
        }
    }

    private void drawPause() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * screen.getWidth() + borderSize - borderLineWidth
                + (segmentWidth * 1.5f) * 4;
        float posY = borderSize;

        drawString(posX, posY, machine.pause.get() ? "PAUSE" : "     ");
    }

    private void drawNumber(float x, float y, int num, int digitCount) {
        x = x + (segmentWidth * 1.5f * digitCount);

        for (int i = digitCount - 1; i >= 0; i--) {
            Integer digit = num % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);

            if (digit == 0 && num < Math.pow(10, i) - 1) {
                //Disabling segments on left zeros
                digit = null;
            }

            drawSegmentChar(x - (segmentWidth * 1.5f) * i, y, SegmentSchematics.getSchematic(digit));
        }
    }

    private void drawString(float x, float y, String string) {
        for (int i = 0; i < string.length(); i++) {
            drawSegmentChar(x + (segmentWidth * 1.5f) * i, y, SegmentSchematics.getSchematic(string.charAt(i)));
        }
    }

    private void drawSegmentChar(float x, float y, boolean[] data) {
        glLineWidth(segmentThick);

        //"a" segment
        setColor(data[0]);
        glBegin(GL_LINES);
        glVertex2f(x + segmentIndent, y + segmentHeight * 2);
        glVertex2f(x + segmentWidth - segmentIndent, y + segmentHeight * 2);
        glEnd();

        //"b" segment
        setColor(data[1]);
        glBegin(GL_LINES);
        glVertex2f(x + segmentWidth, y + segmentHeight * 2 - segmentIndent);
        glVertex2f(x + segmentWidth, y + segmentHeight + segmentIndent);
        glEnd();

        //"c" segment
        setColor(data[2]);
        glBegin(GL_LINES);
        glVertex2f(x + segmentWidth, y + segmentHeight - segmentIndent);
        glVertex2f(x + segmentWidth, y + segmentIndent);
        glEnd();

        //"d" segment
        setColor(data[3]);
        glBegin(GL_LINES);
        glVertex2f(x + segmentIndent, y);
        glVertex2f(x + segmentWidth - segmentIndent, y);
        glEnd();

        //"e" segment
        setColor(data[4]);
        glBegin(GL_LINES);
        glVertex2f(x, y + segmentHeight - segmentIndent);
        glVertex2f(x, y + segmentIndent);
        glEnd();

        //"f" segment
        setColor(data[5]);
        glBegin(GL_LINES);
        glVertex2f(x, y + segmentHeight * 2 - segmentIndent);
        glVertex2f(x, y + segmentHeight + segmentIndent);
        glEnd();

        //"g" segment
        setColor(data[6]);
        glBegin(GL_LINES);
        glVertex2f(x + segmentIndent, y + segmentHeight);
        glVertex2f(x + segmentWidth - segmentIndent, y + segmentHeight);
        glEnd();

        if (data.length == 9) {
            //"h" segment
            setColor(data[7]);
            glBegin(GL_LINES);
            glVertex2f(x + segmentThick, y + segmentHeight * 2 - segmentIndent);
            glVertex2f(x + segmentWidth - segmentThick, y + segmentHeight + segmentIndent);
            glEnd();

            //"i" segment
            setColor(data[8]);
            glBegin(GL_LINES);
            glVertex2f(x + segmentThick, y + segmentHeight - segmentIndent);
            glVertex2f(x + segmentWidth - segmentThick, y + segmentIndent);
            glEnd();
        }
    }

    private void setColor(Pixel pixel) {
        setColor(pixel == BLACK);
    }

    private void setColor(boolean enabled) {
        if (enabled) {
            glColor3ub(fgColor.getR(), fgColor.getG(), fgColor.getB());
        } else {
            glColor3ub(disColor.getR(), disColor.getG(), disColor.getB());
        }
    }
}