package AoC_2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AoC_2 {
	static List<Integer> _intCode = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException {
		int output = 19690720, result = 0;

		readInput();
		System.out.println("Del 1 = " + runProgram(12, 2));

		outerloop: for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				readInput();
				result = runProgram(i, j);
				if (output == result) {
					System.out.println("Del 2 = " + (i * 100 + j));
					break outerloop;
				}
			}
		}
	}

	private static int runProgram(int noun, int verb) throws FileNotFoundException, IOException {
		_intCode.set(1, noun);
		_intCode.set(2, verb);
		int pc = 0;

		while (executeOrQuit(pc)) {
			pc += 4;
		}
		return _intCode.get(0);
	}

	private static boolean executeOrQuit(int a) {
		if (_intCode.get(a) == 99) {
			return false;
		}
		if (_intCode.get(a) == 1) {// add
			_intCode.set(_intCode.get(a + 3), _intCode.get(_intCode.get(a + 1)) + _intCode.get(_intCode.get(a + 2)));
		} else if (_intCode.get(a) == 2) {// mult
			_intCode.set(_intCode.get(a + 3), _intCode.get(_intCode.get(a + 1)) * _intCode.get(_intCode.get(a + 2)));
		} else {
			System.out.println("Unknown opcode");
		}
		return true;
	}

	private static void readInput() throws FileNotFoundException, IOException {
		String str, splitStr[];
		_intCode.clear();
		FileInputStream in = new FileInputStream("C:\\git\\AoC2019\\AoC_2\\src\\AoC_2\\input.txt");
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
