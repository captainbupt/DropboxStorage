package GUI;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;

import java.awt.GridLayout;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JScrollBar;
import java.awt.List;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

public class Temp {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Temp window = new Temp();
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.frame.setVisible(true);
					window.frame.pack();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Temp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 681, 551);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button1,button2,button3;
		String[] columNames = {"FileName", "Path", "Size"};
		Object[][] data = {
				{"123","123","123"},
				{"123","123","123"},
				{"123","123","123"},	
		};
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel1 = new JPanel();
		frame.getContentPane().add(panel1);
		panel1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel1.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.NORTH);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.SOUTH);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.WEST);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		button1 = new JButton("Upload");
		button1.setIcon(new ImageIcon("/home/liuwei90/workspace/ec504_project/Imagesource/upload.png"));
		panel.add(button1);
		button2 = new JButton("Download");
		button2.setIcon(new ImageIcon("/home/liuwei90/workspace/ec504_project/Imagesource/download.png"));
		panel.add(button2);
		button3 = new JButton("Delete");
		button3.setIcon(new ImageIcon("/home/liuwei90/workspace/ec504_project/Imagesource/delete.png"));
		panel.add(button3);
		
		
	}
}