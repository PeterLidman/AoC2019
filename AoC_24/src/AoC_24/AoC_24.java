package AoC_24;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AoC_24 {
	static int[] _eris = new int[25];
	static int[][] _eris2 = new int[25][400];// 99 is in middle

	public static void main(final String[] args) {
		final AoC_24 a = new AoC_24();
		a.run();
	}

	void lifeCycle2() {
		int pos = 0;
		int sum = 0;
		int[][] ng = new int[25][400];

		for (int i = 1; i < 399; i++) { // lämna ytterkanterna
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					pos = y * 5 + x;
					sum = 0;

					if (x == 0) {
						sum += _eris2[11][i - 1];
					}
					if (x == 4) {
						sum += _eris2[13][i - 1];
					}
					if (y == 0) {
						sum += _eris2[7][i - 1];
					}
					if (y == 4) {
						sum += _eris2[17][i - 1];
					}

					if (x != 0) {
						sum += _eris2[y * 5 + x - 1][i];
					}
					if (y != 0) {
						sum += _eris2[(y - 1) * 5 + x][i];
					}
					if (x != 4) {
						sum += _eris2[y * 5 + x + 1][i];
					}
					if (y != 4) {
						sum += _eris2[(y + 1) * 5 + x][i];
					}

					if (pos == 7 || pos == 11 || pos == 13 || pos == 17 || pos == 12) {// 12 is ? square
						sum = 0; // override previous calculations
						switch (pos) {
						case 7:
							sum += _eris2[2][i];
							sum += _eris2[6][i];
							sum += _eris2[8][i];
							sum += _eris2[0][i + 1];
							sum += _eris2[1][i + 1];
							sum += _eris2[2][i + 1];
							sum += _eris2[3][i + 1];
							sum += _eris2[4][i + 1];
							break;
						case 11:
							sum += _eris2[10][i];
							sum += _eris2[6][i];
							sum += _eris2[16][i];
							sum += _eris2[0][i + 1];
							sum += _eris2[5][i + 1];
							sum += _eris2[10][i + 1];
							sum += _eris2[15][i + 1];
							sum += _eris2[20][i + 1];
							break;
						case 13:
							sum += _eris2[8][i];
							sum += _eris2[14][i];
							sum += _eris2[18][i];
							sum += _eris2[4][i + 1];
							sum += _eris2[9][i + 1];
							sum += _eris2[14][i + 1];
							sum += _eris2[19][i + 1];
							sum += _eris2[24][i + 1];
							break;
						case 17:
							sum += _eris2[16][i];
							sum += _eris2[22][i];
							sum += _eris2[18][i];
							sum += _eris2[20][i + 1];
							sum += _eris2[21][i + 1];
							sum += _eris2[22][i + 1];
							sum += _eris2[23][i + 1];
							sum += _eris2[24][i + 1];
							break;
						default:// 12
							ng[pos][i] = 0;// så summan blir bra
						}
					}

					switch (sum) {
					case 1:
						if (_eris2[pos][i] == 1) { // it doesn't die
							ng[pos][i] = 1;
						}
					case 2:
						if (_eris2[pos][i] == 0) { // infest
							ng[pos][i] = 1;
						}
						break;
					default:
						ng[pos][i] = 0;
					}
				}
			}
		}

		for (int j = 0; j < 400; j++) {
			for (int i = 0; i < 25; i++) {
				_eris2[i][j] = ng[i][j];
			}
		}
	}

	void lifeCycle() {
		int pos = 0;
		int sum = 0;
		int[] ng = new int[25];

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
		del2();
	}

	public void del2() {
		readInput(24, 0);

		for (int i = 0; i < 200; i++) {
			lifeCycle2();
		}
		print2();
		System.out.println("Del 2: " + countBugs());
	}

	public void del1() {
		readInput(24, 0);
		Map<Integer, Integer> twice = new HashMap<>();

		while (twice.put(getBiodiverity(), 1) == null) {
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

	private void print2() {
		String s = "";
		for (int r = -5; r < 6; r++) {
			System.out.println("depth " + r);
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					if (x == 2 && y == 2) {
						s += "?";
					} else {
						s += _eris2[y * 5 + x][199 + r] == 1 ? "#" : ".";
					}
				}
				System.out.println(s);
				s = "";
			}
		}
	}

	private int countBugs() {
		int numberOfBugs = 0;
		for (int r = -199; r < 200; r++) {
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					numberOfBugs += _eris2[y * 5 + x][199 + r];
				}
			}
		}
		return numberOfBugs;
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
						_eris2[y * 5 + x][199] = 1;
					} else if (tmp.equals(".")) {
						_eris[y * 5 + x] = 0;
						_eris2[y * 5 + x][199] = 0;
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
