/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package net.coderextreme.jaminate;
import java.lang.reflect.Method;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
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
import org.web3d.x3d.jsail.X3DLoaderDOM;
import org.web3d.x3d.jsail.Core.X3D;
import org.web3d.x3d.jsail.Core.ROUTE;
import org.web3d.x3d.jsail.HAnim.HAnimJoint;
import org.web3d.x3d.jsail.HAnim.HAnimHumanoid;
import org.web3d.x3d.jsail.Interpolation.OrientationInterpolator;
import org.web3d.x3d.jsail.Interpolation.PositionInterpolator;
import org.web3d.x3d.jsail.Rendering.Coordinate;
import org.web3d.x3d.jsail.Geometry3D.IndexedFaceSet;
import org.web3d.x3d.sai.Core.X3DNode;
import org.web3d.x3d.sai.Grouping.X3DGroupingNode;
import org.web3d.x3d.sai.Rendering.X3DGeometryNode;
import org.web3d.x3d.sai.Shape.X3DAppearanceNode;
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
    private List<X3DNode> rotateCoordinates(X3D X3D0) {
	ArrayList<X3DNode> coordinates = traverseChildren(X3D0.getScene().getChildren(), Coordinate.class, 0);
	for (int ci = 0; ci < coordinates.size(); ci++) {
	    Coordinate coordinate = (Coordinate)coordinates.get(ci);
	    float [] point = coordinate.getPoint();
	    for (int i = 0; i < point.length; i += 3) {
		    turnPoint(point, i);
	    }
	    coordinate.setPoint(point);
	}
	return coordinates;
    }
    private List<X3DNode> rotateOrientations(X3D X3D0) {
	ArrayList<X3DNode> orientations = traverseChildren(X3D0.getScene().getChildren(), OrientationInterpolator.class, 0);
	for (int oi = 0; oi < orientations.size(); oi++) {
	    OrientationInterpolator interpolator = (OrientationInterpolator)orientations.get(oi);
	    float[] keys = interpolator.getKey();
	    float[] keyValues = interpolator.getKeyValue();

	    for (int k = 0; k < keys.length; k += 4) {
		    keyValues[k+3] = -keyValues[k+3];
		    turnPoint(keyValues, k);
	    }
	    interpolator.setKeyValue(keyValues);
	}
	return orientations;
    }
    private List<X3DNode> rotatePositions(X3D X3D0) {
	ArrayList<X3DNode> positions = traverseChildren(X3D0.getScene().getChildren(), PositionInterpolator.class, 0);
	for (int pi = 0; pi < positions.size(); pi++) {
	    PositionInterpolator interpolator = (PositionInterpolator)positions.get(pi);
	    float[] keys = interpolator.getKey();
	    float[] keyValues = interpolator.getKeyValue();

	    for (int k = 0; k < keys.length; k += 3) {
		    turnPoint(keyValues, k);
	    }
	    interpolator.setKeyValue(keyValues);
	}
	return positions;
    }
    private void turnPoint(float [] point, int offset) {
	    javax.vecmath.Point3f point3f = new javax.vecmath.Point3f(point[offset+0], point[offset+1], point[offset+2]);
	    // rotate 180 degrees around Y
	    javax.vecmath.AxisAngle4f axisAngle = new javax.vecmath.AxisAngle4f(0f, 1f, 0f, 3.141592654f);
	    javax.vecmath.Matrix3f rotationMatrix = new javax.vecmath.Matrix3f();

	    float [] rotPoint =  new float[3];
            rotationMatrix.set(axisAngle);
            rotationMatrix.transform(point3f);
	    point3f.get(rotPoint);
	    // System.err.print(point[offset+0]+" "+point[offset+1]+" "+point[offset+2]);
	    point[offset+0] = rotPoint[0];
	    point[offset+1] = rotPoint[1];
	    point[offset+2] = rotPoint[2];
	    // System.err.println(" "+point[offset+0]+" "+point[offset+1]+" "+point[offset+2]);
    }
    private List<X3DNode> rotateJoints(X3D X3D0) {
	ArrayList<X3DNode> joints = traverseChildren(X3D0.getScene().getChildren(), HAnimJoint.class, 0);
	for (int ji = 0; ji < joints.size(); ji++) {
	    HAnimJoint joint = (HAnimJoint)joints.get(ji);
	    float [] center = joint.getCenter();
	    turnPoint(center, 0);
	    joint.setCenter(center);
	}
	return joints;
    }
    private List<X3DNode> swapJoints(X3D X3D0) {
	ArrayList<X3DNode> joints = traverseChildren(X3D0.getScene().getChildren(), HAnimJoint.class, 0);
	String LEFT = "l_";
	String RIGHT = "r_";
	for (int ji = 0; ji < joints.size(); ji++) {
	    HAnimJoint joint = (HAnimJoint)joints.get(ji);
	    String name = joint.getName();
	    String DEF = joint.getDEF();
	    String USE = joint.getUSE();
	    if (name != null) {
		if (name.startsWith(LEFT)) {
		    if (DEF != null && DEF.endsWith(name)) {
			int ni = DEF.indexOf(name);
			if (ni > 0) {
			    DEF = DEF.substring(0, ni)+RIGHT+DEF.substring(ni+2);
			    joint.setDEF(DEF);
			}
		    }
		    if (USE != null && USE.endsWith(name)) {
			int ui = USE.indexOf(name);
			if (ui > 0) {
			    USE = USE.substring(0, ui)+RIGHT+USE.substring(ui+2);
			    joint.setUSE(USE);
			}
		    }
		    name = RIGHT+name.substring(2);
	        } else if (name.startsWith(RIGHT)) {
		    if (DEF != null && DEF.endsWith(name)) {
			int ni = DEF.indexOf(name);
			if (ni > 0) {
			    DEF = DEF.substring(0, ni)+LEFT+DEF.substring(ni+2);
			    joint.setDEF(DEF);
			}
		    }
		    if (USE != null && USE.endsWith(name)) {
			int ui = USE.indexOf(name);
			if (ui > 0) {
			    USE = USE.substring(0, ui)+LEFT+USE.substring(ui+2);
			    joint.setUSE(USE);
			}
		    }
		    name = LEFT+name.substring(2);
	        }
	    }

	    joint.setName(name);
	}
	return joints;
    }
    private List<X3DNode> loadOrientations(X3D X3D0) {
	ArrayList<X3DNode> orientations = traverseChildren(X3D0.getScene().getChildren(), OrientationInterpolator.class, 0);
	for (int oi = 0; oi < orientations.size(); oi++) {
	    OrientationInterpolator interpolator = (OrientationInterpolator)orientations.get(oi);
	    // System.out.println(interpolator.toStringX3D());
	    float[] keys = interpolator.getKey();
	    float[] keyValues = interpolator.getKeyValue();

	    for (int k = 0; k < keys.length; k++) {
		    Motion motion = new Motion();
		    Integer id = oi;
		    motion.setId(id);

		    // OrienationInterpolator DEF
		    String orientationDef = interpolator.getDEF(); 
		    motion.setMotion(orientationDef);

		    // key position in list
		    Integer submove = (int)k;
		    motion.setSubmove(submove);
		    // key time
		    Double t = (double)keys[k];
		    motion.setTimeStart(t);

		    // rotation
		    Double x = (double)keyValues[k*4];
		    motion.setxAxis(x);
		    Double y = (double)keyValues[k*4+1];
		    motion.setyAxis(y);
		    Double z = (double)keyValues[k*4+2];
		    motion.setzAxis(z);
		    Double degrees = (double)keyValues[k*4+3];
		    motion.setDegrees(degrees);

		    // String character = m.group(5);
		    //motion.setCharacter(character);
		    this.currentRow = motion;
		    this.mode = rowMode.DATA;
		    this.model.addRow(this.currentRow, this.mode);
		    this.model.getModel().setRowCount(this.model.getModel().getRowCount());
	    }
	}
	return orientations;
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
		if (X3D0 != null) {
			System.err.println("Version: "+X3D0.getVersion());
			System.err.println("Profile: "+X3D0.getProfile());
			//List<X3DNode> positions = rotatePositions(X3D0);
			//System.err.println("read "+positions.size()+" positions");
			//List<X3DNode> orientations = rotateOrientations(X3D0);
			//System.err.println("read "+orientations.size()+" orientations");
			//List<X3DNode> joints = rotateJoints(X3D0);
			//System.err.println("read "+joints.size()+" joints");
			//List<X3DNode> joints2 = swapJoints(X3D0);
			//System.err.println("swapped joints");
			try {
				//List<X3DNode> coordinates = rotateCoordinates(X3D0);
				//System.err.println("read "+coordinates.size()+" coordinates");
				X3D0.toFileClassicVRML("rotated.x3dv");
				X3D0.toFileX3D("rotated.x3d");
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
                
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void loadX3dFile(GenericTableModel model, File selectedFile) {
        this.model = model;
	System.err.println("Opening file "+selectedFile);
	try {
		X3DLoaderDOM xmlLoader = new X3DLoaderDOM();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(true);
		// DocumentType doctype = domImpl.createDocumentType("X3D", "ISO//Web3D//DTD X3D 4.0//EN", "./x3d-4.0.dtd");

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(selectedFile);
		X3D X3D0 = (X3D)xmlLoader.toX3dModelInstance(document);
		if (X3D0 != null) {
			System.err.println("Version: "+X3D0.getVersion());
			System.err.println("Profile: "+X3D0.getProfile());
			//List<X3DNode> positions = rotatePositions(X3D0);
			//System.err.println("read "+positions.size()+" positions");
			//List<X3DNode> orientations = rotateOrientations(X3D0);
			//System.err.println("read "+orientations.size()+" orientations");
			//List<X3DNode> joints = rotateJoints(X3D0);
			//System.err.println("read "+joints.size()+" joints");
			//List<X3DNode> joints2 = swapJoints(X3D0);
			//System.err.println("swapped joints");
			try {
				//List<X3DNode> coordinates = rotateCoordinates(X3D0);
				//System.err.println("read "+coordinates.size()+" coordinates");
				X3D0.toFileClassicVRML("xrotated.x3dv");
				X3D0.toFileX3D("xrotated.x3d");
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
	} catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace(System.err);
        }
    }

private ArrayList<X3DNode> traverseChildren(ArrayList<X3DNode> children, Class clazz, int indent) {
	var collection = new ArrayList<X3DNode>();
	if (children != null) {
		for (int ci = 0; ci < children.size(); ci++) {
			collection.addAll(traverseChild(children.get(ci), clazz, indent+1));
		}
	}
	return collection;
}

private ArrayList<X3DNode> traverseChild(X3DNode child, Class clazz, int indent) {
	var collection = new ArrayList<X3DNode>();
	if (child != null) {
		Class<?> clazzchild = child.getClass();
		if (clazzchild.getName().equals(clazz.getName())) {
			// System.err.println(indent+" "+clazzchild.getName() +" == "+ clazz.getName());
			collection.add(child);
		}
		if (child instanceof ROUTE) {
			return collection;
		}
		try {
			String [] methods = new String [] {"getChildren", /*"getGeometry", "getAppearance", */ "getSkeleton", "getCoord", "getSkinCoord", "getSkin"};
			for (int m = 0; m < methods.length; m++) {
				try {
					Method method = clazzchild.getMethod(methods[m]);
					if (method != null) {
						// System.err.println(methods[m]);
						Object children = method.invoke(child);
						if (children != null) {
							if (children instanceof X3DNode[]) {
								for (int ci = 0; ci < ((X3DNode[])children).length; ci++) {
									collection.addAll(traverseChild(((X3DNode[])children)[ci], clazz, indent+1));
								}
								//break;
							} else if (children instanceof ArrayList) {
								for (int ci = 0; ci < ((ArrayList<X3DNode>)children).size(); ci++) {
									collection.addAll(traverseChild(((ArrayList<X3DNode>)children).get(ci), clazz, indent+1));
								}
								//break;
							} else if (children instanceof X3DGeometryNode) {
								collection.addAll(traverseChild((X3DGeometryNode)children, clazz, indent+1));
								//break;
							} else if (children instanceof X3DAppearanceNode) {
								collection.addAll(traverseChild((X3DAppearanceNode)children, clazz, indent+1));
								//break;
							} else if (children instanceof HAnimJoint) {
								collection.addAll(traverseChild((HAnimJoint)children, clazz, indent+1));
								//break;
							} else if (children instanceof HAnimHumanoid) {
								collection.addAll(traverseChild((HAnimJoint)children, clazz, indent+1));
								//break;
							} else if (children instanceof IndexedFaceSet) {
								System.err.println("IFS");
								collection.addAll(traverseChild((HAnimJoint)children, clazz, indent+1));
								//break;
							} else if (children instanceof Coordinate) {
								System.err.println("IFS");
								collection.addAll(traverseChild((Coordinate)children, clazz, indent+1));
								//break;
							} else {
								System.err.println(children.getClass().getName());
								for (int ci = 0; ci < ((X3DNode[])children).length; ci++) {
									collection.addAll(traverseChild(((X3DNode[])children)[ci], clazz, indent+1));
								}
								//break;
							}
						}
					}
				} catch (NoSuchMethodException e) {
					// System.err.println(child.getClass().getName()+":"+e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			// System.err.println(child.getClass().getName()+":"+e.getMessage());
		}

	}
	return collection;
}
    public enum rowMode { HEADER, DATA, OFF };
    private rowMode mode = rowMode.DATA;
    public static void main(String[] args) {
 
        TableLoadSave loadAndSave = new TableLoadSave();
        // loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File("C:/Users/john/jaminate/Jaminate/app/src/main/resources/New2TemplateNoBoxes.js"));
        // loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File("C:/Users/john/jaminate/Jaminate/app/src/main/resources/Leif8Final.js"));
        loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File("C:/Users/john/jaminate/Jaminate/app/src/main/resources/Leif8Final.x3d"));

        //loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File("C:/Users/john/jaminate/Jaminate/app/src/main/javascript/JinLOA4.js"));
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
        } else if (selectedFile.getName().endsWith(".x3d")) {
            loadX3dFile(model, selectedFile);
        }
    }
    public void loadTxtFile(GenericTableModel model, File selectedFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            Pattern pattern = Pattern.compile("([-0-9\\.e\\+]+)[ \\t]+([-0-9\\.e\\+]+)[ \\t]+([-0-9\\.e\\+]+),[ \\t]+#?\\(([0-9\\.]+)\\)[ \\t]+#?\\(([A-Za-z]*)_([A-Za-z]+)([0-9]*)\\)");
            String str;
	    Integer id = 0;
            while ((str = br.readLine()) != null) {
                Matcher m = pattern.matcher(str);
                //System.err.println(m.toString());
                if (m.matches()) {
                    System.err.println(str+"# found row ");
                    Motion motion = new Motion();
                    id++;
		    motion.setId(id);
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
		    if (!"".equals(m.group(7))) {
			    Integer submove = Integer.valueOf(m.group(7));
			    motion.setSubmove(submove);
		    }
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
                String character = model.getModel().getValueAt(r, 2).toString(); // get character name
                Double time = Double.valueOf(model.getModel().getValueAt(r, 3).toString()); // get time
                String mainMotion = model.getModel().getValueAt(r, 4).toString(); // primary motion
                Integer subMotion = Integer.valueOf(model.getModel().getValueAt(r, 5).toString()); // secondary submotion
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
			/* for WinterAndSpring 
			move = "0"+move;
			*/
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
		    json += "\""+pair3.getKey()+"\": [\""+(character.length() > 0 ? character+"_" : "")+String.join("\", \""+(character.length() > 0 ? character+"_" : ""), pair3.getValue())+"\"]";
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
	    System.err.println("Starting");
            Process p = Runtime.getRuntime().exec(new String[] { "js", "--experimental-options", "--polyglot", "--vm.Djs.allowAllAccess=true", "--vm.Xss1g", "--vm.Xmx4g", "--jvm", "--vm.classpath=C:/Users/john/jaminate/Jaminate/app/lib/X3DJSAIL.4.0.full.jar;C:/Users/john/jaminate/Jaminate/app/lib/saxon-he-12.1.jar", "src/main/resources/takesX3DJSAIL.js"});
	                                                       // js    --experimental-options    --polyglot    --vm.Djs.allowAllAccess=true    --vm.Xss1g    --vm.Xmx4g    --jvm    --vm.classpath='C:/Users/john/jaminate/Jaminate/app/lib/X3DJSAIL.4.0.full.jar;C:/Users/john/jaminate/Jaminate/app/lib/saxon-he-12.1.jar"    src/main/resources/takesX3DJSAIL.js

		    // "node", "-cp", "./lib/X3DJSAIL.4.0.full.jar:./lib/saxon-he-12.1.jar:.", "takes.js"});
            nopw = new PrintWriter(p.getOutputStream());
	    System.err.println("Writing JSON");
            nopw.write(json);
            is = p.getInputStream();
            nopw.close();
            nopw = null;
            String animationChain = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
	    System.err.println("Chain is "+animationChain);
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
