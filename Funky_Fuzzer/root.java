import java.util.*;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement; 
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class root{
	@XmlElement(name = "rootURL")
	String rootURL;
	@XmlElement(name = "toBrowse")
	String toBrowse;
	@XmlElement(name = "browsed")
	browsed browsed;

	public root() {}  

	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL){
		this.rootURL = rootURL;
	}

	public String getToBrowse() {	
		return toBrowse;
	}

	public void setToBrowse(String toBrowse) {	
		this.toBrowse = toBrowse;
	}

	public browsed getBrowsed() {	
		return browsed;
	}

	public void setBrowse(String browse) {	
		this.browsed = browsed;
	}

	
}
