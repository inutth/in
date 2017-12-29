package org.utt.app.ipd;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.utt.app.IPDFrame;
import org.utt.app.InApp;
import org.utt.app.dao.DBmanager;
import org.utt.app.util.DateLabelFormatter;
import org.utt.app.util.Setup;



public class IPDPanel extends JPanel  implements  Observer{
	Dimension screen;
	ObjectIPD oUserInfo;
	JSplitPane split,splitR;
	JPanel leftPanel,midPanel,topMainPanel,mainPanel,pt_info,jPanel,topMainLeftPanel,mainLeftPanel,topLeftPanel;
	String ptStatus=" and admbed.OUTDATETIME  is null";
	
	JTabbedPane tabbedPane;
	JButton ButtonRefresh;
	JRadioButton PtStatus1,PtStatus2;
	ButtonGroup PtGroup = new ButtonGroup();
	Vector<Vector<String>> data;
	JPanel bottomPanel;
	JTextArea textArea;
	JDatePickerImpl picker;
	String day="",month="",dateSearch="";
	Vector<String> columnNames;
	JScrollPane scrollPane;
	JTable table;
	JTextField TextField_AN,TextField_HN,TextField_CID;
	JLabel Label_AN,Label_HN,Label_CID,Label_NAME,Label_Memo,Label_Extra,Label_Extra1,Label_Date,Label_LOS;
	NurseFormIPD nurseFormIPD;

	public IPDPanel(ObjectIPD oUserInfo) {
		super();
		this.oUserInfo=oUserInfo;
		setLayout(new BorderLayout(0, 0));
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(screen.width, screen.height-175));
		nurseFormIPD = new NurseFormIPD(oUserInfo);
		
		split = new JSplitPane(split.HORIZONTAL_SPLIT);
		split.setDividerLocation((screen.width*2)/10);
		splitR = new JSplitPane(split.VERTICAL_SPLIT);
		splitR.setDividerLocation(140);
 
		setTop();
		setLeft();
		setRight();
		 
		add(split, BorderLayout.CENTER);
		split.setRightComponent(splitR);
		split.setLeftComponent(leftPanel);
		splitR.setTopComponent(topMainPanel);
		splitR.setBottomComponent(midPanel);

	}
	public void update(Observable oObservable, Object oObject) {
		oUserInfo = ((ObjectIPD)oObservable); // cast
		TextField_HN.setText(oUserInfo.GetPtHN());
		TextField_CID.setText(oUserInfo.GetPtCID());
		TextField_AN.setText(oUserInfo.GetPtAN());
		Label_NAME.setText(oUserInfo.GetPtLabel());
		textArea.setText(oUserInfo.GetMemo());
		Label_Extra1.setText(oUserInfo.GetMemo1());
		Label_LOS.setText(" LOS :: "+oUserInfo.GetAdmtime()+" วัน");
	}
	public void initForm() {
		//nurseFormIPD.setInit();
		//nurseFormIPD.getValueWard();
		getData();
		nurseFormIPD.setComboBox();
	}
	public void setTop(){
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(screen.width-(screen.width*2)/10, screen.height-175));
		mainPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		 
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.setBackground(Setup.getColor());  
		//add(mainPanel, BorderLayout.CENTER);
		
		topMainPanel = new JPanel();
		topMainPanel.setLayout(new BorderLayout(0, 0));
		topMainPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), 140));
		topMainPanel.setBackground(Setup.getColor());  
		//mainPanel.add(topMainPanel, BorderLayout.NORTH);
		
		 
		
		pt_info = new JPanel();		 
		pt_info.setLayout(null);
		pt_info.setBackground(Setup.getColor());
		topMainPanel.add(pt_info, BorderLayout.CENTER);
		
		JLabel Label_Date = new JLabel("วันที่");
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Date.setBounds(5,12,30,20);
		pt_info.add(Label_Date);
		
		UtilDateModel model = new UtilDateModel();
		model.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "");
		p.put("text.month", "");
		p.put("text.year", "");
		JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
		picker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		picker.setBackground(Setup.getColor());
		picker.getJFormattedTextField().setBackground(Setup.getColor());
		picker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(picker.getModel().getDay()<10){
					day="0"+picker.getModel().getDay();
				}else{
					day=""+picker.getModel().getDay();
				}
				if(picker.getModel().getMonth()+1<10){
					month="0"+(picker.getModel().getMonth()+1);
				}else{
					month=""+(picker.getModel().getMonth()+1);
				}
				dateSearch=picker.getModel().getYear()+"-"+month+"-"+day;
				oUserInfo.setPtVisitdate(dateSearch.trim());
				 
			}
		});
		picker.setShowYearButtons(true);
		picker.setTextEditable(false);
		 
		jPanel = new JPanel();
		jPanel.setBounds(30, 4, 220, 30);
		jPanel.setBackground(Setup.getColor());
		jPanel.add((JComponent)picker);
		pt_info.add(jPanel);
		
		if(picker.getModel().getDay()<10){
			day="0"+picker.getModel().getDay();
		}else{
			day=""+picker.getModel().getDay();
		}
		if(picker.getModel().getMonth()+1<10){
			month="0"+(picker.getModel().getMonth()+1);
		}else{
			month=""+(picker.getModel().getMonth()+1);
		}			
		dateSearch=picker.getModel().getYear()+"-"+month+"-"+day;
		oUserInfo.setPtVisitdate(dateSearch.trim());
		Label_AN = new JLabel("AN");
		Label_AN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		pt_info.add(Label_AN);
		Label_AN.setBounds(270, 12, 20, 14);
		
		TextField_AN = new JTextField();
		TextField_AN.setEditable(false);
		TextField_AN.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pt_info.add(TextField_AN);
		TextField_AN.setBounds(290, 10, 80, 20);
	
		
		Label_HN = new JLabel("HN");
		Label_HN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_HN.setBounds(390, 12, 20, 14);
		pt_info.add(Label_HN);
		
		TextField_HN = new JTextField();
		TextField_HN.setEditable(false);
		TextField_HN.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TextField_HN.setText("");
		TextField_HN.setBounds(410, 10, 80, 20);
		pt_info.add(TextField_HN);

		
		Label_CID = new JLabel("ID");
		Label_CID.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_CID.setBounds(510, 12, 20, 14);
		pt_info.add(Label_CID);
		
		TextField_CID = new JTextField();
		TextField_CID.setEditable(false);
		TextField_CID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		//TextField_CID.setBackground(new Color(229, 200, 179));
		TextField_CID.setBounds(530, 10, 150, 20);
		TextField_CID.setText("");
		pt_info.add(TextField_CID);

		
		Label_NAME = new JLabel(" ชื่อ  ");
		Label_NAME.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_NAME.setBounds(4, 30, (screen.width-((screen.width*2)/10)-40), 30);
		pt_info.add(Label_NAME);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(new Color(255, 0, 0));
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textArea.setBackground(Setup.getColor());
		//textArea.setBackground(new Color(0, 0, 0));
		textArea.setBounds(70, 70, (screen.width-((screen.width*2)/10)-200), 40);
		pt_info.add(textArea);
		
		Label_Memo = new JLabel("การแพ้ยา ");
		Label_Memo.setForeground(new Color(255, 0, 0));
		Label_Memo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Label_Memo.setBackground(Setup.getColor());
		Label_Memo.setBounds(10, 65, 55, 30);
		pt_info.add(Label_Memo);
		
		Label_Extra = new JLabel("ข้อมูลเพิ่มเติม");
		//Label_Extra.setForeground(new Color(255, 0, 0));
		Label_Extra.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra);
		Label_Extra.setBounds(10, 110, 150, 30);
		Label_Extra.setBackground(Setup.getColor());
		pt_info.add(Label_Extra);
		
		Label_Extra1 = new JLabel("");
		//Label_Extra.setForeground(new Color(255, 0, 0));
		Label_Extra1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Setup.SetUnderline(Label_Extra1);
		Label_Extra1.setBounds(150, 110, 400, 30);
		Label_Extra1.setBackground(Setup.getColor());
		pt_info.add(Label_Extra1);
		
		Label_LOS = new JLabel("LOS");
		Label_LOS.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_LOS.setHorizontalAlignment(SwingConstants.LEFT);
		Label_LOS.setBounds(697, 12, 200, 14);
		pt_info.add(Label_LOS);
	
	}
	public void setLeft(){
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-175));
		leftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new BorderLayout(0, 0));
		topLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		leftPanel.add(topLeftPanel, BorderLayout.NORTH);
		
		Label_Date= new JLabel("วันที่ "+Setup.ShowThaiDate(oUserInfo.GetPtVisitdate()),JLabel.CENTER);
		Label_Date.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Label_Date.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		topLeftPanel.add(Label_Date, BorderLayout.NORTH);
		
		mainLeftPanel = new JPanel();
		mainLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, screen.height-405));
		mainLeftPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		//add(mainPanel, BorderLayout.CENTER);
		mainLeftPanel.setLayout(new BorderLayout(0, 0));
		leftPanel.add(mainLeftPanel, BorderLayout.CENTER);
		
		topMainLeftPanel = new JPanel();
		mainLeftPanel.add(topMainLeftPanel, BorderLayout.NORTH);
		topMainLeftPanel.setBackground(Setup.getColor());
		topMainLeftPanel.setLayout(null);
		topMainLeftPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		
		JLabel LabelListdata = new JLabel("รายชื่อผู้รับบริการ",JLabel.CENTER);
		LabelListdata.setFont(new Font("Tahoma", Font.PLAIN, 13));
		LabelListdata.setBounds(5,5,100,20);
		topMainLeftPanel.add(LabelListdata);
		
		PtStatus1 = new JRadioButton("All");
		PtStatus1.setBackground(Setup.getColor());
		PtStatus1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus=" and (admbed.OUTDATETIME  is null or  admbed.OUTDATETIME between '"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+" 00:00:00' and '"+Setup.DateInDBMSSQL(oUserInfo.GetPtVisitdate())+" 23:59:59') ";
							}
		});
		PtGroup.add(PtStatus1);
		PtStatus1.setBounds(100, 5, 40, 25);
		topMainLeftPanel.add(PtStatus1);
		
		PtStatus2 = new JRadioButton("Active");
		PtStatus2.setBackground(Setup.getColor());
		PtStatus2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ptStatus="and admbed.OUTDATETIME  is null";
			}
		});
		PtGroup.add(PtStatus2);
		PtStatus2.setSelected(true);
		PtStatus2.setBounds(143, 5, 100, 25);
		topMainLeftPanel.add(PtStatus2);
		
		columnNames = new Vector<String>();
		columnNames.add("ที่");
		columnNames.add("BedNo.");
		columnNames.add("   รายชื่อผู้รับบริการ  ");
		for (int n = 3; n < 7; n++) {	
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
				
				JLabel jc = (JLabel) c;
				
				if (!isRowSelected(row)){
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					 
					String type = (String)getModel().getValueAt(modelRow, 3);
					//System.out.println(type);
					String status=type.trim();					  
					if ("1".equals(status)){
						c.setBackground(new Color(206,203,208));					 
		 			}
					 
				}
				int modelRow1 = convertRowIndexToModel(row);
				int modelColumn = convertColumnIndexToModel(column);
				
				return c;
			}
		};
		//table.setBackground(new Color(229, 234, 243  ));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
		        int col = table.columnAtPoint(e.getPoint());
		        
		        clearValue();
	        	
		        String select_an=table.getValueAt(row,4).toString().trim();
		        oUserInfo.setPtAN(select_an);
		        String name=table.getValueAt(row,2).toString().trim();
		        oUserInfo.setPtName(name);
		        String hn=table.getValueAt(row,6).toString().trim();
		        oUserInfo.setPtHN(hn);
		        
		        searchAN(select_an,hn);
        
		        String pt_bed=table.getValueAt(row,1).toString().trim();
		        oUserInfo.setPtBed(pt_bed);

		        String inscl="";
		        oUserInfo.setPtLabel(" ชื่อ   "+name+"     อายุ .. "+oUserInfo.GetPtAge()+"     :สิทธิ.. "+oUserInfo.GetRightCode()+" [ NHSO   :  "+ inscl+" ]");		        
		        String select_admtime=table.getValueAt(row,5).toString().trim().substring(0, 10);
		        oUserInfo.setAdmtime(Setup.LOSInDayNow(select_admtime));				 		     

			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setRowHeight(25);		
		table.setFillsViewportHeight(true);
 
		scrollPane = new JScrollPane(table);
		//getData();
		
		mainLeftPanel.add(scrollPane, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Setup.getColor());
		mainLeftPanel.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(null);
		 
		bottomPanel.setPreferredSize(new Dimension((screen.width*2)/10, 30));
		ButtonRefresh = new JButton("Refresh");
		ButtonRefresh.setBackground(Setup.getColor());
		ButtonRefresh.setForeground(UIManager.getColor("Button.darkShadow"));

		ButtonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearValue();
				getData();
			}
		});
		ButtonRefresh.setBounds((((screen.width*2)/10)/2-45), 2, 90, 25);
		bottomPanel.add(ButtonRefresh);
	}
	public void setRight(){
		midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout(0, 0));
		midPanel.setPreferredSize(new Dimension(screen.width-((screen.width*2)/10), screen.height-320));
		//mainPanel.add(midPanel, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane();
        //tabbedPane.setBackground(new Color(232, 248, 245  ));
        midPanel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("<html><b>Nurse FORM</b></html> ",new ImageIcon(getClass().getClassLoader().getResource("images/bullet_black.png")) ,nurseFormIPD);
        //tabbedPane.addTab("<html><b>Lab Result</b></html> ",new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/bell.png")) ,labIPDResult);
        //tabbedPane.addTab("<html><b>Medical Info</b></html> " ,new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/diagram.gif")),medInfoPanel);
        //tabbedPane.addTab("<html><b>EMR</b></html> ",new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/running.png")) ,mainHDFSOPDPanel);
        //tabbedPane.addTab("<html><b>Old EMR</b></html> ",new ImageIcon(InApp.class.getResource("/org/utt/app/gfx/box.png")) ,mainOldHDFSOPDPanel);
        

	}
	public void getData(){
		table.setModel(fetchDataIPD());
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(60 );
		columnModel.getColumn(2).setPreferredWidth((screen.width*2)/10-100);
		
		for (int n = 3; n < 7; n++) {	
			columnModel.getColumn(n).setPreferredWidth(0);
			columnModel.getColumn(n).setMinWidth(0);
			columnModel.getColumn(n).setMaxWidth(0);
		}
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
	}
	public  DefaultTableModel fetchDataIPD(){
		String[] parts=null;
		parts = InApp.userappcode.split("\\,");
		String sql_user1="",sql_user="";
		if(parts.length==1){
			for(int i=0;i<parts.length;i++){
				sql_user1=" and admmaster.admward='"+parts[i]+"'";
			}
			sql_user=sql_user1;
		}
		else if(parts.length>1){
			sql_user1=" and admmaster.admward in (";
			for(int i=0;i<parts.length;i++){
				sql_user1+="'"+parts[i]+"',";				 
			}
			sql_user=sql_user1.substring(0,sql_user1.length()-1)+")";
		}
		 
		
		Connection conn ;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		String query="select admmaster.an,admmaster.hn,admbed.outdatetime,admbed.bedno,admmaster.admdatetime from admmaster,admbed  where admmaster.an=admbed.an  "+ptStatus+"  "+sql_user+" order by admbed.bedno ";				 
		//String query="select an,firstname,lastname,hn,usedrightcode,outdatetime,BirthDateTime,sex,maritalstatus,initialnamecode,ref,bedno from in_view_pt_ipd  where   "+ptStatus+"  and reftype='01'  and suffix='0' "+sql_user+" order by bedno ";			
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		try {

			conn = new DBmanager().getConnMSSql();
			//conn1 = DriverManager.getConnection(InApp.UrlCust21);
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			int p=1;
			while (rs.next()) {
				
				String pt_status="0";
				final Vector<String> vstring = new Vector<String>();
				vstring.add(" "+Integer.toString(p) );
				//vstring.add(rs.getString(1)+": "+initname+" "+rs.getString(2).substring(1)+" "+rs.getString(3).substring(1));
				String bedno="";
				if(rs.getString(4)!=null){
					bedno=rs.getString(4);
				}
				else{
	            	bedno="0000";
	            }
				vstring.add(bedno);
				vstring.add(getName(rs.getString(2).trim()));
				if(rs.getString(3)!=null){
					pt_status="1";
					
				}
				vstring.add(pt_status);		
				vstring.add(rs.getString(1));
				vstring.add(rs.getString(5));
				vstring.add(rs.getString(2));
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
	public void searchAN(String an,String hn){
		Connection conn;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		
		String query_db="select  BirthDateTime from patient_info where hn='"+hn.trim()+"'" ; 
		String query_right="select  usedrightcode from admmaster where an='"+an.trim()+"'" ; 
		String query_ref="select  ref from patient_ref where  reftype='01'  and hn='"+hn.trim()+"'" ; 
		
		conn=new DBmanager().getConnMSSql();
		 
		try {
			stmt = conn.prepareStatement(query_db);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String  right="";
				if(rs.getString(1) !=null){
					Date birthday=rs.getDate(1);
					String age_pt=Setup.AgeInAll(birthday.toString()).trim();
					oUserInfo.setPtAge(age_pt);
				}		
				
			}
			stmt.close();
			//
			stmt1 = conn.prepareStatement(query_right);
			
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				String  right="";				
				if(rs1.getString(1) !=null){
					for(int i=0;i<InApp.rightname.length;i++){
						if(rs1.getString(1).trim().equals(InApp.rightname[i][0])){
							right=InApp.rightname[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					oUserInfo.setRightCode(right+"("+rs1.getString(1).trim()+")");
				}
							
			}
			stmt1.close();
			//
			stmt2 = conn.prepareStatement(query_ref);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {			
				if(rs2.getString(1) !=null){
					oUserInfo.setPtCID(rs2.getString(1).trim());
				}
							
			}
			stmt2.close();
			//
			String sql_allergy="select memo from view_MEDICINE_ALLERGIC where hn=? and suffix='8'";
			stmt3 = conn.prepareStatement(sql_allergy);
			stmt3.setString(1, hn);
			rs3 = stmt3.executeQuery();
			String memo1="",memo2="";
			//String memo="";
			while (rs3.next()) {
				if(rs3.getString(1)!=null){
					 memo1=rs3.getString(1).trim();
				}
			}
			rs3.close();
			stmt3.close();
			
			for(int i=0;i<IPDFrame.hn_z515.length;i++){
				if(hn.equals(IPDFrame.hn_z515[i])){
					memo2="Z515";
					break;
				}
				//System.out.println(i+1+". "+labunitname[i][0]);
			}
			
			 
			oUserInfo.setMemo(memo1);
			oUserInfo.setMemo1(memo2);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getName(String hn){
		String name_to="",initname="";
		Connection conn;
		PreparedStatement stmt,stmt1,stmt2,stmt3;
		ResultSet rs,rs1,rs2,rs3;
		
		String query_db="select  BirthDateTime,sex,maritalstatus,initialnamecode from patient_info where hn='"+hn.trim()+"'" ; 
		String query_name="select firstname, lastname from patient_name where  suffix='0'  and hn='"+hn.trim()+"'" ; 
		
		conn=new DBmanager().getConnMSSql();
		 
		try {
			stmt = conn.prepareStatement(query_db);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String sex_pt="",mar_pt="";
				Date birthday=rs.getDate(1);
				String age_pt=Setup.AgeInAll(birthday.toString()).trim();
				if(rs.getString(4)==null || rs.getString(4).equals("1") || rs.getString(4).equals("2") || rs.getString(4).equals("3") || rs.getString(4).equals("127") || rs.getString(4).equals("128")){
					sex_pt=rs.getString(2).trim();
					mar_pt=rs.getString(3).trim();				 
					int age_y_int= Integer.parseInt(Setup.AgeInYear(birthday.toString()).trim());
					if(age_y_int<15){
						if(sex_pt.equals("1")){
							initname="ด.ช.";
						}else if(sex_pt.equals("2")){
							initname="ด.ญ.";
						}
					}else if(age_y_int>=15){
						if(sex_pt.equals("1")){
							initname="นาย";
						}else if(sex_pt.equals("2")){
							if(mar_pt.equals("1")){
								initname="นางสาว";
							}else {
								initname="นาง";
							}
							
						}
					}					 
					
				}else{
					/**
					for(int i=0;i<oUserInfo.getInitName().length;i++){
						if(rs.getString(4).trim().equals(oUserInfo.getInitName()[i][0])){
							initname=oUserInfo.getInitName()[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					*/
					for(int i=0;i<InApp.initname_indb.length;i++){
						if(rs.getString(4).trim().equals(InApp.initname_indb[i][0])){
							initname=InApp.initname_indb[i][1];
							break;
						}
						//System.out.println(i+1+". "+labunitname[i][0]);
					}
					//initname=rs.getString(10).trim();
				}	
				
			}
			stmt.close();
			//

			stmt2 = conn.prepareStatement(query_name);
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {			
				if(rs2.getString(1) !=null){
					name_to=" "+initname+" "+rs2.getString(1).substring(1).trim()+" "+rs2.getString(2).substring(1).trim();
				}
							
			}
			stmt2.close();
			//
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name_to;
	}
	public void clearValue(){	 
        oUserInfo.setPtAN("");      
        oUserInfo.setPtName("");    
        oUserInfo.setPtHN("");       
        oUserInfo.setRightCode("");        
        oUserInfo.setPtBed("");         
        oUserInfo.setPtAge("");
        oUserInfo.setPtLabel("");        
        oUserInfo.setPtCID("");       
        oUserInfo.setMemo("");
        oUserInfo.setMemo1("");
        //oUserInfo.setReferFrom("");
        oUserInfo.setAdmtime("");
        //mainHDFSOPDPanel.clear();
        //mainOldHDFSOPDPanel.clear();
	}

}
