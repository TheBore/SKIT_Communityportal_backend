package io.intelligenta.communityportal.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import sun.misc.BASE64Encoder;

/**
 *
 * @author JXians template exports word documents and PDF documents
 *
 */
public class ExportOfficeUtils {

    private static Configuration config = null;
    static {
        config = new Configuration(new Version("2.3.0"));
        config.setClassForTemplateLoading(ExportOfficeUtils.class, "/template/");
        config.setDefaultEncoding("UTF-8");
    }

    /**
     * Get configuration object
     *
     * @return
     */
    public static Configuration getConfiguration() {
        return config;
    }

    /**
     * Synthetic data and templates
     *
     *
     * @param obj The value to be filled in the template
     */
    public static String generate(String templateName, Object obj) throws IOException, TemplateException {
        Configuration config = getConfiguration();
        Template template = config.getTemplate(templateName, "utf-8");
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        template.process(obj, writer);
        String htmlStr = stringWriter.toString();
        writer.flush();
        writer.close();
        return htmlStr;
    }

    /**
     * Generate word documents based on templates
     *
     * @param templateName template name
     * @param dataMap template demand data
     * @param OutputFilePath Generated file storage address
     */
    public static void generateWORD(String templateName, Map<String, Object> dataMap, String OutputFilePath) {
        try {
            // Configuration is used to read ftl files
            Configuration configuration = getConfiguration();
            // Output file path and name
            File outFile = new File(OutputFilePath);
            Template template = configuration.getTemplate(templateName, "utf-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
            template.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate pdf documents
     *
     * @param templateName template file
     * @throws Exception
     */
    public static void generatePDF(String templateName, Map<String, Object> dataMap, String PDFoutputFilePath)
            throws Exception {
        // merge template and data model word doc os = ftl + obj
        String generate = ExportOfficeUtils.generate(templateName, dataMap);
        ByteArrayInputStream in = new ByteArrayInputStream(generate.getBytes());
        // word processing object
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(in);
        // wordMLPackage -> pdf os
        FOSettings foSettings = Docx4J.createFOSettings();
        Mapper fontMapper = new IdentityPlusMapper();

        wordMLPackage.setFontMapper(fontMapper);

        foSettings.setWmlPackage(wordMLPackage);
        foSettings.setApacheFopMime("application/pdf");
        Docx4J.toPDF(wordMLPackage, new FileOutputStream(PDFoutputFilePath));
    }

    /**
     * Image transcoding
     */
    public static String getImageStr(String imgpath) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgpath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

}

