/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package net.coderextreme.jaminate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.swing.table.DefaultTableModel;

public class TableLoadSave extends DefaultHandler {

    /**
     * @return the mode
     */
    public rowMode getMode() {
        return mode;
    }
    private GenericTableModel model;
    private Motion currentRow = null;
    public enum rowMode { HEADER, DATA, OFF };
    private rowMode mode = rowMode.DATA;
    public static void main(String[] args) {
 
        TableLoadSave loadAndSave = new TableLoadSave();
        loadAndSave.loadTest(new GenericTableModel(new DefaultTableModel()));

    }
    private int column;

    public TableLoadSave() {
        this.model = null;
    }
    public void loadTest(GenericTableModel model) {
        this.model = model;
        //File selectedFile = new File("src/main/resources/bridge.html");
        //this.loadTableModel(this.model, selectedFile);
        File selectedFile = new File("src/main/resources/0MainStageTotal_7Saved.txt");
        this.loadTableModel(this.model, selectedFile);
        File tmpFile = new File("src/main/resources/tmp.html");
        this.saveTableModel(this.model, tmpFile);
    }
    public void loadTableModel(GenericTableModel model, File selectedFile) {
        this.model = model;
        if (selectedFile.getName().endsWith(".html")) {
            loadXMLTableModel(model, selectedFile);
        } else if (selectedFile.getName().endsWith(".txt")) {
            loadTxtTableModel(model, selectedFile);
        }
    }
    public void loadTxtTableModel(GenericTableModel model, File selectedFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            Pattern pattern = Pattern.compile("([-0-9\\.e\\+]+)[ \\t]+([-0-9\\.e\\+]+)[ \\t]+([-0-9\\.e\\+]+),[ \\t]+#?\\(([0-9\\.]+)\\)[ \\t]+#?\\(([A-Za-z]+)_([A-Za-z]+)([0-9]+)\\)");
            String str;
            while ((str = br.readLine()) != null) {
                Matcher m = pattern.matcher(str);
                //System.err.println(m.toString());
                if (m.matches()) {
                    System.err.println(str+"# found row ");
                    Motion motion = new Motion();
                    Double x = Double.valueOf(m.group(1));
                    motion.setX(x);
                    Double y = Double.valueOf(m.group(2));
                    motion.setY(y);
                    Double z = Double.valueOf(m.group(3));
                    motion.setZ(z);
                    Double t = Double.valueOf(m.group(4));
                    motion.setTimeStart(t);
                    String character = m.group(5);
                    motion.setCharacter(character);
                    String move = m.group(6);
                    motion.setMotion(move);
                    Integer submove = Integer.valueOf(m.group(7));
                    motion.setSubmove(submove);
                    this.currentRow = motion;
                    this.mode = rowMode.DATA;
                    this.model.addRow(this.currentRow, this.mode);
                } else {
                    // System.err.println(str+"# failed row ");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        
    }
    public void loadXMLTableModel(GenericTableModel model, File selectedFile) {
        this.model = model;
         try (FileInputStream fis = new FileInputStream(selectedFile)){
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(fis));
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace(System.err);
        }
    }
    public void saveTableModel(GenericTableModel model, File selectedFile){
             
        try (PrintWriter pw = new PrintWriter(new FileWriter(selectedFile));) {
            int rows = model.getModel().getRowCount();
            int cols = model.getModel().getColumnCount();
            pw.println("<!DOCTYPE html>\n<html>\n<head>\n<title>Main Stage</title>\n</head>\n<body>\n<table>\n<tr>");
            for (int c = 0; c < cols; c++) {
                String chead = model.getModel().getColumnName(c);
                pw.print("<th>"+chead+"</th>");
            }
            pw.println("</tr>");
            for (int r = 0; r < rows; r++) {
                pw.print("<tr>");
                for (int c = 0; c < cols; c++) {
                    Object o = model.getModel().getValueAt(r, c);
                    pw.print("<td>"+o+"</td>");
                }
                pw.println("</tr>");
            }
            pw.println("</table>\n</body>\n</html>");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        System.err.println("Parsing <"+localName+"> "+qName);
        switch (qName.toLowerCase()) {
            case "table" -> {
                this.mode = rowMode.OFF;
                this.column = 0;
            }
            case "tr" -> {
                this.currentRow = new Motion();
                System.err.println("Current row is "+this.currentRow);
                this.column = 0;
                this.mode = rowMode.OFF;
            }
            case "td" -> {
                mode = rowMode.DATA;
            }
            case "th" -> {
                mode = rowMode.HEADER;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.err.println("Done parsing <"+localName+"> "+qName);
        switch (qName.toLowerCase()) {
            case "table" -> {
                this.mode = rowMode.OFF;
                this.column = 0;
                this.currentRow = null;
                System.err.println("Number of rows in endElement "+model.getModel().getRowCount()+" mode "+this.mode);
            }
            case "tr" -> {
                this.model.addRow(this.currentRow, this.mode);
                this.mode = rowMode.OFF;
                this.column = 0;
                this.currentRow = null;
            }
            case "td" -> {
                this.column++;
            }
            case "th" -> {
                this.column++;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
            if (currentRow != null) {
                if (length > 0) {
                    String cell = new String(ch, start, length);
                    System.err.println("Found column "+this.column+" cell "+cell+" length "+length+" mode "+this.mode);
                    try {
                        if (mode != rowMode.OFF) {
                            currentRow.setField(this.column, this.mode, cell);
                        }
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        Logger.getLogger(TableLoadSave.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
    }
}
