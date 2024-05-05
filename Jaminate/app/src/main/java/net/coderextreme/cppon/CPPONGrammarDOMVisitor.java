package net.coderextreme.cppon;

import org.w3c.dom.*;
import org.w3c.dom.ls.*;
import javax.xml.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.validation.*;

import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.util.*;

public class CPPONGrammarDOMVisitor<Node extends org.w3c.dom.Node> extends CPPONGrammarBaseVisitor<Node> implements CPPONGrammarVisitor<Node> {
	private Map<String, Element> nodes = new HashMap<String, Element>();
	private Document document = null;
	public String log(Object s) {
		if (s != null) {
			System.out.print(s.toString());
			return (s.toString());
		} else {
			// System.err.println("log message is null");
			return "";
		}
	}
	public CPPONGrammarDOMVisitor(String filename) throws IOException {
		// first parse
	    CharStream input = new ANTLRFileStream(filename);
	    CPPONGrammarLexer lexer = new CPPONGrammarLexer(input);
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    CPPONGrammarParser parser = new CPPONGrammarParser(tokens);
	    parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            CPPONGrammarParser.X3dContext ctx = null;
	    try {
		ctx = parser.x3d();  // STAGE 1
	    } catch (RecognitionException ex) {
		tokens.reset(); // rewind input stream
		parser.reset();
		parser.getInterpreter().setPredictionMode(PredictionMode.LL);
		ctx = parser.x3d();  // STAGE 2
		// if we parse ok, it's LL not SLL
	    }

	    document = (Document)this.visitX3d(ctx);
	    if (document != null) {
		    DOMImplementationLS ls = (DOMImplementationLS)document.getImplementation();
		    LSOutput output = ls.createLSOutput();
		    LSSerializer ser = ls.createLSSerializer();
		    ser.getDomConfig().setParameter("format-pretty-print", true);
		    StringWriter writer = new StringWriter();
		    output.setCharacterStream(writer);
		    output.setEncoding("UTF-8");
		    try {
			    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			    Schema schema = sf.newSchema(new File("C:/x3d-code/www.web3d.org/specifications/x3d-4.0.xsd"));
			    Validator validator = schema.newValidator();
			    DOMSource source = new DOMSource(document);
			    validator.validate(source);
		    } catch (SAXException e) {
			    e.printStackTrace(System.err);
		    }
		    ser.write(document, output);
		    String xml = writer.toString();
		    log(xml);
	    }
	}
	public static void main(String [] args) throws IOException {
	    for (int a = 0; a < args.length; a++) {
            	CPPONGrammarDOMVisitor root = new CPPONGrammarDOMVisitor(args[a]);
	    }
	}
	@Override
	public Node visitX3d(CPPONGrammarParser.X3dContext ctx) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.newDocument();
			if (document == null) {
				log("document is null\n");
			}
			Element child = (Element)visitLines(ctx.lines());
			if (document != null && child != null) {
				DOMImplementation domImplementation = db.getDOMImplementation();
				DocumentType doctype = domImplementation.createDocumentType("X3D", "ISO//Web3D//DTD X3D 4.0//EN", "https://www.web3d.org/specifications/x3d-4.0.dtd");
				document.appendChild(doctype);
				// document.appendChild(document.createTextNode("\n"));
				document.appendChild(child);
				child.setAttribute("xmlns:xsd",  "http://www.w3.org/2001/XMLSchema-instance");
			} else {
				log("Got nothing from visiting Children");
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace(System.err);
		}
		return (Node)document;
	}
	@Override public Node visitConstruct(CPPONGrammarParser.ConstructContext ctx) {
		Node node = super.visitChildren(ctx);
		CPPONGrammarParser.TypeContext tc = ctx.type();
		CPPONGrammarParser.FunccallContext fc = ctx.funccall();
		CPPONGrammarParser.VariableContext vp = fc.variable();
		CPPONGrammarParser.OperatorContext op = fc.operator();
		CPPONGrammarParser.ParametersContext pa = fc.parameters();

		TerminalNode typ = tc.IDENTIFIER();

		TerminalNode ty = vp.IDENTIFIER(); // variable type
		TerminalNode idx = vp.WHOLE();

		TerminalNode eq = op.EQUALS();

		TerminalNode fn = fc.IDENTIFIER();

		String tystr = ty.toString();
		String typstr = typ.toString();
		String idxstr = "";
		if (idx != null) {
			idxstr = idx.toString();
		}
		// log("Creating ");log(typ); log(" "); log(ty); log(idx); log(eq); log(fn); log(pa); log("\n");
		switch (typstr) {
			case "Connect":
				typstr = "connect";
				break;
			case "CFontStyle":
				typstr = "FontStyle";
				break;
			case "CColor":
				typstr = "Color";
				break;
		}
		if (!(tystr+idxstr).toLowerCase().startsWith(typstr.toLowerCase())) {
			log(tystr+" != "+typstr+"\n");
		}
		Element child = document.createElement(typstr);
		this.nodes.put(tystr+idxstr, child);
		return (Node)child;
	}
	@Override public Node visitVariable(CPPONGrammarParser.VariableContext ctx) {
		TerminalNode ct = ctx.IDENTIFIER(); // child name
		TerminalNode cin = ctx.WHOLE();
		String idxstr = "";
		if (cin != null) {
			idxstr = cin.toString();
		}
		String variable = ct.toString()+idxstr;

		// log("Looking for "+variable+"\n");
		Element child = nodes.get(variable);
		if (child != null) {
			// log("Found "+variable+"\n");
		}
		return (Node)child;
	}
	public Element elementSetAttribute(Element element, String attributeName, String value, boolean isArray) {
		if (!attributeName.equals("DEF") && !attributeName.equals("USE")) {
			attributeName = attributeName.substring(0,1).toLowerCase()+attributeName.substring(1);
		}
		if (attributeName.equals("containerField")) {
			value = value.substring(0,1).toLowerCase()+value.substring(1);
		}
		if (isArray) {
			value = value.replaceAll(",", " ");
		}
		element.setAttribute(attributeName, value);
		return element;
	}
	public Element elementSetAttribute(Element element, TerminalNode field, String value, boolean isArray) {
		String attributeName = field.toString();
		this.elementSetAttribute(element, attributeName, value, isArray);
		return element;
	}
	@Override public Node visitSet_field(CPPONGrammarParser.Set_fieldContext ctx) {
		CPPONGrammarParser.FunccallContext fc = ctx.funccall();
		CPPONGrammarParser.VariableContext vp = fc.variable();
		CPPONGrammarParser.OperatorContext op = fc.operator();
		CPPONGrammarParser.ParametersContext pa = fc.parameters();
		CPPONGrammarParser.VariableContext vc = pa.variable();

		Node parent = visitVariable(vp);

		TerminalNode set = op.SET();
		TerminalNode add = op.ADD();
		TerminalNode x3dnodeset = op.X3DNODESET();
		// log("test set add"); log(set); log(add);log("\n");
		TerminalNode fn = fc.IDENTIFIER();
		if (add == null) {
			String text = "";
			if (parent != null) {
				CPPONGrammarParser.CstringContext cs = pa.cstring();
				CPPONGrammarParser.Construct_arrayContext array = pa.construct_array();
				if (cs != null) {
					CPPONGrammarParser.StringContext sc = cs.string();
					int start = sc.start.getStartIndex();
					int stop = sc.stop.getStopIndex();
					Interval interval = new Interval(start,stop);
					text = sc.start.getInputStream().getText(interval);
					// log(text+"\n");
					if (text.startsWith("\"") && text.endsWith("\"")) {
						text = text.substring(1, text.length() - 1);
						// log(text+"\n");
					}
					this.elementSetAttribute((Element)parent, fn, text, false);
				} else if (array != null) {
					CPPONGrammarParser.ListContext list = array.list();
					CPPONGrammarParser.Boolean_listContext boolean_list = list.boolean_list();
					CPPONGrammarParser.String_listContext string_list = list.string_list();
					CPPONGrammarParser.Float_listContext float_list = list.float_list();
					CPPONGrammarParser.Integer_listContext integer_list = list.integer_list();
					if (float_list != null) {
						int start = float_list.start.getStartIndex();
						int stop = float_list.stop.getStopIndex();
						Interval interval = new Interval(start,stop);
						text = float_list.start.getInputStream().getText(interval);
						this.elementSetAttribute((Element)parent, fn, text, true);
					} else if (integer_list != null) {
						int start = integer_list.start.getStartIndex();
						int stop = integer_list.stop.getStopIndex();
						Interval interval = new Interval(start,stop);
						text = integer_list.start.getInputStream().getText(interval);
						this.elementSetAttribute((Element)parent, fn, text, true);
					} else if (boolean_list != null) {
						int start = boolean_list.start.getStartIndex();
						int stop = boolean_list.stop.getStopIndex();
						Interval interval = new Interval(start,stop);
						text = boolean_list.start.getInputStream().getText(interval);
						this.elementSetAttribute((Element)parent, fn, text, true);
					} else if (string_list != null) {
						List<CPPONGrammarParser.CstringContext> cstring_list = string_list.cstring();
						Iterator<CPPONGrammarParser.CstringContext> i = cstring_list.iterator();
						boolean first = true;
						while (i.hasNext()) {
							if (first) {
								first = false;
							} else {
								text += " ";
							}
							CPPONGrammarParser.CstringContext lc = i.next();
							CPPONGrammarParser.StringContext sc = lc.string();
							int start = sc.start.getStartIndex();
							int stop = sc.stop.getStopIndex();
							Interval interval = new Interval(start,stop);
							text += sc.start.getInputStream().getText(interval);
						}
						// no need to remove commas
						this.elementSetAttribute((Element)parent, fn, text, false);
					}
				} else if (vc != null) {
					Node child = visitVariable(vc);
					String containerFieldName = fn.toString();
					if (containerFieldName.toLowerCase().endsWith("joints") || containerFieldName.toLowerCase().endsWith("segments") || containerFieldName.toLowerCase().endsWith("sites") || containerFieldName.toLowerCase().endsWith("metadata") || containerFieldName.toLowerCase().endsWith("url") || containerFieldName.toLowerCase().endsWith("texture")) {
						this.elementSetAttribute((Element)child, "containerField", containerFieldName, false);
					}
					parent.appendChild(child);
					return (Node)parent;
				} else {
					int start = pa.start.getStartIndex();
					int stop = pa.stop.getStopIndex();
					Interval interval = new Interval(start,stop);
					text = pa.start.getInputStream().getText(interval);
					this.elementSetAttribute((Element)parent, fn, text, false);
				}
				// log(set); log(add); log(fn); log(text); log("\n");
			}
		} else if (add != null) {
			Node child = visitVariable(vc);
			String containerFieldName = fn.toString();
			if (containerFieldName.toLowerCase().endsWith("joints") || containerFieldName.toLowerCase().endsWith("segments") || containerFieldName.toLowerCase().endsWith("sites") || containerFieldName.toLowerCase().endsWith("metadata") || containerFieldName.toLowerCase().endsWith("url") || containerFieldName.toLowerCase().endsWith("texture")) {
				this.elementSetAttribute((Element)child, "containerField", containerFieldName, false);
			}
			parent.appendChild(child);
			return (Node)parent;
		}
		Node node = super.visitChildren(ctx);
		return (Node)node; // send element so we can build
	}
	@Override public Node visitLines(CPPONGrammarParser.LinesContext ctx) {
		Node node = super.visitChildren(ctx);
		Element element = null;

		List<CPPONGrammarParser.LineContext> lines = ctx.line();
		Iterator<CPPONGrammarParser.LineContext> i = lines.iterator();
		while (i.hasNext()) {
			CPPONGrammarParser.LineContext lc = i.next();
			CPPONGrammarParser.ConstructContext cc = lc.construct();
			if (cc != null) {
				Element child = (Element)visitConstruct(cc);
				if (element == null) {
					element = child;
				}
			}
			CPPONGrammarParser.Set_fieldContext sfc = lc.set_field();
			if (sfc != null) {
				visitSet_field(sfc);  // gets element to add to by id set in construct
			}
			CPPONGrammarParser.Add_fieldContext afc = lc.add_field();
			if (afc != null) {
				visitAdd_field(afc);  // gets parent element to append to by id set in construct
			}
		}
		return (Node)element;
	}
}
