/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package net.coderextreme.jaminate;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.ccil.cowan.tagsoup.Parser;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
//import javax.script.Invocable;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptEngineFactory;
//import javax.script.ScriptException;
//import javax.script.CompiledScript;
//import javax.script.Compilable;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Engine;
import org.web3d.x3d.jsail.Core.X3D;
//import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
//import com.oracle.truffle.js.lang.JavaScriptLanguageProvider;

public class TableLoadSave extends Parser {

    /**
     * @return the mode
     */
    public rowMode getMode() {
        return mode;
    }
    private GenericTableModel model;
    private Motion currentRow = null;

    public X3D JavaScriptExec(String javaScriptString)
      {
	  X3D X3D0 = null;
          try
          {
            Engine engine = Engine.newBuilder("js").build();
            Context context = Context.newBuilder("js")
		.engine(engine)
		.allowHostAccess(HostAccess.ALL)
		.allowHostClassLookup(className -> true)
		.arguments("js", new String[]{"--jvm", "--vm.classpath=lib/X3DJSAIL.4.0.full.jar;lib/saxon-he-12.1.jar"})
		.option("js.ecmascript-version", "2022").build();
            context.eval("js", javaScriptString).asHostObject();
	    X3D0 = context.eval("js", "X3D0").asHostObject();
	    context.close();
	    engine.close();
          } catch(Exception e) {
              e.printStackTrace(System.err);
          }
	  return X3D0;
      }
    private void loadJsFile(GenericTableModel model, File selectedFile) {
         this.model = model;
	 System.err.println("Opening file "+selectedFile);
         try (FileInputStream fis = new FileInputStream(selectedFile)){
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                String jsCode = sb.toString();
                X3D X3D0 = this.JavaScriptExec(jsCode);
		System.err.println("Version: "+X3D0.getVersion());
		System.err.println("Profile: "+X3D0.getProfile());
                
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
    public enum rowMode { HEADER, DATA, OFF };
    private rowMode mode = rowMode.DATA;
    public static void main(String[] args) {
 
        TableLoadSave loadAndSave = new TableLoadSave();
        loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File("C:/Users/john/HAnim.code/jaminate/Jaminate/app/src/main/javascript/JinLOA4.js"));
        // loadAndSave.loadTest(new GenericTableModel(new DefaultTableModel()));

    }
    private int column;
    private int row;

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
            loadHTMLFile(model, selectedFile);
        } else if (selectedFile.getName().endsWith(".txt")) {
            loadTxtFile(model, selectedFile);
        } else if (selectedFile.getName().endsWith(".js")) {
            loadJsFile(model, selectedFile);
        }
    }
    public void loadTxtFile(GenericTableModel model, File selectedFile) {
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
                    this.model.getModel().setNumRows(this.model.getModel().getRowCount());
                } else {
                    // System.err.println(str+"# failed row ");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        
    }
    public void loadHTMLFile(GenericTableModel model, File selectedFile) {
        this.model = model;
         try (FileInputStream fis = new FileInputStream(selectedFile)){
            loadHTMLSource(model, new InputSource(fis));
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
    public void loadHTMLSource(GenericTableModel model, InputSource is) {
        this.model = model;
        
        //tagsoup
        try {
            this.parse(is);
        } catch (IOException | SAXException e) {
            e.printStackTrace(System.err);
        }/*
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            reader.parse(is);
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace(System.err);
        }*/
    }
    public String saveTableModel(GenericTableModel model, File selectedFile){
        this.model = model;
        if (selectedFile.getName().endsWith(".html")) {
            return saveXMLTableModel(model, selectedFile);
        } else if (selectedFile.getName().endsWith(".json")) {
            return saveJsonFile(model, selectedFile);
        } else {
            return "";
        }
    }
    /*
     {
"John": {
        "TimesPerCharacter": [ 0,0.10,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1],
        "MovesPerCharacter": [ "John_Stop01","John_Stand01","John_Pitch01","John_Run01","John_Turn01","John_Roll01","John_Walk01","John_Run01","John_Skip01","John_Kick01","John_Stop01" ],
        "Global": [ 0,0.10,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1],
        "John_Walk01": [false,false,false,false,false,false,true,false,false,false,false],
        "John_Stop01": [true,false,false,false,false,false,false,false,false,false,true],
        "John_Kick01": [false,false,false,false,false,false,false,false,false,true,false],
        "John_Pitch01": [false,false,true,false,false,false,false,false,false,false,false],
        "John_Skip01": [false,false,false,false,false,false,false,false,true,false,false],
        "John_Stand01": [false,true,false,false,false,false,false,false,false,false,false],
        "John_Run01": [false,false,false,true,false,false,false,true,false,false,false],
        "John_Roll01": [false,false,false,false,false,true,false,false,false,false,false],
        "John_Turn01": [false,false,false,false,true,false,false,false,false,false,false]
        }
}
*/
    public String saveJson(GenericTableModel model, PrintWriter pw) {
            this.model = model;
            String json = "";
            Map<String, Map<String, List<Double>>> timesAllCharacters = new HashMap<String, Map<String, List<Double>>>();
	    Map<String, Map<String, List<String>>> movesAllCharacters = new HashMap<String, Map<String, List<String>>>();
            int rows = model.getModel().getRowCount();
            int cols = model.getModel().getColumnCount();
	    String MPC = "MovesPerCharacter";
	    String TPC = "TimesPerCharacter";
            for (int r = 0; r < rows; r++) {
                String character = model.getModel().getValueAt(r, 1).toString(); // get character name
                Double time = Double.valueOf(model.getModel().getValueAt(r, 2).toString()); // get time
                String mainMotion = model.getModel().getValueAt(r, 3).toString(); // primary motion
                Integer subMotion = Integer.valueOf(model.getModel().getValueAt(r, 4).toString()); // secondary submotion
								      //
		Map<String, List<Double>> characterTimeInfo = timesAllCharacters.get(character);
		if (characterTimeInfo == null) {
			timesAllCharacters.put(character, new HashMap<String, List<Double>>());
			characterTimeInfo = timesAllCharacters.get(character);
		}

		Map<String, List<String>> characterMoveInfo = movesAllCharacters.get(character);
		if (characterMoveInfo == null) {
			movesAllCharacters.put(character, new HashMap<String, List<String>>());
			characterMoveInfo = movesAllCharacters.get(character);
		}

		List<Double> characterTimes = characterTimeInfo.get(TPC);
		if (characterTimes == null) {
			characterTimeInfo.put(TPC, new ArrayList<Double>());
			characterTimes = characterTimeInfo.get(TPC);
		}
		characterTimes.add(time);

		List<String> characterMoves = characterMoveInfo.get(MPC);
		if (characterMoves == null) {
			characterMoveInfo.put(MPC, new ArrayList<String>());
			characterMoves = characterMoveInfo.get(MPC);
		}
		String move = subMotion.toString();
		if (subMotion < 10 && subMotion >= 0) {
			move = "0"+move;
		}
		characterMoves.add(mainMotion+move);
            }
            json += "{\n";
	    Boolean afterFirst = false;
	    // base this off Times, could be based on moves.
	    for (String character : timesAllCharacters.keySet()) {
		if (afterFirst) {
			json += ",\n";
		} else {
			afterFirst = true;
		}
		json += "\""+character+"\": {\n";
	    	for (Map.Entry<String, List<Double>> pair2 : timesAllCharacters.get(character).entrySet()) {
		    json += "\""+pair2.getKey()+"\": "+pair2.getValue().stream ().map(Object::toString).collect (Collectors.joining(", ", "[", "]"))+",";
		}
                json += "\n";
	    	for (Map.Entry<String, List<String>> pair3 : movesAllCharacters.get(character).entrySet()) {
		    json += "\""+pair3.getKey()+"\": [\""+character+"_"+String.join("\", \""+character+"_", pair3.getValue())+"\"]";
		}
		json += "}\n";
	    }
            json += "}\n";
            pw.print(json);
            return json;
    }
    public String saveJsonFile(GenericTableModel model, File selectedFile) {
        this.model = model;
     
        PrintWriter nopw = null;
        InputStream is = null;
        String json = "";
        try (PrintWriter pw = new PrintWriter(new FileWriter(selectedFile));) {
            json = saveJson(model, pw);
            Process p = Runtime.getRuntime().exec(new String[] { "node", "takes.js"});
            nopw = new PrintWriter(p.getOutputStream());
            nopw.write(json);
            is = p.getInputStream();
            nopw.close();
            nopw = null;
            String animationChain = new BufferedReader(new InputStreamReader(is))
   .lines().collect(Collectors.joining("\n"));
            return animationChain;
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            if (nopw != null) {
                 nopw.close();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return json;
    }
    public String saveXMLTableModel(GenericTableModel model, File selectedFile) {
        this.model = model;
        try (PrintWriter pw = new PrintWriter(new FileWriter(selectedFile));) {
            return saveTableToPrintWriter(model, pw);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return "";
        }
    }
    /**
     * @param model
     * @return 
     */
    @Deprecated
    public String saveHTMLToClipboard(GenericTableModel model) {
        this.model = model;  
        try  {
            String s = saveTableToHTMLString(model);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(s);
            DataHandler dh = new DataHandler(ss, "text/html");
            clipboard.setContents(ss, ss);
            System.err.println(clipboard.getData(DataFlavor.stringFlavor));
            return s;
        } catch (IOException | UnsupportedFlavorException e) {
            Logger.getLogger(TableLoadSave.class.getName()).log(Level.SEVERE, null, e);
        }
        return "";
    }
    public String saveTableToHTMLString(GenericTableModel model) {
        String html = "";
        this.model = model;
        int rows = this.model.getModel().getRowCount();
        int cols = this.model.getModel().getColumnCount();
        html += "<!DOCTYPE html>\n<html>\n<head>\n<title>Main Stage</title>\n</head>\n<body>\n<table>\n<tr>";
        for (int c = 0; c < cols; c++) {
            String chead = model.getModel().getColumnName(c);
            html += "<th>"+chead+"</th>";
        }
        html += "</tr>";
        for (int r = 0; r < rows; r++) {
            html += "<tr>";
            for (int c = 0; c < cols; c++) {
                Object o = model.getModel().getValueAt(r, c);
                html += "<td>"+o+"</td>";
            }
            html += "</tr>\n";
        }
        html += "</table>\n</body>\n</html>";  
        return html;
    }
    private String saveTableToPrintWriter(GenericTableModel model, PrintWriter pw) {
        this.model = model;
        String html = saveTableToHTMLString(model);
        pw.print(html);
        return html;
    } 

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        //System.err.println("Parsing <"+localName+"> "+qName);
        switch (qName.toLowerCase()) {
            case "table" -> {
                this.mode = rowMode.OFF;
                this.column = 0;
                this.row = 0;
            }
            case "tr" -> {
                this.currentRow = new Motion();
                //System.err.println("Current row is "+this.currentRow);
                this.column = 0;
                this.mode = rowMode.OFF;
            }
            case "td" -> {
                if (this.row == 0) {
                    mode = rowMode.HEADER;
                } else {
                    mode = rowMode.DATA;
                }
            }
            case "th" -> {
                mode = rowMode.HEADER;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //System.err.println("Done parsing <"+localName+"> "+qName);
        switch (qName.toLowerCase()) {
            case "table" -> {
                this.mode = rowMode.OFF;
                this.column = 0;
                this.row = 0;
                this.currentRow = null;
                //System.err.println("Number of rows in endElement "+model.getModel().getRowCount()+" mode "+this.mode);
            }
            case "tr" -> {
                this.model.addRow(this.currentRow, this.mode);
                this.model.getModel().setNumRows(this.model.getModel().getRowCount());
                this.mode = rowMode.OFF;
                this.column = 0;
                this.row++;
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
                    //System.err.println("Found column "+this.column+" cell "+cell+" length "+length+" mode "+this.mode);
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
