package jaosn.app.momo.crawler.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MomoData {
	private boolean live;
	private List<MomoRoom> r_infos;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public List<MomoRoom> getR_infos() {
		return r_infos;
	}
	public void setR_infos(List<MomoRoom> r_infos) {
		this.r_infos = r_infos;
	}
	
	
}
