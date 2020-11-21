package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
        String newExpr = expr.replaceAll("\\s+","");
        String temp = "";   

        for (int i = 0; i < newExpr.length(); i++) {
            String tempLetter = "";
            temp = newExpr.charAt(i) + "";
            if (Character.isLetter(newExpr.charAt(i)) && ((i + 1) < newExpr.length() - 1)) {
                if (vars.contains(newExpr.charAt(i))) {
                    continue;
                }
                if (arrays.contains(newExpr.charAt(i))) {
                    continue;
                }
                while (Character.isLetter(newExpr.charAt(i))) {
                    tempLetter += newExpr.charAt(i);
                    if (i != newExpr.length() - 1 && newExpr.charAt(i + 1) != '[') {
                        i++;
                    } else {
                        break;
                    }
                }
                if ((i + 1) < newExpr.length() - 1) {
                    if (newExpr.charAt(i + 1) == '[') {
                        arrays.add(new Array(tempLetter));
                        continue;
                    } else {
                        vars.add(new Variable(tempLetter));
                        continue;
                    }
                }
            }
            if (temp.matches("^[a-zA-Z]*$") && temp.length() > tempLetter.length()) {
                vars.add(new Variable(temp));
                continue;
            } else if (temp.matches("^[a-zA-Z]*$") && tempLetter.length() > temp.length()) {
                vars.add(new Variable(tempLetter));
                continue;
            } else if (temp.matches("^[a-zA-Z]*$") && tempLetter.length() == temp.length()) {
                vars.add(new Variable(temp));
                continue;
            }
        }
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	Stack<String> numbers = new Stack<String>();
        Stack<String> operations = new Stack<String>();
        String newExpr = expr.replaceAll("\\s+","");
    	
    	for (int i = 0; i < newExpr.length(); i++) {
            String temp = "";
            String tempLetter = "";
            if (newExpr.charAt(i) == '(') {
                float count = 0;
                int j = 0;
                for (j = i; j < newExpr.length(); j++) {
                    if (newExpr.charAt(j) == ')') {
                        count --;
                    } else if (newExpr.charAt(j) == '(') {
                        count ++;
                    }
                    if (count == 0) {
                        break;
                    }
                }
                numbers.push(evaluate(newExpr.substring(i + 1, j), vars, arrays) + "");
                i = j;
            } 
            while (Character.isLetter(newExpr.charAt(i))) {
                tempLetter += newExpr.charAt(i);
                if (i != newExpr.length() - 1) {
                    i++;
                    if (newExpr.charAt(i) == '[') {
                        tempLetter += newExpr.charAt(i);
                        float count = 0;
                        int j = 0;
                        for (j = i; j < newExpr.length(); j++) {
                            if (newExpr.charAt(j) == ']') {
                                count --;
                            } else if (newExpr.charAt(j) == '[') {
                                count ++;
                            }
                            if (count == 0) {
                                break;
                            }
                        }
                        numbers.push(evaluate(newExpr.substring(i + 1, j), vars, arrays) + "");
                        i = j;
                    } 
                } else {
                    break;
                }
            }
            while (Character.isDigit(newExpr.charAt(i))) {
                temp += newExpr.charAt(i) + "";
                if (i != newExpr.length() - 1) {
                    i++;
                } else {
                    break;
                }
            }
            if (temp.length() != 0) {
                numbers.push(temp);
            }
            if (tempLetter.length() != 0) {
                numbers.push(getValue(tempLetter, vars, arrays, numbers));
            }
            String newtemp = "";
            String newLetter = "";
            if (newExpr.charAt(i) == '+' || newExpr.charAt(i) == '-' || newExpr.charAt(i) == '*' || newExpr.charAt(i) == '/') {
                operations.push(newExpr.charAt(i) + "");  
                if (newExpr.charAt(i) == '*' || newExpr.charAt(i) == '/') {
                    i++;
                    if (newExpr.charAt(i) == '(') {
                        float count = 0;
                        int j = 0;
                        for (j = i; j < newExpr.length(); j++) {
                            if (newExpr.charAt(j) == ')') {
                                count --;
                            } else if (newExpr.charAt(j) == '(') {
                                count ++;
                            }
                            if (count == 0) {
                                break;
                            }
                        }
                        numbers.push(evaluate(newExpr.substring(i + 1, j), vars, arrays) + "");
                        i = j;
                    } 
                    if (Character.isDigit(newExpr.charAt(i))) {
                        while (Character.isDigit(newExpr.charAt(i))) {
                            newtemp += newExpr.charAt(i) + "";
                            if (i != newExpr.length() - 1) {
                                i++;
                            } else {
                                break;
                            }
                        }
                    } else if(Character.isLetter(newExpr.charAt(i))){
                        while (Character.isLetter(newExpr.charAt(i))) {
                            newLetter += newExpr.charAt(i);
                            if (i != newExpr.length() - 1) {
                                i++;
                                if (newExpr.charAt(i) == '[') {
                                    newLetter += newExpr.charAt(i);
                                    float count = 0;
                                    int j = 0;
                                    for (j = i; j < newExpr.length(); j++) {
                                        if (newExpr.charAt(j) == ']') {
                                            count --;
                                        } else if (newExpr.charAt(j) == '[') {
                                            count ++;
                                        }
                                        if (count == 0) {
                                            break;
                                        }
                                    }
                                    numbers.push(evaluate(newExpr.substring(i + 1, j), vars, arrays) + "");
                                    i = j;
                                } 
                            } else {
                                break;
                            }
                        }
                    }
                    if (newExpr.length() - 1 != i) {
                        i--;                        
                    } 
                    if (newtemp.length() != 0) {
                        numbers.push(newtemp);
                    } else if (newLetter.length() != 0) {
                         numbers.push(getValue(newLetter, vars, arrays, numbers));
                    }
                    
                    calculate(numbers, operations);
                }
            }
        }
        Stack<String> reversedNum = new Stack<String>();
        Stack<String> reversedOp = new Stack<String>();

        while (!numbers.isEmpty()) {
            reversedNum.push(numbers.pop());
        }
        while (!operations.isEmpty()) {
            reversedOp.push(operations.pop());
        }
        while (!reversedOp.isEmpty()) {
            calculate(reversedNum, reversedOp);
        }
    	return calculate(reversedNum, reversedOp);
    }

    private static float calculate (Stack<String> numbers, Stack<String> operations) {

        if (numbers.isEmpty() || operations.isEmpty()) {
            return Float.parseFloat(numbers.peek());
        } 
        String a = numbers.pop();
        String b = numbers.pop();
        float newA = 0;
        float newB = 0;

        switch (operations.pop()) { 
            case ("-"):
                newA = Float.parseFloat(a);
                newB = Float.parseFloat(b);
                numbers.push(newA - newB + "");
                break;
            case ("+"):
                newA = Float.parseFloat(a);
                newB = Float.parseFloat(b);
                numbers.push(newA + newB + "");
                break;
            case ("*"):
                newA = Float.parseFloat(a);
                newB = Float.parseFloat(b);
                numbers.push(newA * newB + "");
                break;
            case("/"):
                newA = Float.parseFloat(a);
                newB = Float.parseFloat(b);
                numbers.push(newB / newA + "");
                break;
            default:
                break;
        }
        return Float.parseFloat(numbers.peek());
    }

    private static String getValue (String input, ArrayList<Variable> vars, ArrayList<Array> arrays, Stack<String> numbers) {
        int index = 0;
        String temp = "";
        String newstr = "";
        if (input.contains("[")) {
            for (int j = 0; j < input.length(); j++) {
                if (input.charAt(j) == '[') {
                    break;
                } else {
                    newstr += input.charAt(j);
                }
            }
            for (int i = 0; i < arrays.size(); i++) {
                if (arrays.get(i).name.equals(newstr)) {
                    index = i;
                    break;
                }
            }
            float num = Float.parseFloat(numbers.pop());
            int num2 = (int) num;
            temp = arrays.get(index).values[num2] + "";
        }
        if (!input.contains("[")) {
            for (int i = 0; i < vars.size(); i++) {
                if (vars.get(i).name.equals(input)) {
                    index = i;
                    break;
                }
            }
            float value = vars.get(index).value;
            temp = value + "";
        }       
        return temp;
    }
}