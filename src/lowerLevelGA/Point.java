package lowerLevelGA;

import java.util.HashMap;
import java.util.Map;

/**
 * seems that this class is only used as a building block for dataSet=
 * meaning this is a point:)
 */
public class Point {
	private Map<String, Double> contextState = new HashMap<String, Double>();
	private double targetValue;

	/**
	 * empty constructor
	 */
	public Point() {
	}

	/**
	 * constructor
	 * @param contextState
	 * @param targetValue
     */
	public Point(Map<String, Double> contextState, double targetValue) {
		this.contextState.putAll(contextState);
		this.targetValue = targetValue;
	}

	/**
	 * will probably be used on an empty point. will add a varible called x with the given value.
	 * probably setYwill be called after to set the y valule for this point
	 * @param variableName
	 * @param variableValue
     * @return
     */
	public Point when(String variableName, double variableValue) {
		this.contextState.put(variableName, variableValue);
		return this;
	}

	/**
	 * simple setter
	 * @param targetValue
	 * @return
     */
	public Point setYval(double targetValue) {
		this.targetValue = targetValue;
		return this;
	}

	/**
	 * simple getter
	 * @return
     */
	public double getYval() {
		return this.targetValue;
	}

	public Map<String, Double> getContextState() {
		return this.contextState;
	}
}
