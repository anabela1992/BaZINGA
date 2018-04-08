import java.util.*;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "headers")
public class Headers {
	@XmlElement(name = "header")
	List<Header> header = new ArrayList<>();

	public void addHeader(Header header) {
        this.header.add(header);
    }

	public void setHeader(List<Header> header){
		this.header = header;
	}

	public List<Header> getHeader(){
		return header;
	}
}
