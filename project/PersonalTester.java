package project;

public class PersonalTester {

	public static void main(String[] args) {
		Machine machine = new Machine(() ->  {});
		int[] dataCopy = new int[Memory.DATA_SIZE];
		int accInit;
		int pcInit;
		for (int i = 0; i < Memory.DATA_SIZE; i++) {
			dataCopy[i] = -5*Memory.DATA_SIZE + 10*i;
			machine.setData(i, -5*Memory.DATA_SIZE + 10*i);
			// Initially the machine will contain a known spread
			// of different numbers: 
			// -2560, -2550, -2540, ..., 0, 10, 20, ..., 2550 
			// This allows us to check that the instructions do 
			// not corrupt machine unexpectedly.
			// 0 is at index 256
		}
		// prefill with 10 NOP instructions
		for(int i = 0; i < 10; i++)
			machine.addCode(new Instruction((byte)0, 0));
		accInit = 7;
		pcInit = 4;
		
		Instruction instr = new Instruction((byte)0b00101000,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		System.out.println(pcInit + 1 + ", " + machine.getPC());
		System.out.println(instr.opcode / 8);
	}
}
