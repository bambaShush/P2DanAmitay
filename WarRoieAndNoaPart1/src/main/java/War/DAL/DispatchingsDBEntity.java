package War.DAL;

import java.io.Serializable;
import java.util.Date;

public class DispatchingsDBEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String launcherId;
	private int launchedAt;
	private String destination;
	private Date timeStamp;
	
	public DispatchingsDBEntity(String launcherId, int launchedAt, String destination, Date timeStamp) {
		this.launcherId = launcherId;
		this.launchedAt = launchedAt;
		this.destination = destination;
		this.timeStamp = timeStamp;
	}
	
	
}
