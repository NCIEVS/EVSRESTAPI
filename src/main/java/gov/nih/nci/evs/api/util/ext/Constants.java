/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2016 NGIS. This software was developed in conjunction
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

package gov.nih.nci.evs.api.util.ext;

/**
 * Constants for computing CTRP extensions.
 */
public class Constants {

  /** The Constant INVERSE_IS_A. */
  public static final String INVERSE_IS_A = "inverse_is_a";

  /** The Constant TRAVERSE_UP. */
  public static final int TRAVERSE_UP = 1;

  /** The Constant TRAVERSE_DOWN. */
  public static final int TRAVERSE_DOWN = 0;

  /** The Constant NEW_CODE. */
  public static final String NEW_CODE = "NHC0";

  /** The Constant NCIT_NS. */
  public static final String NCIT_NS = "<http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#>";

  /** The Constant XML_DECLARATION. */
  public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  /** The Constant INVERSE_IS_OF. */
  public static final String INVERSE_IS_OF = "inverseIsA";

  /** The Constant ROLE. */
  public static final String ROLE = "Role";

  /** The Constant ASSOCIATION. */
  public static final String ASSOCIATION = "Association";

  /** The Constant HIERARCHICAL. */
  public static final String HIERARCHICAL = "Hierarchical";

  /** The Constant FULL_SYN. */
  public static final String FULL_SYN = "FULL_SYN";

  /** The Constant DEFINITION. */
  public static final String DEFINITION = "DEFINITION";

  /** The Constant ALT_DEFINITION. */
  public static final String ALT_DEFINITION = "ALT_DEFINITION";

  /** The Constant EXACT_MATCH. */
  public static final String EXACT_MATCH = "exactMatch";

  /** The Constant STARTS_MATCH. */
  public static final String STARTS_MATCH = "startsWith";

  /** The Constant ENDS_MATCH. */
  public static final String ENDS_MATCH = "endsWith";

  /** The Constant CONTAINS. */
  public static final String CONTAINS = "contains";

  /** The disease is stage. */
  public static String DISEASE_IS_STAGE = "Disease_Is_Stage";

  /** The maximum level. */
  public static int MAXIMUM_LEVEL = 1000;

  /** The Constant EVSRESTAPI_BEAN. */
  public static final String EVSRESTAPI_BEAN = "gov.nih.nci.evs.restapi.bean";

  /** The common properties. */
  public static String[] COMMON_PROPERTIES = {
      "code", "label", "Preferred_Name", "Display_Name", "DEFINITION", "ALT_DEFINITION", "FULL_SYN",
      "Concept_Status", "Semantic_Type"
  };

  /** The default version predicate. */
  public static String DEFAULT_VERSION_PREDICATE = "owl:versionInfo";

  /** The obo version predicate. */
  public static String OBO_VERSION_PREDICATE = "oboInOwl:hasOBOFormatVersion";

  /** The version predicate. */
  public static String[] VERSION_PREDICATE = new String[] {
      DEFAULT_VERSION_PREDICATE, OBO_VERSION_PREDICATE
  };

  /** The association name. */
  public static String ASSOCIATION_NAME = "inverse_is_a";

  /** The Constant TYPE_ROLE. */
  public static final String TYPE_ROLE = "type_role";

  /** The Constant TYPE_ASSOCIATION. */
  public static final String TYPE_ASSOCIATION = "type_association";

  /** The Constant TYPE_SUPERCONCEPT. */
  public static final String TYPE_SUPERCONCEPT = "type_superconcept";

  /** The Constant TYPE_SUBCONCEPT. */
  public static final String TYPE_SUBCONCEPT = "type_subconcept";

  /** The Constant TYPE_INVERSE_ROLE. */
  public static final String TYPE_INVERSE_ROLE = "type_inverse_role";

  /** The Constant TYPE_INVERSE_ASSOCIATION. */
  public static final String TYPE_INVERSE_ASSOCIATION = "type_inverse_association";

  /** The starts with. */
  public static String STARTS_WITH = "startsWith";

  /** The ends with. */
  public static String ENDS_WITH = "endsWith";

  /** The value set uri prefix. */
  public static String VALUE_SET_URI_PREFIX = "http://evs.nci.nih.gov/valueset/";

  /** The terminology subset code. */
  public static String TERMINOLOGY_SUBSET_CODE = "C54443"; // Terminology Subset
                                                           // (Code C54443)

  /** The concept in subset. */
  public static String CONCEPT_IN_SUBSET = "Concept_In_Subset";

  /** The contributing source. */
  public static String CONTRIBUTING_SOURCE = "Contributing_Source";

  /** The nci thesaurus. */
  public static String NCI_THESAURUS = "NCI_Thesaurus";

  /** The default limit. */
  public static int DEFAULT_LIMIT = 15000;

  /**
   * Constructor.
   */
  private Constants() {

  }

} // Class Constants
