package AoC_10;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AoC_10 {
	static List<Point> _field = new ArrayList<>();
	static int _maxx = 0, _maxy = 0;
	static Point _bestPoint = null;
	static int _bestObservable = 0;

	public static void main(final String[] args) {
		final AoC_10 a = new AoC_10();
		a.run();
	}

	public void run() {
		del1();
		del2();
	}

	public void del2() {
		// räkna ut vinkel för alla punkter, sortera och skjut på dem i ordning
		List<Double> a = new ArrayList<>();
		List<Double> d = new ArrayList<>();
		Point p;

		for (int i = 0; i < _field.size(); i++) {
			p = _field.get(i);
			if (p.equals(_bestPoint)) { // skjut inte dig själv
				d.add(Double.MAX_VALUE); // långt bort
				a.add((double) 100);// utanför intervall
			} else {
				d.add(distance(_bestPoint, p));
				a.add(angle(_bestPoint, p));
			}
		}
		// sortera vinkel avstånd sedan skjut och räkna träffar
		Double currentAngle = 99.0;
		Double currentAngleLimit = -1.0;
		Double currentDistance = Double.MAX_VALUE;
		int bestTarget = Integer.MAX_VALUE;
		int counter = 0;

		while (counter < 210) {
			for (int i = 0; i < _field.size(); i++) {
				if (a.get(i) > currentAngleLimit) {
					if (a.get(i) < currentAngle
							|| (Math.abs(a.get(i) - currentAngle) < 0.000001 && d.get(i) < currentDistance)) {
						currentAngle = a.get(i);
						currentDistance = d.get(i);
						bestTarget = i;
					}
				}
			}
			counter++;
			if (counter == 200) {
				System.out.println(_field.get(bestTarget));
				System.out.println("Del 2: " + (_field.get(bestTarget).x * 100 + _field.get(bestTarget).y));
				break;
			}
			// Fire!!!
			currentAngleLimit = currentAngle + 0.000001;
			currentAngle = 99.0;
			currentDistance = Double.MAX_VALUE;
			if (currentAngleLimit >= Math.PI * 2.0) {
				currentAngleLimit = -1.0;
			}
			_field.remove(bestTarget);
			a.remove(bestTarget);
			d.remove(bestTarget);
		}
		//print(_maxx, _maxy, _field);
	}

	private Double angle(Point o, Point p) {
		if (o.x == p.x && o.y > p.y) {
			return (double) 0;
		}
		if (o.x == p.x && o.y < p.y) {
			return Math.PI;
		}
		if (o.x < p.x && o.y == p.y) {
			return Math.PI / 2.0;
		}
		if (o.x > p.x && o.y == p.y) {
			return Math.PI * 3.0 / 2.0;
		}
		if (o.x < p.x && o.y > p.y) {
			return Math.atan((p.getX() - o.getX()) / (o.getY() - p.getY()));
		}
		if (o.x < p.x && o.y < p.y) {
			return Math.PI / 2.0 + Math.atan((p.getY() - o.getY()) / (p.getX() - o.getX()));
		}
		if (o.x > p.x && o.y < p.y) {
			return Math.PI + Math.atan((o.getX() - p.getX()) / (p.getY() - o.getY()));
		}
		if (o.x > p.x && o.y > p.y) {
			return Math.PI * 3.0 / 2.0 + Math.atan((o.getY() - p.getY()) / (o.getX() - p.getX()));
		}
		System.out.println("fel på angle");
		return (double) 0;
	}

	private Double distance(Point a, Point b) {
		return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
	}

	public void del1() {
		readInput(0);
		List<Point> _los = new ArrayList<>(_field);
		for (Point a : _field) {
			for (Point b : _field) {
				if (!a.equals(b)) {
					for (Point c : _los) {
						if (!a.equals(c) && !(b.equals(c))) {
							if (shadows(a, b, c)) { // är de tre på linje med c längst bort? -> ta bort
								_los.remove(c);
								break;
							}
						}
					}
				}
			}
			if (_los.size() - 1 > _bestObservable) {
				_bestObservable = _los.size() - 1;
				_bestPoint = a;
			}
			_los = new ArrayList<>(_field);
		}
		System.out.println( _bestPoint);
		System.out.println("del 1: " + _bestObservable);
	}

	void print(int x, int y, List<Point> p) {
		String s = "";
		for (int i = 0; i <= y; i++) {
			for (int j = 0; j <= x; j++) {
				if (p.contains(new Point(j, i))) {
					s += "#";
				} else {
					s += ".";
				}
			}
			System.out.println(s);
			s = "";
		}
	}

	private boolean shadows(Point a, Point b, Point c) {
		int dxab = a.x - b.x;
		int dxac = a.x - c.x;
		int dyab = a.y - b.y;
		int dyac = a.y - c.y;

		// olika riktningar
		if (dxab < 0 && dxac > 0) {// b höger och c vänster
			return false;
		}
		if (dxab > 0 && dxac < 0) {// b vänster och c höger
			return false;
		}
		if (dxab == 0 && dxac != 0) {// b on x=0 but c is not
			return false;
		}
		if (dxab != 0 && dxac == 0) {// c on x=0 but b is not
			return false;
		}
		if (dyab < 0 && dyac > 0) {// b under och c över
			return false;
		}
		if (dyab > 0 && dyac < 0) {// b över och c under
			return false;
		}
		if (dyab == 0 && dyac != 0) {// b on y=0 but c is not
			return false;
		}
		if (dyab != 0 && dyac == 0) {// c on y=0 but b is not
			return false;
		}
		if (dxab * dxab + dyab * dyab > dxac * dxac + dyac * dyac) {// avstånd längre till b än till c
			return false;
		}
		if (dyab == 0 && dyac == 0) {// b and c y=0
			return true;
		}
		if (dxab == 0 && dxac == 0) {// b and c x=0
			return true;
		}
		if (Math.abs((double) dxab / (double) dyab - (double) dxac / (double) dyac) > 0.0001) {// ej samma k
			return false;
		}
		return true;
	}

	private static void readInput(int a) {
		String str;
		int y = 0;
		_field.clear();
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_10\\src\\AoC_10\\input" + a + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '#') {
						_field.add(new Point(i, y));
						_maxx = Math.max(_maxx, i);
						_maxy = Math.max(_maxy, y);
					}
				}
				y++;
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
