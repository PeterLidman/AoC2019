package AoC_8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AoC_8 {
	static String _image;

	public static void main(String[] args) {
		AoC_8 a = new AoC_8();
		a.run();
	}

	public void run() {
		readInput();
		del1();
		System.out.println();
		del2();
	}

	@SuppressWarnings("static-method")
	public void del1() {
		int t0 = 0, t1 = 0, t2 = 0, t0min = Integer.MAX_VALUE, product = 0;
		char tkn;

		for (int j = 0; j < 100; j++) {
			for (int i = 0; i < 150; i++) {
				tkn = _image.charAt(150 * j + i);
				switch (tkn) {
				case '0':
					t0++;
					break;
				case '1':
					t1++;
					break;
				case '2':
					t2++;
					break;
				default:
				}
			}
			if (t0 < t0min) {
				t0min = t0;
				product = t1 * t2;
			}
			t0 = 0;
			t1 = 0;
			t2 = 0;
		}
		System.out.println("Del 1: " + product);
	}

	@SuppressWarnings("static-method")
	public void del2() {
		String picture = new String(new char[150]).replace("\0", "2");
		char tkn, pictkn;

		for (int j = 0; j < 100; j++) {
			for (int i = 0; i < 150; i++) {
				pictkn = picture.charAt(i);
				tkn = _image.charAt(150 * j + i);
				if (pictkn == '2' && tkn != '2') {
					picture = picture.substring(0, i) + tkn + picture.substring(i + 1);
				}
			}
		}
		picture = picture.replace('0', ' ');
		picture = picture.replace('1', '*');
		for (int i = 0; i < 6; i++) {
			System.out.println("Del 2: " + picture.substring(i * 25, i * 25 + 25));
		}
	}

	private static void readInput() {
		String str;
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_8\\src\\AoC_8\\input.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				_image = str.trim();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
