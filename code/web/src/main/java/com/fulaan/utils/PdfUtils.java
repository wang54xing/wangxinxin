package com.fulaan.utils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;


import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;


public class PdfUtils {

    protected int topValue = 10;
    protected int leftValue = 20;
    protected int rightValue = 10;
    protected int bottomValue = 10;
    protected int userSpaceWidth = 800;

    public static void main(String[] args) {
        try {
        	long a =System.currentTimeMillis();
        	PdfUtils jt = new PdfUtils();
        	for(int i=0;i<5;i++)
        	{
        		System.out.println("i=="+i);
        		long at=System.currentTimeMillis();
        		File targetFile = new File("d:\\pdf", "1.pdf");
        		targetFile.createNewFile();
        		FileOutputStream fileStream = new FileOutputStream(targetFile);
                jt.doConversion("http://www.k6kt.com/testpaper/view1.do?pid=565c10f70cf25daaae0c68ee", fileStream);
                long bt=System.currentTimeMillis();
                
                System.out.println(bt-at);
        	}
        	long b =System.currentTimeMillis();
        	
        	System.out.println(b-a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doConversion( String url, String outputPath )
            throws InvalidParameterException, MalformedURLException, IOException {
        File output = new File(outputPath);
        java.io.FileOutputStream fos = new java.io.FileOutputStream(output);

        PD4ML pd4ml = new PD4ML();

        pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual web browser"

        // choose target paper format and "rotate" it to landscape orientation
        pd4ml.setPageSize(PD4Constants.A4);

        // define PDF page margins
        pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));

        // source HTML document also may have margins, could be suppressed this way
        // (PD4ML *Pro* feature):
        pd4ml.addStyle("BODY {margin: 0}", true);

        // If built-in basic PDF fonts are not sufficient or
        // if you need to output non-Latin texts,
        // TTF embedding feature should help (PD4ML *Pro*)
        //pd4ml.useTTF("/users/qinbo/desktop/fonts", true);
        pd4ml.useTTF("java:fonts", true);
        pd4ml.enableDebugInfo();
        pd4ml.render(new URL(url), fos); // actual document conversion from URL to file
        fos.close();

        System.out.println( outputPath + "\ndone." );
    }
    
   
    public void doConversion( String url, OutputStream fos )
            throws InvalidParameterException, MalformedURLException, IOException {
//        File output = new File(outputPath);
//        java.io.FileOutputStream fos = new java.io.FileOutputStream(output);

        PD4ML pd4ml = new PD4ML();

        pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual web browser"

        // choose target paper format and "rotate" it to landscape orientation
        pd4ml.setPageSize(PD4Constants.A4);
       

        // define PDF page margins
        pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));

        // source HTML document also may have margins, could be suppressed this way
        // (PD4ML *Pro* feature):
        pd4ml.addStyle("BODY {margin: 0}", true);

        // If built-in basic PDF fonts are not sufficient or
        // if you need to output non-Latin texts,
        // TTF embedding feature should help (PD4ML *Pro*)
        //pd4ml.useTTF("/users/qinbo/desktop/fonts", true);
        pd4ml.useTTF("java:fonts", true);
        pd4ml.enableDebugInfo();
        pd4ml.render(new URL(url), fos); // actual document conversion from URL to file
        fos.close();

       // System.out.println( outputPath + "\ndone." );
    }

}

