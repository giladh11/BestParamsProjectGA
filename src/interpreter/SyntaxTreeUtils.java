package interpreter;

import java.util.LinkedList;
import java.util.List;

/**
 * this class holds method to create and manipulte function trees
 */
public class SyntaxTreeUtils {


	/**
	 * creates a new tree using the context
	 * @param depth
	 * @param context
     * @return
     */
	public static Expression createTree(int depth, Context context) {
		if (depth > 0) {

			Function f;
			if (Math.random() >= 0.5) {
				f = context.getRandomNonTerminalFunction();
			} else {
				f = context.getRandomTerminalFunction();
			}

			Expression expr = new Expression(f);

			if (f.argumentsCount() > 0) {

				for (int i = 0; i < f.argumentsCount(); i++) {
					Expression child = createTree(depth - 1, context);
					expr.addChild(child);
				}

			} else {

				if (f.isVariable()) {

					String varName = context.getRandomVariableName();
					expr.setVariable(varName);

				}

			}

			for (int i = 0; i < f.coefficientsCount(); i++) {
				expr.addCoefficient(context.getRandomValue());
			}

			return expr;

		} else {

			Function f = context.getRandomTerminalFunction();
			Expression expr = new Expression(f);

			if (f.isVariable()) {

				String varName = context.getRandomVariableName();
				expr.setVariable(varName);

			}

			for (int i = 0; i < f.coefficientsCount(); i++) {
				expr.addCoefficient(context.getRandomValue());
			}

			return expr;

		}
	}

	/**
	 * make the tree a bit smaller in a random way??
	 * @param tree
	 * @param context
     */
	public static void simplifyTree(Expression tree, Context context) {
		if (hasVariableNode(tree)) {
			for (Expression child : tree.getChildren()) {
				simplifyTree(child, context);
			}
		} else {
			double value = tree.eval(context);
			tree.addCoefficient(value);
			tree.removeChilds();
			List<Function> terminalFunctions = context.getTerminalFunctions();
			for (Function f : terminalFunctions) {
				if (f.isNumber()) {
					tree.setFunction(f);
					break;
				}
			}
		}
	}

	/**
	 * cut on leaf of the tree
	 * @param tree
	 * @param context
	 * @param depth
     */
	public static void cutTree(Expression tree, Context context, int depth) {
		if (depth > 0) {
			for (Expression child : tree.getChildren()) {
				cutTree(child, context, depth - 1);
			}
		} else {
			tree.removeChilds();
			tree.removeCoefficients();
			Function func = context.getRandomTerminalFunction();
			tree.setFunction(func);
			if (func.isVariable()) {
				tree.setVariable(context.getRandomVariableName());
			} else {
				tree.addCoefficient(context.getRandomValue());
			}
		}
	}

	/**
	 * simple checker
	 * @param tree
	 * @return
     */
	public static boolean hasVariableNode(Expression tree) {
		boolean ret = false;

		if (tree.getFunction().isVariable()) {
			ret = true;
		} else {
			for (Expression child : tree.getChildren()) {
				ret = hasVariableNode(child);
				if (ret) {
					break;
				}
			}
		}

		return ret;
	}

	public static Expression generateTree(String str){
		return  null;
	}

	/**
	 * This method return the number of nodes in the tree of expression.
	 * @return
	 */
	public static int getNumberOfNodes(Expression expression) {
		List<Expression> children = expression.getChildren();
		int numOfNodes = 1;

		if(children.size() != 0)
			for (Expression child : children)
				numOfNodes += getNumberOfNodes(child);

		return numOfNodes;

	}




}
