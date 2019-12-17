package AoC_17;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AoC_17 {
	static List<Long> _intCode = new ArrayList<Long>();
	private static int _cy, _cx;
	private HashMap<Point, Integer> _explored;

	public static void main(final String[] args) {
		final AoC_17 a = new AoC_17();
		a.run();
	}

	public void run() {
		del2();
		del1();
	}

	public void del2() {
		readInput(17);
		_intCode.set(0, 2L);// start up

		// 44 ,
		// 65 A
		// 82 R
		// 76 L
		// 49 1 50 2 51 3 52 4 53 5 54 6 55 7 56 8 57 9
		// 121 y 110 n 10 new line

		String result;
		_cx = 0;
		_cy = 0;
		_explored = new HashMap<>();
		final IntComputer c = new IntComputer(_intCode);

// R,8,L,12,R,8,R,12,L,8,R,10,R,12,L,8,R,10,R,8,L,12,R,8,R,8,L,8,L,8,R,8,R,10,R,8,L,12,R,8,R,8,L,12,R,8,R,8,L,8,L,8,R,8,R,10
// R,12,L,8,R,10,R,8,L,8,L,8,R,8,R,10
		
//A		R,8,L,12,R,8,
//B		R,12,L,8,R,10,
//B		R,12,L,8,R,10,
//A		R,8,L,12,R,8,
//C		R,8,L,8,L,8,R,8,R,10,
//A		R,8,L,12,R,8,
//A		R,8,L,12,R,8,
//C		R,8,L,8,L,8,R,8,R,10,
//B		R,12,L,8,R,10,
//C		R,8,L,8,L,8,R,8,R,10,
		
		c.addInput(65L);		c.addInput(44L);
		c.addInput(66L);        c.addInput(44L);
		c.addInput(66L);        c.addInput(44L);
		c.addInput(65L);        c.addInput(44L);
		c.addInput(67L);        c.addInput(44L);
		
		c.addInput(65L);        c.addInput(44L);
		c.addInput(65L);        c.addInput(44L);
		c.addInput(67L);        c.addInput(44L);
		c.addInput(66L);        c.addInput(44L);
		c.addInput(67L);        c.addInput(10L);

		c.addInput(82L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(44L);
		c.addInput(76L);        c.addInput(44L);
		c.addInput(49L);        c.addInput(50L);        c.addInput(44L);
		c.addInput(82L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(10L);
		
		c.addInput(82L);        c.addInput(44L);
		c.addInput(49L);        c.addInput(50L);        c.addInput(44L);
		c.addInput(76L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(44L);
		c.addInput(82L);        c.addInput(44L);
		c.addInput(49L);        c.addInput(48L);		c.addInput(10L);
		
		c.addInput(82L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(44L);
		c.addInput(76L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(44L);
		c.addInput(76L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(44L);
		c.addInput(82L);        c.addInput(44L);
		c.addInput(56L);        c.addInput(44L);
		c.addInput(82L);        c.addInput(44L);
		c.addInput(49L);        c.addInput(48L);		c.addInput(10L);
		
		c.addInput(121L);        c.addInput(10L);
		
		while (true) {
			c.run();
			result = c.getOutput();
			if (result.equals("")) {
				break;
			}
			System.out.println("result: " + result);
		}
		System.out.println("Del 2: ");
	}

	public void del1() {
		readInput(17);
		String result;
		_cx = 0;
		_cy = 0;
		int code = 0;
		int max = 0, may = 0, mix = 0, miy = 0;
		_explored = new HashMap<>();
		final IntComputer c = new IntComputer(_intCode);

		// 35 means #, 46 means ., 10 start
		while (true) {
			c.run();
			result = c.getOutput();
			if (result.equals("")) {
				break;
			}
			max = Math.max(max, _cx);
			may = Math.max(may, _cy);
			mix = Math.min(mix, _cx);
			miy = Math.min(miy, _cy);
			code = Integer.valueOf(result);
			switch (code) {
			case 35: // #
				_explored.put(new Point(_cx, _cy), 1);
				_cx++;
				break;
			case 46: // .
				_explored.put(new Point(_cx, _cy), 0);
				_cx++;
				break;
			case 10: // new line
				_cy++;
				_cx = 0;
				break;
			case 94: // ^
				_explored.put(new Point(_cx, _cy), 2);
				_cx++;
				break;
			default:
				System.out.println("bad input: " + code);
			}
		}
		print(mix, miy, max, may, _explored);

		int sum = 0;
		for (int i = miy + 1; i < may - 1; i++) {
			for (int j = mix + 1; j < max - 1; j++) {
				if (_explored.get(new Point(j, i)) == 1 && //
						_explored.get(new Point(j + 1, i)) == 1 && //
						_explored.get(new Point(j - 1, i)) == 1 && //
						_explored.get(new Point(j, i + 1)) == 1 && //
						_explored.get(new Point(j, i - 1)) == 1) {
					System.out.println("at x= " + j + " y= " + i);
					sum += j * i;
				}
			}
		}
		System.out.println("Del 1: " + sum);
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
				if (c == null) {
					s += "?";
				} else if (c == 1) {
					s += "#";
				} else if (c == 2) {
					s += "^";
				} else if (c == 0) {
					s += ".";
				} else {
					s += "@";
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
