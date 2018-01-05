package org.utt.app.opd;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.utt.app.InApp;
import org.utt.app.opd.ObjectOPD;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.Setup;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;


import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.media.jai.PlanarImage;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class ScanOPDCardUser extends JPanel implements Observer{
	ObjectOPD oUserInfo;
	Dimension screen;
	JPanel LeftSection,MiddleSection;
	JTable table;
	Vector<String> columnNames;
	JScrollPane scrollPane;
	JPanel left1,left2,mid1,mid2;
	JButton ButtonRefresh,ButtonScan,ButtonView;
	ButtonGroup group = new ButtonGroup();
	JRadioButton pageButton1,pageButton2,pageButton3,pageButton4,pageButton5,pageButton6,pageButton7,pageButton8,pageButton9;
	JTextField TextField_page;
	
	JLabel Label_Info1,Label_Info,Label_IMG;
	String page="",hn="",clinic="",visitdate="",clinic_name="",clinic_suffix="";
	ImageIcon icon;
	String nameBefore="",nameAfter="",filename="";
	int status_insert=0;

	/**
	 * Create the panel.
	 */
	public ScanOPDCardUser(ObjectOPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-160));
		
		LeftSection = new JPanel();
		LeftSection.setPreferredSize(new Dimension(200, screen.height-160));
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
		left1.setPreferredSize(new Dimension(200, 160));
		LeftSection.add(left1, BorderLayout.NORTH);
		left1.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("ห้องตรวจ");
		left1.add(label, BorderLayout.NORTH);
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
	 	
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Label_Info.setText("");
				int row_table = table.rowAtPoint(e.getPoint());
		        int col_table = table.columnAtPoint(e.getPoint());
		        clinic=table.getValueAt(row_table,1).toString().trim();
		        String select=table.getValueAt(row_table,0).toString().trim();
		        clinic_name=select.substring(select.indexOf(".")+1,select.indexOf("[")).trim();
		        Label_Info.setText(" "+clinic_name+" ("+clinic+")");
		        
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
				getData(oUserInfo.GetPtVN());
			}
		});
		left1.add(ButtonRefresh, BorderLayout.SOUTH);
		
		left2 = new JPanel();
		left2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u0E2B\u0E19\u0E49\u0E32\u0E17\u0E35\u0E48", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		LeftSection.add(left2, BorderLayout.CENTER);
		left2.setLayout(null);
		
		pageButton1 = new JRadioButton("หน้าที่ 1");
		pageButton1.setSelected(true);
		pageButton1.setBackground(Setup.getColor());
		pageButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="01";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 1");
			}
		});
		group.add(pageButton1);
		pageButton1.setBounds(25, 20, 109, 23);
		left2.add(pageButton1);
		
		pageButton2 = new JRadioButton("หน้าที่ 2");
		pageButton2.setBackground(Setup.getColor());
		pageButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="02";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 2");
			}
		});
		group.add(pageButton2);
		pageButton2.setBounds(25, 45, 109, 23);
		left2.add(pageButton2);
		
		pageButton3 = new JRadioButton("หน้าที่ 3 ");
		pageButton3.setBackground(Setup.getColor());
		pageButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="03";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 3");
			}
		});
		group.add(pageButton3);
		pageButton3.setBounds(25, 70, 109, 23);
		left2.add(pageButton3);
		
		pageButton4 = new JRadioButton("หน้าที่ 4");
		pageButton4.setBackground(Setup.getColor());
		pageButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="04";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 4");
			}
		});
		group.add(pageButton4);
		pageButton4.setBounds(25, 95, 109, 23);
		left2.add(pageButton4);
		
		pageButton5 = new JRadioButton("หน้าที่ 5");
		pageButton5.setBackground(Setup.getColor());
		pageButton5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="05";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 5");
			}
		});
		group.add(pageButton5);
		pageButton5.setBounds(25, 120, 109, 23);
		left2.add(pageButton5);
		
		pageButton6 = new JRadioButton("หน้าที่ 6 ");
		pageButton6.setBackground(Setup.getColor());
		pageButton6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="06";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 6");
			}
		});
		group.add(pageButton6);
		pageButton6.setBounds(25, 145, 109, 23);
		left2.add(pageButton6);
		
		pageButton7 = new JRadioButton("หน้าที่ 7 ");
		pageButton7.setBackground(Setup.getColor());
		pageButton7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="07";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 7");
			}
		});
		group.add(pageButton7);
		pageButton7.setBounds(25, 170, 109, 23);
		left2.add(pageButton7);
		
		pageButton8 = new JRadioButton("หน้าที่ 8 ");
		pageButton8.setBackground(Setup.getColor());
		pageButton8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page="08";
				TextField_page.setText("");
				Label_Info1.setText("  หน้าที่ 8");
			}
		});
		group.add(pageButton8);
		pageButton8.setBounds(25, 195, 109, 23);
		left2.add(pageButton8);
		
		pageButton9 = new JRadioButton("หน้าที่");
		pageButton9.setBackground(Setup.getColor());
		pageButton9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				page=TextField_page.getText().trim();
				if(page.length()==1){
					page="0"+page;
				}
				if(page.length()>2){
					  JOptionPane.showMessageDialog(null,"หน้าที่ไม่ถูกต้อง (มากกว่า 99)","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 						
					  TextField_page.setText("");				  
				}else{
					Label_Info1.setText("  หน้าที่  "+page); 
				}
			}
		});
		group.add(pageButton9);
		pageButton9.setBounds(25, 220, 59, 23);
		left2.add(pageButton9);
		
		TextField_page = new JTextField();
		TextField_page.setBounds(90, 225, 86, 20);
		left2.add(TextField_page);
		TextField_page.setColumns(10);
		
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
		
		ButtonScan = new JButton("Scan");
		ButtonScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scanOPD();
				 
			}
		});
		ButtonScan.setBounds(screen.width-((screen.width*2)/10)-400, 4, 89, 23);
		mid1.add(ButtonScan);
		
		ButtonView = new JButton("View");
		ButtonView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewScan();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				 
			}
		});
		ButtonView.setBounds(screen.width-((screen.width*2)/10)-510, 4, 89, 23);
		mid1.add(ButtonView);
		
		Label_Info1 = new JLabel(" ");
		Label_Info1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Info1.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Info1.setBounds(277, 5, 269, 20);
		mid1.add(Label_Info1);
		
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
	public void getData(String vn){
		table.setModel(fetchData(vn));
		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.getColumn(0).setPreferredWidth(190);
		columnModel.getColumn(1).setPreferredWidth(0);
		columnModel.getColumn(1).setMinWidth(0);
		columnModel.getColumn(1).setMaxWidth(0);

		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		
	}
	public  DefaultTableModel fetchData(String vn){
		 
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Connection conn,conn6 ;
		PreparedStatement stmt,stmt6 ;
	 	String sql_clinic = "select clinic  from  in_view_pt_opd where VN=? and visitdate='"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+"' and reftype='01' and suffix='0'";
		
		try {		 
			conn = new DBmanager().getConnMSSql();
	        stmt = conn.prepareStatement(sql_clinic);
	        stmt.setString(1,vn);
	        ResultSet rs = stmt.executeQuery();
	        int p=1;
	        while (rs.next()) {

	        	String sql_clinic_name = "select clinic_code,clinic_name  from clinic_name where app_code=?";
	        	conn6 = new DBmanager().getConnMySql();
		        stmt6 = conn6.prepareStatement(sql_clinic_name);
		        stmt6.setString(1,rs.getString(1).trim());
		        ResultSet rs6 = stmt6.executeQuery();
		        while( rs6.next() ){
		        	String clinicname="",cliniccode="";
		        	if(rs6.getString(1) ==null){
						cliniccode="";
					}else{
						cliniccode=rs6.getString(1).trim();
					}
		        	if(rs6.getString(2) ==null){
						clinicname="";
					}else{
						clinicname=rs6.getString(2).trim();
					}
		        	final Vector<String> vstring = new Vector<String>();
		        	 
		        	vstring.add(Integer.toString(p)+". "+clinicname+" ["+cliniccode+"]");
		        	vstring.add(cliniccode);
		        	data.add(vstring);
		        }
	        	 
	        	p++;
	        }
	        stmt.close();
	    	conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DefaultTableModel(data, columnNames);
	}
	public void viewScan() throws IOException{
		getFileInput();
		if(nameBefore.equals("")){
			JOptionPane.showMessageDialog(this,"กรุณาตรวจสอบ Folder C:\\utthscan","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
		}else{
			FileInputStream in = new FileInputStream(nameBefore);
		    FileChannel channel = in.getChannel();
		    ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    Image image = load(buffer.array());			 
			Image imageScaled = image.getScaledInstance(560, 600,  Image.SCALE_SMOOTH);
			icon = new ImageIcon(imageScaled);
			
			Label_IMG.setIcon(icon);
			//close load
			channel.close();
			in.close();
		}
	}
	public void getFileInput(){
		String folder="C:\\utthscan\\";
		File f = new File(folder);
		File[] listOfFiles = f.listFiles();
		String file_name[]=new String[listOfFiles.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {     		
        		file_name[i]=listOfFiles[i].getName();
        	}
		}
		if(file_name.length>1){
			JOptionPane.showMessageDialog(this,"มีจำนวนไฟล์ไม่ถูกต้อง ","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
		}else{
			for (int i = 0; i < file_name.length; i++) {			
				if(file_name[i] !=null){
					//System.out.println("file from scan show.."+file_name[i]);
					nameBefore=folder+file_name[i];
					break;
				} 
				else{			
					JOptionPane.showMessageDialog(this,"ไม่มีไฟล์นี้แล้ว กรุณา Scanไฟล์แล้วทำงานต่อไป ","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
			 		  break;
				}
			}
		}
	}
	public Image load(byte[] data) throws IOException{
		Image image = null;
	    SeekableStream stream = new ByteArraySeekableStream(data);
	    String[] names = ImageCodec.getDecoderNames(stream);
	    ImageDecoder dec = 
	      ImageCodec.createImageDecoder(names[0], stream, null);
	    RenderedImage im = dec.decodeAsRenderedImage();
	    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	    return image;
	}
	public void scanOPD(){

		visitdate=oUserInfo.GetPtVisitdate();
		hn=oUserInfo.GetPtHN();
		if(nameBefore.equals("")){
			JOptionPane.showMessageDialog(this,"กรุณาตรวจสอบรูปภาพก่อนบันทึกด้วยการกดปุ่ม View","ข้อผิดพลาด",JOptionPane.WARNING_MESSAGE); 
			
		}
		else{
			filename=Setup.DateInDBMSSQLRef43no543(visitdate)+clinic+page+hn;
			if(filename.length()==21 || filename.length()==25){
				nameAfter=filename+".tif";
				
				saveFile(nameBefore,nameAfter);
				/*
				Connection con6;
				PreparedStatement stmt6,stmt61,stmt62;
				ResultSet rs6 ;
				String sql_getFilename="select hn from fhome where fn=? ";
				try {

					con6 = new DBmanager().getConnMySql(InApp.UrlCust6);
					stmt6 = con6.prepareStatement(sql_getFilename);
					stmt6.setString(1, filename.trim()); 
					rs6 = stmt6.executeQuery();
					while( rs6.next() ){
						if(rs6.getString(1) !=null){
							status_insert=1;
							JOptionPane.showMessageDialog(null,"ระบบทำการแก้ไขไฟล์ชื่อ :"+filename ,"Infomation",JOptionPane.WARNING_MESSAGE); 
							saveFile(nameBefore,nameAfter);							 
							String sql_updatefilestatus = "update fhome set  status='2',scanuser='"+oUserInfo.GetIpCom()+"' where fn='"+filename.trim()+"' ";
							stmt61 = con6.prepareStatement(sql_updatefilestatus);
							int rs_update = stmt61.executeUpdate();
							stmt61.close();
						}
					}
					if(status_insert==0){
						saveFile(nameBefore,nameAfter);
						//insert to database
						String sql_addfile = "insert into fhome (fn, visitdate, hn, clinic_code, page, folder, scanuser, datemodify,status) values ('"+filename+"','"+Setup.DateInDBMSSQL(visitdate)+"','"+hn+"','"+clinic+"','"+page+"','"+hn+"','"+oUserInfo.GetIpCom()+"','"+Setup.GetDateNow()+"','0')";
						stmt62 = con6.prepareStatement(sql_addfile);
						int rs_save =  stmt62.executeUpdate();
						stmt62.close();
					}
					stmt6.close();
					con6.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				status_insert=0;
				clearDataPt();
			}else{					
				JOptionPane.showMessageDialog(null,"filename error:"+filename ,"Infomation",JOptionPane.WARNING_MESSAGE); 				
			}
		}
		 
	}
	public void saveFile(String input,String output){
		
		String Before=input.trim();
		String After=output.trim();		
		String hn_folder=After.substring(14);
		String tiff_in=Before;
		String pdf_after=After.substring(0,After.length()-4);
		String pdf_out="C:\\utthscan\\"+pdf_after+".pdf";
		makePDF(tiff_in,pdf_out);
		 
		sendFileHDFS(pdf_after);
		//System.out.println("*****"+pdf_after);
		 
		nameBefore="";
		nameAfter="";
		
	}
	public void makePDF(String tiff_in,String pdf_out){
		String pdf=pdf_out;
		String tiff=tiff_in;
		/*
		String text_embbed=pdf.substring(pdf.length()-25,pdf.length()-17);
		String d= text_embbed.substring(6, 8);
		String m= text_embbed.substring(4, 6);
		String y= text_embbed.substring(0, 4);
		*/
		RandomAccessFileOrArray ra;
		try {
			ra = new RandomAccessFileOrArray(tiff);
			int n = TiffImage.getNumberOfPages(ra);
	        //Image img;
			float x, y;
	        for (int p = 1; p <= n; p++) {
	        	com.lowagie.text.Image image = TiffImage.getTiffImage(ra, 1);
	        	com.lowagie.text.Rectangle pageSize = new com.lowagie.text.Rectangle(image.getWidth(), image.getHeight()+80);

	        	Document document = new Document(pageSize);
	        	PdfWriter writer = PdfWriter.getInstance(document,  new FileOutputStream(pdf));
	        	
	        	writer.setStrictImageSequence(true);
	        	document.open();
	        	document.add(image);
	        	
	        	Phrase p1 = new Phrase(tiff.trim(),FontFactory.getFont(FontFactory.COURIER, 24, com.lowagie.text.Font.BOLD,new GrayColor(0.5f)));
	        	PdfContentByte canvas = writer.getDirectContent();
	        	x = (pageSize.getLeft() + pageSize.getRight()) / 4;
	        	y = (pageSize.getTop() + pageSize.getBottom()) / 10;
	        	canvas.saveState();
	        	//document.newPage();
	        	PdfGState state = new PdfGState();
	            state.setFillOpacity(0.2f);
	            canvas.setGState(state);
	            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, p1, x, y, 0);
	            canvas.restoreState();
	        	/*
	        	PdfPTable table =new PdfPTable(1);
	    		table.setTotalWidth(500);
	    		table.setLockedWidth(true);
	    		table.getDefaultCell().setFixedHeight(30);
	    		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
	    		table.addCell(("   "+d+"-"+m+"-"+y).trim());
	    		table.writeSelectedRows(0, -1, 10, table.getTotalHeight() + 30, canvas);
	    		*/
	        	//document.newPage();

	        	document.close();
	        	
	            ra.close();
	        }
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
				Path localFilePath = new Path("c://utthscan//"+fn+".pdf");
				Path hdfsFilePath=new Path(newFolderPath+"/"+fn+"HN.pdf");

				fs.moveFromLocalFile(localFilePath, hdfsFilePath);
				//System.out.println("sent "+fn);

				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ClearFile();
		}
	public void clearDataPt(){
		getDataClear();		
		Label_Info.setText("");
		Label_Info1.setText("");		 
		group.clearSelection();
		pageButton1.setSelected(true);
		Label_IMG.setIcon(null);
		page="01";
		TextField_page.setText("");
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
	public void ClearFile(){		
		File folder = new File("C:\\utthscan\\");
		File[] listOfFiles = folder.listFiles();
        //System.out.println(listOfFiles.length);
        String file_name_input[]=new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
        	if (listOfFiles[i].isFile()) {     		
        		listOfFiles[i].delete();
        	}
        	else if(listOfFiles[i].isDirectory()){
        		listOfFiles[i].delete();
        	}
        }
	}
}
