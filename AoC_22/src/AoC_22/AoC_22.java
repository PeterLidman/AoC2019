package AoC_22;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AoC_22 {
	static List<Long> _intCode = new ArrayList<Long>();

	public static void main(final String[] args) {
		final AoC_22 a = new AoC_22();
		a.run();
	}

	public void run() {
		del1();
		del2();
	}

	public void del1() {
		readInput(21);
		String result;
		final IntComputer c = new IntComputer(_intCode);
		// register T J t memory j hoppa
		// read only A B C D false för hole
		// AND X Y X&&Y -> Y
		// OR X Y X||Y -> Y
		// NOT X Y !X -Y

		// 0000
		// 0001 -h
		// 0010
		// 0011 -h
		// 0100
		// 0101 -h
		// 0110
		// 0111 -h
		// 1000
		// 1001 -h
		// 1010
		// 1011 -h
		// 1100
		// 1101 -h
		// 1110
		// 1111 -hoppa inte

		instr("OR A T", c);
		instr("AND B T", c);
		instr("AND C T", c);
		instr("AND D T", c); // T = a && b && c && d
		instr("NOT T T", c); // T = !T
		instr("OR D J", c);
		instr("AND T J", c);
		instr("WALK", c);

		c.listInput();
		String build = "";
		while (true) {
			c.run();
			result = c.getOutput();
			if (result.equals("")) {
				System.out.println("no output");
				break;
			}
			if (Integer.valueOf(result) > 999) {
				System.out.println("del 1: " + Integer.valueOf(result));
				break;
			}
			if (Integer.valueOf(result) == 10) {
				System.out.println(build);
				build = "";
			} else {
				int a = Integer.valueOf(result);
				build += (char) a;
			}
		}
	}

	public void del2() {
		readInput(21);
		String result;
		final IntComputer c = new IntComputer(_intCode);

		// ABCDEFGHI must jump
		// 0********
		// 110100***
		// 10*10****
		//
		// ABCDEFGHI must not jump
		// 1111*****

		instr("NOT A J", c);// must jump if next is hole

		instr("NOT C T", c);
		instr("AND H T", c);
		instr("OR T J", c);

		instr("NOT B T", c);
		instr("AND A T", c);
		instr("AND C T", c);
		instr("OR T J", c);

		instr("AND D J", c);// jump if we can land

		instr("RUN", c);

		c.listInput();
		String build = "";
		while (true) {
			c.run();
			result = c.getOutput();
			if (result.equals("")) {
				System.out.println("no output");
				break;
			}
			if (Integer.valueOf(result) > 999) {
				System.out.println("del 2: " + Integer.valueOf(result));
				break;
			}
			if (Integer.valueOf(result) == 10) {
				System.out.println(build);
				build = "";
			} else {
				int a = Integer.valueOf(result);
				build += (char) a;
			}
		}
	}

	private static void instr(String a, IntComputer b) {
		for (int i = 0; i < a.length(); i++) {
			b.addInput(new Integer((int) a.substring(i, i + 1).charAt(0)).longValue());
		}
		b.addInput(10L);
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

		public void listInput() {
			System.out.println(_in);
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
