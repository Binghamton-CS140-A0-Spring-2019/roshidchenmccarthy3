package projectview;

import project.Machine;
import javax.swing.JFrame;

public class Mediator {
	private Machine machine; 
	private JFrame frame;
	
	public void step(){
		
	}
	
	public Machine getMachine() {
		return machine; 
	}
	
	public void setMachine(Machine m) {
		this.machine = m; 
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	
}
