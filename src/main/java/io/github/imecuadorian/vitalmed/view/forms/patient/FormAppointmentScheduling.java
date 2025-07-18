package io.github.imecuadorian.vitalmed.view.forms.patient;

import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.*;

@SystemForm(
        name = "Agendamiento de Citas",
        description = "Wizard de agendamiento para pacientes",
        tags = {"agendamiento","citas"}
)
public class FormAppointmentScheduling extends Form implements LanguageChangeListener {
    private CardLayout cards = new CardLayout();
    private JPanel pnlCards = new JPanel(cards);
    private JButton btnNext = new JButton("Siguiente"), btnBack = new JButton("Atrás");

    private JComboBox<String> cmbSpecialty, cmbDoctor;
    private JList<LocalTime> lstTimes;
    private DefaultListModel<LocalTime> timesModel;

    private JTextArea txtSummary;
    private JButton btnGeneratePdf;
    private User currentPatient = MyMenuValidation.getUser();

    @Override
    public void formInit() {
        setLayout(new MigLayout("fill","[grow]","[grow][]"));
        pnlCards.add(buildStep2(), "step2");
        pnlCards.add(buildStep3(), "step3");
        add(pnlCards,"grow, wrap");

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav.add(btnBack); nav.add(btnNext);
        add(nav,"growx");

        btnBack.setEnabled(false);
        btnBack.addActionListener(e->onBack());
        cards.show(pnlCards,"step1");
    }

    private JPanel buildStep2() {
        JPanel p = new JPanel(new MigLayout("wrap 2, gap 10","[][grow]"));
        p.add(new JLabel("Paso 2"),"span 2");
        cmbSpecialty = new JComboBox<>(); cmbDoctor = new JComboBox<>();
        timesModel = new DefaultListModel<>();
        lstTimes = new JList<>(timesModel);

        p.add(new JLabel("Especialidad")); p.add(cmbSpecialty,"growx");
        p.add(new JLabel("Doctor"));    p.add(cmbDoctor,"growx");
        p.add(new JLabel("Tiempo"));      p.add(new JScrollPane(lstTimes),"growx, h 80");

        // carga especialidades
        /*patientCtrl.getSpecialties()
                .thenAccept(list-> SwingUtilities.invokeLater(()->{
                    list.forEach(cmbSpecialty::addItem);
                }));

        cmbSpecialty.addActionListener(e->{
            String esp = (String)cmbSpecialty.getSelectedItem();
            appCtrl.getDoctorsBySpecialty(esp)
                    .thenAccept(list-> SwingUtilities.invokeLater(()->{
                        cmbDoctor.removeAllItems();
                        list.forEach(d->cmbDoctor.addItem(d.getFullName()));
                    }));
        });

        dpDate.addActionListener(e-> loadTimesForSelected());*/

        return p;
    }

    /*private void loadTimesForSelected() {
        String docName = (String)cmbDoctor.getSelectedItem();
        LocalDate date = dpDate.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        timesModel.clear();
        appCtrl.getAvailableSlots(docName, date)
                .thenAccept(slots-> SwingUtilities.invokeLater(()->{
                    slots.forEach(timesModel::addElement);
                }));
    }*/

    private JPanel buildStep3() {
        JPanel p = new JPanel(new MigLayout("wrap 1","[grow]","[][grow]"));
        p.add(new JLabel("Paso 3"),"span, center");
        txtSummary = new JTextArea(); txtSummary.setEditable(false);
        p.add(new JScrollPane(txtSummary),"grow, push, h 120");
        btnGeneratePdf = new JButton("Generar PDF");
        p.add(btnGeneratePdf,"center");
        /*btnGeneratePdf.addActionListener(e->{
            Appointment appt = new Appointment(
                    currentPatient,
                    (String)cmbSpecialty.getSelectedItem(),
                    (String)cmbDoctor.getSelectedItem(),
                    dpDate.getDate().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate(),
                    lstTimes.getSelectedValue()
            );
            PDFUtil.generateAppointmentPDF(appt, "./cita_"+currentPatient.getCedula()+".pdf");
            JOptionPane.showMessageDialog(this, I18n.t("form.appSched.success.pdf"));
        });*/
        return p;
    }

    /*private void onNext() {
        if (cards.next(pnlCards);==pnlCards.getComponent(0)) {
            // validamos step1 completado
            cards.next(pnlCards);
            btnBack.setEnabled(true);
        } else if (cards.current(pnlCards)==pnlCards.getComponent(1)) {
            // confirmamos que haya hora seleccionada
            if (lstTimes.getSelectedValue()==null) {
                JOptionPane.showMessageDialog(this,I18n.t("form.appSched.error.selectTime"));
                return;
            }
            // resumo en textarea
            txtSummary.setText(
                    I18n.t("form.appSched.summary.patient")+" "+currentPatient.getFullName()+"\n"+
                    I18n.t("form.appSched.summary.specialty")+" "+cmbSpecialty.getSelectedItem()+"\n"+
                    I18n.t("form.appSched.summary.doctor")+" "+cmbDoctor.getSelectedItem()+"\n"+
                    I18n.t("form.appSched.summary.date")+" "+dpDate.getDate()+"\n"+
                    I18n.t("form.appSched.summary.time")+" "+lstTimes.getSelectedValue()
            );
            cards.next(pnlCards);
            btnNext.setEnabled(false);
        }
    }*/

    private void onBack() {
        cards.previous(pnlCards);
        btnNext.setEnabled(true);
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        // aquí recargas todos los labels con bundle.getString(...)
    }
}
