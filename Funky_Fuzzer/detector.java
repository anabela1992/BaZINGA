import java.util.*;
import java.io.*;

public class detector{
	private List<String> getVariable = null;
	private List<String> sensitive = null;
	private List<String> attribution = null;
	private boolean ifOpen = false;
	private List<ifCond> cond = null;
	private List<String> ifClose = null;
	private List<variable> attributted = null;
	private List<String> ifs;
	private List<vulnerability> vulns = null;
	private int TRIM = 32;

	public detector(){
		getVariable = new ArrayList<String>();
		sensitive = new ArrayList<String>();
		attribution = new ArrayList<String>();
		cond = new ArrayList<ifCond>();
		ifClose = new ArrayList<String>();
		attributted = new ArrayList<variable>();
		ifs = new ArrayList<String>();
		vulns = new ArrayList<vulnerability>();
	}

	public List<variable> getAttributions(){
		return attributted;
	}

	public List<ifCond> getConditions(){
		return cond;
	}

	public List<String> getIfStack(){
		return this.ifs;
	}

	public boolean entryPoint(String line) {
		for (vulnerability v : vulns) {
			List<String> entry = v.getEntryPoints();
			for(int i = 0; i < entry.size(); i++) {
				if (line.contains(entry.get(i))) {
					return true;
				}
			}
		}
		return false;
	}

	public int isSanitized(String line) {
		for (vulnerability v : vulns) {
			List<String> sanit = v.getSanitFunctions();
			for(int i = 0; i < sanit.size(); i++) {
				if (line.contains(sanit.get(i))) {
					return i;
				}
			}
		}
		return -1;
	}

	public boolean sensitiveSink(String line){
		for (vulnerability v : vulns) {
			List<String> sink = v.getSensitiveSinks();
			for(int i = 0; i < sink.size(); i++) {
				if (line.contains(sink.get(i))) {
					return true;
				}
			}
		}
		return false;
	}

	public void interpretLog(String namefile){
		boolean notVulnerable = true;

		// Read patterns file
		try (BufferedReader br = new BufferedReader(new FileReader("/home/beli/Desktop/Funky_Fuzzer/patterns"))) {
			String line;
			while ((line = br.readLine()) != null) {
				vulnerability vuln = new vulnerability(line);
				line = br.readLine();
				String[] eps = line.split(",");
				for (String s : eps) {
					vuln.addEntryPoint(s);
				}
				line = br.readLine();
				String[] funcs = line.split(",");
				for (String s : funcs) {
					vuln.addSanitFunction(s);
				}
				line = br.readLine();
				String[] sanits = line.split(",");
				for (String s : sanits) {
					vuln.addSensitiveSink(s);
				}
				line = br.readLine();
				vulns.add(vuln);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try(BufferedReader br = new BufferedReader(new FileReader(namefile))) {
			String line = br.readLine();
			boolean entryPoint = false;
			boolean foundIf = false;
			int ePoint = -1;
			while (line != null) {
				if(entryPoint(line)) {
				//if(line.contains("$_GET")||line.contains("$_POST")){
					getVariable.add(line);
					ePoint = isSanitized(line);
					if(ePoint < 0) { 
						entryPoint = true;
					}
				}

				if(line.contains("[\"#BEGINIF\"]")){
					ifs.add("BEGINIF");
					foundIf = true;
				} else if(line.contains("[\"#ENDIF\"]")){
					if(foundIf) {
						ifs.add("ENDIF");
					} else {
						ifs.add("BEGINIF");
						ifs.add("ENDIF");
					}
					foundIf = false;
				} else if(line.contains("[\"#BEGINELSE\"]")) {
					ifs.add("BEGINELSE");
					foundIf = true;
				} else if(line.contains("[\"#ENDELSE\"]")) {
					ifs.add("ENDELSE");
				}

				if(line.matches(".*[i][f][ ]*[(].*")){
					List<variable> vars = new ArrayList<variable>();
					String subString = (String)line.subSequence(TRIM, line.length());
					//splitting[0] is the line of the if
					String[] splitting = subString.split(": ");

					String name = null;

					//creation of variables with its value
					if(splitting.length-1 != 1){
						name = splitting[1];
					}
					for(int i=2; i<splitting.length-1; i++){
						String[] var = splitting[i].split("&&");
						vars.add(new variable(name.replaceAll("\\s",""), var[0], 0, false));
						name = var[1];
					}

					String[] var = splitting[splitting.length-1].split(" ");
					vars.add(new variable(name.replaceAll("\\s",""), var[0], 0, false));
					name = var[1];


					//condition[1] is the if condition
					String[] ifCondition = splitting[splitting.length-1].split(" \\[\"");
					//ifTrimmed is/are the if condition(s) without the if operator
					String ifTrimmed = ((String)ifCondition[1].subSequence
						(0, ifCondition[1].length()-5)).replaceAll("\\s","");
					ifTrimmed = (String)ifTrimmed.subSequence(3, ifTrimmed.length()-1);

					//this a function for finding ||, &&
					/**/
					ifCond cnd = new ifCond(Integer.parseInt(splitting[0]), ifTrimmed, vars);
					cnd.findAndOr();
					cond.add(cnd);

				}

				if(line.matches(".*[$].*[=].*") && !(line.matches(".*[<>].*"))){
					attribution.add(line);
					String subString = (String)line.subSequence(TRIM, line.length());
					//splitting[0] is the line of the attribution
					String[] splitting = subString.split(": ");
					//attribute[0] is the value of the attribution
					String[] attribute = splitting[1].split(" \\[\"");
					//variable[0] is the variable
					String[] variable = attribute[1].split("=");
					String trimmed = variable[0].trim();

					boolean saveState = entryPoint;
					int location = -1;
					int i = 0;
					for(variable var : attributted) {
						if(attribute[0].matches(var.getName())) {
							if (var.getTainted()) {
								entryPoint = true;
							}
						}

						if(trimmed.matches(var.getName())) {
							location = i;
						}

						i = i + 1;

					}

					variable v = new variable(trimmed, attribute[0], Integer.parseInt(splitting[0]), entryPoint);
					attributted.add(v);
					System.out.println("attribution: " + trimmed + " value: " + attribute[0]);
				}

				if(sensitiveSink(line)){
					sensitive.add(line);
					for(variable v : attributted){
						if(line.contains(v.getName()) && v.getTainted()){
							String subString = (String)line.subSequence(TRIM, line.length());
							//splitting[0] is the line of the attribution
							String[] splitting = subString.split(": ");
							System.out.println("VULNERABILITY in line: " + splitting[0]);
						}
					}

					if(entryPoint){
						String subString = (String)line.subSequence(TRIM, line.length());
						//splitting[0] is the line of the attribution
						String[] splitting = subString.split(": ");
						System.out.println("VULNERABILITY in line: " + splitting[0]);
					}
				}

				if (line.contains("%%%%%%%%%%%%%")){
					getVariable = new ArrayList<String>();
					sensitive = new ArrayList<String>();
					attribution = new ArrayList<String>();
					ifOpen = false;
					ifClose = new ArrayList<String>();
					attributted = new ArrayList<variable>();
					System.out.println("%%%%%%%%%%%%%");
				}

				line = br.readLine();
				entryPoint = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("cannot open file");
		}
	}




}