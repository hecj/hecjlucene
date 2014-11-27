package org.test.hecjlucene.core10_tika;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class TikaTest {

	public static void main(String[] args) throws Exception {
        // Parser parser = new OOXMLParser();
        // Parser parser = new PDFParser();
        // Parser parser = new HtmlParser();
        //Parser parser = new OOXMLParser() 2010 office用这个
        //Parser parser = new OfficeParser(); //2003以下用这个
        Parser parser = new AutoDetectParser();
        InputStream iStream = new BufferedInputStream(new FileInputStream(new File("e:/1.xml")));
        StringWriter stringWriter = new StringWriter();
        Writer writer = new BufferedWriter(stringWriter);
        ContentHandler handler = new BodyContentHandler(writer);

        parser.parse(iStream, handler, new Metadata(), new ParseContext());
        System.out.println(stringWriter.toString());

    }
}