package libraries;

public class Complex {
	private double re;
	private double i;
	
	public Complex(double re, double i) {
		this.re = re;
		this.i = i;
	}
	
	public Complex(Complex c) {
		this.re = c.re;
		this.i = c.i;
	}
	
	public Complex add(Complex c) {
		return new Complex(re+c.re, i + c.i);
	}
	
	public Complex subtract(Complex c) {
		return new Complex(this.re - c.re, this.i - c.i);
	}
	
	public Complex multiply(Complex c) {
		return new Complex(this.re*c.re - this.i*c.i, this.re*c.i + this.i*c.re);
	}
	
	public Complex divide(Complex c) {
		Complex conj = c.conjugate();
		return this.multiply(conj).divide(conj.square());
	}
	
	public Complex conjugate() {
		return new Complex(this.re, -this.i);
	}
	
	public Complex square() {
		return this.multiply(this);
	}
	
	public Complex pow(double n) {
		Complex c = new Complex(this);
		for (int i = 1; i < n; i++) {
			c = c.multiply(this);
		}
		return c;
	}
}
