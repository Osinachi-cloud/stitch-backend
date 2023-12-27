package com.stitch.commons.service.impl;

import com.stitch.commons.exception.FileException;
import com.stitch.commons.service.FileConverterService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
@Slf4j
public class FileConverterServiceImpl implements FileConverterService {

    private final TemplateEngine templateEngine;
    private final SpringTemplateEngine springTemplateEngine;

    public FileConverterServiceImpl(TemplateEngine templateEngine,
                                    SpringTemplateEngine springTemplateEngine) {
        this.templateEngine = templateEngine;
        this.springTemplateEngine = springTemplateEngine;
    }

    public String createPdf(Context context, String fileName) {
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);

            DefaultFontProvider defaultFont = new DefaultFontProvider(true, true, true);

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setFontProvider(defaultFont);

            String finalHtml = springTemplateEngine.process(fileName, context);

            HtmlConverter.convertToPdf(finalHtml, pdfwriter, converterProperties);

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }catch (Exception ex){
            log.error("error occurred", ex);
            throw new FileException("Unable to process file");
        }
    }
}
