package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.utt.app.opd.ObjectOPD;

import org.utt.app.util.Setup;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.Color;

import javax.swing.ImageIcon;

import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class ScanScope extends JPanel implements Observer{
	ObjectOPD oUserInfo;
	Dimension screen;
	JPanel LeftSection,MiddleSection;
	JTable table;
	Vector<String> columnNames;
	JScrollPane scrollPane;
	JPanel left1,left2,mid1,mid2;
	JButton ButtonRefresh,ButtonScan,ButtonView;

	
	JLabel Label_Info,Label_IMG;
	String page="",hn="",clinic="",visitdate="",clinic_name="",clinic_suffix="";
	ImageIcon icon;
	String namebf="",nameaf="",hnaf="";
	
	String folder="C:\\utthscope";

	/**
	 * Create the panel.
	 */
	public ScanScope(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-160));
		
		checkFolder(folder);
		checkFolder(folder+"\\water");
		
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(150, screen.height-160));
		add(LeftSection, BorderLayout.WEST);
		LeftSection.setLayout(new BorderLayout(0, 0));
		add(LeftSection, BorderLayout.WEST);
		
		columnNames = new Vector<String>();		
		columnNames.add("");
		columnNames.add("");
		
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		DefaultTableModel model = new DefaultTableModel(data, columnNames){
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
		};
		table = new JTable(model){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
				Component c = super.prepareRenderer(renderer, row, column);			
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
				}
				return c;
			}
		};
		
		left1 = new JPanel();
		left1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		left1.setPreferredSize(new Dimension(150, 300));
		LeftSection.add(left1, BorderLayout.NORTH);
		left1.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("รายการไฟล์");
		left1.add(label, BorderLayout.NORTH);
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
	 	
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String pp="";
				visitdate=oUserInfo.GetPtVisitdate();
				hnaf=oUserInfo.GetPtHN();
				Label_Info.setText("");
				int row_table = table.rowAtPoint(e.getPoint());
		        int col_table = table.columnAtPoint(e.getPoint());
		        //clinic=table.getValueAt(row_table,1).toString().trim();
		        String select=table.getValueAt(row_table,0).toString().trim();
		        //clinic_name=select.substring(select.indexOf(".")+1,select.indexOf("[")).trim();
		         
		        
		        if(hnaf.length() ==7) {
		        	getPageScan(hnaf);
		        	int p_in;
		        	if(getPageScan(hnaf)<50) {
		        		p_in=49;
		        	}else {
		        		p_in=getPageScan(hnaf);
		        	}
			        pp=Integer.toString(p_in+1);
			        if(pp.length()==1) {
			        	pp="0"+pp;
			        }
		        }else {
					JOptionPane.showMessageDialog(null,"ไม่มี  HN ","HN Error",JOptionPane.ERROR_MESSAGE);

		        }
		         		        
		        String filen=Setup.DateInDBMSSQLRef43no543(visitdate)+"1802"+pp+"0001"+hnaf;
		        Label_Info.setText(filen);
		        if(filen.length()==25) {
		        	Label_Info.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/tick.png")));
		        	ButtonView.setEnabled(true);
		        }else {
		           	Label_Info.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/cancel.png")));

		        }
		        //System.out.println(Setup.DateInDBMSSQLRef43no543(visitdate)+"1802"+"("+pp+")"+"0001"+hnaf);
		        
		        try {
		        	 
					viewScan("c:\\utthscope\\"+select);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		        namebf=select;
		        nameaf=filen;
		        
		        
			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
		
		scrollPane = new JScrollPane(table);
		left1.add(scrollPane, BorderLayout.CENTER);
		
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getData();
			}
		});
		left1.add(ButtonRefresh, BorderLayout.SOUTH);
		
		left2 = new JPanel();
		left2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u0E2B\u0E19\u0E49\u0E32\u0E17\u0E35\u0E48", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		//LeftSection.add(left2, BorderLayout.CENTER);
		left2.setLayout(null);
		
		 
		
		MiddleSection = new JPanel();
		add(MiddleSection, BorderLayout.CENTER);
		MiddleSection.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10)-300, screen.height-160));
		MiddleSection.setLayout(new BorderLayout(0, 0));
		
		mid1 = new JPanel();
		mid1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mid1.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10)-300, 30));
		MiddleSection.add(mid1, BorderLayout.NORTH);
		mid1.setLayout(null);
		
		Label_Info = new JLabel(" ");
		Label_Info.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Info.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Info.setBounds(15,5,252,20);
		mid1.add(Label_Info);
		
		ButtonScan = new JButton("Send");
		ButtonScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendFileHDFS(nameaf);
				 
				clearDataPt();
				 
				getData();
				
			}
		});
		ButtonScan.setBounds(screen.width-((screen.width*2)/10)-400, 4, 89, 23);
		ButtonScan.setEnabled(false);
		mid1.add(ButtonScan);
		
		ButtonView = new JButton("Save As");
		ButtonView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile(nameaf,namebf);
				ButtonScan.setEnabled(true);
			}
		});
		ButtonView.setBounds(screen.width-((screen.width*2)/10)-510, 4, 89, 23);
		ButtonView.setEnabled(false);
		mid1.add(ButtonView);
		

		
		mid2 = new JPanel();
		mid2.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(176, 224, 230), null, new Color(250, 235, 215), null));
		mid2.setPreferredSize(new Dimension(600, 600));
		MiddleSection.add(mid2, BorderLayout.CENTER);
		mid2.setLayout(null);
		
		Label_IMG = new JLabel("");
		Label_IMG.setHorizontalAlignment(SwingConstants.CENTER);
		Label_IMG.setBounds(30,1,560,600);
		mid2.add(Label_IMG);
	}
	public void update(Observable oObservable, Object oObject) {
		
		oUserInfo = ((ObjectOPD)oObservable); // cast
	}
	public void getData(){
		table.setModel(fetchData());
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(190);
		columnModel.getColumn(1).setPreferredWidth(0);
		columnModel.getColumn(1).setMinWidth(0);
		columnModel.getColumn(1).setMaxWidth(0);

		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchData(){
		String file_in="C:\\utthscope";
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		File folder = new File(file_in);
        File[] listOfFiles = folder.listFiles();
        String file_name_input[]=new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
        	if (listOfFiles[i].isFile()) {       		
        		file_name_input[i]=listOfFiles[i].getName();
        		if(file_name_input[i].substring(file_name_input[i].length()-3).equals("pdf") || file_name_input[i].substring(file_name_input[i].length()-3).equals("PDF")){
        			//System.out.println(file_name_input[i]);
        			final Vector<String> vstring = new Vector<String>();
        			vstring.add(" "+file_name_input[i].trim() );
        			vstring.add(" ");
        			data.add(vstring);
        		}

        	}
        }
        
		return new DefaultTableModel(data, columnNames);
	}
	public void viewScan(String filename) throws IOException{
		icon = new ImageIcon(getImg(filename) );		
		Label_IMG.setIcon(icon);

	}
	public Image getImg(String filename)  {
		Image img=null;
		PDFFile pdffile;
	    
		File f=new File(filename);
		try {
			InputStream in = new FileInputStream(f);
			byte[] b= IOUtils.toByteArray(in);
			ByteBuffer buf = ByteBuffer.wrap(b);
			
			pdffile = new PDFFile(buf);
			PDFPage page = pdffile.getPage(1);
			//get the width and height for the doc at the default zoom
	    	Rectangle rect = new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());
	    	//img = page.getImage(rect.width, rect.height, //width &amp; height
	        img = page.getImage(560, 600, //width &amp; height
	                rect, // clip rect
	                null, // null for the ImageObserver
	                true, // fill background with white
	                true) // block until drawing is done
	        ;
	        buf.clear();
	        in.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	     
		
		return img;
	}

	public void saveFile(String input,String bf){
		if(input.length()==25) {
			addWatermask(input,bf);	 
		}else {
			JOptionPane.showMessageDialog(null,"ความยาวชื่อไฟล์ไม่ถูกต้อง ","File Name Error",JOptionPane.ERROR_MESSAGE);

		}

	}

	public void addWatermask(String fn,String bf){

        PdfReader reader;
		try {
			reader = new PdfReader(folder+"\\"+bf);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(folder+"\\water\\"+fn+"HN.pdf"));

	        // text watermark
	        Phrase p = new Phrase(fn,FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD,new GrayColor(0.5f)));

	        // properties
	        PdfContentByte over;
	        com.lowagie.text.Rectangle pagesize;
	        float x, y;

	        // loop over every page
	        int n = reader.getNumberOfPages();
	        for (int i = 1; i <= n; i++) {

	            // get page size and position
	            pagesize = reader.getPageSizeWithRotation(i);
	            x = (pagesize.getLeft() + pagesize.getRight()) / 4;
	            y = (pagesize.getTop() + pagesize.getBottom()) / 10;
	            over = stamper.getOverContent(i);
	            over.saveState();

	            // set transparency
	            PdfGState state = new PdfGState();
	            state.setFillOpacity(0.2f);
	            over.setGState(state);
	            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 0);
	             

	            over.restoreState();
	        }
	        stamper.close();
	        reader.close();
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
         
	}
 
	 public void sendFileHDFS(String fn){
		 
			String path="/user/hadoop";

			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
			System.setProperty("HADOOP_USER_NAME", "hduser");
			System.setProperty("hadoop.home.dir", "/");
			
			FileSystem fs;
			try {
				fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
				//System.out.println(fs.toString());
				Path homeDir=fs.getHomeDirectory();
				//System.out.println("Home folder -" +homeDir);
				Path workingDir=fs.getWorkingDirectory();
				String hn=fn.trim().substring(fn.length()-7);
				String folderhn=hn.substring(0, 2).trim();
			      Path newFolderPath= new Path(path+"/"+folderhn+"/"+hn);
			      if(!fs.exists(newFolderPath)) {
			         // Create new Directory
			         fs.mkdirs(newFolderPath);
			         //System.out.println("Path "+path+" created.");
			      }
				Path localFilePath = new Path("c://utthscope//water//"+fn+"HN.pdf");
				Path hdfsFilePath=new Path(newFolderPath+"/"+fn+"HN.pdf");

				fs.moveFromLocalFile(localFilePath, hdfsFilePath);
				//System.out.println("sent "+fn);

				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ClearFile(nameaf);
		}
	public void clearDataPt(){
		getDataClear();		
		Label_Info.setText("");	 
		Label_IMG.setIcon(null);
		Label_Info.setIcon(null);
		mid2.revalidate();
		mid2.repaint();
		ClearFile(namebf);
		ButtonView.setEnabled(false);
		ButtonScan.setEnabled(false);
		 
		 
		nameaf="";
		namebf="";
		 
	}
	public void getDataClear(){
		table.setModel(fetchDataClear());
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(190);
		columnModel.getColumn(1).setPreferredWidth(0);
		columnModel.getColumn(1).setMinWidth(0);
		columnModel.getColumn(1).setMaxWidth(0);

		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchDataClear(){		 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		return new DefaultTableModel(data, columnNames);
	}
	public void ClearFile(String file_del){		
		File folder = new File("C:\\utthscope\\");
		File[] listOfFiles = folder.listFiles();
        //System.out.println(listOfFiles.length);
        String file_name_input[]=new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
        	if (listOfFiles[i].isFile()) {  
        		 
        		if(listOfFiles[i].getName().equals(file_del)){
        			if(listOfFiles[i].delete()) {
        				//System.out.println("file del sccess");
        			}
        			else {
        				//System.out.println("file not del ");
        			}
        			 
        		}
        		 
        	}
        	
        }
	}
	public int getPageScan(String hn_s){
		int p=0;
		String path="/user/hadoop";

		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "/");
		
		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			//System.out.println(fs.toString());
			Path homeDir=fs.getHomeDirectory();
			//System.out.println("Home folder -" +homeDir);
			Path workingDir=fs.getWorkingDirectory();
			String hn=hn_s.trim();
			String folderhn=hn.substring(0, 2).trim();
		      Path newFolderPath= new Path(path+"/"+folderhn+"/"+hn);
		      if(!fs.exists(newFolderPath)) {	         
		         System.out.println("Path "+path+" not found");
		      }else {
		    	  
		    	  ArrayList<Integer> arrayList = new ArrayList<Integer>();
		    	  FileStatus[] fileStatus = fs.listStatus(newFolderPath);
		    	    for(FileStatus status : fileStatus){
		    	        //System.out.println(status.getPath().toString().trim()+"-"+status.getPath().toString().trim().length()+"-"+status.getPath().toString().trim().substring(status.getPath().toString().trim().indexOf("25000101555"), status.getPath().toString().trim().indexOf("25000101555")+11)+"-"+status.getPath().toString().trim().indexOf("25000101555")+"-"+(status.getPath().toString().trim().indexOf("25000101555")+11));
		    	        String pp=status.getPath().toString().trim();
		    	        if(pp.length()==78) {
		    	        	String ppp=pp.substring( pp.length()-31,pp.length()-23);
		    	        	if(ppp.equals("25000101")) {
		    	        		 
		    	        	}else {
		    	        		String pppp=pp.substring( pp.length()-23,pp.length()-19);
		    	        		//System.out.println(pppp+"----555");
		    	        		if(pppp.equals("1802")) {
		    	        			String ss=pp.substring( pp.length()-19,pp.length()-17);
			    	        		//System.out.println(ss);
			    	        		arrayList.add( Integer.parseInt(ss));
			    	        		p = Collections.max(arrayList);
		    	        		} 
		    	        		 
		    	        	}
 	        	 
		    	        }
		    	        
		    	    }
		    	    
		    	     
		    	  
		    	     
		      }
			
			fs.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
		
	}
	public void checkFolder(String folder){
		File file = new File(folder);
		if (!file.exists()) {
			if (file.mkdir()) {
				
			}else {
				
			}
		}
	}

 
	 
}
