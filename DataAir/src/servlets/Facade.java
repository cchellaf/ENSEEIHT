package servlets;


import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import entities.Client;
import entities.Pay;
import entities.Utilisateur;
import entities.Vol;


@Singleton
public class Facade {
	
	@PersistenceContext
	EntityManager em;
	
	//id déja utilisé
	public void ajoutClient(String nom, String prenom, String mail, String mdp, String num_telephone,
			String num_passeport, String date_naissance) {
		Client c = new Client(nom,  prenom,  mail,  mdp,  num_telephone, num_passeport,  date_naissance);
		em.persist(c);
	}
	
	public Collection<Client> listeClients() {
		TypedQuery<Client> req = em.createQuery("from Client", Client.class);
		return req.getResultList();
	}
	
	public void ajoutVol(String num_vol, String depart, String arrivee, String date,String heure,  int place_dispo) {
		Vol v = new Vol( num_vol, depart, arrivee,  date, heure,  place_dispo);
		em.persist(v);
	}
	
	public Collection<Utilisateur> connexion(String mail,String mdp) {
		TypedQuery<Utilisateur> req = em.createQuery("SELECT u from Utilisateur u where mail = '" + mail +"' AND mdp = '" +mdp +"'", Utilisateur.class);
		Collection<Utilisateur> clients = req.getResultList();
		if (clients.size() == 1) {
			Utilisateur c = (Utilisateur) clients.toArray()[0];
			//vérification du mot de passe
			if (c.getMdp().contentEquals(mdp)){
				return clients;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	
	public Collection<Vol> listeVols() {
		TypedQuery<Vol> req = em.createQuery("from Vol",Vol.class);
		return req.getResultList();
	}
	
	public Collection<Vol> listeVolsClient(int idClient) {
		entities.Utilisateur c = (Client) em.find(Client.class,idClient);
		return ((Client)c).getListe_vols();
	}
	
	
	public Collection<Vol> listeVols_selection (String depart , String arrivee , String date) {
		TypedQuery<Vol> req = em.createQuery("SELECT v from Vol v where depart = '"+ depart +"' AND  arrivee = '"+arrivee +"' AND date = '"+ date +"'",Vol.class);
		return req.getResultList();
	}
	
	public int reserver(int clientId,int volId) {
		int a = 0;
		entities.Utilisateur c = (Client) em.find(Client.class,clientId);
		Vol v = em.find(Vol.class,volId);
		if (v.getPlace_dispo() > 0 ) {			
			v.setPlace_dispo(v.getPlace_dispo() - 1);
			((Client )c).getListe_vols().add(v);
			a=1;	
		}
		
		return a;	
		
	}
	
	
	public Vol trouverVol(int volId) {
		Vol v = em.find(Vol.class,volId);
		return v;	
		
	}
	
	public void payement(String num, String code) {
		Pay c = new Pay(num,code);
		em.persist(c);
	}

	
	public void modifierMDP(int c, String mdp) {
		Client v = em.find(Client.class,c);
		v.setMdp(mdp);
	}

}
