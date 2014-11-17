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

import Operator.DuplicatedFile;
import Operator.DuplicatedFileOperator;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class Frame1 {

	private JFrame frame;
	DefaultListModel<String> listModel;
	JFrame msgframe;
	JList filelist;
	JButton btnUpload;
	JButton btnDownload;
	JButton btnDelete;
	private JLabel label;
	private JLabel label_1;

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
	@SuppressWarnings("unchecked")
	private void initialize() {

		DuplicatedFileOperator.init();

		frame = new JFrame();
		frame.setBounds(100, 100, 473, 304);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		msgframe = new JFrame();
		listModel = new DefaultListModel<String>();
		listModel.addElement("test1.txt");
		listModel.addElement("test2.txt");
		listModel.addElement("test3.txt");
		listModel.addElement("test4.txt");
		listModel.addElement("test5.txt");
		listModel.addElement("test6.txt");
		
		DuplicatedFile filelist_temp[] = DuplicatedFileOperator.getInstance()
				.getAllFile();
		if (filelist_temp != null) {
			for (int i = 0; i < filelist_temp.length; i++) {
				listModel.addElement(filelist_temp[i].getFileName());
			}
		}
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
						
								// upload button and its event handler
								btnUpload = new JButton("Upload");
								frame.getContentPane().add(btnUpload);
								// when click, a file choose window pop up and can select file from
								// system to upload
								// the file name will add to the list to show on the list panel
								btnUpload.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										uploadfile();
										buttonavaliable();
									}
								});
						
								// download button and its event handler
								btnDownload = new JButton("Download");
								frame.getContentPane().add(btnDownload);
								
								// when click, it should download the file. currently, it does nothing
								// but pops a message telling it works
								btnDownload.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										downloadfile();
									}
								});
						
						label_1 = new JLabel("");
						frame.getContentPane().add(label_1);
								
								label = new JLabel("");
								frame.getContentPane().add(label);
								
										// delete button and its event handler
										btnDelete = new JButton("Delete");
										frame.getContentPane().add(btnDelete);
										//when click, it should delete the file. currently it removes the filename from the list
										//and pops a message telling it works
										btnDelete.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												deletefile();
												buttonavaliable();
											}
										});
						
								// file list
								filelist = new JList(listModel);
								filelist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
								frame.getContentPane().add(filelist);
		buttonavaliable();
	}

	public void uploadfile() {
		// create a file chooser
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt",
				"txt");
		fc.setFileFilter(filter);
		fc.addChoosableFileFilter(filter);

		int response = fc.showSaveDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String filepath = file.getAbsolutePath();
			String filename = file.getName();
			//System.out.println(filepath);
			if (DuplicatedFileOperator.getInstance().insertFile(filepath,filename)) {
				DuplicatedFile filelist_temp[] = DuplicatedFileOperator.getInstance()
						.getAllFile();
				for (int i = 0; i < filelist_temp.length; i++) {
					listModel.addElement(filelist_temp[i].getFileName());
				}
			}
			else{
				JOptionPane.showMessageDialog(msgframe, "Insert Fail!");
			}
			
		}
	}

	public void downloadfile() {
		if (filelist.getSelectedIndex() == -1 && listModel.getSize() != 0) {
			// show a message telling that have to select one
			JOptionPane.showMessageDialog(msgframe, "You have to select one!");
		} 
		else if (filelist.getSelectedIndex() != -1){
			String df = DuplicatedFileOperator.getInstance().loadFile((String)filelist.getSelectedValue());
			if(df != null){
				System.out.println(df);//test if it works. should we put this into a new window?
				JOptionPane.showMessageDialog(msgframe, "Download success!\n"+"You download: "+filelist.getSelectedValue());
			}
		}
		
	}

	public void deletefile() {
		if (filelist.getSelectedIndex() == -1 && listModel.getSize() != 0) {
			// show a message telling that have to select one
			JOptionPane.showMessageDialog(msgframe, "You have to select one!");
		} 
		else if (filelist.getSelectedIndex() != -1){
			String deletefilename = (String)filelist.getSelectedValue();
			if( DuplicatedFileOperator.getInstance().deleteFile(deletefilename)){
				listModel.remove(filelist.getSelectedIndex());
				JOptionPane.showMessageDialog(msgframe, "Delete success!\n"+"You delete: "+deletefilename);
			}
		}
	}
	//check if the download and delete should be avaliable for the current state
	public void buttonavaliable(){
		if (listModel.getSize() == 0) {
			btnDownload.setEnabled(false);
			btnDelete.setEnabled(false);
		} 
		else {
			btnDownload.setEnabled(true);
			btnDelete.setEnabled(true);
		}
	}
}