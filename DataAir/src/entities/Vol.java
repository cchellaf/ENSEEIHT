package entities;


import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;



@Entity
public class Vol {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	 int id ;
	 String  num_vol;
	 String depart;
	 String arrivee;
	 String date;
	 String heure;
	 int place_dispo;
	 @ManyToMany(mappedBy="liste_vols",fetch=FetchType.EAGER)
	 Collection<Client> liste_passagers;
	 
	 
	public Vol(String num_vol, String depart, String arrivee, String date,String heure, int place_dispo) {
		
		this.num_vol = num_vol;
		this.depart = depart;
		this.arrivee = arrivee;
		this.date = date;
		this.heure = heure;
		this.place_dispo = place_dispo;
	}
	public Vol() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNum_vol() {
		return num_vol;
	}
	public void setNum_vol(String num_vol) {
		this.num_vol = num_vol;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public String getArrivee() {
		return arrivee;
	}
	public void setArrivee(String arrivee) {
		this.arrivee = arrivee;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHeure() {
		return this.heure;
	}
	public void setHeure(String heure) {
		this.heure = heure;
	}
	
	public int getPlace_dispo() {
		return place_dispo;
	}
	public void setPlace_dispo(int place_dispo) {
		this.place_dispo = place_dispo;
	}
	 public Collection<Client> getListe_passagers() {
		return this.liste_passagers;
	}
	public void setListe_passagers(Collection<Client> liste_passagers) {
		this.liste_passagers = liste_passagers;
	}
	
}
