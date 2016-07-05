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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Function trees are using this in the lower level.
 * it holds basic function a tree can be made from
 * it holds the value of the variable (x) in the trees - meaning if you want to evaluate the value of
 * 						a tree function for a specific x you have to set it in the context
 */
public class Context {

	private Random random = new Random();

	private double minValue = -50;

	private double maxValue = 50;

	private double minMutationValue = -3;

	private double maxMutationValue = 3;

	private Map<String, Double> variables = new HashMap<String, Double>();

	private List<Function> nonTerminalFunctions = new ArrayList<Function>();

	private List<Function> terminalFunctions = new ArrayList<Function>();

	private int nextRndFunctionIndx = 0;

	/**
	 * constructor
	 * @param functions
	 * @param variables
     */
	public Context(List<? extends Function> functions, Collection<String> variables) {
		for (Function f : functions) {
			if (f.argumentsCount() == 0) {
				this.terminalFunctions.add(f);
			} else {
				this.nonTerminalFunctions.add(f);
			}
		}
		if (this.terminalFunctions.isEmpty()) {
			throw new IllegalArgumentException("At least one terminal function must be defined");
		}

		if (variables.isEmpty()) {
			throw new IllegalArgumentException("At least one variable must be defined");
		}

		for (String variable : variables) {
			this.setVariable(variable, 0);
		}
	}

	/**
	 * returns the current value of a variable
	 * @param variable
	 * @return
     */
	public double lookupVariable(String variable) {
		return this.variables.get(variable);
	}

	/**
	 * will be used before evluating a function tree
	 * @param variable
	 * @param value
     */
	public void setVariable(String variable, double value) {
		this.variables.put(variable, value);
	}

	/**
	 * chooses a new none terminal function
	 * @return
     */
	public Function getRandomNonTerminalFunction() {
		return this.roundRobinFunctionSelection();
		// return this.randomFunctionSelection();
	}

	// private Function randomFunctionSelection() {
	// int indx = this.random.nextInt(this.nonTerminalFunctions.size());
	// return this.nonTerminalFunctions.get(indx);
	// }

	/**
	 * what???
	 * @return
     */
	private Function roundRobinFunctionSelection() {
		if (this.nextRndFunctionIndx >= this.nonTerminalFunctions.size()) {
			this.nextRndFunctionIndx = 0;
			Collections.shuffle(this.nonTerminalFunctions);
		}
		// round-robin like selection
		return this.nonTerminalFunctions.get(this.nextRndFunctionIndx++);
	}

	public Function getRandomTerminalFunction() {
		int indx = this.random.nextInt(this.terminalFunctions.size());
		Function f = this.terminalFunctions.get(indx);
		return f;
	}

	public List<Function> getTerminalFunctions() {
		return this.terminalFunctions;
	}

	public String getRandomVariableName() {
		int indx = this.random.nextInt(this.variables.keySet().size());
		int i = 0;
		for (String varName : this.variables.keySet()) {
			if (i == indx) {
				return varName;
			}
			++i;
		}
		// Unreachable code
		return this.variables.keySet().iterator().next();
	}

	public double getRandomValue() {
		return (this.random.nextDouble() * (this.maxValue - this.minValue)) + this.minValue;
	}

	public double getRandomMutationValue() {
		return (this.random.nextDouble() * (this.maxMutationValue - this.minMutationValue)) + this.minMutationValue;
	}

	public boolean hasVariables() {
		return !this.variables.isEmpty();
	}

}
