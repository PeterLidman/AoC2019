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
	private static int _result = 0;
	static List<String> _phases = new ArrayList<String>();
	static String _phase = new String("43210");

	public static void main(String[] args) {
		AoC_7 a = new AoC_7();
		a.run();
	}

	public void del1Test1() {
//		_intCode = new ArrayList<Integer>(Arrays.asList(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0));
//		_phase = "43210";
//		_intCode = new ArrayList<Integer>(Arrays.asList(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23,99, 0, 0));
//		_phase = "01234";
		_intCode = new ArrayList<Integer>(Arrays.asList(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0,
				33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0));
		_phase = "10432";

		IntComputer ampA = new IntComputer(_intCode);
		IntComputer ampB = new IntComputer(_intCode);
		IntComputer ampC = new IntComputer(_intCode);
		IntComputer ampD = new IntComputer(_intCode);
		IntComputer ampE = new IntComputer(_intCode);
		_result = 0;
		ampA.addInput(Integer.valueOf(_phase.substring(0, 1)));
		ampA.addInput(_result);
		ampA.run();
		_result = ampA.getOutput();
		ampB.addInput(Integer.valueOf(_phase.substring(1, 2)));
		ampB.addInput(_result);
		ampB.run();
		_result = ampB.getOutput();
		ampC.addInput(Integer.valueOf(_phase.substring(2, 3)));
		ampC.addInput(_result);
		ampC.run();
		_result = ampC.getOutput();
		ampD.addInput(Integer.valueOf(_phase.substring(3, 4)));
		ampD.addInput(_result);
		ampD.run();
		_result = ampD.getOutput();
		ampE.addInput(Integer.valueOf(_phase.substring(4, 5)));
		ampE.addInput(_result);
		ampE.run();
		_result = ampE.getOutput();
		_intCode.clear();
		System.out.println("Test1 " + _result);
	}

	public void del1() {
		readInput();
		permutphases("", "01234");
		int _max = 0;

		for (int j = 0; j < _phases.size(); j++) {
			IntComputer ampA = new IntComputer(_intCode);
			IntComputer ampB = new IntComputer(_intCode);
			IntComputer ampC = new IntComputer(_intCode);
			IntComputer ampD = new IntComputer(_intCode);
			IntComputer ampE = new IntComputer(_intCode);
			_result = 0;
			_phase = _phases.get(j);
			ampA.addInput(Integer.valueOf(_phase.substring(0, 1)));
			ampA.addInput(_result);
			ampA.run();
			_result = ampA.getOutput();
			ampB.addInput(Integer.valueOf(_phase.substring(1, 2)));
			ampB.addInput(_result);
			ampB.run();
			_result = ampB.getOutput();
			ampC.addInput(Integer.valueOf(_phase.substring(2, 3)));
			ampC.addInput(_result);
			ampC.run();
			_result = ampC.getOutput();
			ampD.addInput(Integer.valueOf(_phase.substring(3, 4)));
			ampD.addInput(_result);
			ampD.run();
			_result = ampD.getOutput();
			ampE.addInput(Integer.valueOf(_phase.substring(4, 5)));
			ampE.addInput(_result);
			ampE.run();
			_result = ampE.getOutput();
			_max = Math.max(_max, _result);
		}
		System.out.println("Del 1: " + _max);
	}

	public void del2() {
		readInput();
		permutphases("", "56789");
		int _max = 0;

		for (int j = 0; j < _phases.size(); j++) {
			IntComputer ampA = new IntComputer(_intCode);
			IntComputer ampB = new IntComputer(_intCode);
			IntComputer ampC = new IntComputer(_intCode);
			IntComputer ampD = new IntComputer(_intCode);
			IntComputer ampE = new IntComputer(_intCode);
			_result = 0;
			_phase = _phases.get(j);
			ampA.addInput(Integer.valueOf(_phase.substring(0, 1)));
			ampA.addInput(_result);
			ampA.run();
			_result = ampA.getOutput();
			ampB.addInput(Integer.valueOf(_phase.substring(1, 2)));
			ampB.addInput(_result);
			ampB.run();
			_result = ampB.getOutput();
			ampC.addInput(Integer.valueOf(_phase.substring(2, 3)));
			ampC.addInput(_result);
			ampC.run();
			_result = ampC.getOutput();
			ampD.addInput(Integer.valueOf(_phase.substring(3, 4)));
			ampD.addInput(_result);
			ampD.run();
			_result = ampD.getOutput();
			ampE.addInput(Integer.valueOf(_phase.substring(4, 5)));
			ampE.addInput(_result);
			ampE.run();
			_result = ampE.getOutput();
			while (true) {// feedback loop
				ampA.addInput(_result);
				ampA.run();
				Integer output = ampA.getOutput();
				if (output == null) {
					break;
				}
				_result = output;
				ampB.addInput(_result);
				ampB.run();
				_result = ampB.getOutput();
				ampC.addInput(_result);
				ampC.run();
				_result = ampC.getOutput();
				ampD.addInput(_result);
				ampD.run();
				_result = ampD.getOutput();
				ampE.addInput(_result);
				ampE.run();
				_result = ampE.getOutput();
			}
			_max = Math.max(_max, _result);
		}
		System.out.println("Del 2: " + _max);
	}

	public void run() {
		del1Test1();
		del1();
		del2();
	}

	private static void permutphases(String prefix, String str) {
		int n = str.length();
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
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				splitStr = str.split(",");
				for (String s : splitStr) {
					_intCode.add(Integer.parseInt(s));
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

	public class IntComputer {
		List<Integer> _ic;
		List<Integer> _in = new ArrayList<Integer>();
		private int _pc = 0;
		String _out = new String("");
		private int _noIn = 0;

		public IntComputer(List<Integer> p) {
			_ic = new ArrayList<Integer>(p);
		}

		public void run() {
			while (true) {
				int v1, v2, v3;
				String opCode = "0000" + _ic.get(_pc);
				String op = opCode.substring(opCode.length() - 2, opCode.length());
				boolean m1 = opCode.substring(opCode.length() - 3, opCode.length() - 2).equals("1");
				boolean m2 = opCode.substring(opCode.length() - 4, opCode.length() - 3).equals("1");
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

		public void addInput(int a) {
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
