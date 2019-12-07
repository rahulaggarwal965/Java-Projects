package compSciPackage;
import java.awt.Point;

public class CompSci { 

	public static boolean cigarParty(int num, boolean isWeekend) {
		return (num >= 40) ? (num <= 60 || isWeekend) : false;
	}
	
	public static int blackjack(int num1, int num2) {
		if (num1 > 21 && num2 > 21) {
			return 0;
		} else if(num1 > 21 || num2 > 21) {
			return Math.min(num1, num2);
		} else {
			return Math.max(num1, num2);
		}
	}
	
	public static int naiveMinCoins(int cents, int l) {
		if(cents == 0) return 0;
		else if (cents < 0) return Integer.MAX_VALUE;
		else return Math.min(naiveMinCoins(cents - 25, l + 1), Math.min(naiveMinCoins(cents - 10, l + 1), 1 + naiveMinCoins(cents-1, l + 1)));
	}
	
	public static int naiveMinCoins(int cents, int[] coins) {
		if(cents == 0) return 0;
		else {
			int min = Integer.MAX_VALUE;
			for (int c: coins) {
				if (c <= cents) {
					int n = naiveMinCoins(cents - c, coins);
					if(n != Integer.MAX_VALUE) {
						min = (n + 1 < min) ? n + 1 : min;
					}
				}
			}
			return min;
		}
	}
	
	public static int minCoins(int cents) {
		return 1;
	}
	
	public static double squareRoot(double n) {
		double prev = n, next = (n/2 + n/(n/2))/2;
		while(Math.abs(next - prev) > 0.0001) {
			prev = next;
			next = (prev + n/prev)/2;
		}
		return next;
	}
	
	public static void sieveOfEratosthenes(int n) {
		boolean[] p = new boolean[n-1];
		for (int i = 0; i < p.length; i++) {
			p[i] = true;
		}
		
		for (int i = 2; i <= (int) Math.sqrt(n); i++) {
			if(p[i-2]) {
				for (int j = i * i; j <= n; j += i) {
					p[j-2] = false;
				}
			}
		}
		
		for (int i = 0; i < p.length; i++) {
			if(p[i]) System.out.println(i+2);
		}
	}
	
	public static int[] sieveOfEratosthenesReturn(int n) {
		boolean[] p = new boolean[n-1];
		int count = n - 1;
		for (int i = 0; i < p.length; i++) {
			p[i] = true;
		}
		
		for (int i = 2; i <= (int) Math.sqrt(n); i++) {
			if(p[i-2]) {
				for (int j = i * i; j <= n; j += i) {
					if(p[j-2]) {
						p[j-2] = false;
						count--;
					}
				}
			}
		}
		
		int[] r = new int[count];
		for (int i = 0, j = 0; i < p.length && j < r.length; i++) {
			if(p[i]) {
				r[j] = i+2;
				j++;
			}
		}
		return r;
	}
	
	public static int getSmallestPosition(int[] array, int n) {
		int minIndex = 0;
		for (int i = n+1; i < array.length; i++) {
			if(array[i] < array[minIndex]) minIndex = i;
		}
		return minIndex;
	}
	
	public static void merge(int[] array, int l, int m, int r) {
		int n1 = m - l + 1;
		int n2 = r - m;
		
		int[] lowHalf = new int[n1];
		int[] highHalf = new int[n2];
		
		for (int i = 0; i < n1; i++) {
			lowHalf[i] = array[l + i];
		}
		for (int j = 0; j < n2; j++) {
			highHalf[j] = array[m + 1 + j];
		}
		
		int i = 0, j = 0, k = l;
		while(i < n1 && j < n2) {
			if (lowHalf[i] <= highHalf[j]) {
				array[k] = lowHalf[i];
				i++;
			} else {
				array[k] = highHalf[j];
				j++;
			}
			k++;
		}
		
		while( i < n1) {
			array[k] = lowHalf[i];
			i++; 
			k++;
		}
		
		while( j < n2) {
			array[k] = highHalf[j];
			j++; 
			k++;
		}
	}
	
	public static int fibonacci(int n) {
		if(n == 1 || n == 2 ) return 1;
		else return fibonacci( n - 1) + fibonacci(n - 2);
	}
	
	public static void mergeSort(int[] array, int l, int r) {
		if (l < r) {
			int m = (l + r)/2;
			mergeSort(array, l, m);
			mergeSort(array, m + 1, r);
			
			merge(array, l, m, r);
		}

	}
	
	public static void mergeSort(int[] array) {
		mergeSort(array, 0, array.length - 1);
	}
	
	public static int numDigits(int a) {
		if(a > 0) {
			return 1 + numDigits(a/10);
		} else {
			return 0;
		}
	}
	
	public static int sumDigits(int a) {
		if(a > 0) {
			return a % 10 + sumDigits(a/10);
		} else {
			return 0;
		}
	}
	
	public static int strLength(String s) {
		if (!(s.equals(""))) {
			return 1 + strLength(s.substring(1));
		}
		else {
			return 0;
		}
	}
	
	public static int triangularNumber(int n) {
		if (n == 1) return 1;
		else return n + triangularNumber(n - 1);
	}
	
	public static int skipTwo(int n) {
		if (n == 1) return 0;
		else return (n % 2) + 1 + skipTwo(n-1);
	}
	
	public static int skipTwoSum(int n) {
		if (n == 1) return skipTwo(1);
		else return skipTwo(n) + skipTwoSum(n-1);
	}
	
	public static int fibonacciSum(int n) {
		if (n == 1) return 1;
		if (n == 2) return 2;
		else return fibonacciSum(n -1 ) + fibonacciSum(n-2) + 1;
	}

	public static boolean hasSeven(int n) {

		if (n == 0) return false;
		if (n % 10 == 7) {
			return true;
		} else return hasSeven(n / 10);
	}
	
	public static boolean hasX(String s) {
		if (s.equals("")) return false;
		else if(s.charAt(0) == 'x') return true;
		else return hasX(s.substring(1));
	}
	
	public static boolean hasNOccurencesOfX(String s, int n) {
		if(s.equals("")) {
			if(n == 0) return true;
			else return false;
		} else if (s.charAt(0) == 'x') return hasNOccurencesOfX(s.substring(1), n-1);
		else return hasNOccurencesOfX(s.substring(1), n);
	}
	
	public static String removeX(String s) {
		if(s.equals("")) return s;
		char c = s.charAt(0);
		if(c == 'x') return removeX(s.substring(1));
		else return c + removeX(s.substring(1));
	}
	
	public static String reverse(String s) {
		if (s.equals("")) return s;
		else {
			char l = s.charAt(s.length()-1);
			return l + reverse(s.substring(0, s.length()-1));
		}
	}
	
	public static int reverse(int n, int power) {
		if (n == 0) return 0;
		else return (n % 10) * power + reverse(n/10, power/10);
	}
	
	public static int reverse(int n) {
		return reverse(n, (int) Math.pow(10, numDigits(n) - 1));
	}
	
	public static int numFactors(int n, int factor) {
		if(factor == Math.sqrt(n)) return 1;
		else if(factor > (int) Math.sqrt(n)) return 0;
		else if (n % factor == 0) return 2 + numFactors(n, factor+1);
		else return numFactors(n, factor+1);
	}
	
	public static int numFactors(int n) {
		return numFactors(n, 1);
	}
	
	public static int getGCF(int a, int b) {
		if (b != 0) {
			return getGCF(b, a % b);
		} else return a;
	}
	
	
	public static int getLCM(int a, int b) {
		return a * b / getGCF(a, b);
	}
	public static int getLCM(int a, int b, int start) {
		if(start % a == 0 && start % b == 0) {
			return start;
		} else return getLCM(a, b, start+1);
	}
	
	public static String replaceX(String word) {
		if(word.equals("")) return word;
		else if(word.charAt(0) == 'x') return 't' + replaceX(word.substring(1));
		else return word.charAt(0) + replaceX(word.substring(1));
	}
	public static void main(String[] args) {
		System.out.println(replaceX("xest will be xomorrow"));
	}
}
