package jaosn.app.momo.crawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RecommendResponse {
	private int ec;
	private String em;
	private MomoData data;
	public int getEc() {
		return ec;
	}
	public void setEc(int ec) {
		this.ec = ec;
	}
	public String getEm() {
		return em;
	}
	public void setEm(String em) {
		this.em = em;
	}
	public MomoData getData() {
		return data;
	}
	public void setData(MomoData data) {
		this.data = data;
	}
	
	
}
