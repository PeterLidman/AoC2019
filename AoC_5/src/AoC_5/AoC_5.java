package AoC_5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AoC_5 {
	static List<Integer> _intCode = new ArrayList<Integer>();
	private static int _pc = 0;
	static String _output = new String("");
//	static String _input = new String("1");// del1
	static String _input = new String("5");// del2

	public static void main(String[] args) throws IOException, FileNotFoundException {
		readInput();

		System.out.println("input: " + _input);
		while (executeOrQuit()) {
		}
		System.out.println("Output: " + _output);
	}

	private static boolean executeOrQuit() {
		int v1, v2, v3;
		String opCode = "0000" + _intCode.get(_pc);
		String op = opCode.substring(opCode.length() - 2, opCode.length());
		boolean m1 = opCode.substring(opCode.length() - 3, opCode.length() - 2).equals("1");
		boolean m2 = opCode.substring(opCode.length() - 4, opCode.length() - 3).equals("1");
		boolean m3 = opCode.substring(opCode.length() - 5, opCode.length() - 4).equals("1");

		if (op.equals("99")) {
			return false;
		}
		if (op.equals("01")) {// add
			v1 = m1 ? _intCode.get(_pc + 1) : _intCode.get(_intCode.get(_pc + 1));
			v2 = m2 ? _intCode.get(_pc + 2) : _intCode.get(_intCode.get(_pc + 2));
			if (m3) {
				_intCode.set(_pc + 3, v1 + v2);
			} else {
				_intCode.set(_intCode.get(_pc + 3), v1 + v2);
			}
			_pc += 4;
		} else if (op.equals("02")) {// mult
			v1 = m1 ? _intCode.get(_pc + 1) : _intCode.get(_intCode.get(_pc + 1));
			v2 = m2 ? _intCode.get(_pc + 2) : _intCode.get(_intCode.get(_pc + 2));
			if (m3) {
				_intCode.set(_pc + 3, v1 * v2);
			} else {
				_intCode.set(_intCode.get(_pc + 3), v1 * v2);
			}
			_pc += 4;
		} else if (op.equals("03")) {// input
			_intCode.set(_intCode.get(_pc + 1), Integer.valueOf(_input.substring(0, 1)));
			_input = _input.substring(1);
			_pc += 2;
		} else if (op.equals("04")) {// output
			if (m1) {
				_output = _output + _intCode.get(_pc + 1).toString();
			} else {
				_output = _output + _intCode.get(_intCode.get(_pc + 1)).toString();
			}
			_pc += 2;
		} else if (op.equals("05")) {// jump-if-true
			v1 = m1 ? _intCode.get(_pc + 1) : _intCode.get(_intCode.get(_pc + 1));
			v2 = m2 ? _intCode.get(_pc + 2) : _intCode.get(_intCode.get(_pc + 2));
			if (v1 != 0) {
				_pc = v2;
			} else {
				_pc += 3;
			}
		} else if (op.equals("06")) {// jump-if-false
			v1 = m1 ? _intCode.get(_pc + 1) : _intCode.get(_intCode.get(_pc + 1));
			v2 = m2 ? _intCode.get(_pc + 2) : _intCode.get(_intCode.get(_pc + 2));
			if (v1 == 0) {
				_pc = v2;
			} else {
				_pc += 3;
			}
		} else if (op.equals("07")) {// less than
			v1 = m1 ? _intCode.get(_pc + 1) : _intCode.get(_intCode.get(_pc + 1));
			v2 = m2 ? _intCode.get(_pc + 2) : _intCode.get(_intCode.get(_pc + 2));
			if (v1 < v2) {
				v3 = 1;
			} else {
				v3 = 0;
			}
			if (m3) {
				_intCode.set(_pc + 3, v3);
			} else {
				_intCode.set(_intCode.get(_pc + 3), v3);
			}
			_pc += 4;
		} else if (op.equals("08")) {// equals
			v1 = m1 ? _intCode.get(_pc + 1) : _intCode.get(_intCode.get(_pc + 1));
			v2 = m2 ? _intCode.get(_pc + 2) : _intCode.get(_intCode.get(_pc + 2));
			if (v1 == v2) {
				v3 = 1;
			} else {
				v3 = 0;
			}
			if (m3) {
				_intCode.set(_pc + 3, v3);
			} else {
				_intCode.set(_intCode.get(_pc + 3), v3);
			}
			_pc += 4;
		} else {
			System.out.println("Unknown opcode");
		}
		return true;
	}

	private static void readInput() throws FileNotFoundException, IOException {
		String str, splitStr[];
		_intCode.clear();
		FileInputStream in = new FileInputStream("C:\\git\\AoC2019\\AoC_5\\src\\AoC_5\\input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while ((str = br.readLine()) != null) {
			splitStr = str.split(",");
			for (String s : splitStr) {
				_intCode.add(Integer.parseInt(s));
			}
		}
		br.close();
	}

}
