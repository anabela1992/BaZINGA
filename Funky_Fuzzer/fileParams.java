import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "file_params")
public class fileParams {
	@XmlElement(name = "input")
	List<Input> input = new ArrayList<>();

	public void addInput(Input input) {
		this.input.add(input);
	}
	public void setInput(List<Input> input){
		this.input = input;
	}

	public List<Input> getInput(){
		return input;
	}
}
