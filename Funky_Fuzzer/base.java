import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.io.FileOutputStream;
import java.io.FileNotFoundException; 
import java.io.*;

import javax.xml.bind.JAXBContext;  
import javax.xml.bind.JAXBException;  
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import java.util.ArrayList;

public class base {

	public static int newPositionToInject(List<variable> toExplore,
		List<variable> varExplored, int positionToInject) {

		variable v = toExplore.get(positionToInject);
		//varExplored
		for(int explored = 0; explored < varExplored.size(); explored++) {
			
			if(v.getName().equals(varExplored.get(explored).getName()) 
				&& v.getValue().equals(varExplored.get(explored).getValue())){
				explored = 0;
			positionToInject = positionToInject - 1;
			v = toExplore.get(positionToInject);
		}

	}
	return positionToInject;

}



public static void main(String[] args) {
	List<variable> varExplored = new ArrayList<variable>();

	fromxml fx = new fromxml();
	fx.Unmarshall();


	List<String> variables = new ArrayList<String>();
	List<String> values = new ArrayList<String>();
	int i = 0;
	while (fx.notEnded(i)) {
		boolean anotherURL = false;
		for(; anotherURL == false; i++){
			variables.add(fx.variableByIndex(i));
			values.add("" + (i + 1));
			anotherURL = fx.differentURL(i);
		}

		String url = fx.createURL(variables, values);

		Inject a = new Inject(url);
		a.inject();

		detector d = new detector();
		d.interpretLog("/tmp/test.log");

		List<ifCond> conditions = d.getConditions();
		List<String> stack = d.getIfStack();

		solver s =  new solver(conditions, stack);
		s.configureContext();
		List<variable> v = new ArrayList<variable>();
		v = s.injectables(v);

		int valueToInject = -1;
		int positionToInject = v.size() - 1;
		int sizeBeforeInjection = v.size();

		while(v.size() > varExplored.size()) {

			int locationToInject = variables.indexOf(
				v.get(positionToInject).getName());

			values.set(locationToInject, "" + 
				(Integer.parseInt(v.get(positionToInject).
					getValue()) + valueToInject));

			valueToInject = valueToInject + 1;

			url = fx.createURL(variables, values);

			a = new Inject(url);
			a.inject();

			d = new detector();
			d.interpretLog("/tmp/test.log");

			conditions = d.getConditions();
			stack = d.getIfStack();

			s =  new solver(conditions, stack);
			s.configureContext();
			v = s.injectables(v);
			if(valueToInject == 2) {
				varExplored.add(v.get(positionToInject));

				if((sizeBeforeInjection == v.size()) && (positionToInject != 0)) {

					positionToInject = newPositionToInject(v,
						varExplored, positionToInject - 1);
				} else {
					positionToInject = v.size() - 1;
				}
				valueToInject = -1;
			}
		}
		i = i + variables.size();
		variables = new ArrayList<String>();
		values = new ArrayList<String>();
	}

} 
}