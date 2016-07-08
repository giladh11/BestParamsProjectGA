/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * dont really get this!!
 * defines the basic structure of the trees used
 *
 */
public class Expression implements Cloneable, AbstractExpression {

	private List<Expression> childs = new ArrayList<Expression>();

	private List<Double> coefficients = new ArrayList<Double>();

	private String variable;

	private Function function;

	public Expression(Function function) {
		this.function = function;
	}

	@Override
	public double eval(AbstractContext context) {
		return this.function.eval(this, (Context)context);
	}

	public String print() {
		return this.function.print(this);
	}

	public String toString(){return this.function.print(this);}

	public List<Expression> getChilds() {
		return this.childs;
	}

	/**
	 * dont really get this!!
	 * @param childs
	 * @return
     */
	public Expression setChilds(List<Expression> childs) {
		this.childs = childs;
		return this;
	}

	/**
	 * dont really get this!!
	 * @param child
     */
	public void addChild(Expression child) {
		this.childs.add(child);
	}

	/**
	 * dont really get this!!
	 */
	public void removeChilds() {
		this.childs.clear();
	}

	/**
	 * dont really get this!!
	 * @return
     */
	public List<Double> getCoefficientsOfNode() {
		return this.coefficients;
	}

	/**
	 * dont really get this!!
	 * @param coefficients
	 * @return
     */
	public Expression setCoefficientsOfNode(List<Double> coefficients) {
		this.coefficients = coefficients;
		return this;
	}

	/**
	 * dont really get this!!
	 * @param coefficient
     */
	public void addCoefficient(double coefficient) {
		this.coefficients.add(coefficient);
	}

	/**
	 * dont really get this!!
	 */
	public void removeCoefficients() {
		if (this.coefficients.size() > 0) {
			this.coefficients.clear();
		}
	}

	/**
	 * dont really get this!!
	 * @return
     */
	public String getVariable() {
		return this.variable;
	}

	/**
	 * dont really get this!!
	 * @param variable
	 * @return
     */
	public Expression setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	/**
	 * dont really get this!!
	 * @return
     */
	public Function getFunction() {
		return this.function;
	}

	/**
	 * dont really get this!!
	 * @param function
     */
	public void setFunction(Function function) {
		this.function = function;
	}

	/**
	 * dont really get this!!
	 * @return
     */
	public Expression clone() {
		Expression cloned = new Expression(this.function);
		if (this.variable != null) {
			cloned.variable = new String(this.variable);
		}
		for (Expression c : this.childs) {
			cloned.childs.add(c.clone());
		}
		for (Double d : this.coefficients) {
			cloned.coefficients.add(d);
		}
		return cloned;
	}

	/**
	 * dont really get this!!
	 * @return
     */
	public List<Double> getCoefficientsOfTree() {
		LinkedList<Double> coefficients = new LinkedList<Double>();
		this.getCoefficientsOfTree(coefficients);
		Collections.reverse(coefficients);
		return coefficients;
	}

	/**
	 * dont really get this!!
	 * @param coefficients
     */
	private void getCoefficientsOfTree(Deque<Double> coefficients) {
		List<Double> coeffs = this.function.getCoefficients(this);
		for (Double d : coeffs) {
			coefficients.push(d);
		}
		for (int i = 0; i < this.childs.size(); i++) {
			this.childs.get(i).getCoefficientsOfTree(coefficients);
		}
	}

	/**
	 * dont really get this!!
	 * @param coefficients
     */
	public void setCoefficientsOfTree(List<Double> coefficients) {
		this.setCoefficientsOfTree(coefficients, 0);
	}

	/**
	 * dont really get this!!
	 * @param coefficients
	 * @param index
     * @return
     */
	private int setCoefficientsOfTree(List<Double> coefficients, int index) {
		this.function.setCoefficients(this, coefficients, index);
		index += this.function.coefficientsCount();
		if (this.childs.size() > 0) {
			for (int i = 0; i < this.childs.size(); i++) {
				index = this.childs.get(i).setCoefficientsOfTree(coefficients, index);
			}
		}
		return index;
	}

	/**
	 * dont really get this!!
	 * @return
     */
	public List<Expression> getAllNodesAsList() {
		List<Expression> nodes = new LinkedList<Expression>();
		this.getAllNodesBreadthFirstSearch(nodes);
		return nodes;
	}

	/**
	 * dont really get this!!
	 * non-recursive Breadth-first iteration over all node of syntax tree
	 */
	private void getAllNodesBreadthFirstSearch(List<Expression> nodesList) {
		int indx = 0;
		nodesList.add(this);
		while (true) {
			if (indx < nodesList.size()) {
				Expression node = nodesList.get(indx++);
				for (Expression child : node.childs) {
					nodesList.add(child);
				}
			} else {
				break;
			}
		}
	}

	/**
	 * TODO TAL write a method that returns the number of nodes in the tree
	 * @return
     */
	public int getNumberOfNodes() {
		return 1;
	}
}
