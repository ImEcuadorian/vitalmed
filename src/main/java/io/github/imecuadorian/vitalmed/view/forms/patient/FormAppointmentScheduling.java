package io.github.imecuadorian.vitalmed.view.forms.patient;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.AppointmentController;
import io.github.imecuadorian.vitalmed.controller.PatientController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.Appointment;
import io.github.imecuadorian.vitalmed.model.Patient;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.util.PDFUtil;
import io.github.imecuadorian.vitalmed.view.system.Form;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

@SystemForm(
        name = "Agendamiento de Citas",
        description = "Wizard de agendamiento para pacientes",
        tags = {"agendamiento","citas"}
)
public class FormAppointmentScheduling extends Form implements LanguageChangeListener {
    private CardLayout cards = new CardLayout();
    private JPanel pnlCards = new JPanel(cards);
    private JButton btnNext = new JButton("Siguiente"), btnBack = new JButton("Atrás");

    // Etapa 1
    private JTextField regName, regCedula, regEmail, regPhone, regPass, regPassConf;
    private JButton btnRegister, btnLogin;

    // Etapa 2
    private JComboBox<String> cmbSpecialty, cmbDoctor;
    private JXDatePicker dpDate;
    private JList<LocalTime> lstTimes;
    private DefaultListModel<LocalTime> timesModel;

    // Etapa 3
    private JTextArea txtSummary;
    private JButton btnGeneratePdf;

    private final PatientController patientCtrl =
            new PatientController(ServiceFactory.getPATIENT_SERVICE());
    private final AppointmentController appCtrl =
            new AppointmentController(ServiceFactory.getAPPOINTMENT_SERVICE());

    private Patient currentPatient;

    @Override
    public void formInit() {
        setLayout(new MigLayout("fill","[grow]","[grow][]"));
        pnlCards.add(buildStep1(), "step1");
        pnlCards.add(buildStep2(), "step2");
        pnlCards.add(buildStep3(), "step3");
        add(pnlCards,"grow, wrap");

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav.add(btnBack); nav.add(btnNext);
        add(nav,"growx");

        btnBack.setEnabled(false);
        btnNext.addActionListener(e->onNext());
        btnBack.addActionListener(e->onBack());
        cards.show(pnlCards,"step1");
    }

    private JPanel buildStep1() {
        JPanel p = new JPanel(new MigLayout("wrap 2, gap 10","[][grow]"));
        p.add(new JLabel(I18n.t("form.appSched.step1.title")),"span 2");
        regName = new JTextField(); regCedula = new JTextField();
        regEmail = new JTextField(); regPhone = new JTextField();
        regPass = new JPasswordField(); regPassConf = new JPasswordField();
        p.add(new JLabel(I18n.t("form.appSched.label.name")));      p.add(regName,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.cedula")));    p.add(regCedula,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.email")));     p.add(regEmail,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.phone")));     p.add(regPhone,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.password")));  p.add(regPass,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.passConf")));  p.add(regPassConf,"growx");

        btnRegister = new JButton(I18n.t("form.appSched.button.register"));
        btnLogin    = new JButton(I18n.t("form.appSched.button.login"));
        p.add(btnRegister); p.add(btnLogin);

        // listeners:
        btnRegister.addActionListener(e->{
            if (!regPass.getText().equals(regPassConf.getText())) {
                JOptionPane.showMessageDialog(this,I18n.t("form.appSched.error.passMatch"));
                return;
            }
            patientCtrl.register(
                    new Patient(regName.getText(),regCedula.getText(),regEmail.getText(),regPhone.getText(),regPass.getText())
            ).thenAccept(pat-> SwingUtilities.invokeLater(()->{
                currentPatient = pat;
                btnNext.setEnabled(true);
                JOptionPane.showMessageDialog(this,I18n.t("form.appSched.success.register"));
            })).exceptionally(ex->{
                JOptionPane.showMessageDialog(this,I18n.t("form.appSched.error.register"));
                return null;
            });
        });
        btnLogin.addActionListener(e->{
            patientCtrl.login(regEmail.getText(),regPass.getText())
                    .thenAccept(pat-> SwingUtilities.invokeLater(()->{
                        currentPatient = pat;
                        btnNext.setEnabled(true);
                        JOptionPane.showMessageDialog(this,I18n.t("form.appSched.success.login"));
                    })).exceptionally(ex->{
                        JOptionPane.showMessageDialog(this,I18n.t("form.appSched.error.login"));
                        return null;
                    });
        });

        return p;
    }

    private JPanel buildStep2() {
        JPanel p = new JPanel(new MigLayout("wrap 2, gap 10","[][grow]"));
        p.add(new JLabel(I18n.t("form.appSched.step2.title")),"span 2");
        cmbSpecialty = new JComboBox<>(); cmbDoctor = new JComboBox<>();
        dpDate = new JXDatePicker();
        timesModel = new DefaultListModel<>();
        lstTimes = new JList<>(timesModel);

        p.add(new JLabel(I18n.t("form.appSched.label.specialty"))); p.add(cmbSpecialty,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.doctor")));    p.add(cmbDoctor,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.date")));      p.add(dpDate,"growx");
        p.add(new JLabel(I18n.t("form.appSched.label.time")));      p.add(new JScrollPane(lstTimes),"growx, h 80");

        // carga especialidades
        patientCtrl.getSpecialties()
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

        dpDate.addActionListener(e-> loadTimesForSelected());

        return p;
    }

    private void loadTimesForSelected() {
        String docName = (String)cmbDoctor.getSelectedItem();
        LocalDate date = dpDate.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        timesModel.clear();
        appCtrl.getAvailableSlots(docName, date)
                .thenAccept(slots-> SwingUtilities.invokeLater(()->{
                    slots.forEach(timesModel::addElement);
                }));
    }

    private JPanel buildStep3() {
        JPanel p = new JPanel(new MigLayout("wrap 1","[grow]","[][grow]"));
        p.add(new JLabel(I18n.t("form.appSched.step3.title")));
        txtSummary = new JTextArea(); txtSummary.setEditable(false);
        p.add(new JScrollPane(txtSummary),"grow, push, h 120");
        btnGeneratePdf = new JButton(I18n.t("form.appSched.button.generatePdf"));
        p.add(btnGeneratePdf,"center");
        btnGeneratePdf.addActionListener(e->{
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
        });
        return p;
    }

    private void onNext() {
        if (cards.current(pnlCards)==pnlCards.getComponent(0)) {
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
    }

    private void onBack() {
        cards.previous(pnlCards);
        btnNext.setEnabled(true);
        if (cards.current(pnlCards)==pnlCards.getComponent(0)) btnBack.setEnabled(false);
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        // aquí recargas todos los labels con bundle.getString(...)
    }
}
