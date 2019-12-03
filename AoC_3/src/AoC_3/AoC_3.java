package AoC_3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AoC_3 {
	static List<String> _a = new ArrayList<>();
	static List<String> _b = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		readInput();
		int minm = Integer.MAX_VALUE;
		int minsteps = Integer.MAX_VALUE;

		List<Key> tota = new ArrayList<>();
		List<Key> totb = new ArrayList<>();
		tota.add(new Key(0, 0));
		totb.add(new Key(0, 0));

		for (String s : _a) {
			tota.addAll(getPoints(tota.get(tota.size() - 1), s));
		}
		for (String s : _b) {
			totb.addAll(getPoints(totb.get(totb.size() - 1), s));
		}
		for (int i = 1; i < tota.size() - 1; i++) {
			for (int j = 1; j < totb.size() - 1; j++) {
				if (tota.get(i).equals(totb.get(j))) {
					minm = Math.min(minm, tota.get(i).getManhattan());
					minsteps = Math.min(minsteps, i + j);
					System.out.println("Manhattan " + tota.get(i).getManhattan() + " steps " + (i + j));
				}
			}
		}
		System.out.println("min Manhattan " + minm + " min Steps " + minsteps);
	}

	private static List<Key> getPoints(Key start, String path) {
		List<Key> out = new ArrayList<>();
		int x = start.x;
		int y = start.y;

		for (int i = 0; i < Integer.valueOf(path.substring(1)); i++) {
			switch (path.substring(0, 1)) {
			case "R":
				x++;
				break;
			case "U":
				y++;
				break;
			case "L":
				x--;
				break;
			default:// D
				y--;
			}
			out.add(new Key(x, y));
		}
		return out;
	}

	private static void readInput() throws FileNotFoundException, IOException {
		String str, splitStr[];
		FileInputStream in = new FileInputStream("C:\\git\\AoC2019\\AoC_3\\src\\AoC_3\\input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		str = br.readLine();
		splitStr = str.split(",");
		for (String s : splitStr) {
			_a.add(s);
		}
		str = br.readLine();
		splitStr = str.split(",");
		for (String s : splitStr) {
			_b.add(s);
		}
		br.close();
	}

	public static class Key {
		private final int x;
		private final int y;

		public Key(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getManhattan() {
			return Math.abs(x) + Math.abs(y);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Key))
				return false;
			Key key = (Key) o;
			return x == key.x && y == key.y;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}

		@Override
		public String toString() {
			return "Key [x=" + x + ", y=" + y + "]";
		}
	}

}
