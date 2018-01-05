package org.utt.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.utt.app.dao.DBmanager;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class OPDReport extends JPanel {
	Dimension screen;
	JTable table;
	Vector<Vector<String>> data;
	Vector<String> columnNames;
	JButton ButtonRefresh;
	JPanel midPanel,ButtomPanel;
	JLabel Label_Username,Label_Password,Label_Name,Label_Position,Label_Type,Label_Clinic_code,Label_Report_code,Label_Certifyno,Label_Status;
	JTextField TextField_ID,TextField_Username,TextField_Password,TextField_Name,TextField_Position,TextField_Type,TextField_Clinic_code,TextField_Report_code,TextField_Certifyno;

	JScrollPane scrollPane;
	JRadioButton userStatus1,userStatus2,ButtonStatus1,ButtonStatus2;
	ButtonGroup bg = new ButtonGroup();
	ButtonGroup bgStatus = new ButtonGroup();

	public OPDReport() {
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-175));
		setLayout(new BorderLayout(0, 0));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(206,203,208));
		add(leftPanel, BorderLayout.WEST);
		leftPanel.setPreferredSize(new Dimension(450, screen.height-175));
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		leftPanel.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(null);
		topPanel.setPreferredSize(new Dimension(450, 30));
		
		JLabel Label_Info = new JLabel(" รายชื่อแบบฟอร์ม");
		Label_Info.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Label_Info.setBounds(160,5,100,20);
		topPanel.add(Label_Info);
		
		userStatus1 = new JRadioButton("ALL");
		userStatus1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		bg.add(userStatus1);
		userStatus1.setBounds(300, 5, 50, 25);
		topPanel.add(userStatus1);
		
		userStatus2 = new JRadioButton("Active");
		userStatus2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		userStatus2.setSelected(true);
		bg.add(userStatus2);
		userStatus2.setEnabled(true);
		userStatus2.setBounds(360, 5, 70, 25);
		topPanel.add(userStatus2);
		
		columnNames = new Vector<String>();
  		columnNames.add("ที่");
  		columnNames.add(" Code ");
		columnNames.add(" Clinic ");
		columnNames.add(" ");
		columnNames.add(" ชื่อ ");
		for (int n = 5; n < 9; n++) {	
			columnNames.add(" ");
		}
		
		data = new Vector<Vector<String>>();
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
					int modelRow = convertRowIndexToModel(row);
					String type = (String)getModel().getValueAt(modelRow, 6);
					String status=type.trim();					  
					if ("1".equals(status)){
						c.setBackground(new Color(240, 248, 255));						
					}
				}
				return c;
			}
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
		        int col = table.columnAtPoint(e.getPoint());
		        ClearTextField();
		        TextField_ID.setEditable(false);
		        TextField_ID.setText(table.getValueAt(row,1).toString().trim());
		        TextField_Username.setEditable(false);
		        TextField_Username.setText(table.getValueAt(row,2).toString().trim());
		        TextField_Password.setText(table.getValueAt(row,3).toString().trim());
		        TextField_Name.setText(table.getValueAt(row,4).toString().trim());
		        TextField_Position.setText(table.getValueAt(row,5).toString().trim());
		        TextField_Type.setText(table.getValueAt(row,6).toString().trim());
		        TextField_Clinic_code.setText(table.getValueAt(row,7).toString().trim());

		        if(table.getValueAt(row,5).toString().trim().equals("1")){
		        	ButtonStatus1.setSelected(true);
		        }else{
		        	ButtonStatus2.setSelected(true);
		        }
		        //username=table.getValueAt(row,2).toString().trim();
		        //op=table.getValueAt(row,6).toString().trim();
		        /*
		        if(op.equals("6")){
		        	setAppCodeIPD(table.getValueAt(row,2).toString().trim());
		        }else{
		        	setAppCode(table.getValueAt(row,2).toString().trim());
		        }
		         */
		       
			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
 
		scrollPane = new JScrollPane(table);
		getData();
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		leftPanel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(null);
		
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.setForeground(UIManager.getColor("Button.darkShadow"));

		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getData();
				//hack();
			}
		});
		ButtonRefresh.setBounds((450/2-45), 2, 90, 25);
		bottomPanel.add(ButtonRefresh);
		
		midPanel = new JPanel();
		add(midPanel, BorderLayout.CENTER);
		midPanel.setLayout(null);
		
		JLabel Label_Info_1 = new JLabel("ข้อมูลผู้ใช้งาน");
		Label_Info_1.setHorizontalAlignment(SwingConstants.CENTER);
		Label_Info_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		Label_Info_1.setBounds(10, 11, 182, 25);
		midPanel.add(Label_Info_1);
		
		JLabel Label_ID = new JLabel("ID");
		Label_ID.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_ID.setHorizontalAlignment(SwingConstants.LEFT);
		Label_ID.setBounds(20, 45, 80, 20);
		midPanel.add(Label_ID);
		
		Label_Username = new JLabel("Username");
		Label_Username.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Username.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Username.setBounds(20, 75, 80, 20);
		midPanel.add(Label_Username);
		
		Label_Password = new JLabel("Password");
		Label_Password.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Password.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Password.setBounds(20, 105, 80, 20);
		midPanel.add(Label_Password);
		
		Label_Name = new JLabel("Name");
		Label_Name.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Name.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Name.setBounds(20, 135, 80, 20);
		midPanel.add(Label_Name);
		
		Label_Position = new JLabel("Position");
		Label_Position.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Position.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Position.setBounds(20, 165, 80, 20);
		midPanel.add(Label_Position);
		
		Label_Type = new JLabel("Type");
		Label_Type.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Type.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Type.setBounds(20, 195, 80, 20);
		midPanel.add(Label_Type);
		
		Label_Clinic_code = new JLabel("Clinic_code");
		Label_Clinic_code.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Clinic_code.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Clinic_code.setBounds(20, 225, 80, 20);
		midPanel.add(Label_Clinic_code);
		
		Label_Report_code = new JLabel("Report_code");
		Label_Report_code.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Report_code.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Report_code.setBounds(20, 255, 80, 20);
		midPanel.add(Label_Report_code);
		
		Label_Certifyno = new JLabel("Cert No.");
		Label_Certifyno.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Certifyno.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Certifyno.setBounds(20, 285, 80, 20);
		midPanel.add(Label_Certifyno);
		
		Label_Status = new JLabel("Status");
		Label_Status.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Status.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Status.setBounds(20, 345, 80, 20);
		midPanel.add(Label_Status);
		
		TextField_ID = new JTextField();
		TextField_ID.setEditable(false);
		TextField_ID.setBounds(110, 45, 100, 20);
		midPanel.add(TextField_ID);
		
		TextField_Username = new JTextField();
		TextField_Username.setEditable(false);
		TextField_Username.setBounds(110, 75, 100, 20);
		midPanel.add(TextField_Username);
		
		TextField_Password = new JTextField();
		TextField_Password.setBounds(110, 105, 100, 20);
		midPanel.add(TextField_Password);
		
		TextField_Name = new JTextField();
		TextField_Name.setBounds(110, 135, 200, 20);
		midPanel.add(TextField_Name);
		
		TextField_Position = new JTextField();
		TextField_Position.setBounds(110, 165, 150, 20);
		midPanel.add(TextField_Position);
		
		TextField_Type = new JTextField();
		TextField_Type.setBounds(110, 195, 150, 20);
		midPanel.add(TextField_Type);
		
		TextField_Clinic_code = new JTextField();
		TextField_Clinic_code.setBounds(110, 225, 150, 20);
		midPanel.add(TextField_Clinic_code);
		
		TextField_Report_code = new JTextField();
		TextField_Report_code.setBounds(110, 255, 150, 20);
		midPanel.add(TextField_Report_code);
		
		TextField_Certifyno = new JTextField();
		TextField_Certifyno.setBounds(110, 285, 150, 20);
		midPanel.add(TextField_Certifyno);
		
		ButtonStatus1 = new JRadioButton("Enable");
		ButtonStatus1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
			}
		});
		bgStatus.add(ButtonStatus1);
		ButtonStatus1.setBounds(106, 345, 64, 23);
		midPanel.add(ButtonStatus1);
		
		ButtonStatus2 = new JRadioButton("ALL");
		ButtonStatus2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
			}
		});
		bgStatus.add(ButtonStatus2);
		ButtonStatus2.setBounds(174, 345, 64, 23);
		midPanel.add(ButtonStatus2);


	}
	public void getData(){
		table.setModel(fetchData());
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(60);
		columnModel.getColumn(2).setPreferredWidth(60 );
		columnModel.getColumn(3).setPreferredWidth(0);
		columnModel.getColumn(3).setMinWidth(0);
		columnModel.getColumn(3).setMaxWidth(0);
		columnModel.getColumn(4).setPreferredWidth(290);
		
		for (int n = 4; n < 8; n++) {	
			columnModel.getColumn(n+1).setPreferredWidth(0);
			columnModel.getColumn(n+1).setMinWidth(0);
			columnModel.getColumn(n+1).setMaxWidth(0);
		}
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchData(){
		Connection conn;
		PreparedStatement stmt,stmt1;
		ResultSet rs,rs1;
		String query = "select form_code,clinic,form_name,type,orderpage,page,form_name_print,typeofprint from formopd where type='1' order by clinic" ;
		
		
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		try {
			
			conn = new DBmanager().getConnMySql();
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			int p=1;
			while (rs.next()) {
				final Vector<String> vstring = new Vector<String>();
				vstring.add(" "+Integer.toString(p) );
	            vstring.add("  "+rs.getString(1));
	            vstring.add("  "+rs.getString(2));
	            vstring.add("  "+rs.getString(4));
	            vstring.add("  "+rs.getString(3));
	            vstring.add("  "+rs.getString(5));
	            vstring.add("  "+rs.getString(6));
	            vstring.add("  "+rs.getString(7));
	            vstring.add("  "+rs.getString(8));
	          
	            
	             
	            data.add(vstring);
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
	public void ClearTextField(){
		TextField_ID.setText("");
		TextField_Username.setText("");
		TextField_Password.setText("");
		TextField_Name.setText("");
		TextField_Certifyno.setText("");
		TextField_Position.setText("");
		TextField_Type.setText("");
		TextField_Clinic_code.setText("");
		TextField_Report_code.setText("");
		bgStatus.clearSelection();

	}
	public void hack() {
		 String pdf = "C://utthdev//pdf//11.pdf";
		 String txt = "C://utthdev//text//11.txt";
		 PdfReader reader;
		try {
			reader = new PdfReader(pdf);
			 PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		        TextExtractionStrategy strategy;
		        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
		            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
		            out.println(strategy.getResultantText());
		        }
		        reader.close();
		        out.flush();
		        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Trans success..");
	        
	}

}
