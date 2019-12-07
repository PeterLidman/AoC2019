package AoC_7;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AoC_7 {
	static List<Integer> _intCode = new ArrayList<Integer>();
	static List<String> _phases = new ArrayList<String>();

	public static void main(final String[] args) {
		final AoC_7 a = new AoC_7();
		a.run();
	}

	public void del1Test1() {
		int result = 0;
//		_intCode = new ArrayList<Integer>(Arrays.asList(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0));
//		String phase = new String("43210");
//		_intCode = new ArrayList<Integer>(Arrays.asList(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23,99, 0, 0));
//		String phase = new String("01234");
		_intCode = new ArrayList<Integer>(Arrays.asList(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0,
				33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0));
		final String phase = new String("10432");
		final IntComputer ampA = new IntComputer(_intCode);
		final IntComputer ampB = new IntComputer(_intCode);
		final IntComputer ampC = new IntComputer(_intCode);
		final IntComputer ampD = new IntComputer(_intCode);
		final IntComputer ampE = new IntComputer(_intCode);

		ampA.addInput(Integer.valueOf(phase.substring(0, 1)));
		ampA.addInput(result);
		ampA.run();
		result = ampA.getOutput();
		ampB.addInput(Integer.valueOf(phase.substring(1, 2)));
		ampB.addInput(result);
		ampB.run();
		result = ampB.getOutput();
		ampC.addInput(Integer.valueOf(phase.substring(2, 3)));
		ampC.addInput(result);
		ampC.run();
		result = ampC.getOutput();
		ampD.addInput(Integer.valueOf(phase.substring(3, 4)));
		ampD.addInput(result);
		ampD.run();
		result = ampD.getOutput();
		ampE.addInput(Integer.valueOf(phase.substring(4, 5)));
		ampE.addInput(result);
		ampE.run();
		result = ampE.getOutput();
		System.out.println("Test1 " + result);
	}

	public void del1() {
		int result = 0;
		String phase;
		readInput();
		_phases.clear();
		permutphases("", "01234");
		int _max = 0;

		for (int j = 0; j < _phases.size(); j++) {
			final IntComputer ampA = new IntComputer(_intCode);
			final IntComputer ampB = new IntComputer(_intCode);
			final IntComputer ampC = new IntComputer(_intCode);
			final IntComputer ampD = new IntComputer(_intCode);
			final IntComputer ampE = new IntComputer(_intCode);
			result = 0;
			phase = _phases.get(j);
			ampA.addInput(Integer.valueOf(phase.substring(0, 1)));
			ampA.addInput(result);
			ampA.run();
			result = ampA.getOutput();
			ampB.addInput(Integer.valueOf(phase.substring(1, 2)));
			ampB.addInput(result);
			ampB.run();
			result = ampB.getOutput();
			ampC.addInput(Integer.valueOf(phase.substring(2, 3)));
			ampC.addInput(result);
			ampC.run();
			result = ampC.getOutput();
			ampD.addInput(Integer.valueOf(phase.substring(3, 4)));
			ampD.addInput(result);
			ampD.run();
			result = ampD.getOutput();
			ampE.addInput(Integer.valueOf(phase.substring(4, 5)));
			ampE.addInput(result);
			ampE.run();
			result = ampE.getOutput();
			_max = Math.max(_max, result);
		}
		System.out.println("Del 1: " + _max);
	}

	public void del2() {
		int result = 0;
		String phase;
		readInput();
		_phases.clear();// underligt att det funkade utan denna radem?
		permutphases("", "56789");
		int _max = 0;

		for (int j = 0; j < _phases.size(); j++) {
			final IntComputer ampA = new IntComputer(_intCode);
			final IntComputer ampB = new IntComputer(_intCode);
			final IntComputer ampC = new IntComputer(_intCode);
			final IntComputer ampD = new IntComputer(_intCode);
			final IntComputer ampE = new IntComputer(_intCode);
			result = 0;
			phase = _phases.get(j);
			ampA.addInput(Integer.valueOf(phase.substring(0, 1)));
			ampA.addInput(result);
			ampA.run();
			result = ampA.getOutput();
			ampB.addInput(Integer.valueOf(phase.substring(1, 2)));
			ampB.addInput(result);
			ampB.run();
			result = ampB.getOutput();
			ampC.addInput(Integer.valueOf(phase.substring(2, 3)));
			ampC.addInput(result);
			ampC.run();
			result = ampC.getOutput();
			ampD.addInput(Integer.valueOf(phase.substring(3, 4)));
			ampD.addInput(result);
			ampD.run();
			result = ampD.getOutput();
			ampE.addInput(Integer.valueOf(phase.substring(4, 5)));
			ampE.addInput(result);
			ampE.run();
			result = ampE.getOutput();
			while (true) {// feedback loop
				ampA.addInput(result);
				ampA.run();
				final Integer output = ampA.getOutput();
				if (output == null) {
					break;
				}
				result = output;
				ampB.addInput(result);
				ampB.run();
				result = ampB.getOutput();
				ampC.addInput(result);
				ampC.run();
				result = ampC.getOutput();
				ampD.addInput(result);
				ampD.run();
				result = ampD.getOutput();
				ampE.addInput(result);
				ampE.run();
				result = ampE.getOutput();
			}
			_max = Math.max(_max, result);
		}
		System.out.println("Del 2: " + _max);
	}

	public void run() {
		del1Test1();
		del1();
		del2();
	}

	private static void permutphases(final String prefix, final String str) {
		final int n = str.length();
		if (n == 0) {
			_phases.add(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				permutphases(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
			}
		}
	}

	private static void readInput() {
		String str, splitStr[];
		_intCode.clear();
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_7\\src\\AoC_7\\input.txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				splitStr = str.split(",");
				for (final String s : splitStr) {
					_intCode.add(Integer.parseInt(s));
				}
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

	public class IntComputer {
		List<Integer> _ic;
		List<Integer> _in = new ArrayList<Integer>();
		private int _pc = 0;
		String _out = new String("");
		private int _noIn = 0;

		public IntComputer(final List<Integer> p) {
			_ic = new ArrayList<Integer>(p);
		}

		public void run() {
			while (true) {
				int v1, v2, v3;
				final String opCode = "0000" + _ic.get(_pc);
				final String op = opCode.substring(opCode.length() - 2, opCode.length());
				final boolean m1 = opCode.substring(opCode.length() - 3, opCode.length() - 2).equals("1");
				final boolean m2 = opCode.substring(opCode.length() - 4, opCode.length() - 3).equals("1");
				boolean m3 = opCode.substring(opCode.length() - 5, opCode.length() - 4).equals("1");

				if (op.equals("99")) {
					return;
				}
				if (op.equals("01")) {// addition
					v1 = m1 ? _ic.get(_pc + 1) : _ic.get(_ic.get(_pc + 1));
					v2 = m2 ? _ic.get(_pc + 2) : _ic.get(_ic.get(_pc + 2));
					if (m3) {
						_ic.set(_pc + 3, v1 + v2);
					} else {
						_ic.set(_ic.get(_pc + 3), v1 + v2);
					}
					_pc += 4;
				} else if (op.equals("02")) {// multiplication
					v1 = m1 ? _ic.get(_pc + 1) : _ic.get(_ic.get(_pc + 1));
					v2 = m2 ? _ic.get(_pc + 2) : _ic.get(_ic.get(_pc + 2));
					if (m3) {
						_ic.set(_pc + 3, v1 * v2);
					} else {
						_ic.set(_ic.get(_pc + 3), v1 * v2);
					}
					_pc += 4;
				} else if (op.equals("03")) {// input
					if (_noIn > _in.size() - 1) {
						return; // halt until next input ready!
					}
					_ic.set(_ic.get(_pc + 1), _in.get(_noIn++));
					_pc += 2;
				} else if (op.equals("04")) {// output
					if (m1) {
						_out = _out + _ic.get(_pc + 1).toString();
					} else {
						_out = _out + _ic.get(_ic.get(_pc + 1)).toString();
					}
					_pc += 2;
				} else if (op.equals("05")) {// jump-if-true
					v1 = m1 ? _ic.get(_pc + 1) : _ic.get(_ic.get(_pc + 1));
					v2 = m2 ? _ic.get(_pc + 2) : _ic.get(_ic.get(_pc + 2));
					if (v1 != 0) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("06")) {// jump-if-false
					v1 = m1 ? _ic.get(_pc + 1) : _ic.get(_ic.get(_pc + 1));
					v2 = m2 ? _ic.get(_pc + 2) : _ic.get(_ic.get(_pc + 2));
					if (v1 == 0) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("07")) {// less than
					v1 = m1 ? _ic.get(_pc + 1) : _ic.get(_ic.get(_pc + 1));
					v2 = m2 ? _ic.get(_pc + 2) : _ic.get(_ic.get(_pc + 2));
					m3 = false;
					if (v1 < v2) {
						v3 = 1;
					} else {
						v3 = 0;
					}
					if (m3) {
						_ic.set(_pc + 3, v3);
					} else {
						_ic.set(_ic.get(_pc + 3), v3);
					}
					_pc += 4;
				} else if (op.equals("08")) {// equals
					v1 = m1 ? _ic.get(_pc + 1) : _ic.get(_ic.get(_pc + 1));
					v2 = m2 ? _ic.get(_pc + 2) : _ic.get(_ic.get(_pc + 2));
					m3 = false;
					if (v1 == v2) {
						v3 = 1;
					} else {
						v3 = 0;
					}
					if (m3) {
						_ic.set(_pc + 3, v3);
					} else {
						_ic.set(_ic.get(_pc + 3), v3);
					}
					_pc += 4;
				} else {
					System.out.println("Error, Unknown opcode: " + op + " at position " + _pc);
				}
			}
		}

		public void addInput(final int a) {
			_in.add(a);
		}

		public Integer getOutput() {
			Integer sendAndClear;
			if (_out.equals("")) {
				return null;
			} else {
				sendAndClear = new Integer(Integer.valueOf(_out));
			}
			_out = "";// important :-)
			return sendAndClear;
		}
	}

}
