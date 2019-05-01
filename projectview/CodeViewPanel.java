package projectview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import project.Instruction;
import project.Machine;
import project.Memory;

public class CodeViewPanel {
	private Machine machine; 
	private Instruction instr; 
	private JScrollPane scroller; 
	private JTextField[] codeText = new JTextField[Memory.DATA_SIZE];; 
	private JTextField[] codeBinHex = new JTextField[Memory.DATA_SIZE];
	private int previousColor = -1; 
	
	public CodeViewPanel(Machine m) {
		machine = m;
	}
	
	public JComponent createCodeDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		Border border = BorderFactory.createTitledBorder(
		        BorderFactory.createLineBorder(Color.BLACK),
		        "Data Memory View ["+ lower +"-"+ upper +"]",
		        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		panel.setBorder(border);
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		innerPanel.setBorder(border);
		
		JPanel numPanel = new JPanel();
		JPanel decimalPanel = new JPanel();
		JPanel hexPanel = new JPanel(); 
		numPanel.setLayout(new GridLayout(0,1));
		decimalPanel.setLayout(new GridLayout(0,1));
		hexPanel.setLayout(new GridLayout(0,1));
		innerPanel.add(numPanel, BorderLayout.LINE_START); 
		innerPanel.add(decimalPanel, BorderLayout.CENTER); 
		innerPanel.add(hexPanel, BorderLayout.LINE_END);
	}
}
