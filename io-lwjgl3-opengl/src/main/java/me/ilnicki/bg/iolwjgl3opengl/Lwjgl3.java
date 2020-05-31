package me.ilnicki.bg.iolwjgl3opengl;

import me.ilnicki.bg.core.io.Drawer;
import me.ilnicki.bg.core.io.KeyReader;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.state.keyboard.Keyboard.SysKey;
import me.ilnicki.bg.core.state.keyboard.UpdatableKeyMap;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.container.Inject;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class Lwjgl3 implements Drawer, KeyReader {
    // The window handle
    private long window;

    private final SegmentSchematics segments = new SegmentSchematics();
    private static final boolean CONFIG_ENABLE_VSYNC = true;

    private final float pixelSize = 24.0f;
    private final float pixelDecorSize = pixelSize - pixelSize / 6;
    private final float pixelInnerSize = pixelDecorSize - pixelSize / 6;
    private final float pixelDistance = pixelSize / 8;
    private final float borderSize = pixelSize;
    private final float borderLineWidth = borderSize / 5.0f;

    private final float segmentSize = pixelSize;
    private final float segmentHeight = segmentSize / 2;
    private final float segmentWidth = segmentSize / 4;
    private final float segmentThickness = segmentSize / 15;
    private final float segmentIndent = segmentThickness / 2;

    private final float volumeIconSize = pixelSize / 2;

    private final Color bgColor = new Color(0x6D, 0x77, 0x5C);
    private final Color disColor = new Color(0x60, 0x6F, 0x5C);
    private final Color fgColor = new Color(0x0, 0x0, 0x0);

    @Inject
    private Kernel kernel;

    @Inject
    private State state;

    private int width;
    private int height;

    @Override
    public void load() {
        width = (int) (borderSize * 3
                + pixelSize * state.getField().getWidth()
                + pixelDistance * (state.getField().getWidth() - 1)
                + (pixelSize * state.getHelper().getWidth()
                + pixelDistance * (state.getHelper().getWidth() - 1)));

        height = (int) (borderSize * 2
                + pixelSize * state.getField().getHeight()
                + pixelDistance * (state.getField().getHeight() - 1));


        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        GLFW.glfwDefaultWindowHints(); // required, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 1);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation

        // Create the window
        window = GLFW.glfwCreateWindow(width, height, "Brick Game", MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        if (CONFIG_ENABLE_VSYNC) {
            GLFW.glfwSwapInterval(1);
        }

        // Make the window visible
        GLFW.glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        GL11.glClearColor(bgColor.getFloatR(), bgColor.getFloatG(), bgColor.getFloatB(), 0.0f);
    }

    @Override
    public void update(long tick) {
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        GLFW.glfwPollEvents();

        if (GLFW.glfwWindowShouldClose(window)) {
            kernel.stop();
        }

        updateKeys();
        draw();

        GLFW.glfwSwapBuffers(window); // swap the color buffers
    }

    @Override
    public void stop() {
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null);
    }

    private void updateKeys() {
        UpdatableKeyMap<CtrlKey> ctrlKeys = this.state.getKeyboard().getCtrlKeyMap();
        ctrlKeys.update(CtrlKey.UP, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS);
        ctrlKeys.update(CtrlKey.DOWN, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS);
        ctrlKeys.update(CtrlKey.LEFT, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS);
        ctrlKeys.update(CtrlKey.RIGHT, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS);
        ctrlKeys.update(CtrlKey.ROTATE, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS);

        UpdatableKeyMap<SysKey> sysKeys = this.state.getKeyboard().getSysKeyMap();
        sysKeys.update(SysKey.ONOFF, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DELETE) == GLFW.GLFW_PRESS);
        sysKeys.update(SysKey.RESET, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_END) == GLFW.GLFW_PRESS);
        sysKeys.update(SysKey.START, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS);
        sysKeys.update(SysKey.SOUND, GLFW.glfwGetKey(window, GLFW.GLFW_KEY_PAGE_DOWN) == GLFW.GLFW_PRESS);
    }

    private void draw() {
        GL11.glLoadIdentity();

        GL11.glOrtho(0, width, -1, height, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        drawBorder();
        drawField();
        drawScoreAndHiscore();
        drawHelper();
        drawSpeedAndLevel();
        drawVolume();
        drawPause();
    }

    private void drawBorder() {
        GL11.glLineWidth(borderLineWidth);

        GL11.glColor3ub(fgColor.getR(), fgColor.getG(), fgColor.getB());

        float borderX = borderSize / 2.0f;
        float borderY = borderSize / 2.0f;
        float borderWidth = pixelSize * state.getField().getWidth()
                + pixelDistance * (state.getField().getWidth() - 1) + borderSize;
        float borderHeight = pixelSize * state.getField().getHeight()
                + pixelDistance * (state.getField().getHeight() - 1) + borderSize;

        //Top line
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(borderX - borderLineWidth / 2, borderY + borderHeight);
        GL11.glVertex2f(borderX + borderWidth + borderLineWidth / 2, borderY + borderHeight);
        GL11.glEnd();

        //Bottom line
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(borderX - borderLineWidth / 2, borderY);
        GL11.glVertex2f(borderX + borderWidth + borderLineWidth / 2, borderY);
        GL11.glEnd();

        //Left line
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(borderX, borderY);
        GL11.glVertex2f(borderX, borderY + borderHeight);
        GL11.glEnd();

        //Right line
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(borderX + borderWidth, borderY + borderHeight);
        GL11.glVertex2f(borderX + borderWidth, borderY);
        GL11.glEnd();
    }

    private void drawField() {
        for (int y = 0; y < state.getField().getHeight(); y++) {
            for (int x = 0; x < state.getField().getWidth(); x++) {
                Pixel pixel = state.getField().getPixel(new Vector(x, y));

                drawPixel(borderSize + (pixelSize + pixelDistance) * x,
                        borderSize + (pixelSize + pixelDistance) * y, pixel);
            }
        }
    }

    private void drawScoreAndHiscore() {
        float posX = borderSize
                + (pixelSize + pixelDistance) * state.getField().getWidth()
                + borderSize - borderLineWidth + segmentWidth;
        float posY = borderSize * 2
                + (pixelSize + pixelDistance) * state.getField().getHeight()
                - (borderSize * 1.5f);

        float factor = 1.25f;

        drawString(posX, posY, "HI-SCORE");
        drawNumber(posX, posY - segmentSize * factor,
                state.params.hiscore.get(), 6);

        drawString(posX, posY - segmentSize * factor * 2, "SCORE");
        drawNumber(posX, posY - segmentSize * factor * 3,
                state.params.score.get(), 6);
    }

    private void drawHelper() {
        PixelMatrix helper = state.getHelper();

        float posX = borderSize + (pixelSize + pixelDistance)
                * state.getField().getWidth() + borderSize - borderLineWidth;
        float posY = borderSize + (pixelSize + pixelDistance)
                * state.getField().getHeight() + borderSize
                - ((pixelSize + pixelDistance) * helper.getHeight() * 2 + borderSize);

        for (int i = 0; i < helper.getHeight(); i++) {
            for (int j = 0; j < helper.getWidth(); j++) {
                Pixel pixelColor = helper.getPixel(new Vector(j, i));

                float pixelX = posX + (pixelSize + pixelDistance) * j;
                float pixelY = posY + (pixelSize + pixelDistance) * i;

                drawPixel(pixelX, pixelY, pixelColor);
            }
        }
    }

    private void drawSpeedAndLevel() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * state.getField().getWidth() + borderSize - borderLineWidth + segmentWidth;
        float posY = borderSize + (pixelSize + pixelDistance)
                * state.getField().getHeight() + borderSize
                - ((pixelSize + pixelDistance) * state.getHelper().getHeight() * 2.3f + borderSize);

        drawNumber(posX, posY,
                state.params.speed.get(), 2);
        drawString(posX, posY - segmentSize * 1.5f, "SPEED");

        drawNumber(posX + (segmentWidth * 10), posY,
                state.params.level.get(), 2);
        drawString(posX + (segmentWidth * 10), posY - segmentSize * 1.5f, "LEVEL");
    }

    private void drawPixel(float x, float y, Pixel pixel) {
        setColor(pixel);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + pixelSize, y);
        GL11.glVertex2f(x + pixelSize, y + pixelSize);
        GL11.glVertex2f(x, y + pixelSize);
        GL11.glEnd();

        GL11.glColor3ub(bgColor.getR(), bgColor.getG(), bgColor.getB());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x + pixelSize - pixelDecorSize, y + pixelSize - pixelDecorSize);
        GL11.glVertex2f(x + pixelDecorSize, y + pixelSize - pixelDecorSize);
        GL11.glVertex2f(x + pixelDecorSize, y + pixelDecorSize);
        GL11.glVertex2f(x + pixelSize - pixelDecorSize, y + pixelDecorSize);
        GL11.glEnd();

        setColor(pixel);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x + pixelSize - pixelInnerSize, y + pixelSize - pixelInnerSize);
        GL11.glVertex2f(x + pixelInnerSize, y + pixelSize - pixelInnerSize);
        GL11.glVertex2f(x + pixelInnerSize, y + pixelInnerSize);
        GL11.glVertex2f(x + pixelSize - pixelInnerSize, y + pixelInnerSize);
        GL11.glEnd();
    }

    private void drawVolume() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * state.getField().getWidth() + borderSize - borderLineWidth
                + (segmentWidth * 1.5f) * 4;
        float posY = borderSize + segmentSize * 2;

        setColor(true);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(posX, posY);
        GL11.glVertex2f(posX + volumeIconSize / 2, posY);
        GL11.glVertex2f(posX + volumeIconSize / 2, posY + volumeIconSize);
        GL11.glVertex2f(posX, posY + volumeIconSize);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(posX, posY + volumeIconSize / 2);
        GL11.glVertex2f(posX + volumeIconSize, posY + volumeIconSize * 1.5f);
        GL11.glVertex2f(posX + volumeIconSize, posY - volumeIconSize * 0.5f);
        GL11.glEnd();

        int volume = state.volume.get();

        GL11.glLineWidth(volumeIconSize / 4);

        if (volume == 0) {
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(posX + volumeIconSize * 1.5f, posY);
            GL11.glVertex2f(posX + volumeIconSize * 1.5f + volumeIconSize / 2, posY + volumeIconSize);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(posX + volumeIconSize * 1.5f, posY + volumeIconSize);
            GL11.glVertex2f(posX + volumeIconSize * 1.5f + volumeIconSize / 2, posY);
            GL11.glEnd();
        } else {
            for (int i = 0; i < volume; i++) {
                GL11.glBegin(GL11.GL_LINES);
                float x = posX + volumeIconSize * 1.5f + (volumeIconSize / 2 * i);
                GL11.glVertex2f(x, posY + (volumeIconSize / 4 * i) + volumeIconSize);
                GL11.glVertex2f(x, posY - (volumeIconSize / 4 * i));
                GL11.glEnd();
            }
        }
    }

    private void drawPause() {
        float posX = borderSize + (pixelSize + pixelDistance)
                * state.getField().getWidth() + borderSize - borderLineWidth
                + (segmentWidth * 1.5f) * 4;
        float posY = borderSize;

        drawString(posX, posY, state.pause.get() ? "PAUSE" : "     ");
    }

    private void drawNumber(float x, float y, int num, int digitCount) {
        x = x + (segmentWidth * 1.5f * digitCount);

        for (int i = digitCount - 1; i >= 0; i--) {
            Integer digit = num % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);

            if (digit == 0 && num < Math.pow(10, i)) {
                //Disabling segments on left zeros
                digit = null;
            }

            drawSegmentChar(x - (segmentWidth * 1.5f) * i, y, segments.get(digit));
        }
    }

    private void drawString(float x, float y, String string) {
        for (int i = 0; i < string.length(); i++) {
            drawSegmentChar(x + (segmentWidth * 1.5f) * i, y, segments.get(string.charAt(i)));
        }
    }

    private void drawSegmentChar(float x, float y, boolean[] data) {
        GL11.glLineWidth(segmentThickness);

        //"a" segment
        setColor(data[0]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x + segmentIndent, y + segmentHeight * 2);
        GL11.glVertex2f(x + segmentWidth - segmentIndent, y + segmentHeight * 2);
        GL11.glEnd();

        //"b" segment
        setColor(data[1]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x + segmentWidth, y + segmentHeight * 2 - segmentIndent);
        GL11.glVertex2f(x + segmentWidth, y + segmentHeight + segmentIndent);
        GL11.glEnd();

        //"c" segment
        setColor(data[2]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x + segmentWidth, y + segmentHeight - segmentIndent);
        GL11.glVertex2f(x + segmentWidth, y + segmentIndent);
        GL11.glEnd();

        //"d" segment
        setColor(data[3]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x + segmentIndent, y);
        GL11.glVertex2f(x + segmentWidth - segmentIndent, y);
        GL11.glEnd();

        //"e" segment
        setColor(data[4]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x, y + segmentHeight - segmentIndent);
        GL11.glVertex2f(x, y + segmentIndent);
        GL11.glEnd();

        //"f" segment
        setColor(data[5]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x, y + segmentHeight * 2 - segmentIndent);
        GL11.glVertex2f(x, y + segmentHeight + segmentIndent);
        GL11.glEnd();

        //"g" segment
        setColor(data[6]);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x + segmentIndent, y + segmentHeight);
        GL11.glVertex2f(x + segmentWidth - segmentIndent, y + segmentHeight);
        GL11.glEnd();

        if (data.length == 9) {
            //"h" segment
            setColor(data[7]);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(x + segmentThickness, y + segmentHeight * 2 - segmentIndent);
            GL11.glVertex2f(x + segmentWidth - segmentThickness, y + segmentHeight + segmentIndent);
            GL11.glEnd();

            //"i" segment
            setColor(data[8]);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(x + segmentThickness, y + segmentHeight - segmentIndent);
            GL11.glVertex2f(x + segmentWidth - segmentThickness, y + segmentIndent);
            GL11.glEnd();
        }
    }

    private void setColor(Pixel pixel) {
        setColor(pixel == Pixel.BLACK);
    }

    private void setColor(boolean enabled) {
        if (enabled) {
            GL11.glColor3ub(fgColor.getR(), fgColor.getG(), fgColor.getB());
        } else {
            GL11.glColor3ub(disColor.getR(), disColor.getG(), disColor.getB());
        }
    }
}