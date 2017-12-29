package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.utt.app.util.Setup;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class MainHDFSOPDPanel extends JPanel implements  Observer{

	ObjectOPD oUserInfo;
	Dimension screen;
	JPanel LeftSection,RightSection,MiddleSection,MainSection;
	JPanel right1,mid2,mid1,right2,right21,right22;

	JTable table;
	Vector<String> columnNames;
	JScrollPane scrollPane,scrollPaneImg;
	JLabel Label_Info;
	JButton ButtonSearch;
	JSplitPane split;
	String fn="";
	Image img;
	Image background;
    Image scaled;
    float zoom = 1f;

    Dimension scaledSize;
    JViewport con;
    ZoomPane pane;
	/**
	 * 
	 */
	public MainHDFSOPDPanel(final ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), screen.height-175));
		
		 
		
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation(115);
		
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(200, screen.height-175));
		split.setLeftComponent(LeftSection);
		LeftSection.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("ประวัติการรักษา");
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		LeftSection.add(label, BorderLayout.NORTH);
		//System.out.println("**************"+oUserInfo.GetPtHN()+"------");
		ButtonSearch = new JButton("Show Files");
		ButtonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(oUserInfo.GetPtHN().equals("")){
					JOptionPane.showMessageDialog(null,"กรุณาเลือก HN ","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
					 
				}else{
					getData(oUserInfo.GetPtHN());
				}
				 
			}
		});
		ButtonSearch.setFont(new Font("Tahoma", Font.PLAIN, 13));
		 
		LeftSection.add(ButtonSearch, BorderLayout.SOUTH);
		
		//add(LeftSection, BorderLayout.WEST);
		
		columnNames = new Vector<String>();		
		columnNames.add("");
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
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
 
				 
				int row = table.rowAtPoint(e.getPoint());
		        int col = table.columnAtPoint(e.getPoint());
		        
		        String filename=table.getValueAt(row, 2).toString().trim();
		       // System.out.println(filename);
		        int l=filename.length();
		        String d=filename.substring(6, 8).trim();
		        String m=filename.substring(4, 6).trim();
		        String y=filename.substring(0, 4).trim();
		        String c=filename.substring(8, 12).trim();
		        String p=filename.substring(12,14).trim();
		        Label_Info.setText(d+" "+Setup.getMonthShortThaiName(m)+" "+y+"  รหัสห้องตรวจ:  "+c+"  หน้าที่ : "+p);
		       // oUserInfo.setFN(filename.trim());
				//System.out.println(filename);
				//pane.clearImg();
		        mid2.removeAll();
		        mid2.revalidate();
		        mid2.repaint();
				showFilePDF(filename.trim());
				
			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
 
		scrollPane = new JScrollPane(table);
		 
		LeftSection.add(scrollPane, BorderLayout.CENTER);
		
		MainSection = new JPanel();
		MainSection.setPreferredSize(new Dimension((screen.width-(screen.width*2)/10)-200, screen.height-175));
		MainSection.setLayout(new BorderLayout(0, 0));
		split.setRightComponent(MainSection);
		
		MiddleSection = new JPanel();
		MiddleSection.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-450), screen.height-175));
		 
		MainSection.add(MiddleSection, BorderLayout.CENTER);
		MiddleSection.setLayout(new BorderLayout(0, 0));
		
		mid2 = new JPanel();
		mid2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//mid2.setBackground(new Color(206,203,208));
		mid2.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), screen.height-215));
		MiddleSection.add(mid2, BorderLayout.CENTER);
		mid2.setLayout(new BorderLayout(0, 0));

		//scrollPaneImg = new JScrollPane(Label_IMG);
		 
		mid1 = new JPanel();
		mid1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u0E23\u0E32\u0E22\u0E25\u0E30\u0E40\u0E2D\u0E35\u0E22\u0E14\u0E02\u0E2D\u0E07\u0E44\u0E1F\u0E25\u0E4C", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mid1.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10-480), 40));
		MiddleSection.add(mid1, BorderLayout.NORTH);
		mid1.setLayout(null);
		
		Label_Info = new JLabel(" ");
		Label_Info.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Info.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Info.setBounds(20, 18, 300, 15);
		mid1.add(Label_Info);
		
		JButton ButtonPrint = new JButton("");
		ButtonPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//printOPD(fn);
			}
		});
		ButtonPrint.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/printer.png")));
		ButtonPrint.setBounds(screen.width-((screen.width*2)/10)-460, 12, 100, 25);
		//mid1.add(ButtonPrint);
				
		add(split, BorderLayout.CENTER);
	}
	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectOPD)oObservable); // cast
	}

	public void getData(String hn){
		table.setModel(fetchData(hn));
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(220);
		columnModel.getColumn(2).setPreferredWidth(0);
		columnModel.getColumn(2).setMinWidth(0);
		columnModel.getColumn(2).setMaxWidth(0);
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchData(String hn){
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		String folderhn=hn.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn;
		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "C:\\winutils");
		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			Path newFolderPath= new Path(path1);
			FileStatus[] fileStatus = fs.listStatus(newFolderPath);
			int num=1;
			for(FileStatus status : fileStatus){
				String pp=status.getPath().toString().trim();
				//System.out.println(pp);
    	        if(pp.length()==78) {
    	        	String ppp=pp.substring( pp.length()-31,pp.length()-23);
    	        	if(ppp.equals("25000101")) {	 
    	        	}
    	        	else{
    	        		 
    	        		String pppp=pp.substring( pp.length()-23,pp.length()-19);
    	        		String ss=pp.substring( pp.length()-19,pp.length()-17);
    	        		String ss1=pp.substring( pp.length()-17,pp.length()-15);
    	        		String ss2=pp.substring( pp.length()-15,pp.length()-13);
    	        		if(ppp.substring(0,2).equals("25") && (ss1.equals("00") || ss1.equals("01") || ss1.equals("02") || ss1.equals("03"))) {
    	        			//System.out.println(ppp+"- "+pppp+"-"+ss+"-"+ss1);
            	        	List<ArrayList<String>> namesAndNumbers = new ArrayList<ArrayList<String>>();
            				namesAndNumbers.add(new ArrayList<String>(Arrays.asList(ppp, pp)));
            				Collections.sort(namesAndNumbers, new Comparator<ArrayList<String>>() {    
            				        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            				            return o1.get(0).compareTo(o2.get(0));
            				        }               
            				});
            				//System.out.println(namesAndNumbers);
            				final Vector<String> vstring = new Vector<String>();
            				vstring.add(" "+num );
            				String pp1=pp.substring(pp.length()-31,pp.length()-6);
            				String year_fn=pp1.substring(0, 4);	             
            	            String month_fn=pp1.substring(4, 6);            
            	            String date_fn=pp1.substring(6, 8);
            	            String clinic_fn=pp1.substring(8, 12);
            	            String page_fn=pp1.substring(12, 14);
            				vstring.add(" "+date_fn+" "+Setup.getMonthShortThaiName(month_fn)+" "+year_fn+"   |"+clinic_fn);
            				vstring.add(pp.substring(pp.length()-31,pp.length()-6));	           				
            				 
            				data.add(vstring);
            				num++;
    	        		}	        		 
    	    	         
    	        	}    		
    	        	 
    	        }
    	        //
    	        else if(pp.length()==74) {
    	        	String ppp=pp.substring( pp.length()-27,pp.length()-19);
    	        	if(ppp.equals("25000101")) {
    	        		 
    	        	}
    	        	else{
    	        		 
    	        		String pppp=pp.substring( pp.length()-19,pp.length()-17);
    	        		if(ppp.substring(0,2).equals("25")) {
    	        			//System.out.println(ppp+"- "+pppp);
            	        	List<ArrayList<String>> namesAndNumbers = new ArrayList<ArrayList<String>>();
            				namesAndNumbers.add(new ArrayList<String>(Arrays.asList(ppp, pp)));
            				Collections.sort(namesAndNumbers, new Comparator<ArrayList<String>>() {    
            				        public int compare(ArrayList<String> o1, ArrayList<String> o2) {
            				            return o1.get(0).compareTo(o2.get(0));
            				        }               
            				});
            				//System.out.println(namesAndNumbers);
            				final Vector<String> vstring = new Vector<String>();
            				vstring.add(" "+num );
            				String pp1=pp.substring(pp.length()-27,pp.length()-6);
            				String year_fn=pp1.substring(0, 4);	             
            	            String month_fn=pp1.substring(4, 6);            
            	            String date_fn=pp1.substring(6, 8);
            	            String clinic_fn=pp1.substring(8, 12);
            	            String page_fn=pp1.substring(12, 14);
            				vstring.add(" "+date_fn+" "+Setup.getMonthShortThaiName(month_fn)+" "+year_fn+"   |"+clinic_fn);
            				vstring.add(pp.substring(pp.length()-27,pp.length()-6));	
            				
            				 
            				data.add(vstring);
            				num++;
    	        		}
    	    	         
    	        	}    		
    	        	 
    	        }
    	    }
				
			fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null,"ไม่พบ file ของ HN:"+hn,"File Error",JOptionPane.ERROR_MESSAGE);
		}
	
		return new DefaultTableModel(data, columnNames);
	}
	public  void showFilePDF(String filename){
		fn=filename;
		if(filename.length()==21 || filename.length()==25){
			pane = new ZoomPane(previewPDFDocumentInImage(filename));       
            scrollPaneImg = new JScrollPane(pane);
            pane.centerInViewport();
    		mid2.add(scrollPaneImg, BorderLayout.CENTER);
    					 
		}else{
			JOptionPane.showMessageDialog(null,"filename error:"+filename ,"Infomation",JOptionPane.WARNING_MESSAGE); 			
		}
	}

	public  Image previewPDFDocumentInImage(String filescanname){
		Image img=null;
		ByteBuffer buf = null;
		String hn=oUserInfo.GetPtHN().trim();
		String folderhn=hn.substring(0, 2).trim();
		String path1="/user/hadoop/"+folderhn+"/"+hn;
		 
		//String filename="255511290001014700005HN.pdf";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://172.17.71.2:9000");
		System.setProperty("HADOOP_USER_NAME", "hduser");
		System.setProperty("hadoop.home.dir", "C:\\winutils");
		FileSystem fs;
		PDFFile pdffile;
		try {
			fs = FileSystem.get(URI.create("hdfs://172.17.71.2:9000"), conf);
			Path newFolderPath= new Path(path1);
			 Path path = new Path(newFolderPath+"/"+filescanname.trim()+"HN.pdf");
			    if (!fs.exists(path)) {
			      System.out.println("File " + filescanname+"HN.pdf" + " does not exists");
			 
			}
			FSDataInputStream in = fs.open(path);
		    byte[] b= IOUtils.toByteArray(in);
		    buf = ByteBuffer.wrap(b);

			
			pdffile = new PDFFile(buf);
			PDFPage page = pdffile.getPage(1);
			//get the width and height for the doc at the default zoom
	    	Rectangle rect = new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());
	    	//img = page.getImage(rect.width, rect.height, //width &amp; height
	        img = page.getImage(800, 1200, //width &amp; height
	                rect, // clip rect
	                null, // null for the ImageObserver
	                true, // fill background with white
	                true) // block until drawing is done
	        ;
			in.close();
			fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			img=null;
		}
		return img;
    	 
	}
	public void clear(){
		table.setModel(fetchDataClear());
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(220);
		columnModel.getColumn(2).setPreferredWidth(0);
		columnModel.getColumn(2).setMinWidth(0);
		columnModel.getColumn(2).setMaxWidth(0);
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		 

		Label_Info.setText("");
		mid2.removeAll();
		mid2.revalidate();
	    mid2.repaint();
	}
	public  DefaultTableModel fetchDataClear(){
		 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		return new DefaultTableModel(data, columnNames);
	}

}
