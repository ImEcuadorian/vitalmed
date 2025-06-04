package io.github.imecuadorian.vitalmed.view.forms.auth;

import io.github.imecuadorian.vitalmed.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import org.slf4j.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

@SystemForm(name = "Cerrar Sesión", description = "Cerrar sesión del sistema", tags = {"cerrar sesión"})
public class FormLogout extends Form implements LanguageChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormLogout.class);
    private static final String CONSTRAINT_CENTER = "align center";

    private JLabel title;
    private JLabel message;
    private JButton btnLogout;

    private void initUI() {
        setLayout(new MigLayout("insets 0, fill", "[center]", "[center]"));

        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, align center, gapy 15", "[300::300]", "[]"));

        title = new JLabel(I18n.t("form.formLogout.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        contentPanel.add(title, CONSTRAINT_CENTER);

        message = new JLabel(I18n.t("form.formLogout.message"));
        contentPanel.add(message, CONSTRAINT_CENTER);

        btnLogout = new JButton(I18n.t("form.formLogout.btnLogout"));
        contentPanel.add(btnLogout, CONSTRAINT_CENTER);

        btnLogout.addActionListener(e -> {
            LOGGER.info("Closed session by user: {}", MyMenuValidation.getUser().getEmail());
            JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(btnLogout);
            dashboardFrame.dispose();

            EventQueue.invokeLater(() -> {
                MyMenuValidation.setUser(null);
                new Vitalmed().setVisible(true);
            });
        });

        add(contentPanel);
    }

    @Override
    public void formInit() {
        initUI();
        I18n.addListener(this);
    }

    @Override
    public void formOpen() {
        // This method is called when the form is opened.
    }

    @Override
    public void formRefresh() {
        SwingUtilities.invokeLater(() -> {
            if (getComponentCount() > 0) {
                removeAll();
            }
            initUI();
            revalidate();
            repaint();
        });
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        title.setText(I18n.t("form.formLogout.title"));
        message.setText(I18n.t("form.formLogout.message"));
        btnLogout.setText(I18n.t("form.formLogout.btnLogout"));
    }
}
