package gov.nih.nci.evs.api.model.evs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "goId", "goTerm", "goEvi", "goSource", "sourceDate"})
public class EvsGoAnnotationByCode {
	@JsonProperty("P387")
	private String goId;

	@JsonProperty("P388")
	private String goTerm;

	@JsonProperty("P389")
	private String goEvi;

	@JsonProperty("P390")
	private String goSource;

	@JsonProperty("P391")
	private String sourceDate;
	
	public String getGoId() {
		return goId;
	}
	public void setGoId(String goId) {
		this.goId = goId;
	}
	public String getGoTerm() {
		return goTerm;
	}
	public void setGoTerm(String goTerm) {
		this.goTerm = goTerm;
	}
	public String getGoEvi() {
		return goEvi;
	}
	public void setGoEvi(String goEvi) {
		this.goEvi = goEvi;
	}
	public String getGoSource() {
		return goSource;
	}
	public void setGoSource(String goSource) {
		this.goSource = goSource;
	}
	public String getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(String sourceDate) {
		this.sourceDate = sourceDate;
	}

}