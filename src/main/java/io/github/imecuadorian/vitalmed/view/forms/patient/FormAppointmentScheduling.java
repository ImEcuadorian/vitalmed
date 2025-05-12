package io.github.imecuadorian.vitalmed.view.forms.patient;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.ui.*;
import com.formdev.flatlaf.util.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.component.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.layout.*;
import net.miginfocom.swing.*;
import raven.extras.*;
import raven.modal.slider.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

@SystemForm(name = "Agendamiento de Citas", description = "Agendamiento de citas", tags = {"agendamiento, citas"})
public class FormAppointmentScheduling extends Form {

    public FormAppointmentScheduling() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]", "[][grow,fill]"));
        add(createInfo());
        add(createOptions());
    }
    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Agendamiento de Citas");
        JTextPane text = new JTextPane();
        text.setText("SlidePane is a custom Java Swing component that enables smooth transitions between panels with animated sliding effects.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 2,fillx", "[grow 0,fill][fill]", "[fill][][grow,fill]"));
        panel.add(createExample(), "span 2");
        return panel;
    }


    private Component createTestButton() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Show slide"));
        LabelButton lbTest1 = new LabelButton("Show slide-1");
        LabelButton lbTest2 = new LabelButton("Show slide-2");

        lbTest1.addOnClick(o -> {
            slidePane.addSlide(new FormRegister(), SlidePaneTransition.Type.FORWARD);
        });
        lbTest2.addOnClick(o -> {
            slidePane.addSlide(new FormRegister(), SlidePaneTransition.Type.FORWARD);
        });

        panel.add(lbTest1);
        panel.add(lbTest2);
        return panel;
    }

    private Component createExample() {
        JPanel panel = new JPanel(new MigLayout("wrap,al center", "[fill,400]"));
        panel.setBorder(new TitledBorder("Example"));
        PanelSlider.PaneSliderLayoutSize layoutSize = (container, component) -> {
            if (jrComponentPreferredSize.isSelected()) {
                return minSize(container, component);
            } else {
                return container.getSize();
            }
        };
        slidePane = new SlidePane(layoutSize);
        slidePane.addSlide(new FormRegister());
        panel.add(slidePane);
        return panel;
    }

    private Dimension minSize(Container container, Component component) {
        Container parent = container.getParent();
        Dimension comSize = component.getPreferredSize();
        Dimension parentSize = parent.getSize();
        Insets parentInsets = FlatUIUtils.addInsets(parent.getInsets(), getMiglayoutDefaultInsets());
        int width = Math.min(comSize.width, parentSize.width - (parentInsets.left + parentInsets.right));
        int height = Math.min(comSize.height, parentSize.height - (parentInsets.top + parentInsets.bottom));
        return new Dimension(width, height);
    }

    private Insets getMiglayoutDefaultInsets() {
        int top = (int) PlatformDefaults.getPanelInsets(0).getValue();
        int left = (int) PlatformDefaults.getPanelInsets(1).getValue();
        int bottom = (int) PlatformDefaults.getPanelInsets(2).getValue();
        int right = (int) PlatformDefaults.getPanelInsets(3).getValue();
        return UIScale.scale(new Insets(top, left, bottom, right));
    }

    private SlidePane slidePane;

    private JRadioButton jrContainerSize;
    private JRadioButton jrComponentPreferredSize;

}
