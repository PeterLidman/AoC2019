package AoC_9;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AoC_9 {
	static List<Long> _intCode = new ArrayList<Long>();

	public static void main(final String[] args) {
		final AoC_9 a = new AoC_9();
		a.run();
	}

	public void test1() {
		String result;
		_intCode = new ArrayList<Long>(Arrays.asList(109L, 1L, 204L, -1L, 1001L, 100L, 1L, 100L, 1008L, 100L, 16L, 101L,
				1006L, 101L, 0L, 99L));
		final IntComputer c = new IntComputer(_intCode);
		c.run();
		result = c.getOutput();
		System.out.println("Test1 " + result);
	}

	public void test2() {
		String result;
		_intCode = new ArrayList<Long>(Arrays.asList(1102L, 34915192L, 34915192L, 7L, 4L, 7L, 99L, 0L));
		final IntComputer c = new IntComputer(_intCode);
		c.run();
		result = c.getOutput();
		System.out.println("Test2 " + result);
	}

	public void test3() {
		String result;
		_intCode = new ArrayList<Long>(Arrays.asList(104L, 1125899906842624L, 99L));
		final IntComputer c = new IntComputer(_intCode);
		c.run();
		result = c.getOutput();
		System.out.println("Test3 " + result);
	}

	public void del1() {
		String result;
		readInput();
		final IntComputer c = new IntComputer(_intCode);
		c.addInput(1L);
		c.run();
		result = c.getOutput();
		System.out.println("Del 1: " + result);
	}

	public void del2() {
		String result;
		readInput();
		final IntComputer c = new IntComputer(_intCode);
		c.addInput(2L);
		c.run();
		result = c.getOutput();
		System.out.println("Del 2: " + result);
	}

	public void run() {
		test1();
		test2();
		test3();
		del1();
		del2();
	}

	private static void readInput() {
		String str, splitStr[];
		_intCode.clear();
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_9\\src\\AoC_9\\input.txt");
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
				} else if (op.equals("05")) {// jump-if-true
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1 != 0) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("06")) {// jump-if-false
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1 == 0) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("07")) {// less than
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1 < v2) {
						setV(1L, m3);
					} else {
						setV(0L, m3);
					}
					_pc += 4;
				} else if (op.equals("08")) {// equals
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1 == v2) {
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
			return _out;
		}
	}

}
