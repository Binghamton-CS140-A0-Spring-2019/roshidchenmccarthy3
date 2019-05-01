package project;
import static project.Instruction.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
public class Machine {
	private class CPU{
		private int accum;
		private int pc;
	}
	public final Map<Integer, Consumer<Instruction>> ACTION = new TreeMap<>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private boolean withGUI = false;
	private HaltCallback callBack;
	
	public Machine(HaltCallback cb) {
		callBack = cb;
		//ACTION entry for "NOP"
		ACTION.put(OPCODES.get("NOP"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags != 0){
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		});
		ACTION.put(OPCODES.get("ADD"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum += memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum += instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum += memory.getData(memory.getData(instr.arg));				
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		});
		ACTION.put(OPCODES.get("HALT"), instr -> {
			int flags = instr.opcode & 6;
			if(flags != 0){
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			callBack.halt();
		});
		ACTION.put(OPCODES.get("JUMP"), instr -> {
			int flags = instr.opcode & 6;
			if(flags == 0) { // direct addressing
				cpu.pc += instr.arg;
			} else if(flags == 2) { // immediate addressing
				cpu.pc = instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.pc += memory.getData(instr.arg);			
			} 
			else if(flags == 6) {
				cpu.pc = memory.getData(instr.arg);
			}
			else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
		});
		ACTION.put(OPCODES.get("JMPZ"), instr -> {
			int flags = instr.opcode & 6;
			if(cpu.accum == 0) {
				if(flags == 0) { // direct addressing
					cpu.pc += instr.arg;
				} else if(flags == 2) { // immediate addressing
					cpu.pc = instr.arg;
				} else if(flags == 4) { // indirect addressing
					cpu.pc += memory.getData(instr.arg);			
				} 
				else if(flags == 6) {
					cpu.pc = memory.getData(instr.arg);
				}
				else {
					String fString = "(" + (flags%8 > 3?"1":"0") 
							+ (flags%4 > 1?"1":"0") + ")";
					throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
				}
			}
			else cpu.pc++;
		});
		ACTION.put(OPCODES.get("LOD"), instr -> {
			int flags = instr.opcode & 6;
			if(flags == 0) { // direct addressing
				cpu.accum = memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum = instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum = memory.getData(memory.getData(instr.arg));				
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});
		ACTION.put(OPCODES.get("STO"), instr -> {
			int flags = instr.opcode & 6;
			if(flags == 0) { // direct addressing
				memory.setData(instr.arg, cpu.accum);
			} 
		    else if(flags == 4) { // indirect addressing
		    	memory.setData(memory.getData(instr.arg), cpu.accum);		
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});
		ACTION.put(OPCODES.get("NOT"), instr -> {
			int flags = instr.opcode & 6;
			if(flags != 0) {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			
			if(cpu.accum == 0) cpu.accum = 1;
			else cpu.accum = 0;
			cpu.pc++;
		});
		ACTION.put(OPCODES.get("AND"), instr -> {
			int flags = instr.opcode & 6;
			if(flags == 0) {
				if(cpu.accum != 0 && memory.getData(instr.arg) != 0) cpu.accum = 1; 
				else cpu.accum = 0;
				cpu.pc++;
			}
			else if(flags == 2) {
				if(cpu.accum != 0 && instr.arg != 0) cpu.accum = 1;
				else cpu.accum = 0;
				cpu.pc++;
			}
			else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
		});
		ACTION.put(OPCODES.get("CMPL"), instr -> {
			int flags = instr.opcode & 6;
			if(flags != 0) {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			if(memory.getData(instr.arg) < 0) cpu.accum = 1;
			else cpu.accum = 0;
			cpu.pc++;
		});
		ACTION.put(OPCODES.get("CMPZ"), instr -> {
			int flags = instr.opcode & 6;
			System.out.println(flags);
			if(flags != 0) {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			if(memory.getData(instr.arg) == 0) cpu.accum = 1;
			else cpu.accum = 0;
			cpu.pc++;
		});
		ACTION.put(OPCODES.get("SUB"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum -= memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum -= instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum -= memory.getData(memory.getData(instr.arg));				
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		});
		ACTION.put(OPCODES.get("MUL"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum *= memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum *= instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum *= memory.getData(memory.getData(instr.arg));				
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		});
		ACTION.put(OPCODES.get("DIV"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				if(memory.getData(instr.arg) == 0) throw new DivideByZeroException("Zero Division");
				cpu.accum /= memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				if(instr.arg == 0) throw new DivideByZeroException("Zero Division");
				cpu.accum /= instr.arg;
			} else if(flags == 4) { // indirect addressing
				if(memory.getData(memory.getData(instr.arg)) == 0) throw new DivideByZeroException("Zero Division");
				cpu.accum /= memory.getData(memory.getData(instr.arg));				
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		});
	}
	
	public void halt() {
		callBack.halt();
	}
	public int[] getData() {
		return memory.getData();
	}
	public int getData(int i){
		return memory.getData(i);
	}
	public int[] getData(int i, int j){
		return memory.getData(i, j);
	}
	public int getPC() {
		return cpu.pc;
	}
	public int getAccum() {
		return cpu.accum;
	}
	public void setData(int i, int j) {
		memory.setData(i, j);		
	}
	public void setAccum(int i) {
		cpu.accum = i;
	}
	public void setPC(int i) {
		cpu.pc = i;
	}
	
	public Instruction getCode(int index) {
		return memory.getCode(index);
	}
	public int getProgramSize() {
		return memory.getProgramSize();
	}
	public void addCode(Instruction j) {
		memory.addCode(j);	
	}
	// package private
	void setCode(int index, Instruction instr) {
		memory.setCode(index, instr);	
	}
	public List<Instruction> getCode() {
		return memory.getCode();
	}
	//package private
	Instruction[] getCode(int min, int max) {
		return memory.getCode(min,max);
	}
	
	
	public int getChangedDataIndex() {
		return memory.getchangedDataIndex();
	}

	
	public void clear() {
	// TODO
	// call clearData and clearCode in memory
	// set cpu.pc and cpu.accum to zero
		memory.clearData();
		memory.clearCode();
		cpu.pc = 0; 
		cpu.accum = 0; 
	}
	public void step(){
	// TODO
	// needs a try/catch
	// in the try, make an Instruction instr equal to
	// getCode(cpu.pc). Call Instruction.checkParity with argument instr.
	// That could throw an Exception, so we only do the next instruction if 
	// the parity bit is OK. 
	// Call ACTION.get(instr.opcode/8).accept(instr)
	// next we have the catch(Exception e) 
	// here put in the commented line // e.printStackTrace();
	// in case we want to find out what exception is occurring when debugging
	// the other lines of the exception are halt(); and throw e;
		try{
			Instruction instr = getCode(cpu.pc);
			Instruction.checkParity(instr);
			ACTION.get(instr.opcode/8).accept(instr); 
		}
		catch(Exception e) {
			e.printStackTrace();
			halt(); 
			throw e;
		}
	}
	
	
}
