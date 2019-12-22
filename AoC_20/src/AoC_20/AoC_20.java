package AoC_20;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class AoC_20 {
	static HashMap<Point, Integer> _space = new HashMap<>();
	static HashMap<Point, String> _warp = new HashMap<>();
	private static int _maxx;
	private static int _maxy;

	public static void main(final String[] args) {
		final AoC_20 a = new AoC_20();
		a.run();
	}

	public void run() {
		// del1();
		del2();
	}

	public void del2() {
		Scanner scan = new Scanner(System.in);
		readInput(20, 3);
		// System.out.println(_warp);
//		print(_space, false);
		ArrayList<HashMap<Point, Integer>> listOfSpaces = new ArrayList<HashMap<Point, Integer>>();

		HashMap<Point, Integer> innerspace = new HashMap<>();
		for (Entry<Point, Integer> e : _space.entrySet()) { // deep copy
			innerspace.put(new Point(e.getKey().x, e.getKey().y), new Integer(e.getValue()));
		}
		listOfSpaces.add(innerspace);

		int dist = 0;
		int recurseLevel = 0;

		Point start = null;
		for (Entry<Point, String> entry : _warp.entrySet()) {
			if (entry.getValue().equals("AA")) {
				start = entry.getKey();
			}
		}

		boolean run = true;
		listOfSpaces.get(recurseLevel).put(start, 1);
		while (run) {
			dist++;
			Point punkt;
			recurseLevel = listOfSpaces.size() - 1;
			for (int rl = 0; rl <= recurseLevel; rl++) { // öka i alla recurce space samtidigt
				for (Entry<Point, Integer> entry : listOfSpaces.get(recurseLevel).entrySet()) {
					if (entry.getValue().equals(dist)) { // hittat det som skall expandera i 4 riktningar
						punkt = entry.getKey();
						Point sidoPunkter[] = new Point[4];
						sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
						sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
						sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
						sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);
						for (int i = 0; i < 4; i++) {
							Point sido = sidoPunkter[i];
							if (listOfSpaces.get(recurseLevel).get(sido) != null) {// är det ens i grottan?
								String maybe;
								maybe = _warp.get(sido);
								if (maybe != null && (rl > 0 || (rl == 0 && sido.x > 3 && sido.x < _maxx - 3
										&& sido.y > 3 && sido.y > _maxy - 3))) { // warp här , leta upp andra warpen och
																					// öka där också
																					// only inner at recurselevel=0
									if (maybe.equals("ZZ") && rl == 0) { // funkar bara på recurselevel=0
										run = false;
									} else if (maybe.equals("AA")) {
										// do nothing
									} else {
//										System.out.println("warpa in " + sido);

										if (recurseLevel + 1 >= listOfSpaces.size()) {
											innerspace = new HashMap<>();
//												String text = scan.nextLine();
											for (Entry<Point, Integer> e : _space.entrySet()) { // deep copy
												innerspace.put(new Point(e.getKey().x, e.getKey().y),
														new Integer(e.getValue()));
											}
											listOfSpaces.add(innerspace);
										}

										for (Entry<Point, String> ent : _warp.entrySet()) {
											if (ent.getValue().equals(maybe) && !ent.getKey().equals(sido)) {
												if (sido.x > 3 && sido.x < _maxx - 3 && sido.y > 3
														&& sido.y > _maxy - 3) { // warpa in
													listOfSpaces.get(recurseLevel + 1).put(ent.getKey(), dist + 2);
//													System.out.println("Warp in " + (recurseLevel +1));
												} else { // warpa ut
													listOfSpaces.get(recurseLevel - 1).put(ent.getKey(), dist + 2);
//													System.out.println("Warp ut " + (recurseLevel -1));
												}
//												System.out.println("warpa ut" + ent.getKey());
//												String text = scan.nextLine();
											}
										}
									}
								} else if (listOfSpaces.get(recurseLevel).get(sido) < dist) {
									listOfSpaces.get(recurseLevel).put(sido, dist + 1);
								}
							}
						}
					}
				}
			}
		}
		System.out.println("Del 2: " + dist);
	}

	public void del1() {
		Scanner scan = new Scanner(System.in);
		readInput(20, 0);
		// System.out.println(_warp);
		print(_space, false);
		int dist = 0;

		Point start = null;
		for (Entry<Point, String> entry : _warp.entrySet()) {
			if (entry.getValue().equals("AA")) {
				start = entry.getKey();
			}
		}
		boolean run = true;
		_space.put(start, 1);
		while (run) {
			dist++;
			Point punkt;
			for (Entry<Point, Integer> entry : _space.entrySet()) {
				if (entry.getValue().equals(dist)) { // hittat det som skall expandera i 4 riktningar
					punkt = entry.getKey();
					Point sidoPunkter[] = new Point[4];
					sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
					sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
					sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
					sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);
					for (int i = 0; i < 4; i++) {
						Point sido = sidoPunkter[i];
						if (_space.get(sido) != null) {// är det ens i grottan?
							String maybe;
							maybe = _warp.get(sido);
							if (maybe != null) { // warp här , leta upp andra warpen och öka där också
								if (maybe.equals("ZZ")) {
									run = false;
								} else if (maybe.equals("AA")) {
									// do nothing
								} else {
									System.out.println("warpa in " + sido);
									for (Entry<Point, String> ent : _warp.entrySet()) {
										if (ent.getValue().equals(maybe) && !ent.getKey().equals(sido)) {
											_space.put(ent.getKey(), dist + 2); // warp tar 1 steg att göra
											System.out.println("warpa ut" + ent.getKey());
//												String text = scan.nextLine();
										}
									}
								}
							} else if (_space.get(sido) == 0) { // ta bara nollor
								_space.put(sido, dist + 1);
							}
						}
					}
				}
			}
		}
		System.out.println("Del 1: " + dist);
	}

	void print(HashMap<Point, Integer> cave, boolean warp) {
		String s = "";
		Integer c = 0;
		String w = "";
//		Scanner scan = new Scanner(System.in);
		int maxx = 0, maxy = 0;
		for (Point key : cave.keySet()) {
			maxx = Math.max(maxx, key.x);
			maxy = Math.max(maxy, key.y);
		}
		for (int y = 0; y <= maxy + 1; y++) {
			for (int x = 0; x <= maxx + 1; x++) {
				w = _warp.get(new Point(x, y));
				if (warp) {
					if (w == null) {
						c = cave.get(new Point(x, y));
						if (c == null) {
							s += "#";
						} else {
							s += ".";
						}
					} else {
						s += w.substring(0, 1);
					}
				} else {
					c = cave.get(new Point(x, y));
					if (c == null) {
						s += "#";
					} else {
						s += ".";
					}
				}
			}
			System.out.println(s);
			s = "";
		}
//		String text = scan.nextLine();
	}

	private void clearSpace() {
		for (Point key : _space.keySet()) {
			_space.put(key, 0);
		}
	}

	private static boolean isVersal(String a) {
		if (a.charAt(0) >= 'A' && a.charAt(0) <= 'Z') {
			return true;
		}
		return false;
	}

	private static void readInput(int day, int suffix) {
		String str;
		int y = 0;
		_maxx = 0;
		_maxy = 0;
		HashMap<Point, String> versaler = new HashMap<>();
		_space.clear();
		FileInputStream in;
		try {
			in = new FileInputStream(
					"C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input" + suffix + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				String tmp = "";
				for (int x = 0; x < str.length(); x++) {
					_maxx = Math.max(_maxx, x);
					tmp = str.substring(x, x + 1);
					if (tmp.equals(".")) {
						_space.put(new Point(x, y), 0);
					}
					if (isVersal(tmp)) {
						versaler.put(new Point(x, y), tmp);
					}

				}
				_maxy = Math.max(_maxy, y);
				y++;
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning " + e);
		}
		// System.out.println(versaler);
		for (Entry<Point, String> entry : versaler.entrySet()) {
			if (entry.getKey().y == 0) {// överst
				// System.out.println("övre " + entry.getValue());
				_warp.put(new Point(entry.getKey().x, 2),
						entry.getValue() + versaler.get(new Point(entry.getKey().x, 1)));
			}
			if (entry.getKey().x == 0) {// början
				// System.out.println("början " + entry.getValue());
				_warp.put(new Point(2, entry.getKey().y),
						entry.getValue() + versaler.get(new Point(1, entry.getKey().y)));
			}
			if (entry.getKey().y == _maxy) {// underst
//				System.out.println("unders " + entry.getValue());
				_warp.put(new Point(entry.getKey().x, _maxy - 2),
						versaler.get(new Point(entry.getKey().x, _maxy - 1)) + entry.getValue());
			}
			if (entry.getKey().x == _maxx) {// bakre
//				System.out.println("bakre " + entry.getValue());
				_warp.put(new Point(_maxx - 2, entry.getKey().y),
						versaler.get(new Point(_maxx - 1, entry.getKey().y)) + entry.getValue());
			}
			if (entry.getKey().y > 2 && entry.getKey().y < _maxy - 2
					&& _space.getOrDefault(new Point(entry.getKey().x, entry.getKey().y - 1), 242) == 0) {// överstinre
//				System.out.println("övreinre " + entry.getValue() + " " + entry.getKey());
				_warp.put(new Point(entry.getKey().x, entry.getKey().y - 1),
						entry.getValue() + versaler.get(new Point(entry.getKey().x, entry.getKey().y + 1)));
			}
			if (entry.getKey().x > 2 && entry.getKey().x < _maxx - 2
					&& _space.getOrDefault(new Point(entry.getKey().x - 1, entry.getKey().y), 242) == 0) {// börjaninre
//				System.out.println("börjaninre " + entry.getValue()+ " " + entry.getKey());
				_warp.put(new Point(entry.getKey().x - 1, entry.getKey().y),
						entry.getValue() + versaler.get(new Point(entry.getKey().x + 1, entry.getKey().y)));
			}
			if (entry.getKey().y > 2 && entry.getKey().y < _maxy - 2
					&& _space.getOrDefault(new Point(entry.getKey().x, entry.getKey().y + 1), 242) == 0) {// understinre
//				System.out.println("underinre " + entry.getValue()+ " " + entry.getKey());
				_warp.put(new Point(entry.getKey().x, entry.getKey().y + 1),
						versaler.get(new Point(entry.getKey().x, entry.getKey().y - 1)) + entry.getValue());
			}
			if (entry.getKey().x > 2 && entry.getKey().x < _maxx - 2
					&& _space.getOrDefault(new Point(entry.getKey().x + 1, entry.getKey().y), 242) == 0) {// bakreinre
//				System.out.println("bakreinre " + entry.getValue()+ " " + entry.getKey());
				_warp.put(new Point(entry.getKey().x + 1, entry.getKey().y),
						versaler.get(new Point(entry.getKey().x - 1, entry.getKey().y)) + entry.getValue());
			}

		}
	}

}
