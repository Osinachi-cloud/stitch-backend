package com.stitch.commons.service;

import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public interface FileConverterService {

    String createPdf(Context context, String fileName);
}
