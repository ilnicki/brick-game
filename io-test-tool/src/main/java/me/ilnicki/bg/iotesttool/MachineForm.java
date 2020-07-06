package me.ilnicki.bg.iotesttool;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.HashPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.state.GameState;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.SystemState;
import me.ilnicki.bg.core.state.parameters.BoolParameter;
import me.ilnicki.bg.core.state.parameters.IntParameter;

public class MachineForm extends JFrame {
  private JPanel contentPanel;
  private JPanel fieldPanel;

  private JSpinner spinnerHiScore;
  private JSpinner spinnerScore;
  private JSpinner spinnerSpeed;
  private JSpinner spinnerLevel;
  private JSpinner spinnerVolume;
  private JCheckBox checkBoxPause;
  private JPanel helperPanel;

  public MachineForm(State state) {
    $$$setupUI$$$();

    setTitle("State control");
    setContentPane(contentPanel);

    GameState gameState = state.getGameState();
    SystemState sysState = state.getSystemState();

    MutablePixelMatrix fieldPm = new HashPixelMatrix(10, 20);
    gameState.field.getLayers().add(new Layer<>(fieldPm));
    attachPixelMatrixToPanel(fieldPm, fieldPanel);

    attachParamToSpinner(spinnerHiScore, gameState.hiscore);
    attachParamToSpinner(spinnerScore, gameState.score);
    attachParamToSpinner(spinnerSpeed, gameState.speed);
    attachParamToSpinner(spinnerLevel, gameState.level);
    attachParamToSpinner(spinnerVolume, sysState.volume);

    attachParamToCheckbox(checkBoxPause, sysState.pause);

    MutablePixelMatrix helperPm = new HashPixelMatrix(4, 4);
    gameState.helper.getLayers().add(new Layer<>(helperPm));
    attachPixelMatrixToPanel(helperPm, helperPanel);
  }

  private void attachParamToSpinner(JSpinner spinner, IntParameter param) {
    spinner.setValue(param.get());
    spinner.addChangeListener(
        e -> {
          param.set((Integer) spinner.getValue());
          spinner.setValue(param.get());
        });
  }

  private void attachParamToCheckbox(JCheckBox spinner, BoolParameter param) {
    spinner.setSelected(param.get());
    spinner.addChangeListener(
        e -> {
          param.set(spinner.isSelected());
          spinner.setSelected(param.get());
        });
  }

  private void attachPixelMatrixToPanel(MutablePixelMatrix pm, JPanel panel) {
    for (int y = 0; y < pm.getHeight(); y++) {
      for (int x = 0; x < pm.getWidth(); x++) {
        final Vector point = new Vector(x, pm.getHeight() - 1 - y);
        final CellBox cellBox = new CellBox(pm, point);
        cellBox.addActionListener(
            e -> pm.setPixel(point, cellBox.isSelected() ? Pixel.BLACK : Pixel.WHITE));

        panel.add(
            cellBox,
            new GridConstraints(
                y,
                x,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false));
      }
    }
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    contentPanel = new JPanel();
    contentPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
    fieldPanel = new JPanel();
    fieldPanel.setLayout(new GridLayoutManager(20, 10, new Insets(10, 10, 10, 10), -1, -1));
    contentPanel.add(
        fieldPanel,
        new GridConstraints(
            0,
            0,
            3,
            1,
            GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null,
            null,
            null,
            0,
            false));
    fieldPanel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(-16777216)),
            null,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null,
            null));
    final Spacer spacer1 = new Spacer();
    contentPanel.add(
        spacer1,
        new GridConstraints(
            0,
            1,
            3,
            1,
            GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            1,
            null,
            null,
            null,
            0,
            false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(
        panel1,
        new GridConstraints(
            0,
            2,
            1,
            1,
            GridConstraints.ANCHOR_NORTH,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            null,
            null,
            null,
            0,
            false));
    panel1.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(-16777216)),
            null,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null,
            null));
    final JLabel label1 = new JLabel();
    label1.setText("Score:");
    panel1.add(
        label1,
        new GridConstraints(
            1,
            0,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    spinnerHiScore = new JSpinner();
    spinnerHiScore.setToolTipText("Hi-Score");
    panel1.add(
        spinnerHiScore,
        new GridConstraints(
            0,
            1,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    final JLabel label2 = new JLabel();
    label2.setText("Hi-Score:");
    panel1.add(
        label2,
        new GridConstraints(
            0,
            0,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    spinnerScore = new JSpinner();
    spinnerScore.setToolTipText("Score");
    panel1.add(
        spinnerScore,
        new GridConstraints(
            1,
            1,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    spinnerSpeed = new JSpinner();
    spinnerSpeed.setToolTipText("Score");
    panel1.add(
        spinnerSpeed,
        new GridConstraints(
            2,
            1,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    final JLabel label3 = new JLabel();
    label3.setText("Speed:");
    panel1.add(
        label3,
        new GridConstraints(
            2,
            0,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    final JLabel label4 = new JLabel();
    label4.setText("Level:");
    panel1.add(
        label4,
        new GridConstraints(
            3,
            0,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    spinnerLevel = new JSpinner();
    spinnerLevel.setToolTipText("Score");
    panel1.add(
        spinnerLevel,
        new GridConstraints(
            3,
            1,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    spinnerVolume = new JSpinner();
    spinnerVolume.setToolTipText("Score");
    panel1.add(
        spinnerVolume,
        new GridConstraints(
            4,
            1,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    final JLabel label5 = new JLabel();
    label5.setText("Volume:");
    panel1.add(
        label5,
        new GridConstraints(
            4,
            0,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    final JLabel label6 = new JLabel();
    label6.setText("Pause:");
    panel1.add(
        label6,
        new GridConstraints(
            5,
            0,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    checkBoxPause = new JCheckBox();
    checkBoxPause.setText("");
    panel1.add(
        checkBoxPause,
        new GridConstraints(
            5,
            1,
            1,
            1,
            GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null,
            null,
            null,
            0,
            false));
    helperPanel = new JPanel();
    helperPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(
        helperPanel,
        new GridConstraints(
            2,
            2,
            1,
            1,
            GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null,
            null,
            null,
            0,
            false));
    helperPanel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(-16777216)),
            null,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            null,
            null));
  }

  /** @noinspection ALL */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }
}
