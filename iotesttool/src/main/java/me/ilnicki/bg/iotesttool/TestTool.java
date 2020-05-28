package me.ilnicki.bg.iotesttool;

import me.ilnicki.bg.core.io.Drawer;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.lwjgl3opengl.Lwjgl3;
import me.ilnicki.bg.lwjgltick.LwjglTickProvider;
import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestTool {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Container container = new ComponentContainer();
        container.share(new State());
        container.singleton(Lwjgl3.class);
        container.link(Drawer.class, Lwjgl3.class);

        final LwjglTickProvider ticker = new LwjglTickProvider(60);

        final JFrame frame = new MachineForm(container.get(State.class));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        container.singleton(Kernel.class, new TestKernel() {
            @Override
            public void stop() {
                super.stop();
                ticker.stop();
                frame.dispose();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                final Drawer drawer = container.get(Drawer.class);

                (new Thread(() -> {
                    drawer.load();
                    ticker.start(drawer::update);
                    drawer.stop();
                })).start();
            }

            public void windowClosing(WindowEvent e) {
                container.get(Kernel.class).stop();
            }
        });

        frame.pack();
        frame.setVisible(true);
    }
}
