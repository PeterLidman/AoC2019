package AoC_16;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AoC_16 {
	static List<Integer> _inputSignal = new ArrayList<>();
	static List<Integer> _basePattern = new ArrayList<>(Arrays.asList(0, 1, 0, -1));

	public static void main(final String[] args) {
		final AoC_16 a = new AoC_16();
		a.run();
	}

	public static void run() {
		del1();
		del2();
	}

	public static void del2() {
		readInput(16, 0);
		List<Integer> _outputSignal = new ArrayList<>();
		_outputSignal = new ArrayList<>(_inputSignal);
		int memoization = 0, offset = 0;

		for (int i = 0; i < 9999; i++) {
			_inputSignal.addAll(_outputSignal);
		}
		_outputSignal = new ArrayList<>(_inputSignal); // lika stora
		String off = "";
		for (int i = 0; i < 7; i++) {
			off += _inputSignal.get(i).toString();
		}
		offset = Integer.parseInt(off);

		for (int k = 1; k <= 100; k++) {
			for (int j = _inputSignal.size() - 1; j >= 0; j--) {
				memoization += _inputSignal.get(j) * getBloatedPattern(j + 1, j + 1);
				_outputSignal.set(j, Math.abs(memoization % 10));
			}
			_inputSignal = new ArrayList<>(_outputSignal);
			memoization = 0;
//			System.out.println("phase " + k);
			if (k == 100) {
				off = "";
				for (int i = 0; i < 8; i++) {
					off += _inputSignal.get(i + offset).toString();
				}
				System.out.println("Del 2: " + off);
			}
		}
	}

	public static void del1() {
		readInput(16, 0);
		List<Integer> _outputSignal = new ArrayList<>();
		for (int k = 1; k <= 100; k++) {
			for (int j = 0; j < _inputSignal.size(); j++) {
				int sum = 0;
				for (int i = j; i < _inputSignal.size(); i++) {
					sum += _inputSignal.get(i) * getBloatedPattern(i + 1, j + 1);
				}
				sum = Math.abs(sum % 10);
				_outputSignal.add(sum);
			}
			_inputSignal = new ArrayList<>(_outputSignal);
			_outputSignal.clear();
//			System.out.println("phase " + k);
			if (k == 100) {
				System.out.println("Del 1: " + print(_inputSignal).substring(0, 8));
			}
		}
	}

	private static String print(List<Integer> a) {
		String r = "";
		for (int i = 0; i < a.size(); i++) {
			r += a.get(i).toString();
		}
		return r;
	}

	private static int getBloatedPattern(int pos, int bloat) {
		return (_basePattern.get((pos / bloat) % _basePattern.size()));
	}

	private static void readInput(int day, int suffix) {
		String str;
		_inputSignal.clear();
		FileInputStream in;
		try {
			in = new FileInputStream(
					"C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input" + suffix + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				for (int i = 0; i < str.length(); i++) {
					_inputSignal.add(Integer.valueOf(str.substring(i, i + 1)));
				}
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
