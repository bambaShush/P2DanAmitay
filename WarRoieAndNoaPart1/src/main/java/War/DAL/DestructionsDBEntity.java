package War.DAL;

import java.io.Serializable;
import java.util.Date;

public class DestructionsDBEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String missileId;
	private boolean isHit;
	private Date timeStamp;
	public DestructionsDBEntity(String missileId, boolean isHit, Date timeStamp) {
		this.missileId = missileId;
		this.isHit = isHit;
		this.timeStamp = timeStamp;
	}
	
}
