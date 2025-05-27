package io.github.imecuadorian.vitalmed.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class AppointmentPdfExporter {

    public static final String OUTPUT_FOLDER = "citas/";

    private final Logger logger;

    public AppointmentPdfExporter(Logger logger) {
        this.logger = logger;
    }

    public void export(Map<String, Object> parameters, String patientName, String patientId) throws JRException {
        InputStream jasperStream = AppointmentPdfExporter.class.getResourceAsStream("/io/github/imecuadorian/vitalmed/templates/appointment_template.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        File outputDir = new File(OUTPUT_FOLDER);
        if (!outputDir.exists()) outputDir.mkdirs();

        String safeName = patientName.trim().replaceAll("\\s+", "_");
        String fileName = String.format("cita_%s_%s.pdf", safeName, patientId);
        String outputPath = OUTPUT_FOLDER + fileName;

        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

        if (logger.isLoggable(Level.INFO)) {
            logger.info("PDF exported to: " + outputPath);
        }
    }
}

