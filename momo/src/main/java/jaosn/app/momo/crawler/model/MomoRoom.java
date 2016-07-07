package jaosn.app.momo.crawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MomoRoom {
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getStid() {
		return stid;
	}
	public void setStid(String stid) {
		this.stid = stid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public int getOn() {
		return on;
	}
	public void setOn(int on) {
		this.on = on;
	}
	public boolean isFod() {
		return fod;
	}
	public void setFod(boolean fod) {
		this.fod = fod;
	}
	public int getRtype() {
		return rtype;
	}
	public void setRtype(int rtype) {
		this.rtype = rtype;
	}
	private String rid;
	private String stid;
	private String title;
	private String cover;
	private boolean live;
	private int on;
	private boolean fod;
	private int rtype;
	
	
}
