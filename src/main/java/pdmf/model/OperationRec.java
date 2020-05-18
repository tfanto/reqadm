package pdmf.model;

import java.time.Instant;

public class OperationRec {

	public OperationKey key;
	public String description;
	public String shortdescr;
	public Instant crtdat;
	public String crtusr;
	public Instant chgdat;
	public String chgusr;
	public Integer chgnbr;
	public Integer crtver;
	public Instant dltdat;
	public String dltusr;

	public OperationRec(OperationKey key, String description, Instant crtdat, Integer chgnbr) {
		this.key = key;
		this.description = description;
		this.crtdat = crtdat;
		this.chgnbr = chgnbr;
	}

}
