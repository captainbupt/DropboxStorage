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

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.filechooser.FileNameExtensionFilter;

import Operator.DuplicatedFile;
import Operator.DuplicatedFileOperator;

public class Frame2 {

	private JFrame frmDropboxStorage;
	DefaultListModel<String> listModel;
	JFrame msgframe;
	JList filelist;
	JButton btnUpload;
	JButton btnDownload;
	JButton btnDelete;
	String[] columNames = { "File_Name", "Date_Modified", "Size" };
	Object[][] data = { { "text1.txt", "123", "123" }, { "123", "123", "123" },
			{ "123", "123", "123" }, };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame2 window = new Frame2();
					window.frmDropboxStorage
							.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.frmDropboxStorage.setVisible(true);
					window.frmDropboxStorage.pack();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		DuplicatedFileOperator.init();

		frmDropboxStorage = new JFrame();
		frmDropboxStorage.setTitle("Dropbox Storage");
		frmDropboxStorage.setBounds(100, 100, 932, 604);
		frmDropboxStorage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDropboxStorage.getContentPane().setLayout(new BorderLayout(0, 0));
		// /////////////////////////////////////////////////////////////////////////////////////
		msgframe = new JFrame();
		listModel = new DefaultListModel<String>();

		ArrayList<DuplicatedFile> filelist_temp = DuplicatedFileOperator
				.getInstance().getAllFile();
		if (filelist_temp != null) {
			for (DuplicatedFile tmp : filelist_temp) {
				listModel.addElement(tmp.getFileName());
			}
		}
		// //////////////////////////////////////////////////////////////////////////////////////
		JPanel ListPanel = new JPanel();
		frmDropboxStorage.getContentPane().add(ListPanel);
		ListPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		ListPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);

		filelist = new JList(listModel);
		filelist.setFont(new Font("Tahoma", Font.PLAIN, 18));
		scrollPane.setViewportView(filelist);
		filelist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.NORTH);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.SOUTH);

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.WEST);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.EAST);

		JPanel ButtonPanel = new JPanel();
		frmDropboxStorage.getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
		ButtonPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		ButtonPanel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new GridLayout(0, 3, 10, 0));
		btnUpload = new JButton("Upload");
		btnUpload.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_6.add(btnUpload);
		btnUpload.setIcon(new ImageIcon("Imagesource\\upload.png"));
		// when click, a file choose window pop up and can select file from
		// system to upload
		// the file name will add to the list to show on the list panel
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uploadfile();
				buttonavaliable();
			}
		});

		btnDownload = new JButton("Download");
		btnDownload.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_6.add(btnDownload);
		btnDownload.setIcon(new ImageIcon("Imagesour\\download.png"));
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				downloadfile();
			}
		});

		btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_6.add(btnDelete);
		btnDelete.setIcon(new ImageIcon("Imagesource\\delete.png"));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deletefile();
				buttonavaliable();
			}
		});
		// /////////////////////////////////////////////////////////////////////////////
		JPanel panel_7 = new JPanel();
		ButtonPanel.add(panel_7, BorderLayout.WEST);

		JPanel panel_8 = new JPanel();
		ButtonPanel.add(panel_8, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		ButtonPanel.add(panel_9, BorderLayout.SOUTH);
		// /////////////////////////////////////////////////////////////////////////////
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		frmDropboxStorage.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		menuBar.add(mnFile);

		JMenuItem mntmOpen_1 = new JMenuItem("Open");
		mntmOpen_1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnFile.add(mntmOpen_1);

		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnFile.add(mntmRefresh);

		JMenuItem mntmOpen = new JMenuItem("Exit");
		mntmOpen.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnFile.add(mntmOpen);

		JMenu mnEdit = new JMenu("View");
		mnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		menuBar.add(mnEdit);

		JMenu mnArrangeItems = new JMenu("Arrange Items");
		mnArrangeItems.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnEdit.add(mnArrangeItems);

		JRadioButtonMenuItem rdbtnmntmBySize = new JRadioButtonMenuItem(
				"By Size");
		rdbtnmntmBySize.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnArrangeItems.add(rdbtnmntmBySize);

		JRadioButtonMenuItem rdbtnmntmByName = new JRadioButtonMenuItem(
				"By Name");
		rdbtnmntmByName.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnArrangeItems.add(rdbtnmntmByName);

		JRadioButtonMenuItem rdbtnmntmByDate = new JRadioButtonMenuItem(
				"By Date");
		rdbtnmntmByDate.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		mnArrangeItems.add(rdbtnmntmByDate);

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
			// System.out.println(filepath);
			if (DuplicatedFileOperator.getInstance().insertFile(filepath,
					filename)) {
				/*
				 * DuplicatedFile filelist_temp[] =
				 * DuplicatedFileOperator.getInstance() .getAllFile(); for (int
				 * i = 0; i < filelist_temp.length; i++) {
				 * listModel.addElement(filelist_temp[i].getFileName()); }
				 */
				listModel.addElement(filename);
			} else {
				JOptionPane.showMessageDialog(msgframe, "Insert Fail!");
			}

		}
	}

	public void downloadfile() {
		if (filelist.getSelectedIndex() == -1 && listModel.getSize() != 0) {
			// show a message telling that have to select one
			JOptionPane.showMessageDialog(msgframe, "You have to select one!");
		} else if (filelist.getSelectedIndex() != -1) {
			String df = DuplicatedFileOperator.getInstance().loadFile(
					(String) filelist.getSelectedValue());
			if (df != null) {
				System.out.println(df);// test if it works. should we put this
										// into a new window?
				JOptionPane.showMessageDialog(msgframe, "Download success!\n"
						+ "You download: " + filelist.getSelectedValue());
			}
		}

	}

	public void deletefile() {
		if (filelist.getSelectedIndex() == -1 && listModel.getSize() != 0) {
			// show a message telling that have to select one
			JOptionPane.showMessageDialog(msgframe, "You have to select one!");
		} else if (filelist.getSelectedIndex() != -1) {
			String deletefilename = (String) filelist.getSelectedValue();
			if (DuplicatedFileOperator.getInstance().deleteFile(deletefilename)) {
				listModel.remove(filelist.getSelectedIndex());
				JOptionPane.showMessageDialog(msgframe, "Delete success!\n"
						+ "You delete: " + deletefilename);
			}
		}
	}

	// check if the download and delete should be avaliable for the current
	// state
	public void buttonavaliable() {
		if (listModel.getSize() == 0) {
			btnDownload.setEnabled(false);
			btnDelete.setEnabled(false);
		} else {
			btnDownload.setEnabled(true);
			btnDelete.setEnabled(true);
		}
	}
}