package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		
		float co = 0;
		int deg = 0;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node polySum = null;
		
		if(poly1 == null) {
			return ptr2;
		}
		if (poly2 == null) {
			return ptr1;
		}	
		
		while (ptr1 != null && ptr2 != null) {
			if (ptr1.term.degree == ptr2.term.degree) {
				deg = ptr1.term.degree;
				co = ptr1.term.coeff + ptr2.term.coeff;
				if (co == 0) {
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
				}
				else {
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					polySum = new Node(co, deg, polySum);
				}
				
			} else if (ptr1.term.degree > ptr2.term.degree) {
				deg = ptr2.term.degree;
				co = ptr2.term.coeff;
				ptr2 = ptr2.next;
				polySum = new Node(co, deg, polySum);

				
			} else if (ptr1.term.degree < ptr2.term.degree){
				deg = ptr1.term.degree;
				co = ptr1.term.coeff;
				ptr1 = ptr1.next;
				polySum = new Node(co, deg, polySum);
			}
			
		}
		
		while(ptr1 != null && ptr2 == null) {
            co = ptr1.term.coeff;
            deg = ptr1.term.degree;
            ptr1 = ptr1.next;
			polySum = new Node(co, deg, polySum);
        }
        while(ptr2 != null && ptr1 == null) {
            co = ptr2.term.coeff;
            deg = ptr2.term.degree;
            ptr2 = ptr2.next;
			polySum = new Node(co, deg, polySum);
        }
        
        Node polySumCopy = null;
        Node ptrTemp = polySum;
       while (ptrTemp != null) {
    	   if (ptrTemp.term.coeff == 0) {
    		   ptrTemp = ptrTemp.next;
    		   continue;
    	   }
    	  polySumCopy = new Node(ptrTemp.term.coeff, ptrTemp.term.degree, polySumCopy);
    	  ptrTemp = ptrTemp.next;
    	 
       }
       return polySumCopy;
       
	}
		
		
		
		
		/*Node ptr1 = poly1;
		Node ptr2 = poly2;
		float co = 0;
		int deg = 0;
		Node polysum = null;
		
		if (poly1 == null) {
			return ptr2;
		}
		if (poly2 == null) {
			return ptr1;
		}
		while (ptr1 != null && ptr2 != null) {
			if (ptr1.term.degree == ptr2.term.degree) {
				co = (ptr1.term.coeff + ptr2.term.coeff);
				if (co == 0) {
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					continue;
				}
				deg = ptr1.term.degree;
				polysum = new Node(co, deg, polysum);
			} 
			if (ptr1.term.degree > ptr2.term.degree) {
				co = ptr1.term.coeff;
				if (co == 0) {
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					continue;
				}
				deg = ptr1.term.degree;
				polysum = new Node(co, deg, polysum);
			}
			if (ptr2.term.degree > ptr1.term.degree) {
				co = ptr2.term.coeff;
				if (co == 0) {
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					continue;
				}
				deg = ptr2.term.degree;
				polysum = new Node(co, deg, polysum);
			}
			ptr1 = ptr1.next;
			ptr2 = ptr2.next;
		}
		return polysum;
	}*/
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		float co = 0;
		int deg = 0;
		Node polyprod = null;
		
		if (poly1 == null || poly2 == null) {
			return polyprod;
		}
		for (Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next) {
			for (Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next) {
				co = (ptr1.term.coeff * ptr2.term.coeff);
				deg = (ptr1.term.degree + ptr2.term.degree);
				Node temp = new Node(co, deg, null);
				polyprod = add(polyprod, temp);
			}
		}
		
		return polyprod;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		float value = 0;
		while (poly != null) {
			value += (poly.term.coeff * (Math.pow(x, poly.term.degree)));
			poly = poly.next;
		}
		return value;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
