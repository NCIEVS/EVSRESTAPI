package gov.nih.nci.evs.restapi.util;
import gov.nih.nci.evs.restapi.test.*;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GraphGenerator {
	static Vector COLORS = new Vector();
	static {
		COLORS.add("black");
		COLORS.add("blue");
		COLORS.add("blueviolet");
		COLORS.add("brown");
		COLORS.add("brown4");
		COLORS.add("burlywood");
		COLORS.add("cadetblue4");
		COLORS.add("chocolate4");
		COLORS.add("coral3");
		COLORS.add("cyan4");
		COLORS.add("crimson");
		COLORS.add("darkgreen");
		COLORS.add("darkolivegreen");
		COLORS.add("darkgoldenrod");
		COLORS.add("darkorchid");
		COLORS.add("darklategray");
		COLORS.add("darkviolet");
		COLORS.add("deeppink3");
		COLORS.add("firebrick2");
		COLORS.add("forestgreen");
		COLORS.add("gold");
		COLORS.add("grey6");
		COLORS.add("green3");
		COLORS.add("green4");
		COLORS.add("lawngreen");
		COLORS.add("lightblue");
		COLORS.add("lightslateblue");
		COLORS.add("limegreen");
		COLORS.add("maroon");
		COLORS.add("magenta");
		COLORS.add("maroon");
		COLORS.add("midnightblue");
		COLORS.add("navyblue");
		COLORS.add("orange");
		COLORS.add("orangered");
		COLORS.add("orangered4");
		COLORS.add("orchid");
		COLORS.add("palevioletred");
		COLORS.add("pink");
		COLORS.add("purple");
		COLORS.add("red");
		COLORS.add("saddlebrown");
		COLORS.add("sienna");
		COLORS.add("slateblue");
		COLORS.add("snow");
		COLORS.add("springgreen");
		COLORS.add("tan");
		COLORS.add("turquoise3");
		COLORS.add("tomato");
		COLORS.add("violet");
		COLORS.add("theat");
		COLORS.add("yellowgreen");
	}

    public GraphGenerator() {

	}

	public static String generateColor() {
		int n = new RandomVariateGenerator().uniform(0, COLORS.size()-1);
		return (String) COLORS.elementAt(n);
	}

	public static void createDotGraph(String dotFormat,String fileName, String type)
	{
	    GraphViz gv=new GraphViz();
	    gv.addln(gv.start_graph());
	    gv.add(dotFormat);
	    gv.addln(gv.end_graph());
	    gv.decreaseDpi();
	    gv.decreaseDpi();
	    String fName = fileName+"."+ type;
	    System.out.println("fName: " + fName);
	    File out = new File(fName);
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
	}

	public static String toString(Vector v) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			t = t.trim();
			buf.append(t).append(" ");
		}
		String s = buf.toString();
		return s;
	}


//Allele_Has_Activity|R159|Gene|C16612|Property or Attribute|C20189
	public static Vector getNodes(Vector v) {
		Vector w = new Vector();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			String node = (String) u.elementAt(2);
            if (!w.contains(node)) {
				w.add(node);
			}
			node = (String) u.elementAt(4);
            if (!w.contains(node)) {
				w.add(node);
			}
		}
		w = new SortUtils().quickSort(w);
		return w;
	}

	public static Vector getEdgeLabels(Vector v) {
		Vector w = new Vector();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			String edge = (String) u.elementAt(0);
            if (!w.contains(edge)) {
				w.add(edge);
			}
		}
		w = new SortUtils().quickSort(w);
		return w;
	}

	public static Vector getEdges(Vector v) {
		//graviz_tbox_data.txt
		Vector w = new Vector();
		String role = null;
		String domain = null;
		String range = null;
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			role = (String) u.elementAt(0);
			domain = (String) u.elementAt(2);
			range = (String) u.elementAt(4);
			w.add(role + "|" + domain + "|" + range);
		}
		w = new SortUtils().quickSort(w);
		return w;
	}

    public static void generateGraph(PrintWriter pw, Vector nodes, Vector edges, Vector selected_nodes) {
		HashMap label2IdMap = new HashMap();
		HashMap id2LabelMap = new HashMap();
		HashMap id2ColorMap = new HashMap();
		Vector w = nodes;

		if (selected_nodes != null) {
			w = selected_nodes;
		}

		int knt = 0;
		for (int i=0; i<w.size(); i++) {
			knt++;
			String label = (String) w.elementAt(i);
			String id = "node_" + knt;
			label2IdMap.put(label, id);
			id2LabelMap.put(id, label);
			String color = generateColor();

			if (nodes == null) {
				pw.println(id + " [label=\"" + label + "\", fontcolor=white, shape=box, style=filled, fillcolor=" + color + "];");
			} else {
				if (nodes.contains(label)) {
					pw.println(id + " [label=\"" + label + "\", fontcolor=white, shape=box, style=filled, fillcolor=" + color + "];");
				}
			}
			id2ColorMap.put(id, color);
		}

		w = edges;
		HashMap nodePair2EdgeMap = new HashMap();
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			Vector u = StringUtils.parseData(t, '|');
			String role = (String) u.elementAt(0);
			String domain = (String) u.elementAt(1);
			String range = (String) u.elementAt(2);
			String domain_id = (String) label2IdMap.get(domain);
			String range_id = (String) label2IdMap.get(range);
			//String color = (String) id2ColorMap.get(domain_id);
			String key = domain_id + "|" + range_id;
			String roles = (String) nodePair2EdgeMap.get(key);
			if (roles == null) {
				roles = role;
			} else {
				roles = roles + "\\n" + role;
			}
			nodePair2EdgeMap.put(key, roles);
		}

		Iterator it = nodePair2EdgeMap.keySet().iterator();
		while (it.hasNext()) {
			String nodePair = (String) it.next();
			Vector u = StringUtils.parseData(nodePair);
			String domain_id = (String) u.elementAt(0);
			String range_id = (String) u.elementAt(1);
			String color = (String) id2ColorMap.get(domain_id);
			String role = (String) nodePair2EdgeMap.get(nodePair);

			String domain_label = (String) id2LabelMap.get(domain_id);
			String range_label = (String) id2LabelMap.get(range_id);
			if (selected_nodes == null) {
				pw.println(domain_id + " -> " + range_id
				   + " [label=" + "\"" + role + "\"" + " fontcolor=" + color + ", color=" + color + "];");
			} else {

				System.out.println("domain_label: " + domain_label + "  range_label: " + range_label);

				if (selected_nodes.contains(domain_label) && selected_nodes.contains(range_label)) {
					pw.println(domain_id + " -> " + range_id
					   + " [label=" + "\"" + role + "\"" + " fontcolor=" + color + ", color=" + color + "];");

					System.out.println("***************** selected.");

				} else {
					System.out.println("not selected.");
				}
			}
		}
    }


	public static void generateGraphvizDataFile(Vector nodes, Vector edges, Vector selected_nodes, String outputfile) {
        float ranksep = (float) 1.0;
        float nodesep = (float) 1.5;
        generateGraphvizDataFile(nodes, edges, selected_nodes, outputfile, ranksep, nodesep);
	}

	public static void generateGraphvizDataFile(Vector nodes, Vector edges, Vector selected_nodes, String outputfile, float ranksep, float nodesep) {
         PrintWriter pw = null;
         try {
 			pw = new PrintWriter(outputfile, "UTF-8");
//ranksep = 1;
//nodesep=1.5;
			pw.println("ranksep = \"" + ranksep + "\";");
			pw.println("nodesep = \"" + nodesep + "\";");
			pw.println("ratio=fill;");
			pw.println("overlap=scale;");
			pw.println("concentrate=true;");
			pw.println("ratio=auto;");
			pw.println("overlap = false;");
			pw.println("splines = true;");
			pw.println("node [style=filled,color=lightblue,shape=box];");
			pw.println("style=filled;");
			pw.println("color=lightgrey;");
			pw.println("nodesep=1.0;");

            generateGraph(pw, nodes, edges, selected_nodes);

 		} catch (Exception ex) {

 		} finally {
 			try {
 				pw.close();
 				System.out.println("Output file " + outputfile + " generated.");
 		    } catch (Exception ex) {
 				ex.printStackTrace();
 			}
		}
	}

    public static void run(Vector nodes, Vector edges, Vector selected_nodes, String outputfile, String format, float ranksep, float nodesep) {
		generateGraphvizDataFile(nodes, edges, selected_nodes, outputfile, ranksep, nodesep);
		String dotFormat = toString(Utils.readFile(outputfile));
        int n = outputfile.lastIndexOf(".");
        if (n != -1) {
        	outputfile = outputfile.substring(0, n);
		}
		System.out.println(dotFormat);
		createDotGraph(dotFormat, outputfile, format);
	}


//gif, svg, png
	public static void main(String[] args) throws Exception {
		String serviceUrl = args[0];
		String named_graph = args[1];

		System.out.println("serviceUrl: " + serviceUrl);
		System.out.println("named_graph: " + named_graph);

		OWLSPARQLUtils owlSPARQLUtils = new OWLSPARQLUtils(serviceUrl, null, null);
        //String queryfile = args[2];
        owlSPARQLUtils.set_named_graph(named_graph);

		String format = args[2];
		String subgraphfile = null;
		if (args.length > 3) {
			subgraphfile = args[3];
		}

		System.out.println(serviceUrl);
		System.out.println(named_graph);
		System.out.println(format);
		System.out.println(subgraphfile);

        Vector v = owlSPARQLUtils.getDomainAndRangeData(named_graph);
        Utils.saveToFile("domain_range.txt", v);

		Vector nodes = getNodes(v);
		Vector edges = getEdges(v);
		Vector selected_nodes = Utils.readFile(subgraphfile);

		String datafile = "graphvizDataFile.txt";

		run(nodes, edges, selected_nodes, datafile, format, (float) 1.0, (float) 1.5);

	}

}