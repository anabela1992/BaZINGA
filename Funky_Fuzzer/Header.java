import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public class Header {
		@XmlAttribute(name = "name")
		String name;
		@XmlAttribute(name = "value")
		String value;

		public void setName(String name) {
    			this.name = name;
		}

		public String getName() {
    			return this.name;
		}


		public void setValue(String value) {
    			this.value = value;
		}

		public String getValue() {
    			return this.value;
		}
}
