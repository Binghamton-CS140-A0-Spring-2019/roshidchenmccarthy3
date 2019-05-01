package project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Memory {
	public static final int DATA_SIZE = 512; 
	private int[] data = new int[DATA_SIZE];
	private int CODE_SIZE = 256; 
	private List<Instruction> code = new ArrayList<>();
	int changedDataIndex = -1; 
	
	int[] getData(int min, int max) {
		if(min<0 || max<0 || max>DATA_SIZE-1 || min>DATA_SIZE-1 || min > max)
			throw new DataAccessException("Invalid input");
		return Arrays.copyOfRange(data, min, max);	
	}
	
	int[] getData(){
		return data;
	}
	
	List<Instruction> getCode(){
		return code;
	}
	
	int getData(int index) {
		if(index < 0 || index > DATA_SIZE - 1) 
			throw new DataAccessException("Invalid index");
		return data[index];
	}
	
	void setData(int index, int value) {
		if(index < 0 || index > DATA_SIZE - 1) 
			throw new DataAccessException("Invalid index");
		data[index] = value; 
		changedDataIndex = index; 
		}
	
	Instruction getCode(int index){
		if(index > code.size() || index < 0) 
			throw new CodeAccessException("Illegal access to code");
		return code.get(index);
	}
	
	public Instruction[] getCode(int min, int max) {
		// throw CodeAccessException if it is NOT true 
		// that 0 <= min <= max < code.size()
		if(min < 0 || min > max || min > code.size() || max < 0 || max > code.size())
			throw new CodeAccessException("Illegal access to code");
		Instruction[] temp = {};
		temp = code.toArray(temp);
		return Arrays.copyOfRange(temp, min, max); 
		}
	
	void addCode(Instruction value) {
		if(code.size() < CODE_SIZE)
			code.add(value);
	}
	
	void setCode(int index, Instruction instr) {
		if(index > code.size() || index < 0) 
			throw new CodeAccessException("Illegal access to code");
		else code.set(index, instr);
	}
	
	void clearCode() {
		code.clear();
	}
	
	int getProgramSize() { 
		return code.size();
	}
	
	
	void clearData() {
		for(int i = 0; i < data.length; i++) data[i] = 0;
		changedDataIndex = -1;
	}
	
	int getchangedDataIndex() {
		return this.changedDataIndex;
	}
	
}
