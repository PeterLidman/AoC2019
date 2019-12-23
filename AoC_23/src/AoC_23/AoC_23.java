package AoC_23;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AoC_23 {
	static List<Long> _intCode = new ArrayList<Long>();

	public static void main(final String[] args) {
		final AoC_23 a = new AoC_23();
		a.run();
	}

	public void run() {
		del1();
		del2();
	}

	public void del1() {
		readInput(23);
		String result;

		List<IntComputer> network = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			network.add(new IntComputer(_intCode));
			network.get(i).addInput((long) i);
			network.get(i).addInput(-1L);
		}

		Long addr = 0L;
		Long x = 0L;
		Long y = 0L;
		boolean run = true;

		runloop: while (run) {
			run = false;
			for (int i = 0; i < 50; i++) {
				if (network.get(i).getInputQueueSize() == 0) {
					network.get(i).addInput(-1L);
				}
				network.get(i).run();

				while (true) {
					result = network.get(i).getOutput();
					if (!result.equals("")) {
						run = true;// any output = continue running
						addr = Long.valueOf(result);
					} else {
						break;
					}
					network.get(i).run();
					result = network.get(i).getOutput();
					if (!result.equals("")) {
						x = Long.valueOf(result);
					}
					network.get(i).run();
					result = network.get(i).getOutput();
					if (!result.equals("")) {
						y = Long.valueOf(result);
					}
					if (addr == 255) {
						System.out.println("Del 1: " + y);
						run = false;
						break runloop;
					} else {
						network.get(addr.intValue()).addInput((long) x);
						network.get(addr.intValue()).addInput((long) y);
					}
				}
			}
		}
	}

	public void del2() {
		readInput(23);
		String result;
		HashMap<Long, Boolean> yduplicate = new HashMap<>();

		List<IntComputer> network = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			network.add(new IntComputer(_intCode));
			network.get(i).addInput((long) i);
			network.get(i).addInput(-1L);
		}

		Long addr = 0L;
		Long x = 0L;
		Long y = 0L;
		Long natX = 0L;
		Long natY = 0L;
		boolean run = true;

		runloop: while (run) {
			run = false;
			for (int i = 0; i < 50; i++) {
				if (network.get(i).getInputQueueSize() == 0) {
					network.get(i).addInput(-1L);
				}
				network.get(i).run();

				while (true) {
					result = network.get(i).getOutput();
					if (!result.equals("")) {
						run = true;// any output = continue running
						addr = Long.valueOf(result);
					} else {
						break;
					}
					network.get(i).run();
					result = network.get(i).getOutput();
					if (!result.equals("")) {
						x = Long.valueOf(result);
					}
					network.get(i).run();
					result = network.get(i).getOutput();
					if (!result.equals("")) {
						y = Long.valueOf(result);
					}
					if (addr == 255) { // NAT
						natX = x;
						natY = y;
					} else {
						network.get(addr.intValue()).addInput((long) x);
						network.get(addr.intValue()).addInput((long) y);
					}
				}
			}
			if (!run) { // network is idle
				network.get(0).addInput(natX);
				network.get(0).addInput(natY);
				if (yduplicate.put(natY, true) != null) {
					System.out.println("Del 2: " + natY);
					break runloop;
				}
				run = true;
			}
		}
	}

	private static void readInput(int day) {
		String str, splitStr[];
		_intCode.clear();
		FileInputStream in;
		try {
			in = new FileInputStream("C:\\git\\AoC2019\\AoC_" + day + "\\src\\AoC_" + day + "\\input.txt");
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				splitStr = str.split(",");
				for (final String s : splitStr) {
					_intCode.add(Long.parseLong(s));
				}
			}
			br.close();
		} catch (final IOException e) {
			System.out.println("Något sket sig vid inläsning");
		}
	}

	public class IntComputer {
		private HashMap<Long, Long> _ic = new HashMap<>();
		private List<Long> _in = new ArrayList<Long>();
		private Long _pc = 0L;
		private String _out = new String("");
		private int _noIn = 0;
		private Long _relativeBase = 0L;

		public IntComputer(final List<Long> p) {
			Long pos = 0L;
			for (Long i : p) {
				_ic.put(pos++, i);
			}
		}

		public void listInput() {
			System.out.println(_in);
		}

		public int getInputQueueSize() {
			return _in.size();
		}

		private Long hashGet(Long pos) {
			Long ret;

			if (pos < 0) {
				System.out.println("Minnet kan ej vara negativt");
				return 0L;
			}
			if ((ret = _ic.get(pos)) == null) {
				_ic.put(pos, new Long("0"));// sätt till 0 i nytt minne
				ret = 0L;
			}
			return ret;
		}

		private void hashSet(Long pos, Long value) {
			if (pos < 0) {
				System.out.println("Minnet kan ej vara negativt");
				return;
			}
			_ic.put(pos, value);
		}

		public void run() {
			while (true) {
				Long v1, v2;
				final String opCode = "000000" + hashGet(_pc);
				final String op = opCode.substring(opCode.length() - 2, opCode.length());
				final int m1 = Integer.valueOf(opCode.substring(opCode.length() - 3, opCode.length() - 2));
				final int m2 = Integer.valueOf(opCode.substring(opCode.length() - 4, opCode.length() - 3));
				final int m3 = Integer.valueOf(opCode.substring(opCode.length() - 5, opCode.length() - 4));

				if (op.equals("99")) {
					return;
				}
				if (op.equals("01") || op.equals("02")) {// + & *
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (op.equals("01")) { // operation
						v1 = v1 + v2;
					} else {
						v1 = v1 * v2;
					}
					setV(v1, m3);
					_pc += 4;
				} else if (op.equals("03")) {// input
					if (_noIn > _in.size() - 1) {
						return; // halt until next input ready!
					}
					switch (m1) {
					case 0:
						hashSet(hashGet(_pc + 1), _in.get(_noIn++));
						break;
					case 1:
						hashSet(_pc + 1, _in.get(_noIn++));
						break;
					case 2:
						hashSet(_relativeBase + hashGet(_pc + 1), _in.get(_noIn++));
						break;
					default:
						System.out.println("Error, unknown mode: " + m1);
					}
					_pc += 2;
				} else if (op.equals("04")) {// output
					v1 = getV(1L, m1);
					_out = _out + v1;
					_pc += 2;
					return;
				} else if (op.equals("05")) {// jump-if-true
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (!v1.equals(0L)) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("06")) {// jump-if-false
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1.equals(0L)) {
						_pc = v2;
					} else {
						_pc += 3;
					}
				} else if (op.equals("07")) {// less than
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1.compareTo(v2) < 0) {
						setV(1L, m3);
					} else {
						setV(0L, m3);
					}
					_pc += 4;
				} else if (op.equals("08")) {// equals
					v1 = getV(1L, m1);
					v2 = getV(2L, m2);
					if (v1.equals(v2)) {
						setV(1L, m3);
					} else {
						setV(0L, m3);
					}
					_pc += 4;
				} else if (op.equals("09")) {// adjust relative base
					v1 = getV(1L, m1);
					_relativeBase += v1;
					_pc += 2;
				} else {
					System.out.println("Error, Unknown opcode: " + op + " at position " + _pc);
				}
			}
		}

		private void setV(Long value, final int mode) {
			switch (mode) {
			case 0:
				hashSet(hashGet(_pc + 3), value);
				break;
			case 1:
				hashSet(_pc + 3, value);
				break;
			case 2:
				hashSet(_relativeBase + hashGet(_pc + 3), value);
				break;
			default:
				System.out.println("Error, unknown mode: " + mode);
			}
		}

		private Long getV(final Long offset, final int mode) {
			Long v = 0L;
			switch (mode) {
			case 0:
				v = hashGet(hashGet(_pc + offset));
				break;
			case 1:
				v = hashGet(_pc + offset);
				break;
			case 2:
				v = hashGet(_relativeBase + hashGet(_pc + offset));
				break;
			default:
				System.out.println("Error, unknown mode: " + mode);
			}
			return v;
		}

		public void addInput(final Long a) {
			_in.add(a);
		}

		public String getOutput() {
			String ret = _out;
			_out = "";
			return ret;
		}
	}

}
