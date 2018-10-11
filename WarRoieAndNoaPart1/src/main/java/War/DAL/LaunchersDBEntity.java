package War.DAL;

import java.io.Serializable;
import java.util.Date;

public class LaunchersDBEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String launcherId;
	private Date timeStamp;
	public LaunchersDBEntity(String launcherId, Date timeStamp) {
		this.launcherId = launcherId;
		this.timeStamp = timeStamp;
	} 
	
}
