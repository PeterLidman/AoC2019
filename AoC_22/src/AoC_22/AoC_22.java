package AoC_22;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AoC_22 {
	static List<String> _shuffle = new ArrayList<>();
	static List<Integer> _deckA = new ArrayList<Integer>();
	static List<Integer> _deckB = new ArrayList<Integer>();
	static boolean _isYourDeckA = true;// else it's B
	static int _deckSize = 10007;
//	static int _deckSize = 10;// example

	public static void main(final String[] args) {
		final AoC_22 a = new AoC_22();
		a.run();
	}

	public void run() {
		del1();
		del2();
	}

	private BigInteger seek(BigInteger deckSize, BigInteger timesShuffled, int position) {
		BigInteger[] in = new BigInteger[] { new BigInteger("1"), new BigInteger("0") };

		for (int i = _shuffle.size() - 1; i >= 0; i--) {
			if (_shuffle.get(i).startsWith("deal into")) {
				reverseDealIntoNewStack(in);
			} else if (_shuffle.get(i).startsWith("deal with")) {
				int with = Integer.valueOf(_shuffle.get(i).substring(19).trim());
				reverseDealWithIncrement(in, deckSize, with);
			} else {// cut
				int cut = Integer.valueOf(_shuffle.get(i).substring(3).trim());
				reverseCutNcards(in, cut);
			}
			in[0] = in[0].mod(deckSize);
			in[1] = in[1].mod(deckSize);
		}
		BigInteger pow = in[0].modPow(timesShuffled, deckSize);

		return pow.multiply(new BigInteger("" + position))
				.add(in[1].multiply(pow.add(deckSize).subtract(new BigInteger("1"))).multiply(
						in[0].subtract(new BigInteger("1")).modPow(deckSize.subtract(new BigInteger("2")), deckSize)))
				.mod(deckSize);
	}

	private void reverseCutNcards(BigInteger[] in, Integer cut) {
		in[1] = in[1].add(new BigInteger(cut.toString()));
	}

	private void reverseDealWithIncrement(BigInteger[] in, BigInteger deckSize, Integer with) {
		BigInteger p = new BigInteger(with.toString()).modPow(deckSize.subtract(new BigInteger("2")), deckSize);
		in[0] = in[0].multiply(p);
		in[1] = in[1].multiply(p);
	}

	private void reverseDealIntoNewStack(BigInteger[] in) {
		in[0] = in[0].multiply(new BigInteger("-1"));
		in[1] = in[1].add(new BigInteger("1")).multiply(new BigInteger("-1"));
	}

	public void del2() {
		readInput(22);
		System.out
				.println("del 2: " + seek(new BigInteger("119315717514047"), new BigInteger("101741582076661"), 2020));
	}

	private void init() {
		for (int i = 0; i < _deckSize; i++) {
			_deckA.add(i);
			_deckB.add(i);// ifall första blir en deal
		}
	}

	private void cutNcards(int n) {
		int cut = n;

		if (cut < 0) {
			cut = _deckSize + cut;
		}

		if (_isYourDeckA) {
			_deckB.clear();
			for (int i = cut; i < _deckSize; i++) {
				_deckB.add(_deckA.get(i));
			}
			for (int i = 0; i < cut; i++) {
				_deckB.add(_deckA.get(i));
			}
			_isYourDeckA = false;
		} else {
			_deckA.clear();
			for (int i = cut; i < _deckSize; i++) {
				_deckA.add(_deckB.get(i));
			}
			for (int i = 0; i < cut; i++) {
				_deckA.add(_deckB.get(i));
			}
			_isYourDeckA = true;
		}
	}

	private void dealWithIncrement(int n) {
		if (_isYourDeckA) {
			for (int i = 0; i < _deckSize; i++) {
				_deckB.set((i * n) % _deckSize, _deckA.get(i));
			}
			_isYourDeckA = false;
		} else {
			for (int i = 0; i < _deckSize; i++) {
				_deckA.set((i * n) % _deckSize, _deckB.get(i));
			}
			_isYourDeckA = true;
		}
	}

	private void dealIntoNewStack() {
		if (_isYourDeckA) {
			for (int i = 0; i < _deckSize; i++) {
				_deckB.set(_deckSize - i - 1, _deckA.get(i));
			}
			_isYourDeckA = false;
		} else {
			for (int i = 0; i < _deckSize; i++) {
				_deckA.set(_deckSize - i - 1, _deckB.get(i));
			}
			_isYourDeckA = true;
		}
	}

	public void del1() {
		init();
		readInput(22);

		for (int i = 0; i < _shuffle.size(); i++) {
			if (_shuffle.get(i).startsWith("deal into")) {
				dealIntoNewStack();
			} else if (_shuffle.get(i).startsWith("deal with")) {
				int with = Integer.valueOf(_shuffle.get(i).substring(19).trim());
				dealWithIncrement(with);
			} else {// cut
				int cut = Integer.valueOf(_shuffle.get(i).substring(3).trim());
				cutNcards(cut);
			}
		}
		if (_isYourDeckA) {
			for (int i = 0; i < _deckSize; i++) {
				if (_deckA.get(i) == 2019) {
					System.out.println("Del 1: " + i);
				}
			}
		} else {
			for (int i = 0; i < _deckSize; i++) {
				if (_deckB.get(i) == 2019) {
					System.out.println("Del 1: " + i);
				}
			}
		}
	}

	private static void readInput(int day) {
		String str;
		_shuffle.clear();
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input.txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				_shuffle.add(str);
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

}
