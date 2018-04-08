import java.util.*;
import java.util.stream.Collectors;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Model;
import com.microsoft.z3.Status;

public class solver{
	private Context context;
	private List<ifCond> conditions = null;
	private List<variable> variables = null;
	private Map<String, Expr> expressions = null;
	private List<String> ifOrder = null;

	private List<String> solvers = null;
	
	public solver(List<ifCond> conditions, List<String> ifOrder) {
		this.conditions = conditions;
		this.expressions = new TreeMap<String, Expr>();
		this.ifOrder = ifOrder;
		this.solvers = new ArrayList<String>();
	}

	public boolean existVariable(List<variable> variablesToReturn, variable v) {
		for(variable var : variablesToReturn) {
			if(var.getName().equals(v.getName()) && 
				var.getValue().equals(v.getValue())) {
				return true;
		}
	} 

	return false;
}

public List<variable> injectables(List<variable> variablesToReturn) {
	for(String s : solvers){
		String[] splitting = s.split("\\n");

		for(String line : splitting) {

			if(line.contains("assert")) {
				int variableLocation = line.indexOf("$");

				while(variableLocation >= 0) {
					int valueLocation = line.indexOf(" ", variableLocation) + 1;
					int finalValue = line.indexOf(")", variableLocation);

					String value = ((String)line.subSequence
						(valueLocation, finalValue));

					String nameVariable = ((String)line.subSequence
						(variableLocation + 1, valueLocation - 1));

					variable v = new variable(nameVariable, value,
						0, false);
					if(!existVariable(variablesToReturn, v)){

						variablesToReturn.add(v);

						System.out.println("name: " + nameVariable
							+ " value:" + value + " " +variablesToReturn.indexOf(v));
					}

					variableLocation = line.indexOf("$", variableLocation + 1);
				}
			}

		}
	}
	return variablesToReturn;
}

public BoolExpr createCondition(String condition) {

	variable v = this.variables.get(
		Integer.parseInt(
			Character.toString(condition.charAt(4))));
	String newCondition = v.getName();



	if(newCondition.contains(">=")) {
		String[] splitCondition = newCondition.split(">=");

		ArithExpr a = null;

		if(!expressions.containsKey(splitCondition[0])) {
			a = (ArithExpr) context.mkConst(context.mkSymbol(splitCondition[0]), 
				context.getIntSort());
			expressions.put(splitCondition[0], a);
		} else {
			a = (ArithExpr)expressions.get(splitCondition[0]);
		}

		BoolExpr b = context.mkGe(a, context.mkInt(Integer.parseInt(splitCondition[1])));

		return b;

			//context.
	} else if(newCondition.contains(">")) {
		String[] splitCondition = newCondition.split(">");

		ArithExpr a = null;

		if(!expressions.containsKey(splitCondition[0])) {
			a = (ArithExpr) context.mkConst(context.mkSymbol(splitCondition[0]), 
				context.getIntSort());
			expressions.put(splitCondition[0], a);
		} else {
			a = (ArithExpr)expressions.get(splitCondition[0]);
		}

		BoolExpr b = context.mkGt(a, context.mkInt(Integer.parseInt(splitCondition[1])));

		return b;

			//context.
	} else if(newCondition.contains("<=")) {
		String[] splitCondition = newCondition.split("<=");

		ArithExpr a = null;

		if(!expressions.containsKey(splitCondition[0])) {
			a = (ArithExpr) context.mkConst(context.mkSymbol(splitCondition[0]), 
				context.getIntSort());
			expressions.put(splitCondition[0], a);
		} else {
			a = (ArithExpr)expressions.get(splitCondition[0]);
		}

		BoolExpr b = context.mkLe(a, context.mkInt(Integer.parseInt(splitCondition[1])));

		return b;
	} else if(newCondition.contains("<")) {
		String[] splitCondition = newCondition.split("<");

		ArithExpr a = null;

		if(!expressions.containsKey(splitCondition[0])) {
			a = (ArithExpr) context.mkConst(context.mkSymbol(splitCondition[0]), 
				context.getIntSort());
			expressions.put(splitCondition[0], a);
		} else {
			a = (ArithExpr)expressions.get(splitCondition[0]);
		}

		BoolExpr b = context.mkLt(a, context.mkInt(Integer.parseInt(splitCondition[1])));

		return b;
	}

	return context.mkTrue();
}

public Model getMax(Solver solver) {
	Model model = null;
	int i = 0;
	while(true) {
		Status status = solver.check();
		if(status.toInt() == -1) {
			if(model != null) {
				return model;
			} else {
				return null;
			}
		}
		model = solver.getModel();

		solver.add(context.mkGt((ArithExpr)expressions.get
			(((TreeMap)expressions).firstKey()),
			context.mkInt(Integer.parseInt(model.getConstInterp(expressions.get
				(((TreeMap)expressions).firstKey())).toString()))));
		if(i>1000)
			return model;
		i = i + 1;		
	}
}

public Model getMin(Solver solver) {
	Model model = null;
	int i = 0;
	while(true) {
		Status status = solver.check();
		if(status.toInt() == -1) {
			if(model != null) {
				return model;
			} else {
				return null;
			}
		}
		model = solver.getModel();

		solver.add(context.mkLt((ArithExpr)expressions.get
			(((TreeMap)expressions).firstKey()),
			context.mkInt(Integer.parseInt(model.getConstInterp(expressions.get
				(((TreeMap)expressions).firstKey())).toString()))));

		if(i>1000)
			return model;
		i = i + 1;
	}
}

public void configureContext(){
	this.context = new Context();
	Solver solverMax = context.mkSolver();
	Solver solverMin = context.mkSolver();
	int iterateIfOrder = 0;
	int intoDeep = 0;
	List<BoolExpr> saveBool = new ArrayList<BoolExpr>();
	Stack<BoolExpr> bools = new Stack<BoolExpr>();
	List<Solver> allSolvers = new ArrayList<Solver>();
	
	try {
		for(ifCond ifs : conditions){

			variables = ifs.getVariables();
			String[] cond = ifs.getConditions();
			List<String> op = ifs.getOperators();
			List<BoolExpr> boolExpressions = new ArrayList<BoolExpr>();

			Stack<String> parenthesis = new Stack<String>();
			Stack<String> condition = new Stack<String>();
				//AND, OR
			Stack<String> contexts = new Stack<String>();

			for(String value : op) {
				if(value.contains("OPEN")) {
					if(condition.empty()) {
						parenthesis.push("OPEN");
					} else {
						parenthesis.push(condition.pop());
						parenthesis.push("OPEN");
					}

				} else if(value.contains("CLOSE")) { 
					parenthesis.pop();
					String toAdd;

					if((!parenthesis.empty()) && (!condition.empty())) {

						if(parenthesis.peek().contains("COND")) {
							BoolExpr e = null;
							if(condition.peek().contains("COND")) {
								if(contexts.peek().contains("AND")) {
									contexts.pop();
									e = context.mkAnd(
										this.createCondition(condition.peek()),
										this.createCondition(parenthesis.peek()));
								} else if(contexts.peek().contains("OR")) {
									contexts.pop();
									e = context.mkOr(
										this.createCondition(condition.peek()),
										this.createCondition(parenthesis.peek()));
								}
								boolExpressions.add(e);
								toAdd = "BOOL" + (boolExpressions.size() - 1);
								parenthesis.pop();
								condition.pop();
								condition.push(toAdd);
							} else {
								int index = Integer.parseInt(
									Character.toString(condition.peek().charAt(4)));
								if(contexts.peek().contains("AND")) {
									contexts.pop();
									e = context.mkAnd(
										boolExpressions.get(index),
										this.createCondition(parenthesis.peek()));
								} else if(contexts.peek().contains("OR")) {
									contexts.pop();
									e = context.mkOr(
										boolExpressions.get(index),
										this.createCondition(parenthesis.peek()));
								}
								boolExpressions.remove(index);

								toAdd = "BOOL" + index;
								parenthesis.pop();
								condition.pop();
								condition.push(toAdd);
							}
						} else if(parenthesis.peek().contains("BOOL")) {

							BoolExpr e;
							if(condition.peek().contains("COND")) {
								int index = Integer.parseInt(
									Character.toString(parenthesis.peek().charAt(4)));
								if(contexts.peek().contains("AND")) {
									contexts.pop();
									e = context.mkAnd(
										boolExpressions.get(index),
										this.createCondition(condition.peek()));
								} else if(contexts.peek().contains("OR")) {
									contexts.pop();
									e = context.mkOr(
										boolExpressions.get(index),
										this.createCondition(condition.peek()));
								}
								boolExpressions.remove(index);

								toAdd = "BOOL" + index;
								parenthesis.pop();
								condition.pop();
								condition.push(toAdd);
							} else {
								int indexA = Integer.parseInt(
									Character.toString(parenthesis.peek().charAt(4)));
								int indexB = Integer.parseInt(
									Character.toString(condition.peek().charAt(4)));

								if(contexts.peek().contains("AND")) {
									contexts.pop();
									e = context.mkAnd(
										boolExpressions.get(indexA),
										boolExpressions.get(indexB));
								} else if(contexts.peek().contains("OR")) {
									contexts.pop();
									e = context.mkAnd(
										boolExpressions.get(indexA),
										boolExpressions.get(indexB));
								}

								boolExpressions.remove(indexA);
								boolExpressions.remove(indexB);

								toAdd = "BOOL" + indexA;
								parenthesis.pop();
								condition.pop();
								condition.push(toAdd);
							}
						}
					}
				} else if(value.contains("COND")) {
					if(!condition.empty()){
						BoolExpr e = null;
						String toAdd;

						if(condition.peek().contains("COND")) {
							if(contexts.peek().contains("AND")) {
								contexts.pop();
								e = context.mkAnd(
									this.createCondition(condition.peek()),
									this.createCondition(value));
							} else if(contexts.peek().contains("OR")) {
								contexts.pop();
								e = context.mkOr(
									this.createCondition(condition.peek()),
									this.createCondition(value));
							}
							boolExpressions.add(e);

							toAdd = "BOOL" + (boolExpressions.size() - 1);
							condition.pop();
							condition.push(toAdd);

						} else {
							int index = Integer.parseInt(
								Character.toString(condition.peek().charAt(4)));
							if(contexts.peek().contains("AND")) {
								contexts.pop();
								e = context.mkAnd(
									boolExpressions.get(index),
									this.createCondition(value));
							} else if(contexts.peek().contains("OR")) {
								contexts.pop();
								e = context.mkOr(
									boolExpressions.get(index),
									this.createCondition(value));
							}
							boolExpressions.remove(index);

							toAdd = "BOOL" + index;
							condition.pop();
							condition.push(toAdd);
						}
					} else {
						condition.push(value);
					}

				} else if(value.contains("AND")) {
					contexts.push("AND");

				} else if(value.contains("OR")) {
					contexts.push("OR");

				}
			}
			BoolExpr b = null;
			if((!condition.empty()) && (condition.peek().contains("COND"))){
				b = context.mkOr(this.createCondition(condition.peek()),
					this.createCondition(condition.peek()));
			} else if(!condition.empty())
				b = boolExpressions.get(Integer.parseInt(
					Character.toString(condition.peek().charAt(4))));

			boolean alreadyEnded = false;
			boolean alreadyEncounteredIf = false;

			while((iterateIfOrder < ifOrder.size()) && (!op.isEmpty())) {
				if(ifOrder.get(iterateIfOrder).contains("ENDIF")) {
					if(!alreadyEnded) {
						Solver s = context.mkSolver();

						for(int i = 0; i < intoDeep; i++){
							s.add(saveBool.get(i));
						}								

						allSolvers.add(s);
						alreadyEnded = true;
					}

					intoDeep = intoDeep - 1;
				} else {
					if(ifOrder.get(iterateIfOrder).contains("BEGINIF")) {
						if(alreadyEncounteredIf){
							break;
						}

						saveBool.add(intoDeep, b);

						intoDeep = intoDeep + 1;
						alreadyEncounteredIf = true;
					} else if(ifOrder.get(iterateIfOrder).contains("BEGINELSE")) {
						if(alreadyEncounteredIf){
							break;
						}

						saveBool.add(intoDeep, b);

						intoDeep = intoDeep + 1;
						alreadyEncounteredIf = true;
					}
				}
				iterateIfOrder = iterateIfOrder + 1;
			}		

		} 
	} catch (EmptyStackException e) {
		e.printStackTrace();
		System.out.println("Something wrong with the stacks");
	}

	boolean max = false;
	for(Solver s : allSolvers){

		s.check();
			//System.out.println("solver: " + s.toString());
		solvers.add(s.toString());

	}

}

}