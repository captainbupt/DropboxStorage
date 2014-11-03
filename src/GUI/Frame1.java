package GUI;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JList;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Frame1 {

	private JFrame frame;
	DefaultListModel listModel;
	JFrame msgframe;
	JList filelist;
	JButton btnUpload;
	JButton btnDownload;
	JButton btnDelete;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 473, 304);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		listModel = new DefaultListModel();
		msgframe = new JFrame();
		
		//file list
		filelist = new JList(listModel);
		filelist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		filelist.setBounds(22, 25, 410, 171);
		frame.getContentPane().add(filelist);
		
		//upload button and its event handler
		btnUpload = new JButton("Upload");
		btnUpload.setBounds(22, 208, 117, 29);
		frame.getContentPane().add(btnUpload);
		//when click, a file choose window pop up and can select file from system to upload
		//the file name will add to the list to show on the list panel
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadfile();
				
			}
		});

		
		//download button and its event handler
		btnDownload = new JButton("Download");
		btnDownload.setBounds(172, 208, 117, 29);
		frame.getContentPane().add(btnDownload);
		if(listModel.getSize()==0){
			btnDownload.setEnabled(false);
		}
		else
			btnDownload.setEnabled(true);
		//when click, it should download the file. currently, it does nothing but pops a message telling it works
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloadfile();
			}
		});
		
		//delete button and its event handler
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(315, 208, 117, 29);
		frame.getContentPane().add(btnDelete);
		if(listModel.getSize()==0){
			btnDelete.setEnabled(false);
		}
		else
			btnDelete.setEnabled(true);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletefile();
			}
		});

	}
	public void uploadfile(){
		//create a file chooser
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt","txt");
		fc.setFileFilter(filter);
		fc.addChoosableFileFilter(filter);
		
		int response = fc.showSaveDialog(null);
		if(response == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			String filename = file.getName();
			listModel.addElement(filename);
			if(listModel.getSize()==0){
				btnDownload.setEnabled(false);
				btnDelete.setEnabled(false);
			}
			else{
				btnDownload.setEnabled(true);
				btnDelete.setEnabled(true);
			}
		}
	}
	public void downloadfile(){
		if(filelist.getSelectedIndex()==-1 && listModel.getSize()!=0){
			//show a message telling that have to select one
			JOptionPane.showMessageDialog(msgframe, "You have to select one!");
		}
		else{
				JOptionPane.showMessageDialog(msgframe, "Download success!");
		}
	}
	public void deletefile(){
		if(filelist.getSelectedIndex()==-1 && listModel.getSize()!=0){
			//show a message telling that have to select one
			JOptionPane.showMessageDialog(msgframe, "You have to select one!");
		}
		else{
				listModel.remove(filelist.getSelectedIndex());
				JOptionPane.showMessageDialog(msgframe, "Delete success!");
		}
	}
	
}
