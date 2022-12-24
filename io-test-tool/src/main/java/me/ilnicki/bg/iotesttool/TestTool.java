package me.ilnicki.bg.iotesttool;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import me.ilnicki.bg.core.io.Drawer;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.iolwjgl3opengl.Lwjgl3;
import me.ilnicki.bg.ticklwjgl.LwjglTickProvider;
import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;

public class TestTool {
  public static void main(String[] args) {
    run(Lwjgl3.class);
  }

  public static <T extends Drawer> void run(Class<T> clazz) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    Container container = new ComponentContainer();
    container.share(new State());
    container.singleton(clazz);
    container.link(Drawer.class, clazz);

    final LwjglTickProvider ticker = new LwjglTickProvider(60);

    final JFrame frame = new MachineForm(container.get(State.class));
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    container.singleton(
        Kernel.class,
        new Kernel() {
          @Override
          public void reset() {
          }

          @Override
          public void stop() {
            ticker.stop();
            frame.dispose();
          }
        });

    frame.addWindowListener(
        new WindowAdapter() {
          public void windowOpened(WindowEvent e) {
            final Drawer drawer = container.get(Drawer.class);

            (new Thread(
                () -> {
                  drawer.load();
                  ticker.start(drawer::update);
                  drawer.stop();
                }))
                .start();
          }

          public void windowClosing(WindowEvent e) {
            container.get(Kernel.class).stop();
          }
        });

    frame.pack();
    frame.setVisible(true);
  }
}
