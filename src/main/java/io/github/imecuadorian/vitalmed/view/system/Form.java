package io.github.imecuadorian.vitalmed.view.system;


import javax.swing.*;

public class Form extends JPanel {
    private transient LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public void formInit(){ }

    public void formOpen(){ }

    public void formRefresh(){ }
    protected final void formCheck() {
        if (oldTheme != UIManager.getLookAndFeel()) {
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
}
