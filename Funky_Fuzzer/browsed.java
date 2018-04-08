import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "browsed")
public class browsed {
	@XmlElement(name = "resouce")
	List<resouce> resouce;
	
	public browsed() {}

	public void addResouce(resouce resouce) {
        this.resouce.add(resouce);
    }
	
	public void setResouce(List<resouce> resouce){
		this.resouce = resouce;
	}

	public List<resouce> getResouce(){
		return resouce;
	}
}
