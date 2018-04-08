import java.util.*;

public class ifCond{
	private int line;
	//if condition
	private String condition;
	//if condition without ||,&&
	private String[] cond;
	private List<Integer> open;
	private List<Integer> close;
	private List<String> indexes = new ArrayList<String>();
	private List<variable> variables = new ArrayList<variable>();
	
	public ifCond(int line, String condition, List<variable> variables){
		this.line = line;
		this.condition = condition;
		this.variables = variables;
	}

	public int getLine(){
		return this.line;
	}

	public String[] getConditions(){
		return this.cond;
	}

	//an ordered list with parenthesis and boolean operators (||,&&)
	public List<String> getOperators(){
		return indexes;
	}

	public List<variable> getVariables(){
		return this.variables;
	}

	//this functions finds || and && in the conditions
	public void findAndOr(){
		//Search for parenthesis
		int indexOpen = condition.indexOf("(");
		int indexClose = condition.indexOf(")");
		open = new ArrayList<Integer>();
		close = new ArrayList<Integer>();
		Stack<String> op = new Stack<String>();
		while((indexClose >= 0) || (indexOpen >= 0)){
			if(indexOpen >= 0){
				open.add(indexOpen);
				op.push("open");
				indexOpen = condition.indexOf("(", indexOpen + 1);
			}
			if(indexClose >= 0){
				close.add(indexClose);
				indexClose = condition.indexOf(")", indexClose + 1);
			}
		}
		open.add(-1);
		close.add(-1);

		//Search for || and &&
		int indexOr = condition.indexOf("||");
		int indexAnd = condition.indexOf("&&");

		//Search condition in the if
		int iter = 0;
		int maxIter = this.variables.size();

		int i = 0;
		if(open.get(i)==0){
			indexes.add("OPEN");
			if((condition.indexOf(variables.get(iter).getName()) - 1) == open.get(i)) {
				indexes.add("COND" + iter);
				if(iter < maxIter) {
					iter = iter + 1;
				}
			}
			i = 1;
		}

		if(condition.indexOf(variables.get(iter).getName()) == 0) {
			indexes.add("COND" + iter);
			if(iter < maxIter) {
				iter = iter + 1;

			}
		}

		int j = 0;
		while ((indexOr >= 0)||(indexAnd >= 0)) {  // indexOf returns -1 if no match found
			if((indexOr > indexAnd) && (indexAnd >= 0)){
				if(indexAnd == close.get(j)+1) {
					indexes.add("CLOSE");
					op.pop();
					j = j + 1;
				}
				indexes.add("AND");

				if((condition.indexOf(variables.get(iter).getName()) - 2) == indexAnd) {
					indexes.add("COND" + iter);
					if(iter < maxIter) {
						iter = iter + 1;
					}
				}

				if(indexAnd == open.get(i)-2){
					indexes.add("OPEN");
					if((condition.indexOf(variables.get(iter).getName()) - 1) == open.get(i)) {
						indexes.add("COND" + iter);
						if(iter < maxIter) {
							iter = iter + 1;
						}
					}
					i = i + 1;
				}
				indexAnd = condition.indexOf("&&", indexAnd + 1);
			} else if((indexOr > indexAnd)){
				if(indexOr == close.get(j)+1){
					indexes.add("CLOSE");
					op.pop();
					j = j + 1;
				}
				indexes.add("OR");

				if((condition.indexOf(variables.get(iter).getName()) - 2) == indexOr) {
					indexes.add("COND" + iter);
					if(iter < maxIter) {
						iter = iter + 1;
					}
				}

				if(indexOr == open.get(i)-2){
					indexes.add("OPEN");
					if((condition.indexOf(variables.get(iter).getName()) - 1) == open.get(i)) {
						indexes.add("COND" + iter);
						if(iter < maxIter) {
							iter = iter + 1;
						}
					}
					i = i + 1;
				}
				indexOr = condition.indexOf("||", indexOr + 1);	
			} else if((indexAnd > indexOr) && (indexOr >= 0)){
				if(indexOr == close.get(j)+1){
					indexes.add("CLOSE");
					op.pop();
					j = j + 1;
				}
				indexes.add("OR");

				if((condition.indexOf(variables.get(iter).getName()) - 2) == indexOr) {
					indexes.add("COND" + iter);
					if(iter < maxIter) {
						iter = iter + 1;
					}
				}

				if(indexOr == open.get(i)-2){
					indexes.add("OPEN");
					if((condition.indexOf(variables.get(iter).getName()) - 1) == open.get(i)) {
						indexes.add("COND" + iter);
						if(iter < maxIter) {
							iter = iter + 1;
						}
					}
					i = i + 1;
				}
				indexOr = condition.indexOf("||", indexOr + 1);
			} else if((indexAnd > indexOr)){
				if(indexAnd == close.get(j)+1){
					indexes.add("CLOSE");
					op.pop();
					j = j + 1;
				}

				indexes.add("AND");

				if((condition.indexOf(variables.get(iter).getName()) - 2) == indexAnd) {
					indexes.add("COND" + iter);
					if(iter < maxIter) {
						iter = iter + 1;
					}
				}

				if(indexAnd == open.get(i)-2){
					indexes.add("OPEN");
					if((condition.indexOf(variables.get(iter).getName()) - 1) == open.get(i)) {
						indexes.add("COND" + iter);
						if(iter < maxIter) {
							iter = iter + 1;
						}
					}
					i = i + 1;
				}
				indexAnd = condition.indexOf("&&", indexAnd + 1);
			}
		}
		if(!op.empty() && ((condition.indexOf("||") > 0) || (condition.indexOf("&&") > 0))){
			indexes.add("CLOSE");
		}

		// condition = condition.replace("\\\"", "\"");
		condition = condition.replace("(", "");
		condition = condition.replace(")", "");
		cond = condition.split("\\|\\||&&");
		
	}

}