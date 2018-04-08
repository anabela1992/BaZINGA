import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.io.FileOutputStream;
import java.io.FileNotFoundException; 

import javax.xml.bind.JAXBContext;  
import javax.xml.bind.JAXBException;  
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;

public class fromxml {
	
	private root r = new root();

	//function that translates .xml to object
	public void Unmarshall() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(root.class);
			File file = new File("/home/beli/.wapiti/scans/localhost.xml");
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			r = (root) jaxbUnmarshaller.unmarshal(file);

		} catch (JAXBException e) {  
			e.printStackTrace();  
		} 
	}

	//function that translates objects to .xml
	public void Marshall(){

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(root.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(r, new File("nosferatu.xml"));

		} catch (JAXBException e) {  
			e.printStackTrace();  
		}

	}

	public root getRoot() {
		return r;
	}


	public void setRoot(root r) {
		this.r = r;
	}

	//function that modifies the first occurrence of the variable
	//in the future it will change according to something more useful
	public void generateInput(String variable){
		List<resouce> resouce = this.r.getBrowsed().getResouce();
		
		for(resouce tmp : resouce){
			List<Input> listInput = tmp.getGet_Params().getInput();
			if(!listInput.isEmpty()){
				
				for(Input input : listInput){
					if(input.getName().equals(variable)){
						int index = listInput.indexOf(input);
						listInput.remove(index);
						String oldValue = input.getValue();

						int size = oldValue.length();
						String value = oldValue.substring(0,size-2)+"??";

						input.setValue(value);
						listInput.add(index, input);

						break;
					}
				}
			}
		}
	}

	//return the name of the variable with value index
	public String variableByIndex(int index) {
		List<resouce> resouce = this.r.getBrowsed().getResouce();
		int reached = 0;
		int select = 0;

		for(resouce tmp : resouce){
			List<Input> list;
			list = tmp.getPost_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						return input.getName();

					}

					reached = reached + 1;
				}
			}

			list = tmp.getFile_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						return input.getName();

					}

					reached = reached + 1;
				}
			}

			list = tmp.getGet_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						return input.getName();

					}

					reached = reached + 1;
				}
			}
		}
		return null;
	}
	
	public boolean differentURL(int index) {
		List<resouce> resouce = this.r.getBrowsed().getResouce();
		int reached = 0;
		
		String url = null;
		for(resouce tmp : resouce){
			List<Input> list;
			list = tmp.getPost_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						url = tmp.getReferer();

					} else if(reached == (index + 1)) {
						if (!(url.equals(tmp.getReferer()))) {
							return true;
						} else {
							return false;
						}
					}

					reached = reached + 1;
				}
			}

			list = tmp.getFile_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						url = tmp.getReferer();

					} else if(reached == (index + 1)) {
						if (!(url.equals(tmp.getReferer()))) {
							return true;
						} else {
							return false;
						}
					}

					reached = reached + 1;
				}
			}

			list = tmp.getGet_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						url = tmp.getReferer();

					} else if(reached == (index + 1)) {
						if (!(url.equals(tmp.getReferer()))) {
							return true;
						} else {
							return false;
						}
					}

					reached = reached + 1;
				}
			}
		}
		return true;
	}

	public boolean notEnded(int index) {
		List<resouce> resouce = this.r.getBrowsed().getResouce();
		int reached = 0;
		String url = null;
		for(resouce tmp : resouce){
			List<Input> list;
			list = tmp.getPost_Params().getInput();
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						return true;

					}

					reached = reached + 1;
				}
			}

			list = tmp.getFile_Params().getInput();
			
			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						return true;

					}

					reached = reached + 1;
				}
			}

			list = tmp.getGet_Params().getInput();
				

			if(!list.isEmpty()){
				for(Input input : list){
					if(reached == index) {

						return true;

					}

					reached = reached + 1;
				}
			}
		}
		return false;
	}

	//selects the index occurretion of param and changes its value to value
	public void chooseIndexToChange(int index, String value, String param){
		List<resouce> resouce = this.r.getBrowsed().getResouce();
		int reached = 0;
		int select = 0;

		if(param.equals("post")){
			select = 1;
		} else if(param.equals("file")){
			select = 2;
		}

		for(resouce tmp : resouce){
			List<Input> list;
			switch (select){
				case 1: list = tmp.getPost_Params().getInput();
				break;
				case 2: list = tmp.getFile_Params().getInput();
				break;
				default: list = tmp.getGet_Params().getInput();
				break;
			}
			
			if(!list.isEmpty()){

				if(reached == index) {
					
					Input i = list.get(0);
					list.remove(0);

					if(i.getValue().contains(".php")){
						i.setValue(value + ".php");
					} else {
						i.setValue(value);
					}
					list.add(0, i);

					break;
				}

				reached = reached + 1;
			}
		}
	}

	public void chooseVariableToChange(String variable, String value, String param){
		List<resouce> resouce = this.r.getBrowsed().getResouce();int select = 0;

		if(param.equals("post")){
			select = 1;
		} else if(param.equals("file")){
			select = 2;
		}

		for(resouce tmp : resouce){
			List<Input> listInput;
			switch (select){
				case 1: listInput = tmp.getPost_Params().getInput();
				break;
				case 2: listInput = tmp.getFile_Params().getInput();
				break;
				default: listInput = tmp.getGet_Params().getInput();
				break;
			}

			if(!listInput.isEmpty()){
				
				for(Input input : listInput){
					if(input.getName().equals(variable)){
						int index = listInput.indexOf(input);
						listInput.remove(index);

						if(input.getValue().contains(".php")){
							input.setValue(value + ".php");
						} else {
							input.setValue(value);
						}
						listInput.add(index, input);

						break;
					}
				}
			}
		}
	}

	public String createURL(List<String> variables, List<String> values){
		List<resouce> resouce = this.r.getBrowsed().getResouce();
		int size = variables.size();
		String url = "initial";

		for(resouce tmp : resouce){
			List<Input> listInput;
			
			listInput = tmp.getPost_Params().getInput();
			
			if(!listInput.isEmpty()){
				for(Input input : listInput){
					if(variables.contains(input.getName())){
						size = size - 1;
						if(size == 0){
							url = tmp.getReferer();
							break;
						}
					}
				}
			}
			
			listInput = tmp.getFile_Params().getInput();

			if(!listInput.isEmpty()){
				for(Input input : listInput){
					if(variables.contains(input.getName())){
						size = size - 1;
						if(size == 0){
							url = tmp.getReferer();
							break;
						}
					}
				}
			}

			listInput = tmp.getGet_Params().getInput();
			
			if(!listInput.isEmpty()){
				for(Input input : listInput){
					if(variables.contains(input.getName())){
						size = size - 1;
						if(size == 0){
							url = tmp.getReferer();
							break;
						}
					}
				}
			}
		}
		if(!url.equals("initial")){
			for(int i = 0; i < variables.size(); i++){
				if(i!=0){
					url = url + "&" + variables.get(i) + "=" + values.get(i);
				} else {
					url = url + "?" + variables.get(i) + "=" + values.get(i);
				}
			}
			return url;
		}

		return null;
	}
}

