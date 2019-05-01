package project;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Loader {
	public static String load(Machine machine, File file) throws Exception{
		if(machine == null || file == null) return null;
		int numInstr = 0; 
		boolean flag = true; 
		try (DataInputStream input = new DataInputStream(new FileInputStream(file))){
			while(true) {
				int x = input.readInt();
				int tmp = input.readInt();
				System.out.println(tmp);
				if(flag && x == -1) flag = false; 
				else if(flag) {
					numInstr+=1; 
					machine.addCode(new Instruction((byte)x, tmp));
				}
				else machine.setData(x, tmp);
			}
		} catch (EOFException e) {
			return "" + numInstr;
		} catch (FileNotFoundException e1) {
			return("File " + file.getName() + " Not Found");
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Machine var = new Machine(() -> System.exit(0));
		String str = Loader.load(var, new File("factorial.pexe"));
		int x = Integer.parseInt(str);
		for(int i = 0; i < x; i++) System.out.println(var.getCode(i).getText());
		System.out.println(Arrays.toString(var.getData(0,20)));
		var.setPC(0);
		while(true) {
			System.out.println(var.getCode(var.getPC()).getText());
			System.out.println(var.getCode(var.getPC()).opcode);
			System.out.println(var.getCode(var.getPC()).arg);
			var.step();
			System.out.println(Arrays.toString(var.getData(0,20)));
		}
	}
	
}
