package AoC_24;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AoC_24 {
	static int[] _eris = new int[25];

	public static void main(final String[] args) {
		final AoC_24 a = new AoC_24();
		a.run();
	}

	void lifeCycle() {
		int pos = 0;
		int sum = 0;
		int[] ng = new int[25];

//		for (int i = 0; i < 25; i++) {
//			ng[i] = _eris[i];
//		}
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {

				pos = y * 5 + x;
				sum = 0;
				if (x != 0) {
					sum += _eris[y * 5 + x - 1];
				}
				if (y != 0) {
					sum += _eris[(y - 1) * 5 + x];
				}
				if (x != 4) {
					sum += _eris[y * 5 + x + 1];
				}
				if (y != 4) {
					sum += _eris[(y + 1) * 5 + x];
				}
				// System.out.println("sum" + sum);
				switch (sum) {
				case 1:
					if (_eris[pos] == 1) { // it doesn't die
						ng[pos] = 1;
					}
				case 2:
					if (_eris[pos] == 0) { // infest
						ng[pos] = 1;
					}
					break;
				default:
					ng[pos] = 0;
				}
			}
		}
		for (int i = 0; i < 25; i++) {
			_eris[i] = ng[i];
		}
	}

	private int getBiodiverity() {
		int sum = 0;
		for (int i = 0; i < 25; i++) {
			if (_eris[i] == 1) {
				sum += Math.pow(2, i);
			}
		}
		return sum;
	}

	public void run() {
		del1();
	}

	public void del1() {
		readInput(24, 0);
		Map <Integer, Integer> twice = new HashMap<>();		
		
		while(twice.put(getBiodiverity(), 1) == null) {
			lifeCycle();
		}
		System.out.println("Del 1: " + getBiodiverity());
	}

	private void print() {
		String s = "";
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				s += _eris[y * 5 + x] == 1 ? "#" : ".";
			}
			System.out.println(s);
			s = "";
		}
	}

	private static void readInput(int day, int suffix) {
		String str;
		int y = 0;
		FileInputStream in;
		try {
			in = new FileInputStream(
					"C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input" + suffix + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				String tmp = "";
				for (int x = 0; x < str.length(); x++) {
					tmp = str.substring(x, x + 1);
					if (tmp.equals("#")) {
						_eris[y * 5 + x] = 1;
					} else if (tmp.equals(".")) {
						_eris[y * 5 + x] = 0;
					}
				}
				y++;
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
