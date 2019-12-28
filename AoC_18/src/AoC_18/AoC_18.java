package AoC_18;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class AoC_18 {
	static HashMap<Point, Integer> _space = new HashMap<>();
	static int _middleX = 0;
	static int _middleY = 0;
	static HashMap<Point, String> _keysAndDoors = new HashMap<>();
	static HashMap<String, String> _neighbors = new HashMap<>();
	static HashMap<String, Integer> _keyDistances = new HashMap<>();
	static HashMap<Set<String>, Set<String>> _memoizationReachableKeys = new HashMap<>();
	static HashMap<String, HashMap<Set<String>, Integer>> _memoizationCurrentKeyAndStateToDistance = new HashMap<>();
	static Integer _numberOfKeys = 0;

	public static void main(final String[] args) {
		final AoC_18 a = new AoC_18();
		a.run();
	}

	public void run() {
//		del1();
		del2();
	}

	public void del1() {
		readInput(18, 0);
		getNeighbors();
		getDistances();
		String keysAndDoors = printKeys(_keysAndDoors);
		for (int i = 0; i < keysAndDoors.length(); i++) {
			if (keysAndDoors.substring(i, i + 1).equals(keysAndDoors.substring(i, i + 1).toLowerCase())) {
				_numberOfKeys++;
			}
		}
		System.out.println("Del 1: " + recursiveDfs("@", 0));
	}

	public void del2() {
//		readInput(18, 9);// 6 7 8 9 // 8 går inte, men skiter i det för testet liknar inte input
		readInput(18, 0);// 40, 40
		_space.remove(new Point(40, 40));
		_space.remove(new Point(39, 40));
		_space.remove(new Point(41, 40));
		_space.remove(new Point(40, 39));
		_space.remove(new Point(40, 41));
		_keysAndDoors.remove(new Point(40, 40));// bort med @
		_keysAndDoors.put(new Point(41, 39), "1");
		_keysAndDoors.put(new Point(39, 39), "2");
		_keysAndDoors.put(new Point(39, 41), "3");
		_keysAndDoors.put(new Point(41, 41), "4");

		getNeighbors();
		getDistances();
		String keysAndDoors = printKeys(_keysAndDoors);
		for (int i = 0; i < keysAndDoors.length(); i++) {
			if (keysAndDoors.substring(i, i + 1).equals(keysAndDoors.substring(i, i + 1).toLowerCase())) {
				_numberOfKeys++;
			}
		}

		Set<String> s = new HashSet<>();
		s.add("1");
		s.add("2");
		s.add("3");
		s.add("4");
		System.out.println("Del 2: " + recursiveDfs2(s, "1234", 0));
	}

	private int recursiveDfs2(Set<String> state, String currentRobotPosition, int bestDist) {
		if (state.size() == _numberOfKeys) {
			return bestDist;
		}

		Integer bestDistFromThisNode;
		HashMap<Set<String>, Integer> hashMap = _memoizationCurrentKeyAndStateToDistance.get(currentRobotPosition);
		if (hashMap == null) {
			_memoizationCurrentKeyAndStateToDistance.put(currentRobotPosition, new HashMap<Set<String>, Integer>());
		} else {
			bestDistFromThisNode = hashMap.get(state);
			if (bestDistFromThisNode != null) {
				return bestDistFromThisNode + bestDist;
			}
		}
		bestDistFromThisNode = Integer.MAX_VALUE / 2;

		Set<String> visit = getPossibleDestinationMemoization(state);

		if (visit.isEmpty()) {
			if (state.size() < _numberOfKeys) {
				System.out.println("Error deadlocked maze! currentNode= " + currentRobotPosition);
				return Integer.MAX_VALUE / 2;
			}
			return bestDist;
		}

		for (String s : visit) {
			int dist = 0;
			String newRobotPosition = "";
			int sKvadrant = getKvadrantFromNode(s);
			for (int i = 0; i < currentRobotPosition.length(); i++) {
				String fromKey = currentRobotPosition.substring(i, i + 1);
				if (sKvadrant == getKvadrantFromNode(fromKey)) {
					dist = _keyDistances.get(fromKey + s);
					newRobotPosition += s; // uppdatera med ny currentNode
				} else {
					newRobotPosition += fromKey;
				}
			}
			Set<String> newState = new HashSet<>(state);
			newState.add(s);
			bestDistFromThisNode = Math.min(bestDistFromThisNode,
					recursiveDfs2(newState, newRobotPosition, bestDist + dist));
		}
		_memoizationCurrentKeyAndStateToDistance.get(currentRobotPosition).put(state, bestDistFromThisNode - bestDist);
		return bestDistFromThisNode;
	}

	private static Set<String> getSetFromString(String inStr) {
		Set<String> ret = new HashSet<>();
		for (int i = 0; i < inStr.length(); i++) {
			ret.add(inStr.substring(i, i + 1));
		}
		return ret;
	}

	private static int getKvadrantFromNode(String node) {
		Point start = null;
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			if (entry.getValue().equals(node)) {
				start = entry.getKey();
			}
		}
		if (start.y > _middleY) { // undre
			if (start.x > _middleX) {// högre
				return 4;
			} else { // vänstra
				return 3;
			}
		} else { // övre
			if (start.x > _middleX) {// högre
				return 1;
			} else {// vänstra
				return 0;
			}
		}
	}

	private int recursiveDfs(String path, int bestDist) {
		String currentNode = path.substring(path.length() - 1, path.length());
		String pathBefore = path.substring(0, path.length() - 1);
		Set<String> stateBefore = getSetFromString(pathBefore);

		Set<String> state = getSetFromString(path);
		if (state.size() == _numberOfKeys) {
			return bestDist;
		}

		Integer bestDistFromThisNode = 0;
		HashMap<Set<String>, Integer> hashMap = _memoizationCurrentKeyAndStateToDistance.get(currentNode);
		if (hashMap == null) {
			_memoizationCurrentKeyAndStateToDistance.put(currentNode, new HashMap<Set<String>, Integer>());
		} else {
			bestDistFromThisNode = hashMap.get(stateBefore);
			if (bestDistFromThisNode != null) {
				return bestDistFromThisNode + bestDist;
			}
		}
		bestDistFromThisNode = Integer.MAX_VALUE / 2;

		Set<String> visit = getPossibleDestinationMemoization(state);
		if (visit.isEmpty()) {
			if (state.size() < _numberOfKeys) {
				System.out.println("Error deadlocked maze!");
				return Integer.MAX_VALUE / 2;
			}
			return bestDist;
		}

		for (String s : visit) {
			int dist = _keyDistances.get(currentNode + s);
			bestDistFromThisNode = Math.min(bestDistFromThisNode, recursiveDfs(path + s, bestDist + dist));
		}
		_memoizationCurrentKeyAndStateToDistance.get(currentNode).put(stateBefore, bestDistFromThisNode - bestDist);

		return bestDistFromThisNode;
	}

	private static Set<String> getPossibleDestinationMemoization(Set<String> takenKeys) {
		Set<String> reach = _memoizationReachableKeys.get(takenKeys);
		if (reach != null) {
			return reach;
		}

		String openDoors = "";
		for (String key : takenKeys) {
			openDoors += key;
		}
		openDoors = openDoors.toUpperCase();
		final String finalOpenDoors = openDoors;

		reach = new HashSet<>();
		for (String key : takenKeys) {
			String n = _neighbors.get(key);
			for (int i = 0; i < n.length(); i++) {
				reach.add(n.substring(i, i + 1));
			}
		}
		reach.removeIf((String element) -> {
			return (!finalOpenDoors.contains(element) && element.equals(element.toUpperCase()));
		});

		Set<String> previousReach;
		do {
			previousReach = new HashSet<>(reach);
			for (String key : previousReach) {
				if (openDoors.contains(key)) {// it's a door, and it's open
					String n = _neighbors.get(key); // open door sees
					for (int i = 0; i < n.length(); i++) {
						String tkn = n.substring(i, i + 1);
						reach.add(tkn);
					}
				}
			}
			reach.removeIf((String element) -> {
				return (!finalOpenDoors.contains(element) && element.equals(element.toUpperCase()));
			});
		} while (previousReach.size() != reach.size());
		for (String key : takenKeys) {// remove open doors and visited keys
			reach.remove(key.toUpperCase());
			reach.remove(key);
		}
		// remember this crazy computation
		_memoizationReachableKeys.put(takenKeys, reach);
		return reach;
	}

	private static void getDistances() {
		_keyDistances.clear();
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			boolean full = false;
			int dist = 0;
			_space.put(entry.getKey(), 1);
			while (!full) {
				full = true;// nollställs om vi uppdaterat något
				dist++;
				Point punkt;
				for (Entry<Point, Integer> ent : _space.entrySet()) {// flooding
					if (ent.getValue().equals(dist)) { // hittat det som skall expandera i 4 riktningar
						punkt = ent.getKey();
						Point sidoPunkter[] = new Point[4];
						sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
						sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
						sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
						sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);
						for (int i = 0; i < 4; i++) {
							Point sido = sidoPunkter[i];
							if (_space.get(sido) != null) {// är det ens i grottan?
								String maybe;
								maybe = _keysAndDoors.get(sido);
								if (maybe != null) { // här ligger det visst en nyckel eller dörr
									if (!maybe.equals(entry.getValue())) {
										if (_keyDistances.get(entry.getValue() + maybe) == null) {
											_keyDistances.put(entry.getValue() + maybe, dist);
											_keyDistances.put(maybe + entry.getValue(), dist);
										}
									}
								}
								if (_space.get(sido) == 0) {
									_space.put(sido, dist + 1);
									full = false;
								}
							}
						}
					}
				}
			}
			for (Entry<Point, Integer> e : _space.entrySet()) {// rensa
				_space.put(e.getKey(), 0);
			}
		}
	}

	private static void getNeighbors() {
		_neighbors.clear();
		for (Entry<Point, String> entry : _keysAndDoors.entrySet()) {
			boolean full = false;
			int dist = 0;
			String see = "";
			_space.put(entry.getKey(), 1);
			while (!full) {
				full = true;// nollställs om vi uppdaterat något
				dist++;
				Point punkt;
				for (Entry<Point, Integer> ent : _space.entrySet()) {// flooding
					if (ent.getValue().equals(dist)) { // hittat det som skall expandera i 4 riktningar
						punkt = ent.getKey();
						Point sidoPunkter[] = new Point[4];
						sidoPunkter[0] = new Point(punkt.x + 1, punkt.y);
						sidoPunkter[1] = new Point(punkt.x - 1, punkt.y);
						sidoPunkter[2] = new Point(punkt.x, punkt.y + 1);
						sidoPunkter[3] = new Point(punkt.x, punkt.y - 1);
						for (int i = 0; i < 4; i++) {
							Point sido = sidoPunkter[i];
							if (_space.get(sido) != null) {// är det ens i grottan?
								String maybe;
								maybe = _keysAndDoors.get(sido);
								if (maybe != null) { // här ligger det visst en nyckel eller dörr
									if (!maybe.equals(entry.getValue())) {// ej sig själv
										see += maybe;
									}
								} else {
									if (_space.get(sido) == 0) {
										_space.put(sido, dist + 1);
										full = false;
									}
								}
							}
						}
					}
				}
			}
			_neighbors.put(entry.getValue(), see);
			for (Entry<Point, Integer> e : _space.entrySet()) {// rensa
				_space.put(e.getKey(), 0);
			}
		}
	}

	private static String printKeys(HashMap<Point, String> keys) {
		String s = "";
		for (Entry<Point, String> entry : keys.entrySet()) {
			s += entry.getValue();
		}
		return s;
	}

	static void print(HashMap<Point, Integer> cave, HashMap<Point, String> keys) {
		String s = "", k = "";
		Integer c = 0;

		int maxx = 0, maxy = 0;
		for (Point key : cave.keySet()) {
			maxx = Math.max(maxx, key.x);
			maxy = Math.max(maxy, key.y);
		}
		for (int y = 0; y <= maxy + 1; y++) {
			for (int x = 0; x <= maxx + 1; x++) {
				c = cave.get(new Point(x, y));
				k = keys.get(new Point(x, y));
				if (c == null) {
					s += "#";
				} else if (k == null) {
					// s += "" + (c % 10);
					s += ".";
				} else {
					s += k;
				}
			}
			System.out.println(s);
			s = "";
		}
	}

	private static void readInput(int day, int suffix) {
		String str;
		int y = 0;
		_space.clear();
		_keysAndDoors.clear();
		FileInputStream in;
		try {
			in = new FileInputStream(
					"C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input" + suffix + ".txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				String tmp = "";
				_middleX = str.length() / 2;
				for (int x = 0; x < str.length(); x++) {
					tmp = str.substring(x, x + 1);
					if (tmp.equals("#")) {
						// ignore walls
					} else if (tmp.equals(".")) {
						_space.put(new Point(x, y), 0);
					} else { // keys, doors or starting point
						_space.put(new Point(x, y), 0);
						_keysAndDoors.put(new Point(x, y), tmp);
					}
				}
				y++;
			}
			_middleY = y / 2;
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
