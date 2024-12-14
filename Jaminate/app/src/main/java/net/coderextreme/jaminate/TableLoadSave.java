package net.coderextreme.jaminate;

//import com.oracle.truffle.js.lang.JavaScriptLanguageProvider;
//import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
//import javax.script.Compilable;
//import javax.script.CompiledScript;
//import javax.script.Invocable;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineFactory;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;  // for exceptions
import java.lang.Override;
import java.lang.reflect.*;  // for exceptions
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.coderextreme.remove.NewOrientationInterpolator;
import net.coderextreme.remove.NewROUTE;
import net.coderextreme.remove.Remove;
import org.ccil.cowan.tagsoup.Parser;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.web3d.x3d.jsail.Core.ROUTE;
import org.web3d.x3d.jsail.Core.IS;
import org.web3d.x3d.jsail.Core.ProtoInstance;
import org.web3d.x3d.jsail.Core.ProtoBody;
import org.web3d.x3d.jsail.Core.ProtoDeclare;
import org.web3d.x3d.jsail.Core.ProtoInterface;
import org.web3d.x3d.jsail.Core.connect;
import org.web3d.x3d.jsail.Core.field;
import org.web3d.x3d.jsail.Core.fieldValue;
import org.web3d.x3d.jsail.Text.Text;
import org.web3d.x3d.jsail.Core.X3D;
import org.web3d.x3d.jsail.EnvironmentalSensor.ProximitySensor;
import org.web3d.x3d.jsail.Navigation.Billboard;
import org.web3d.x3d.jsail.fields.SFRotation;
import org.web3d.x3d.jsail.fields.MFString;
import org.web3d.x3d.jsail.fields.MFVec3f;
import org.web3d.x3d.jsail.Geometry3D.IndexedFaceSet;
import org.web3d.x3d.jsail.Grouping.Group;
import org.web3d.x3d.jsail.Grouping.Transform;
import org.web3d.x3d.jsail.HAnim.HAnimHumanoid;
import org.web3d.x3d.jsail.HAnim.HAnimJoint;
import org.web3d.x3d.jsail.HAnim.HAnimSegment;
import org.web3d.x3d.jsail.HAnim.HAnimSite;
import org.web3d.x3d.jsail.Interpolation.OrientationInterpolator;
import org.web3d.x3d.jsail.Interpolation.PositionInterpolator;
import org.web3d.x3d.jsail.Rendering.ColorRGBA;
import org.web3d.x3d.jsail.Rendering.Color;
import org.web3d.x3d.jsail.Rendering.LineSet;
import org.web3d.x3d.jsail.Rendering.Coordinate;
import org.web3d.x3d.jsail.Shape.Appearance;
import org.web3d.x3d.jsail.Shape.Material;
import org.web3d.x3d.jsail.Shape.Shape;
import org.web3d.x3d.jsail.Text.FontStyle;
import org.web3d.x3d.jsail.Texturing.ImageTexture;
import org.web3d.x3d.jsail.Time.TimeSensor;
import org.web3d.x3d.jsail.X3DConcreteElement;
import org.web3d.x3d.jsail.X3DConcreteNode;
import org.web3d.x3d.jsail.X3DLoaderDOM;
import org.web3d.x3d.jsail.PointingDeviceSensor.TouchSensor;
import org.web3d.x3d.sai.Core.X3DNode;
import org.web3d.x3d.sai.Grouping.X3DGroupingNode;
import org.web3d.x3d.sai.Rendering.X3DGeometryNode;
import org.web3d.x3d.sai.Rendering.X3DColorNode;
import org.web3d.x3d.sai.Shape.X3DAppearanceNode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

		    // OrientationInterpolator DEF
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
				concatenateOrientationInterpolators(X3D0);
				X3D0.toFileClassicVRML("jsconcatenated.x3dv");
				X3D0.toFileX3D("jsconcatenated.x3d");
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
    private ProtoDeclare addBillboardProto() {
	    ProtoDeclare pd = new ProtoDeclare().setName("TouchAndBillboard")
		    .setProtoInterface(new ProtoInterface()
		    	.addField(new field().setType("MFString").setName("string").setAccessType(field.ACCESSTYPE_INPUTOUTPUT).setValue("\"-1\""))
		    	.addField(new field().setType("SFString").setName("description").setAccessType(field.ACCESSTYPE_INPUTOUTPUT).setValue("HAnim Feature Point Description")))
		    .setProtoBody(new ProtoBody()
		    	.addChild(new Group()
			    .addChild(new TouchSensor()
				    .setIS(new IS()
					    .addConnect(new connect().setNodeField("description").setProtoField("description"))))
			    .addChild(new Shape()
				.setGeometry(new IndexedFaceSet()
					.setCreaseAngle(0.5)
					.setSolid(false)
					.setCoordIndex(new int[]{0, 1, 2, -1, 0, 2, 3, -1, 0, 3, 4, -1, 0, 4, 1, -1, 5, 2, 1, -1, 5, 3, 2, -1, 5, 4, 3, -1, 5, 1, 4, -1})
					.setColor(new ColorRGBA().setColor(new float[]{1, 1, 0, 1, 1, 1, 0, 0.1f, 1, 1, 0, 1, 1, 1, 0, 0.1f, 1, 1, 0, 1, 1, 1, 0, 0.1f}))
					.setCoord(new Coordinate().setPoint(new float[]{0, 0.01f, 0, -0.01f, 0, 0, 0, 0, 0.01f, 0.01f, 0, 0, 0, 0, -0.01f, 0, -0.01f, 0})))
				.setAppearance(new Appearance()
					.setMaterial(new Material()
						.setDiffuseColor(new float[]{1, 1, 1})
						.setTransparency(0))))
			    .addChild(new Billboard()
				.setAxisOfRotation(new float[]{0, 0, 0})
				.addChild(new Shape()
					.setGeometry(new Text()
						.setFontStyle(new FontStyle()
								.setSize(0.035))
						.setIS(new IS()
						    .addConnect(new connect().setNodeField("string").setProtoField("string"))))))));
	    return pd;

    }
    private void addBillboard(X3D X3D0) {
	// prepend the PROTO
 	ArrayList children = X3D0.getScene().getChildren();
	children.add(0, addBillboardProto());
 	X3D0.getScene().setChildren(children);
	ArrayList sites = traverseChildren(X3D0.getScene().getChildren(), HAnimSite.class, 0);
	for (int s = 0; s < sites.size(); s++) {
		HAnimSite site = (HAnimSite)sites.get(s);
		int siteIndex = HAnimSite.getNameIndex(site.getName());
		String siteIndexOrName = ""+siteIndex;
		if (siteIndex == -1) {
			siteIndexOrName = site.getName();
		}
		site.addChild(new ProtoInstance().setName("TouchAndBillboard")
				.addFieldValue(new fieldValue().setName("description").setValue("HAnimSite "+siteIndexOrName+" "+site.getDEF()))
				.addFieldValue(new fieldValue().setName("string").setValue("\""+siteIndexOrName+"\"")));
	}
    }
    private void addBillboardNoProto(X3D X3D0) {
	ArrayList sites = traverseChildren(X3D0.getScene().getChildren(), HAnimSite.class, 0);
	for (int s = 0; s < sites.size(); s++) {
		HAnimSite site = (HAnimSite)sites.get(s);
		int siteIndex = HAnimSite.getNameIndex(site.getName());
		String siteIndexOrName = ""+siteIndex;
		if (siteIndex == -1) {
			siteIndexOrName = site.getName();
		}
		site.addChild(new TouchSensor().setDescription("HAnimSite "+siteIndexOrName+" "+site.getDEF()))
			    .addChild(new Shape()
				.setGeometry(new IndexedFaceSet()
					.setCreaseAngle(0.5)
					.setSolid(false)
					.setCoordIndex(new int[]{0, 1, 2, -1, 0, 2, 3, -1, 0, 3, 4, -1, 0, 4, 1, -1, 5, 2, 1, -1, 5, 3, 2, -1, 5, 4, 3, -1, 5, 1, 4, -1})
					.setColor(new ColorRGBA().setColor(new float[]{1, 1, 0, 1, 1, 1, 0, 0.1f, 1, 1, 0, 1, 1, 1, 0, 0.1f, 1, 1, 0, 1, 1, 1, 0, 0.1f}))
					.setCoord(new Coordinate().setPoint(new float[]{0, 0.01f, 0, -0.01f, 0, 0, 0, 0, 0.01f, 0.01f, 0, 0, 0, 0, -0.01f, 0, -0.01f, 0})))
				.setAppearance(new Appearance()
					.setMaterial(new Material()
						.setDiffuseColor(new float[]{1, 1, 1})
						.setTransparency(0))))
			    .addChild(new Billboard()
				.setAxisOfRotation(new float[]{0, 0, 0})
				.addChild(new Shape()
					.setGeometry(new Text()
						.setFontStyle(new FontStyle()
								.setSize(0.035))
						.setString(siteIndexOrName))));
	}
    }
    private void addSegmentGeometry(X3D X3D0) {
	// prepend the PROTO
 	ArrayList children = X3D0.getScene().getChildren();
 	X3D0.getScene().setChildren(children);
	ArrayList<X3DNode> joints = traverseChildren(X3D0.getScene().getChildren(), HAnimJoint.class, 0);
	System.err.println("joints "+ joints.size() + " " + joints);
	for (int j = 0; j < joints.size(); j++) {
		HAnimJoint joint = (HAnimJoint)joints.get(j);
		X3DNode[] segments = joint.getChildren();
		for (int s = 0; s < segments.length; s++) {
			Object o = segments[s];
			if (o instanceof HAnimSegment) {
				HAnimSegment segment = (HAnimSegment)o;
				System.err.println("segment " + s + " " + segment);
				System.err.println("joint "+ j + " "+ joint);
				MFVec3f point = new MFVec3f(joint.getCenter());
				// TODO should be child
				if (joint.getParent() instanceof HAnimJoint) {
					point.append(((HAnimJoint)joint.getParent()).getCenter());
					segment.addChild(new Shape()
						.setGeometry(new LineSet().setVertexCount(new int[]{2})
							.setCoord(new Coordinate().setPoint(point))
						.setColor(new ColorRGBA().setColor(new float[]{1, 1, 0, 1, 1, 1, 0, 1}))));
				}
			}
		}

	}
    }
    private void loadX3dFile(GenericTableModel model, File selectedFile) {
        this.model = model;
	System.err.println("Opening file "+selectedFile);
	try {
		X3DLoaderDOM xmlLoader = new X3DLoaderDOM();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// dbf.setNamespaceAware(true);
		// dbf.setValidating(true);
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
			//List<X3DNode> coordinates = rotateCoordinates(X3D0);
			//System.err.println("read "+coordinates.size()+" coordinates");
			try {
				// concatenateX3DOrientationInterpolators(X3D0);
				// X3D0.toFileClassicVRML("x3dconcatenated.x3dv");
				// System.err.println("writing XML");
				// X3D0.toFileX3D("x3dconcatenated.x3d");
				// unUSE(X3D0);
				// unUSE(X3D0);
				// X3D0.toFileX3D("unuse.x3d");
				// addBillboardNoProto(X3D0);
				// X3D0.toFileX3D("billboardednoproto.x3d");
				// addSegmentGeometry(X3D0);
				// X3D0.toFileX3D("billboardedsegment.x3d");
				createUSENodes(X3D0);
				X3D0.toFileX3D("useHAnim.x3d");
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
            System.err.println("Number of rows in reader "+model.getModel().getRowCount());
	} catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace(System.err);
        }
    }

private void unUSE(X3D X3D0) {
	ArrayList imagetextures = traverseChildren(X3D0.getScene().getChildren(), ImageTexture.class, 0);
	ArrayList colors = traverseChildren(X3D0.getScene().getChildren(), Color.class, 0);
	System.err.println("Found "+colors.size()+" colors");
	ArrayList materials = traverseChildren(X3D0.getScene().getChildren(), Material.class, 0);
	System.err.println("Found "+materials.size()+" materials");
	ArrayList appearances = traverseChildren(X3D0.getScene().getChildren(), Appearance.class, 0);
	ArrayList fontStyles = traverseChildren(X3D0.getScene().getChildren(), FontStyle.class, 0);
	ArrayList shapes = traverseChildren(X3D0.getScene().getChildren(), Shape.class, 0);
	ArrayList transform = traverseChildren(X3D0.getScene().getChildren(), Transform.class, 0);
	ArrayList groups = traverseChildren(X3D0.getScene().getChildren(), Group.class, 0);
	ArrayList linesets = traverseChildren(X3D0.getScene().getChildren(), LineSet.class, 0);
	ArrayList indexedfacesets = traverseChildren(X3D0.getScene().getChildren(), IndexedFaceSet.class, 0);
	ArrayList[] lists = new ArrayList[] { imagetextures, colors, materials, appearances, fontStyles, shapes, transform, groups, linesets, indexedfacesets };
	// populate defs map
	HashMap defs = new HashMap();
	for (int l = 0; l < lists.length; l++) {
		ArrayList list = lists[l];
		Iterator nodeitr = list.iterator();
		while (nodeitr.hasNext()) {
			Object node = nodeitr.next();
			X3DConcreteNode x3dDEFNode = (X3DConcreteNode)node;
			String def = x3dDEFNode.getDEF();
			if (def != null && !def.trim().equals("")) {
				System.err.println("Found "+def);
				defs.put(def, x3dDEFNode);
			}
		}
	}
	int counter = 0;
	for (int l = 0; l < lists.length; l++) {
		ArrayList list = lists[l];
		Iterator nodeitr = list.iterator();
		while (nodeitr.hasNext()) {
			Object node = nodeitr.next();
			X3DConcreteNode x3dUSENode = (X3DConcreteNode)node;
			// check to see if this node is one to copy over fields
			String use = x3dUSENode.getUSE();
			if (use != null && !use.trim().equals("")) {
				// System.err.println("Replacing "+use);
				X3DConcreteNode x3dDEFNode = (X3DConcreteNode)(defs.get(use));
				x3dUSENode.setUSE(null);
				// x3dUSENode.setDEF(use+"_"+counter);
				counter++;
				Class clazz = x3dDEFNode.getClass();
				System.err.println("Class is "+clazz.getCanonicalName());
				Method[] methods = clazz.getMethods();
				// create map to hold field values
				HashMap valueMap = new HashMap();
				for (int m = 0; m < methods.length; m++) {
					Method method = methods[m];
					String methodName = method.getName();
					if (methodName.startsWith("get") && method.getParameterCount() == 0) {
						try {
							String retTypeName = method.getReturnType().getName();
							// populate field value map
							if (retTypeName.equals("boolean") || retTypeName.equals("float") || retTypeName.equals("int")) {
								valueMap.put(methodName, method.invoke(x3dDEFNode));
							} else {
								valueMap.put(methodName, method.getReturnType().cast(method.invoke(x3dDEFNode)));
							}
							// System.err.println("Retrieved with "+methodName+" "+valueMap.get(methodName));
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace(System.err);
						}
					}
				}
				// populate USE node fields
				for (int m = 0; m < methods.length; m++) {
					Method method = methods[m];
					String methodName = method.getName();
					if (methodName.startsWith("set") && !methodName.equals("setUSE") && !methodName.equals("setDEF")) {
						try {
							String getMethodName = "g"+methodName.substring(1);
							System.err.println("Saving with "+methodName+" "+valueMap.get(getMethodName));
							Object o = valueMap.get(getMethodName);
							if (o != null) {
								if (method.getParameterTypes()[0].isAssignableFrom(o.getClass())) {
									System.err.println("Setting "+o.toString()+" with "+methodName);
									if (!"".equals(o.toString().trim())) {
										method.invoke(x3dUSENode, o);
										System.err.println("Set "+o.toString()+" with "+methodName);
									} else {
										System.err.println("Didn't set  "+o.toString());
									}
								} else {
									System.err.println("Bad types with parameter 0 type "+method.getParameterTypes()[0].getCanonicalName()+" not assignable from "+o.getClass().getCanonicalName()+" type with "+clazz.getCanonicalName()+"."+methodName);
								}
							} else {
								System.err.println("Value from "+getMethodName+" is null");
							}
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace(System.err);
						}
					}
				}
				replaceDEFs(x3dUSENode);
			}
		}
	}
}

private void replaceDEFs(X3DConcreteNode x3dUSENode) {
	ArrayList imagetextures = traverseChild((X3DNode)x3dUSENode, ImageTexture.class, 0);
	ArrayList colors = traverseChild((X3DNode)x3dUSENode, Color.class, 0);
	System.err.println("Found "+colors.size()+" colors");
	ArrayList materials = traverseChild((X3DNode)x3dUSENode, Material.class, 0);
	System.err.println("Found "+materials.size()+" materials");
	ArrayList appearances = traverseChild((X3DNode)x3dUSENode, Appearance.class, 0);
	ArrayList fontStyles = traverseChild((X3DNode)x3dUSENode, FontStyle.class, 0);
	ArrayList shapes = traverseChild((X3DNode)x3dUSENode, Shape.class, 0);
	ArrayList transform = traverseChild((X3DNode)x3dUSENode, Transform.class, 0);
	ArrayList groups = traverseChild((X3DNode)x3dUSENode, Group.class, 0);
	ArrayList linesets = traverseChild((X3DNode)x3dUSENode, LineSet.class, 0);
	ArrayList indexedfacesets = traverseChild((X3DNode)x3dUSENode, IndexedFaceSet.class, 0);
	ArrayList[] lists = new ArrayList[] { imagetextures, colors, materials, appearances, fontStyles, shapes, transform, groups, linesets, indexedfacesets };
	for (int l = 0; l < lists.length; l++) {
		ArrayList list = lists[l];
		Iterator nodeitr = list.iterator();
		while (nodeitr.hasNext()) {
			Object node = nodeitr.next();
			X3DConcreteNode x3dDEFNode = (X3DConcreteNode)node;
			String def = x3dDEFNode.getDEF();
			if (def != null && !def.trim().equals("")) {
				x3dDEFNode.setUSE(def);
				x3dDEFNode.setDEF(null);
			}
		}
	}
}

private void createUSENodes(X3D X3D0) {
	ArrayList joints = traverseChildren(X3D0.getScene().getChildren(), HAnimJoint.class, 0);
	ArrayList segments = traverseChildren(X3D0.getScene().getChildren(), HAnimSegment.class, 0);
	ArrayList sites = traverseChildren(X3D0.getScene().getChildren(), HAnimSite.class, 0);
	ArrayList humanoids = traverseChildren(X3D0.getScene().getChildren(), HAnimHumanoid.class, 0);
	HAnimHumanoid humanoid = (HAnimHumanoid)humanoids.get(0);
	for (int i = 0; i < joints.size(); i++) {
		HAnimJoint existing = (HAnimJoint)joints.get(i);
        	X3DConcreteNode joint = new HAnimJoint().setUSE(existing.getDEF()).setContainerFieldOverride("joints");
		humanoid.addJoints((HAnimJoint)joint);
	}
	for (int i = 0; i < segments.size(); i++) {
		HAnimSegment existing = (HAnimSegment)segments.get(i);
        	X3DConcreteNode segment = new HAnimSegment().setUSE(existing.getDEF()).setContainerFieldOverride("segments");
		humanoid.addSegments((HAnimSegment)segment);
	}
	for (int i = 0; i < sites.size(); i++) {
		HAnimSite existing = (HAnimSite)sites.get(i);
        	X3DConcreteNode site = new HAnimSite().setUSE(existing.getDEF()).setContainerFieldOverride("sites");
		humanoid.addSites((HAnimSite)site);
	}
}
private void concatenateOrientationInterpolators(X3D X3D0) {
	ArrayList routes = traverseChildren(X3D0.getScene().getChildren(), NewROUTE.class, 0);
	LinkedHashSet<OrientationInterpolator> oldois = new LinkedHashSet<OrientationInterpolator>();
	LinkedHashSet<OrientationInterpolator> newois = new LinkedHashSet<OrientationInterpolator>();
	LinkedHashSet<ROUTE> oldroutes = new LinkedHashSet<ROUTE>();
	float cycleInterval = 0.0f;
	LinkedHashSet<TimeSensor> oldsensors = new LinkedHashSet<TimeSensor>();
	System.err.println("route "+routes.size());
	LinkedHashMap<TimeSensor, TimeSensor> timing = new LinkedHashMap<TimeSensor, TimeSensor>();
	LinkedHashMap<HAnimJoint, ArrayList<OrientationInterpolator>> joint2oi = new LinkedHashMap<HAnimJoint, ArrayList<OrientationInterpolator>>();
	LinkedHashMap<OrientationInterpolator, ArrayList<TimeSensor>> oi2time = new LinkedHashMap<OrientationInterpolator, ArrayList<TimeSensor>>();

        ProximitySensor prox = new ProximitySensor();
	prox.setDEF("ShinyNewActivate");
	prox.setSize(new float[] { 1000000, 1000000, 1000000});
	X3D0.getScene().addChild(prox);

	TimeSensor sensor = new TimeSensor();
	sensor.setDEF("ShinyNewTimer");
	// sensor.setCycleInterval(100.0f);
	sensor.setEnabled(true);
	sensor.setLoop(true);
	X3D0.getScene().addChild(sensor);

	ROUTE newRoute3 = new ROUTE();
	newRoute3.setFromNode("ShinyNewActivate");
	newRoute3.setFromField("enterTime");
	newRoute3.setToNode("ShinyNewTimer");
	newRoute3.setToField("set_startTime");
	X3D0.getScene().addChild(newRoute3);

	// first, go through all the routes, collecting up information
	for (int ri = 0; ri < routes.size(); ri++) {
		NewROUTE route = (NewROUTE)routes.get(ri);
		// System.err.println("ROUTE "+ri);

		X3DConcreteElement from = X3D0.findNodeByDEF(route.getFromNode());
		if (from == null) {
			from = X3D0.findElementByNameValue(route.getFromNode());
       		}

		X3DConcreteElement to = X3D0.findNodeByDEF(route.getToNode());
		if (to == null) {
			to = X3D0.findElementByNameValue(route.getToNode());
       		}
		if (from instanceof OrientationInterpolator  && to instanceof HAnimJoint) {
			// System.err.println("from "+from+" to "+to+" "+route.getFromNode()+"."+route.getFromField()+" TO "+route.getToNode()+"."+route.getToField());
			ArrayList<OrientationInterpolator> ois = joint2oi.get(to);
			if (ois == null) {
				ois = new ArrayList<OrientationInterpolator>();
			}
			ois.add((OrientationInterpolator)from);
			oldois.add((OrientationInterpolator)from);
			oldroutes.add((ROUTE)route);
			joint2oi.put((HAnimJoint)to, ois);
		} else if (from instanceof TimeSensor && to instanceof OrientationInterpolator) {
			// System.err.println("from "+from+" to "+to+" "+route.getFromNode()+"."+route.getFromField()+" TO "+route.getToNode()+"."+route.getToField());
			ArrayList<TimeSensor> sensors = oi2time.get(to);
			if (sensors == null) {
				sensors = new ArrayList<TimeSensor>();
			}
			sensors.add((TimeSensor)from);
			oldois.add((OrientationInterpolator)to);
			oldroutes.add((ROUTE)route);
			oldsensors.add((TimeSensor)from);
			oi2time.put((OrientationInterpolator)to, sensors);
		} else if (from instanceof TimeSensor && to instanceof TimeSensor && "stopTime_changed".equals(route.getFromField()) && "set_startTime".equals(route.getToField())) {
			System.err.println("from "+from+" to "+to+" "+route.getFromNode()+"."+route.getFromField()+" TO "+route.getToNode()+"."+route.getToField());
			timing.put((TimeSensor)from, (TimeSensor)to);
			oldsensors.add((TimeSensor)from);
			oldsensors.add((TimeSensor)to);
			if (from != null) {
				((TimeSensor)from).setEnabled(false);
				cycleInterval += ((TimeSensor)from).getCycleInterval();
			} else {
				System.err.println("OOPS, from TimeSensor is null");
			}
			if (to != null) {
				((TimeSensor)to).setEnabled(false);
			} else {
				System.err.println("OOPS, to TimeSensor is null");
			}
		}
	}
	TimeSensor first = null;
	TimeSensor next = null;
	// then go through all the joints and interpolators, collecting up keys and values for new OrientationInterpolators
	for (Map.Entry<HAnimJoint, ArrayList<OrientationInterpolator>> joint2oiEntry : joint2oi.entrySet()) {
		ArrayList<OrientationInterpolator> oij = joint2oiEntry.getValue();
		HAnimJoint joint = joint2oiEntry.getKey();
		OrientationInterpolator newOI = new OrientationInterpolator();
		ArrayList<float[]> newKeys = new ArrayList<float[]>();
		ArrayList<float[]> newKeyValues = new ArrayList<float[]>();
		StringBuffer oiName = new StringBuffer();
		for (Map.Entry<OrientationInterpolator, ArrayList<TimeSensor>> oi2timeEntry : oi2time.entrySet()) {
			OrientationInterpolator oi = oi2timeEntry.getKey();
			ArrayList<TimeSensor> sensors = oi2timeEntry.getValue();
			first = sensors.get(0);
			next = first;
			LinkedHashSet<TimeSensor> set = new LinkedHashSet<TimeSensor>();
			// now traverse through all of the sensors for this interpolator
			do {

				// this OrientationInterpolator is part of this sensor.
				if (oij.contains(oi)) {
					newKeys.add(oi.getKey());
					newKeyValues.add(oi.getKeyValue());
					oiName.append(oi.getDEF());
					/*
				} else {
					newKeys.add(null);
					newKeyValues.add(null);
					oiName.append("");
					*/
				}
				set.add(next);
				next = timing.get(next);
			} while (!set.contains(next));
		}

		// now actually add keys and values to the new orientation interpolator
                Integer start = 0;
		Integer animCount = 0;
		SFRotation currot = null;
		for (int ik = 0; ik < newKeys.size(); ik++) {
			if (newKeys.get(ik) == null) {
				newOI.addKey(start);
			} else {
				for (float k : newKeys.get(ik)) {
					newOI.addKey(k+start);
				}
				animCount++;
			}
			if (newKeyValues.get(ik) == null) {
				if (currot == null) {
					currot = new SFRotation(0, 0, 1, 0);
				}
				newOI.addKeyValue(currot);
			} else {
				float [] keyValue = newKeyValues.get(ik);
				for (int kv = 0; kv < keyValue.length; kv += 4) {
					if (keyValue[kv] == 0 && keyValue[kv+1] == 0 && keyValue[kv+2] == 0) {
						System.err.println("Error, axis is 0,0,0, changing to 0,0,1 (default)");
						keyValue[kv+2] = 1;
					}
					currot = new SFRotation(
							keyValue[kv],
							keyValue[kv + 1],
							keyValue[kv + 2],
							keyValue[kv + 3]);
					newOI.addKeyValue(currot);
				}
			}
			start += 1; 
		}

		newOI.setDEF(oiName.toString());

		// make the keys in the interpolator go from 0 to 1
		float [] key = newOI.getKey();
		float end = key[key.length-1];
		if (end != 0) {
			for (int i = 0; i < key.length; i++) {
				key[i] = key[i]/end;
			}
			newOI.setKey(key);
		}



		// search for existing interpolators
		Iterator<OrientationInterpolator> itr = newois.iterator();
		boolean found = false;
		if (animCount == 1) {  // There's only one interpolator in this list, so no need to provide a new one
			found = true;
			// System.err.println("Found OI "+newOI.getDEF());
		}
		while (itr.hasNext()) {
			OrientationInterpolator present = itr.next();
			if (newOI.getDEF().equals(present.getDEF())) {
				found = true;
				// System.err.println("Found OI "+newOI.getDEF());
			}
		}

       		ArrayList copyois = traverseChildren(X3D0.getScene().getChildren(), NewOrientationInterpolator.class, 0);
		Iterator<OrientationInterpolator> citr = (Iterator<OrientationInterpolator>)(newois.iterator());
		while (citr.hasNext()) {
			OrientationInterpolator present = citr.next();
			if (newOI.getDEF().equals(present.getDEF())) {
				found = true;
				// System.err.println("Found OI "+newOI.getDEF());
			}
		}

		// if the interpolator wasn't found above, add it
		if (!found) {
			X3D0.getScene().addChild(newOI);
			newois.add(newOI);
		}

		ROUTE newRoute = new ROUTE();
		newRoute.setFromNode(oiName.toString());
		newRoute.setFromField("value_changed");
		newRoute.setToNode(joint.getDEF());
		newRoute.setToField("set_rotation");
		X3D0.getScene().addChild(newRoute);

		ROUTE newRoute2 = new ROUTE();
		newRoute2.setFromNode(sensor.getDEF());
		newRoute2.setFromField("fraction_changed");
		newRoute2.setToNode(oiName.toString());
		newRoute2.setToField("set_fraction");
		X3D0.getScene().addChild(newRoute2);
	}
	sensor.setCycleInterval(cycleInterval);


	//Remove rem = new Remove();
	//rem.removeChildren(X3D0.getScene().getChildren(), oldroutes);
	//rem.removeChildren(X3D0.getScene().getChildren(), oldois);
	//rem.removeChildren(X3D0.getScene().getChildren(), oldsensors);
}
private void concatenateX3DOrientationInterpolators(X3D X3D0) {
	ArrayList routes = traverseChildren(X3D0.getScene().getChildren(), ROUTE.class, 0);
	LinkedHashSet<OrientationInterpolator> oldois = new LinkedHashSet<OrientationInterpolator>();
	LinkedHashSet<OrientationInterpolator> newois = new LinkedHashSet<OrientationInterpolator>();
	LinkedHashSet<ROUTE> oldroutes = new LinkedHashSet<ROUTE>();
	float cycleInterval = 0.0f;
	LinkedHashSet<TimeSensor> oldsensors = new LinkedHashSet<TimeSensor>();
	System.err.println("route "+routes.size());
	LinkedHashMap<TimeSensor, TimeSensor> timing = new LinkedHashMap<TimeSensor, TimeSensor>();
	LinkedHashMap<HAnimJoint, ArrayList<OrientationInterpolator>> joint2oi = new LinkedHashMap<HAnimJoint, ArrayList<OrientationInterpolator>>();
	LinkedHashMap<OrientationInterpolator, ArrayList<TimeSensor>> oi2time = new LinkedHashMap<OrientationInterpolator, ArrayList<TimeSensor>>();

        ProximitySensor prox = new ProximitySensor();
	prox.setDEF("ShinyActivate");
	prox.setSize(new float[] { 1000000, 1000000, 1000000});
	X3D0.getScene().addChild(prox);

	TimeSensor sensor = new TimeSensor();
	sensor.setDEF("ShinyTimer");
	// sensor.setCycleInterval(100.0f);
	sensor.setEnabled(true);
	sensor.setLoop(true);
	X3D0.getScene().addChild(sensor);

	ROUTE newRoute3 = new ROUTE();
	newRoute3.setFromNode("ShinyActivate");
	newRoute3.setFromField("enterTime");
	newRoute3.setToNode("ShinyTimer");
	newRoute3.setToField("set_startTime");
	X3D0.getScene().addChild(newRoute3);

	// first, go through all the routes, collecting up information
	for (int ri = 0; ri < routes.size(); ri++) {
		ROUTE route = (ROUTE)routes.get(ri);
		// System.err.println("ROUTE "+ri);

		X3DConcreteElement from = X3D0.findNodeByDEF(route.getFromNode());
		if (from == null) {
			from = X3D0.findElementByNameValue(route.getFromNode());
       		}

		X3DConcreteElement to = X3D0.findNodeByDEF(route.getToNode());
		if (to == null) {
			to = X3D0.findElementByNameValue(route.getToNode());
       		}
		if (from instanceof OrientationInterpolator  && to instanceof HAnimJoint) {
			// System.err.println("from "+from+" to "+to+" "+route.getFromNode()+"."+route.getFromField()+" TO "+route.getToNode()+"."+route.getToField());
			ArrayList<OrientationInterpolator> ois = joint2oi.get(to);
			if (ois == null) {
				ois = new ArrayList<OrientationInterpolator>();
			}
			ois.add((OrientationInterpolator)from);
			oldois.add((OrientationInterpolator)from);
			oldroutes.add((ROUTE)route);
			joint2oi.put((HAnimJoint)to, ois);
		} else if (from instanceof TimeSensor && to instanceof OrientationInterpolator) {
			// System.err.println("from "+from+" to "+to+" "+route.getFromNode()+"."+route.getFromField()+" TO "+route.getToNode()+"."+route.getToField());
			ArrayList<TimeSensor> sensors = oi2time.get(to);
			if (sensors == null) {
				sensors = new ArrayList<TimeSensor>();
			}
			sensors.add((TimeSensor)from);
			oldois.add((OrientationInterpolator)to);
			oldroutes.add((ROUTE)route);
			oldsensors.add((TimeSensor)from);
			oi2time.put((OrientationInterpolator)to, sensors);
		} else if (from instanceof TimeSensor && to instanceof TimeSensor && "stopTime_changed".equals(route.getFromField()) && "set_startTime".equals(route.getToField())) {
			System.err.println("from "+from+" to "+to+" "+route.getFromNode()+"."+route.getFromField()+" TO "+route.getToNode()+"."+route.getToField());
			timing.put((TimeSensor)from, (TimeSensor)to);
			oldsensors.add((TimeSensor)from);
			oldsensors.add((TimeSensor)to);
			if (from != null) {
				((TimeSensor)from).setEnabled(false);
				cycleInterval += ((TimeSensor)from).getCycleInterval();
			} else {
				System.err.println("OOPS, from TimeSensor is null");
			}
			if (to != null) {
				((TimeSensor)to).setEnabled(false);
			} else {
				System.err.println("OOPS, to TimeSensor is null");
			}
		}
	}
	TimeSensor first = null;
	TimeSensor next = null;
	// then go through all the joints and interpolators, collecting up keys and values for new OrientationInterpolators
	LinkedHashSet<String> handledOIDefs = new LinkedHashSet<String>();
	for (Map.Entry<HAnimJoint, ArrayList<OrientationInterpolator>> joint2oiEntry : joint2oi.entrySet()) {
		ArrayList<OrientationInterpolator> oij = joint2oiEntry.getValue();
		HAnimJoint joint = joint2oiEntry.getKey();
		OrientationInterpolator newOI = new OrientationInterpolator();
		ArrayList<float[]> newKeys = new ArrayList<float[]>();
		ArrayList<float[]> newKeyValues = new ArrayList<float[]>();
		StringBuffer oiName = new StringBuffer();
		for (Map.Entry<OrientationInterpolator, ArrayList<TimeSensor>> oi2timeEntry : oi2time.entrySet()) {
			OrientationInterpolator oi = oi2timeEntry.getKey();
			ArrayList<TimeSensor> sensors = oi2timeEntry.getValue();
			first = sensors.get(0);
			next = first;
			LinkedHashSet<TimeSensor> set = new LinkedHashSet<TimeSensor>();
			// now traverse through all of the sensors for this interpolator
			do {

				// this OrientationInterpolator is part of this sensor.
				if (oij.contains(oi) && !handledOIDefs.contains(oi.getDEF())) {
					newKeys.add(oi.getKey());
					newKeyValues.add(oi.getKeyValue());
					oiName.append(oi.getDEF());
					handledOIDefs.add(oi.getDEF());
					/*
				} else {
					newKeys.add(null);
					newKeyValues.add(null);
					oiName.append("");
					*/
				}
				set.add(next);
				next = timing.get(next);
			} while (!set.contains(next));
		}

		// now actually add keys and values to the new orientation interpolator
                Integer start = 0;
		Integer animCount = 0;
		SFRotation currot = null;
		for (int ik = 0; ik < newKeys.size(); ik++) {
			if (newKeys.get(ik) == null) {
				newOI.addKey(start);
			} else {
				for (float k : newKeys.get(ik)) {
					newOI.addKey(k+start);
				}
				animCount++;
			}
			if (newKeyValues.get(ik) == null) {
				if (currot == null) {
					currot = new SFRotation(0, 0, 1, 0);
				}
				newOI.addKeyValue(currot);
			} else {
				float [] keyValue = newKeyValues.get(ik);
				for (int kv = 0; kv < keyValue.length; kv += 4) {
					if (keyValue[kv] == 0 && keyValue[kv+1] == 0 && keyValue[kv+2] == 0) {
						System.err.println("Error, axis is 0,0,0, changing to 0,0,1 (default) "+(kv/4+1)+"th tuple in "+(ik+1)+" orientation interpolator in this list: "+oiName);
						keyValue[kv+2] = 1;
					}
					currot = new SFRotation(
							keyValue[kv],
							keyValue[kv + 1],
							keyValue[kv + 2],
							keyValue[kv + 3]);
					newOI.addKeyValue(currot);
				}
			}
			start += 1; 
		}

		newOI.setDEF(oiName.toString());

		// make the keys in the interpolator go from 0 to 1
		float [] key = newOI.getKey();
		if (key.length > 0)  {
			float end = key[key.length-1];
			if (end != 0) {
				for (int i = 0; i < key.length; i++) {
					key[i] = key[i]/end;
				}
				newOI.setKey(key);
			}
		} else {
			System.err.println("Warning, key length = 0 for "+oiName.toString());
		}



		// search for existing interpolators
		Iterator<OrientationInterpolator> itr = newois.iterator();
		boolean found = false;
		if (animCount == 1) {  // There's only one interpolator in this list, so no need to provide a new one
			found = true;
			// System.err.println("Found OI "+newOI.getDEF());
		}
		while (itr.hasNext()) {
			OrientationInterpolator present = itr.next();
			if (newOI.getDEF().equals(present.getDEF())) {
				found = true;
				// System.err.println("Found OI "+newOI.getDEF());
			}
		}

       		ArrayList copyois = traverseChildren(X3D0.getScene().getChildren(), OrientationInterpolator.class, 0);
		Iterator<OrientationInterpolator> citr = (Iterator<OrientationInterpolator>)(newois.iterator());
		while (citr.hasNext()) {
			OrientationInterpolator present = citr.next();
			if (newOI.getDEF().equals(present.getDEF())) {
				found = true;
				// System.err.println("Found OI "+newOI.getDEF());
			}
		}

		// if the interpolator wasn't found above, add it
		if (!found) {
			X3D0.getScene().addChild(newOI);
			newois.add(newOI);
		}

		if (!"".equals(oiName.toString())) {
			ROUTE newRoute = new ROUTE();
			newRoute.setFromNode(oiName.toString());
			newRoute.setFromField("value_changed");
			newRoute.setToNode(joint.getDEF());
			newRoute.setToField("set_rotation");
			X3D0.getScene().addChild(newRoute);

			ROUTE newRoute2 = new ROUTE();
			newRoute2.setFromNode(sensor.getDEF());
			newRoute2.setFromField("fraction_changed");
			newRoute2.setToNode(oiName.toString());
			newRoute2.setToField("set_fraction");
			X3D0.getScene().addChild(newRoute2);
		}

	}
	sensor.setCycleInterval(cycleInterval);


	Remove rem = new Remove();
	rem.removeChildren(X3D0.getScene().getChildren(), oldroutes);
	rem.removeChildren(X3D0.getScene().getChildren(), oldois);
	rem.removeChildren(X3D0.getScene().getChildren(), oldsensors);
}

private ArrayList traverseChildren(ArrayList<X3DNode> children, Class clazz, int indent) {
	var collection = new ArrayList();
	if (children != null) {
		for (int ci = 0; ci < children.size(); ci++) {
			collection.addAll(traverseChild(children.get(ci), clazz, indent+1));
		}
	}
	return collection;
}

private ArrayList traverseChildren(X3DNode[] children, Class clazz, int indent) {
	var collection = new ArrayList();
	if (children != null) {
		for (int ci = 0; ci < children.length; ci++) {
			collection.addAll(traverseChild(children[ci], clazz, indent+1));
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
		if (child instanceof ROUTE || child instanceof NewROUTE) {
			return collection;
		}
		try {
			String [] methods = new String [] {"getChildren", "getGeometry", "getAppearance",  "getSkeleton", "getCoord", "getColor", "getFontStyle", "getMaterial", "getSkinCoord", "getSkin"};
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
							} else if (children instanceof HAnimSite) {
								collection.addAll(traverseChild((HAnimSite)children, clazz, indent+1));
								//break;
							} else if (children instanceof HAnimSegment) {
								collection.addAll(traverseChild((HAnimSegment)children, clazz, indent+1));
								//break;
							} else if (children instanceof HAnimJoint) {
								collection.addAll(traverseChild((HAnimJoint)children, clazz, indent+1));
								//break;
							} else if (children instanceof HAnimHumanoid) {
								collection.addAll(traverseChild((HAnimHumanoid)children, clazz, indent+1));
								//break;
							} else if (children instanceof IndexedFaceSet) {
								collection.addAll(traverseChild((IndexedFaceSet)children, clazz, indent+1));
								//break;
							} else if (children instanceof Color) {
								collection.addAll(traverseChild((Color)children, clazz, indent+1));
								//break;
							} else if (children instanceof Color) {
								collection.addAll(traverseChild((ColorRGBA)children, clazz, indent+1));
								//break;
							} else if (children instanceof X3DColorNode) {
								collection.addAll(traverseChild((ColorRGBA)children, clazz, indent+1));
								//break;
							} else if (children instanceof FontStyle) {
								collection.addAll(traverseChild((FontStyle)children, clazz, indent+1));
								//break;
							} else if (children instanceof Material) {
								collection.addAll(traverseChild((Material)children, clazz, indent+1));
								//break;
							} else if (children instanceof float[]) {
								// System.err.println("float[]");
								//break;
							} else if (children instanceof Coordinate) {
								collection.addAll(traverseChild((Coordinate)children, clazz, indent+1));
								//break;
							} else {
								System.err.println(children.getClass().getCanonicalName());
								for (int ci = 0; ci < ((X3DNode[])children).length; ci++) {
									collection.addAll(traverseChild(((X3DNode[])children)[ci], clazz, indent+1));
								}
								//break;
							}
						}
					}
				} catch (NoSuchMethodException e) {
					// System.err.println(child.getClass().getCanonicalName()+":"+e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			// System.err.println(child.getClass().getCanonicalName()+":"+e.getMessage());
		}

	}
	return collection;
}
    public enum rowMode { HEADER, DATA, OFF };
    private rowMode mode = rowMode.DATA;
    public static void main(String[] args) {
 
        TableLoadSave loadAndSave = new TableLoadSave();
	String home = "C:/Users/jcarl/";
        // loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/resources/New2TemplateNoBoxes.js"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/resources/Leif8Final.x3d"));
        // loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/resources/Leif8Final.js"));
	//
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinScaledV2L1LOA4Sites10h.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinLOA4.scaled1joe06c.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinScaledV2L1LOA4Sites08o.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinLOA4scaled1joe06gForJohn2.x3d"));
        //loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinScaledV2L1LOA4OnlyMarkers11c.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinScaledV2L1LOA4OnlyMarkers11f.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinScaledV2L1LOA4OnlyMarkers11g.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinLOA1.x3d"));

	
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinLOA4.x3d"));
        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"/X3DJSONLD/blend/JinLOA4.scaled1.x3d"));
        loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JoeSkinTexcoordDisplacerKickUpdate2.x3d"));

        // loadAndSave.loadX3dFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinScaledV2L1LOA4MinimumSkeleton20f.x3d"));

        //loadAndSave.loadJsFile(new GenericTableModel(new DefaultTableModel()), new File(home+"jaminate/Jaminate/app/src/main/javascript/JinLOA4.js"));
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
            // Process p = Runtime.getRuntime().exec(new String[] { "js", "--experimental-options", "--polyglot", "--vm.Djs.allowAllAccess=true", "--vm.Xss1g", "--vm.Xmx4g", "--jvm", "--vm.classpath=/c/Users/jcarl/jaminate/Jaminate/app/lib/X3DJSAIL.4.0.full.jar;/c/Users/jcarl/jaminate/Jaminate/app/lib/saxon-he-12.1.jar", "src/main/resources/takesX3DJSAIL.js"});
            Process p = Runtime.getRuntime().exec(new String[] { "js", "--experimental-options", "--polyglot", "--vm.Djs.allowAllAccess=true", "--vm.Xss1g", "--vm.Xmx4g", "--jvm", "--vm.classpath=/c/Users/jcarl/jaminate/Jaminate/app/lib/X3DJSAIL.4.0.full.jar;/c/Users/jcarl/jaminate/Jaminate/app/lib/saxon-he-12.1.jar", "src/main/resources/takesSimple.js"});
	                                                       // js    --experimental-options    --polyglot    --vm.Djs.allowAllAccess=true    --vm.Xss1g    --vm.Xmx4g    --jvm    --vm.classpath='/c/Users/jcarl/jaminate/Jaminate/app/lib/X3DJSAIL.4.0.full.jar;/c/Users/jcarl/jaminate/Jaminate/app/lib/saxon-he-12.1.jar"    src/main/resources/takesX3DJSAIL.js

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
