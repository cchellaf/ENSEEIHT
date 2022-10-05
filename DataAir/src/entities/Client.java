package entities;


import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Client extends Utilisateur {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id ; 
	 String num_telephone;
	 String num_passeport;
	 String date_naissance;
	 @ManyToMany(fetch=FetchType.EAGER)
	 Collection<Vol> liste_vols;    
	 
	public Client() {
		super();
	}
	 
	public Client(String nom, String prenom, String mail, String mdp, String num_telephone,
			String num_passeport, String data_naissance) {
		super(nom,prenom,mail,mdp);
		this.num_telephone = num_telephone;
		this.num_passeport = num_passeport;
		this.date_naissance = data_naissance;
		//this.liste_vols = new ArrayList<Vol>();
	}
	
	public int getId() {
		return this.id;
	}

	public String getNum_telephone() {
		return num_telephone;
	}
	public void setNum_telephone(String num_telephone) {
		this.num_telephone = num_telephone;
	}
	public String getNum_passeport() {
		return num_passeport;
	}
	public void setNum_passeport(String num_passeport) {
		this.num_passeport = num_passeport;
	}
	public String getDate_naissance() {
		return date_naissance;
	}
	public void setDate_naissance(String data_naissance) {
		this.date_naissance = data_naissance;
	}
	 public Collection<Vol> getListe_vols() {
		return this.liste_vols;
	}
	public void setListe_vols(Collection<Vol> liste_vols) {
		this.liste_vols = liste_vols;
	} 
	 
}
