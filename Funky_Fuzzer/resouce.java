import java.util.*;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "resouce")
public class resouce {
	@XmlAttribute(name="encoding")
	String encoding;
	@XmlAttribute(name="method")
	String method;
	@XmlAttribute(name="path")
	String path;
	@XmlElement(name="referer")
	String referer;
	@XmlElement(name="get_params")
	getParams get_params;
	@XmlElement(name="post_params")
	postParams post_params;
	@XmlElement(name="file_params")
	fileParams file_params;
	@XmlElement(name="headers")
	Headers headers;

	public resouce() {}  

	public getParams getGet_Params() {
		return get_params;
	}

	public void setGet_Params(getParams get_params){
		this.get_params = get_params;
	}

	public postParams getPost_Params() {
		return post_params;
	}

	public void setPost_Params(postParams post_params){
		this.post_params = post_params;
	}

	public fileParams getFile_Params() {
		return file_params;
	}

	public void setFile_Params(fileParams file_params){
		this.file_params = file_params;
	}

	public String getEncoding() {	
		return encoding;
	}

	public void setEncoding(String encoding) {	
		this.encoding = encoding;
	}	

	public String getMethod() {	
		return method;
	}

	public void setMethod(String method) {	
		this.method = method;
	}

	public void setPath(String path) {	
		this.path = path;
	}

	public String getPath() {	
		return path;
	}

	public void setReferer(String referer) {	
		this.referer = referer;
	}

	public String getReferer() {
		return referer;
	}

	public void setHeaders(Headers headers) {	
		this.headers = headers;
	}

	public Headers getHeaders() {	
		return headers;
	}
	  
}
