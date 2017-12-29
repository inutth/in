package org.utt.app.ipd;

import java.util.Observable;

public class ObjectIPD extends Observable{
	
	String visit_date="",hn="",an="",pt_bed="",admtime="",pt_name="",pt_age="",ptcid="",ptLabel="",memo="",memo1="",rightcode="";
	
	public ObjectIPD(){

	}
	public void setPtVisitdate(String visit_date ){	
		this.visit_date=visit_date;
		setChanged();
		notifyObservers();
	}
	public String GetPtVisitdate(){		
		return visit_date;
	}
	public void setPtHN(String hn ){		
		this.hn=hn;
		setChanged();
		notifyObservers();
	}
	public String GetPtHN(){		
		return hn;
	}
	public void setPtName(String pt_name ){		
		this.pt_name=pt_name;
		setChanged();
		notifyObservers();
	}
	public String GetPtName(){		
		return pt_name;
	}
	public void setPtAN(String an ){		
		this.an=an;
		setChanged();
		notifyObservers();
	}
	public String GetPtAN(){	
		return an;
	}
	public void setPtBed(String pt_bed ){		
		this.pt_bed=pt_bed;
		setChanged();
		notifyObservers();
	} 
	public String GetPtBed(){	
		return pt_bed;
	}
	public void setAdmtime(String admtime ){	
		this.admtime=admtime;
		setChanged();
		notifyObservers();
	}
	public String GetAdmtime(){	
		return admtime;
	}
	public void setPtAge(String pt_age ){		
		this.pt_age=pt_age;
		setChanged();
		notifyObservers();
	}
	public String GetPtAge(){		
		return pt_age;
	}
	public void setPtCID(String ptcid ){		
		this.ptcid=ptcid;
		setChanged();
		notifyObservers();
	}
	public String GetPtCID(){		
		return ptcid;
	}
	public void setPtLabel(String ptLabel ){	
		this.ptLabel=ptLabel;
		setChanged();
		notifyObservers();
	} 
	public String GetPtLabel(){		
		return ptLabel;
	}
	public void setMemo(String memo ){		
		this.memo=memo;
		setChanged();
		notifyObservers();
	}
	public String GetMemo(){		
		return memo;
	}
	public void setMemo1(String memo1 ){		
		this.memo1=memo1;
		setChanged();
		notifyObservers();
	}
	public String GetMemo1(){		
		return memo1;
	}
	public void setRightCode(String rightcode ){
		this.rightcode=rightcode;
		setChanged();
		notifyObservers();
	}
	public String GetRightCode(){		
		return rightcode;
	}


}
