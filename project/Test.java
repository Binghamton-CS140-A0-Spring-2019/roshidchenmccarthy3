package project;

public class Test {

	public static void main(String[] args) {
		Instruction i = new Instruction((byte) 0111,33);
		Instruction.checkParity(i);
	}

}
