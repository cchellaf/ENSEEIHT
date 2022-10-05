package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Pay {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	String num;
	String code;
	
	public Pay(String num, String code) {
		super();
		this.num = num;
		this.code = code;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
