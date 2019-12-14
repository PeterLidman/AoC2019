package AoC_13;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class AoC_13 {
	static List<Long> _intCode = new ArrayList<Long>();

	public static void main(final String[] args) {
		final AoC_13 a = new AoC_13();
		a.run();
	}

	public void run() {
//		del1();
		del2();
	}

	public void del2() {
		Scanner scan = new Scanner(System.in);
		int diff = 0;
		Point ball = new Point(0, 0);
		Point paddle = null;
		String result;
		readInput(13);
		_intCode.set(0, 2L);// play for free
		int blockTiles = 1;// start
		int max = -999, may = -999, mix = 999, miy = 999;
		HashMap<Point, Integer> screen = new HashMap<>();
		final IntComputer c = new IntComputer(_intCode);

		while (blockTiles > 0) {
			while (true) {
				c.run();
				result = c.getOutput();
				if (result.equals("")) {// halt
					break;
				}
				int x = Integer.valueOf(result);
				c.run();
				result = c.getOutput();
				if (result.equals("")) {// halt
					break;
				}
				int y = Integer.valueOf(result);
				c.run();
				result = c.getOutput();
				if (result.equals("")) {// halt
					break;
				}
				int b = Integer.valueOf(result);
//			System.out.println("x " + x + " y " + y + " b " + b);
				max = Math.max(max, x);
				may = Math.max(may, y);
				mix = Math.min(mix, x);
				miy = Math.min(miy, y);
				screen.put(new Point(x, y), b);
				if (b == 4) {
					ball = new Point(x, y);
				}
				if (b == 3) {
					paddle = new Point(x, y);
				}
			}
			blockTiles = 0;
			for (Entry<Point, Integer> a : screen.entrySet()) {
				if (a.getValue() == 2) {
					blockTiles++;
				}
			}
			System.out.println(
					"Score: " + screen.get(new Point(-1, 0)) + " Blocks left: " + blockTiles);
			print(0, miy, max, may, screen);
			// calculate joystick move
			diff = ball.x - paddle.x;
			if(diff > 0) {
				c.addInput(1L);
			} else if(diff < 0) {
				c.addInput(-1L);
			} else {
				c.addInput(0L);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//String text = scan.nextLine();
		}
		scan.close();
	}

	public void del1() {
		String result;
		readInput(13);
		int blockTiles = 0;
		int max = -999, may = -999, mix = 999, miy = 999;
		HashMap<Point, Integer> screen = new HashMap<>();
		final IntComputer c = new IntComputer(_intCode);
		while (true) {
			c.run();
			result = c.getOutput();
			if (result.equals("")) {// halt
				break;
			}
			int x = Integer.valueOf(result);
			c.run();
			result = c.getOutput();
			if (result.equals("")) {// halt
				break;
			}
			int y = Integer.valueOf(result);
			c.run();
			result = c.getOutput();
			if (result.equals("")) {// halt
				break;
			}
			int b = Integer.valueOf(result);
//			System.out.println("x " + x + " y " + y + " b " + b);
			max = Math.max(max, x);
			may = Math.max(may, y);
			mix = Math.min(mix, x);
			miy = Math.min(miy, y);
			screen.put(new Point(x, y), b);
		}
		for (Entry<Point, Integer> a : screen.entrySet()) {
			if (a.getValue() == 2) {
				blockTiles++;
			}
		}
		System.out.println("Del 1: " + blockTiles);
		print(mix, miy, max, may, screen);
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
					s += "%";
				} else {
					switch (c) {
					case 0:
						s += ".";
						break;
					case 1:
						s += "W";
						break;
					case 2:
						s += "B";
						break;
					case 3:
						s += "~";
						break;
					case 4:
						s += "o";
						break;
					default:
						s += "!";
					}
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
