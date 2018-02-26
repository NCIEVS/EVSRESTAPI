package gov.nih.nci.evs.restapi.appl;

import gov.nih.nci.evs.restapi.util.*;
import gov.nih.nci.evs.restapi.bean.*;
import gov.nih.nci.evs.restapi.common.*;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.codec.binary.Base64;
import org.json.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2017 NGIS. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIS and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIS" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIS
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIS, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class ConceptDetailsBatchRunner {
    JSONUtils jsonUtils = null;
    HTTPUtils httpUtils = null;
    String named_graph = null;
    String prefixes = null;
    String sparql_endpoint = null;
    String serviceUrl = null;
    String named_graph_id = ":NHC0";
    String base_uri = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl";
    TreeBuilder treeBuilder = null;//new TreeBuilder(this);
    ParserUtils parser = new ParserUtils();

    gov.nih.nci.evs.restapi.util.OWLSPARQLUtils owlSPARQLUtils = null;
    MainTypeHierarchy mth = null;
    ExportUtils exportUtils = null;
    String ncit_version = "17.07c";
	HashSet main_type_set = null;
	Vector category_vec = null;

	private Vector parent_child_vec = null;
	private static String stage_file = "DISEASE_IS_STAGE.txt";
	private static String grade_file = "DISEASE_IS_GRADE.txt";
	private static String NCI_THESAURUS = "NCI_Thesaurus";

	Vector disease_is_stage_vec = null;
	Vector disease_is_grade_vec = null;

	public ConceptDetailsBatchRunner(String sparql_endpoint) {
        this.sparql_endpoint = sparql_endpoint;//"https://sparql-evs-dev.nci.nih.gov/ctrp/?query=";
        this.serviceUrl = sparql_endpoint + "?query=";
        initialize();
	}

    public void initialize() {
		long ms = System.currentTimeMillis();
		MetadataUtils mdu = new MetadataUtils(sparql_endpoint);

		this.named_graph = mdu.getNamedGraph(NCI_THESAURUS);
		this.ncit_version = mdu.getLatestVersion(NCI_THESAURUS);
		this.owlSPARQLUtils = new gov.nih.nci.evs.restapi.util.OWLSPARQLUtils(serviceUrl, null, null);
		this.owlSPARQLUtils.set_named_graph(named_graph);

		String parent_child_file = "parent_child.txt";
		if (parent_child_vec == null) {
			File file = new File(parent_child_file);
			boolean exists = file.exists();
			if (exists) {
				System.out.println("Loading parent_child_vec...");
				parent_child_vec = Utils.readFile("parent_child.txt");
			} else {
				System.out.println("Generating parent_child_vec...");
				parent_child_vec = owlSPARQLUtils.getHierarchicalRelationships(named_graph);
			}
		}

		File f = new File(stage_file);
		if(f.exists() && !f.isDirectory()) {
			disease_is_stage_vec = Utils.readFile(stage_file);
			System.out.println(stage_file + " exists.");
		}  else {
			String association_name = "Disease_Is_Stage";
			disease_is_stage_vec = owlSPARQLUtils.getAssociationSourceCodes(named_graph, association_name);
			Utils.saveToFile(stage_file, disease_is_stage_vec);
		}
		HashMap stageConceptHashMap = new ParserUtils().getCode2LabelHashMap(disease_is_stage_vec);
		System.out.println("Number of stage terms: " + stageConceptHashMap.keySet().size());

		f = new File(grade_file);
		if(f.exists() && !f.isDirectory()) {
			disease_is_grade_vec = Utils.readFile(grade_file);
			System.out.println(grade_file + " exists.");
		}  else {
			String association_name = "Disease_Is_Grade";
			disease_is_grade_vec = owlSPARQLUtils.getAssociationSourceCodes(named_graph, association_name);
			Utils.saveToFile(grade_file, disease_is_grade_vec);
		}
		HashMap gradeConceptHashMap = new ParserUtils().getCode2LabelHashMap(disease_is_grade_vec);
		System.out.println("Number of grade terms: " + gradeConceptHashMap.keySet().size());

 		MainTypeHierarchyData mthd = new MainTypeHierarchyData(serviceUrl, named_graph);

		main_type_set = mthd.get_main_type_set();
		System.out.println("main_type_set: " + main_type_set.size());
		category_vec = mthd.get_broad_category_vec();
		System.out.println("category_vec: " + category_vec.size());
		Utils.dumpVector("category_vec", category_vec);

		String version = mthd.getVersion();
		System.out.println("version: " + version);

		System.out.println("*** Instantiating MainTypeHierarchy " + parent_child_vec.size());

		this.mth = new MainTypeHierarchy(
            ncit_version,
            parent_child_vec,
            main_type_set,
            category_vec,
            stageConceptHashMap,
            gradeConceptHashMap);

		this.httpUtils = new HTTPUtils(serviceUrl, null, null);
        this.jsonUtils = new JSONUtils();
        this.treeBuilder = new TreeBuilder(owlSPARQLUtils);
        this.exportUtils = new ExportUtils(owlSPARQLUtils);
        System.out.println("Total initialization run time (ms): " + (System.currentTimeMillis() - ms));
	}

    public HashSet combineMainTypesAndCategories() {
		HashSet hset = main_type_set;
        for (int i=0; i<category_vec.size(); i++) {
			String code = (String) category_vec.elementAt(i);
			hset.add(code);
		}
		return hset;
	}

	public ConceptDetails createConceptDetails(String named_graph, String code) {
        List mainMenuAncestors = mth.getMainMenuAncestors(code);
        Boolean isMainType = new Boolean(mth.isMainType(code));
        Boolean isSubtype = new Boolean(mth.isSubtype(code));
        Boolean isDiseaseStage = new Boolean(mth.isDiseaseStage(code));
        Boolean isDiseaseGrade = new Boolean(mth.isDiseaseGrade(code));
        Boolean isDisease = new Boolean(mth.isDisease(code));
        ConceptDetails cd = exportUtils.buildConceptDetails(named_graph, code,
			mainMenuAncestors,
			isMainType,
			isSubtype,
			isDiseaseStage,
			isDiseaseGrade,
			isDisease);
        return cd;
	}

	public ConceptDetailsBatch getConceptDetailsBatch(Vector codes) {
		ConceptDetailsBatch cdb = new ConceptDetailsBatch();
		List list = new ArrayList();
		for (int i=0; i<codes.size(); i++) {
			String code = (String) codes.elementAt(i);
			String label = mth.getLabel(code);
			if (label == null) {
				System.out.println("Label for " + code + " not found???");
			}
			ConceptDetails cd = createConceptDetails(named_graph, code);
			list.add(cd);
		}
		cdb.setConceptDetailsList(list);
		return cdb;
	}


    public void generateMainTypeHierarchy() {
		MainTypeHierarchy.save_main_type_hierarchy();
		this.mth.generate_main_type_label_code_file(combineMainTypesAndCategories());
	}

    public static void main(String[] args) {
		long ms = System.currentTimeMillis();
		String sparql_endpoint = args[0];//"https://sparql-evs-dev.nci.nih.gov/ctrp/";
        ConceptDetailsBatchRunner cdbr = new ConceptDetailsBatchRunner(sparql_endpoint);
        Vector codes = new Vector();
        /*
        codes.add("C3058");
        codes.add("C125890");
        codes.add("C2924");
        codes.add("C4896");
        codes.add("C54705");
        codes.add("C7057");
        codes.add("C2991");
        codes.add("C3262");
        codes.add("C4897");
        codes.add("C7834");
        codes.add("C48232");
        */
        //codes.add("C146724");
        codes.add("C123181");
        codes.add("C12354");

        //cdbr.run(named_graph, codes);
        ConceptDetailsBatch cdb = cdbr.getConceptDetailsBatch(codes);
        Vector u = new Vector();
        u.add(cdb.toJson());
        Utils.saveToFile("cdb_" + StringUtils.getToday() + ".txt", u);
        //System.out.println(cdb.toJson());
        //cdbr.generateMainTypeHierarchy();
        System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
    }
}
