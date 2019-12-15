package AoC_15;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AoC_15 {
	static List<Long> _intCode = new ArrayList<Long>();
	private static int _cy, _cx;
	private static int _foundy = 0, _foundx = 0;
	private HashMap<Point, Integer> _explored;
	private boolean _found;
	private boolean _filled;

	public static void main(final String[] args) {
		final AoC_15 a = new AoC_15();
		a.run();
	}

	public void run() {
		del1och2();
	}

	private static int best(int a, int b) {
		if (a == 0) {
			return b;
		}
		if (b == 0) {
			return a;
		}
		return Math.min(a, b);
	}

	public void del1och2() {
		readInput(15);
		Scanner scan = new Scanner(System.in);
		String result;
		_cx = 0;
		_cy = 0;
		Long dir = 0L;
		int bestWay = 0;
		int max = 0, may = 0, mix = 0, miy = 0;
		_explored = new HashMap<>();
		final IntComputer c = new IntComputer(_intCode);

		// 1 north 2 south 3 west 4 east
		// 0 blocked 1 moved 2 found
		_explored.put(new Point(0, 0), 1);// startrutan är ok
		while (bestWay < 13) { // godtyckligt värde då det såg ok ut

			// idé proba och gå till ej utforskade rutor först
			// om vi måste gå bakåt, ta rutan med lägst värde, öka värdet på varje
			// återbesökt ruta (ej oxygenplatsen)
			if (_explored.get(new Point(_cx, _cy - 1)) == null) {
				dir = 1L;
			} else if (_explored.get(new Point(_cx, _cy + 1)) == null) {
				dir = 2L;
			} else if (_explored.get(new Point(_cx - 1, _cy)) == null) {
				dir = 3L;
			} else if (_explored.get(new Point(_cx + 1, _cy)) == null) {
				dir = 4L;
			} else { // inga lätta kvar
				bestWay = best(5000, _explored.get(new Point(_cx, _cy - 1)));
				bestWay = best(bestWay, _explored.get(new Point(_cx, _cy + 1)));
				bestWay = best(bestWay, _explored.get(new Point(_cx - 1, _cy)));
				bestWay = best(bestWay, _explored.get(new Point(_cx + 1, _cy)));
				if (_explored.get(new Point(_cx, _cy - 1)) == bestWay) {
					dir = 1L;
				} else if (_explored.get(new Point(_cx, _cy + 1)) == bestWay) {
					dir = 2L;
				} else if (_explored.get(new Point(_cx - 1, _cy)) == bestWay) {
					dir = 3L;
				} else if (_explored.get(new Point(_cx + 1, _cy)) == bestWay) {
					dir = 4L;
				}
			}
			c.addInput(dir);
//			String text = scan.nextLine();
//			System.out.println("Tryckte på: " + text);
//			if (text.equals("w")) {
//				dir = 1L;
//			} else if (text.equals("s")) {
//				dir = 2L;
//			} else if (text.equals("a")) {
//				dir = 3L;
//			} else if (text.equals("d")) {
//				dir = 4L;
//			} else {
//				dir = Long.valueOf(text);
//			}
//			if (dir > 0L && dir < 5L) {
//				c.addInput(dir);
//			} else {
//				break;
//			}
			c.run();
			result = c.getOutput();
			if (result.equals("")) {
				break;
			}
			switch (dir.intValue()) {
			case 1:
				_cy--;
				break;
			case 2:
				_cy++;
				break;
			case 3:
				_cx--;
				break;
			default:
				_cx++;
			}
			max = Math.max(max, _cx);
			may = Math.max(may, _cy);
			mix = Math.min(mix, _cx);
			miy = Math.min(miy, _cy);
			int b = Integer.valueOf(result);
			if (b == 2) { // found
				_foundx = _cx;
				_foundy = _cy;
				Integer cb;
				cb = _explored.get(new Point(_cx, _cy));
				if (cb == null) {
					cb = 1;
				}
				_explored.put(new Point(_cx, _cy), cb + 1);
			} else if (b == 0) { // wall
				_explored.put(new Point(_cx, _cy), 0);
			} else { // increase
				Integer cb;
				cb = _explored.get(new Point(_cx, _cy));
				if (cb == null) {
					cb = 1;
				}
				_explored.put(new Point(_cx, _cy), cb + 1);
			}

			if (b == 0) {
				switch (dir.intValue()) {// back up if hit a wall
				case 1:
					_cy++;
					break;
				case 2:
					_cy--;
					break;
				case 3:
					_cx++;
					break;
				default:
					_cx--;
				}
			}
		}
		print(mix, miy, max, may, _explored);

		// idé: rensa alla bestway till 1, vi vet start och mål , behåll bara nollor
		// börja i startpunkten och öka alla möjliga intillliggande möjliga vägar ett
		// steg
		// repetera till vi når målet
		_explored.forEach((k, v) -> {
			if (v != 0) {
				_explored.put(k, 1);
			}
		});
		_explored.put(new Point(0, 0), 0); // startpunkten för droid
		_explored.put(new Point(-1, 0), 2); // start

		_found = false;
		int cd = 2;
		while (!_found) {
			increaseAndLookForOxygen(cd++);
		}
		System.out.println("del 1: " + (cd - 2));

		// idé: nästan samma som innan
		_explored.forEach((k, v) -> {
			if (v != 0) {
				_explored.put(k, 1);
			}
		});
		_explored.put(new Point(_foundx, _foundy), 0); // startpunkten för syre
		_explored.put(new Point(_foundx, _foundy + 1), 2); // start
		_found = false;
		cd = 2;
		while (!_found) {
			increaseAndLookForOxygen(cd++);
		}
		System.out.println("del 2: " + (cd - 2));
		scan.close();
	}

	private void increaseAndLookForOxygen(int currentDistance) {
		_filled = false;
		_explored.forEach((k, v) -> {
			if (v == currentDistance) {
				if (_explored.get(new Point(k.x, k.y - 1)) == 1) {
					_filled = true;
					_explored.put(new Point(k.x, k.y - 1), currentDistance + 1);
				}
				if (_explored.get(new Point(k.x, k.y + 1)) == 1) {
					_filled = true;
					_explored.put(new Point(k.x, k.y + 1), currentDistance + 1);
				}
				if (_explored.get(new Point(k.x - 1, k.y)) == 1) {
					_filled = true;
					_explored.put(new Point(k.x - 1, k.y), currentDistance + 1);
				}
				if (_explored.get(new Point(k.x + 1, k.y)) == 1) {
					_filled = true;
					_explored.put(new Point(k.x + 1, k.y), currentDistance + 1);
				}
				if ((k.x == _foundx && k.y == _foundy && v != 0)) { // found
					_found = true;
				}
			}
		});
		if (!_filled) {
			_found = true;
		}
	}

	private static void readInput(int day) {
		String str, splitStr[];
		_intCode.clear();
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input.txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				splitStr = str.split(",");
				for (final String s : splitStr) {
					_intCode.add(Long.parseLong(s));
				}
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

	static void print(int minx, int miny, int maxx, int maxy, HashMap<Point, Integer> screen) {
		String s = "";
		Integer c = 0;
		for (int i = miny; i <= maxy; i++) {
			for (int j = minx; j <= maxx; j++) {
				c = screen.get(new Point(j, i));
				if (j == 0 && i == 0) { // startpunkt
					s += "!";
				} else if (j == _cx && i == _cy) { // droid
					s += "D";
				} else if (j == _foundx && i == _foundy) {// oxygen
					s += "*";
				} else if (c == null) {
					s += ".";
				} else if (c == 0) {
					s += "#";
				} else {
					s += " ";
				}
			}
			System.out.println(s);
			s = "";
		}
	}

	public class IntComputer {
		private HashMap<Long, Long> _ic = new HashMap<>();
		private List<Long> _in = new ArrayList<Long>();
		private Long _pc = 0L;
		private String _out = new String("");
		private int _noIn = 0;
		private Long _relativeBase = 0L;

		public IntComputer(final List<Long> p) {
			Long pos = 0L;
			for (Long i : p) {
				_ic.put(pos++, i);
			}
		}

		private Long hashGet(Long pos) {
			Long ret;

			if (pos < 0) {
				System.out.println("Minnet kan ej vara negativt");
				return 0L;
			}
			if ((ret = _ic.get(pos)) == null) {
				_ic.put(pos, new Long("0"));// sätt till 0 i nytt minne
				ret = 0L;
			}
			return ret;
		}

		private void hashSet(Long pos, Long value) {
			if (pos < 0) {
				System.out.println("Minnet kan ej vara negativt");
				return;
			}
			_ic.put(pos, value);
		}

		public void run() {
			while (true) {
				Long v1, v2;
				final String opCode = "000000" + hashGet(_pc);
				final String op = opCode.substring(opCode.length() - 2, opCode.length());
				final int m1 = Integer.valueOf(opCode.substring(opCode.length() - 3, opCode.length() - 2));
				final int m2 = Integer.valueOf(opCode.substring(opCode.length() - 4, opCode.length() - 3));
				final int m3 = Integer.valueOf(opCode.substring(opCode.length() - 5, opCode.length() - 4));

				if (op.equals("99")) {
					return;
				}
				if (op.equals("01") || op.equals("02")) {// + & *
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (op.equals("01")) { // operation
						v1 = v1 + v2;
					} else {
						v1 = v1 * v2;
					}
					setV(v1, m3);
					_pc += 4;
				} else if (op.equals("03")) {// input
					if (_noIn > _in.size() - 1) {
						return; // halt until next input ready!
					}
					switch (m1) {
					case 0:
						hashSet(hashGet(_pc + 1), _in.get(_noIn++));
						break;
					case 1:
						hashSet(_pc + 1, _in.get(_noIn++));
						break;
					case 2:
						hashSet(_relativeBase + hashGet(_pc + 1), _in.get(_noIn++));
						break;
					default:
						System.out.println("Error, unknown mode: " + m1);
					}
					_pc += 2;
				} else if (op.equals("04")) {// output
					v1 = getV(1L, m1);
					_out = _out + v1;
					_pc += 2;
					return;
				} else if (op.equals("05")) {// jump-if-true
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (!v1.equals(0L)) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("06")) {// jump-if-false
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1.equals(0L)) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("07")) {// less than
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1.compareTo(v2) < 0) {
						setV(1L, m3);
					} else {
						setV(0L, m3);
					}
					_pc += 4;
				} else if (op.equals("08")) {// equals
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1.equals(v2)) {
						setV(1L, m3);
					} else {
						setV(0L, m3);
					}
					_pc += 4;
				} else if (op.equals("09")) {// adjust relative base
					v1 = getV(1L, m1);
					_relativeBase += v1;
					_pc += 2;
				} else {
					System.out.println("Error, Unknown opcode: " + op + " at position " + _pc);
				}
			}
		}

		private void setV(Long value, final int mode) {
			switch (mode) {
			case 0:
				hashSet(hashGet(_pc + 3), value);
				break;
			case 1:
				hashSet(_pc + 3, value);
				break;
			case 2:
				hashSet(_relativeBase + hashGet(_pc + 3), value);
				break;
			default:
				System.out.println("Error, unknown mode: " + mode);
			}
		}

		private Long getV(final Long offset, final int mode) {
			Long v = 0L;
			switch (mode) {
			case 0:
				v = hashGet(hashGet(_pc + offset));
				break;
			case 1:
				v = hashGet(_pc + offset);
				break;
			case 2:
				v = hashGet(_relativeBase + hashGet(_pc + offset));
				break;
			default:
				System.out.println("Error, unknown mode: " + mode);
			}
			return v;
		}

		public void addInput(final Long a) {
			_in.add(a);
		}

		public String getOutput() {
			String ret = _out;
			_out = "";
			return ret;
		}
	}

}
