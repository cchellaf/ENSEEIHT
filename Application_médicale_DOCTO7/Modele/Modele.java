package Modele;

import bdd.BaseDeDonnees;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Modele extends Observable {

    private BaseDeDonnees bdd;

    public Modele(BaseDeDonnees bdd) {
        this.bdd = bdd;
    }

    public Patient getPatient(String login, String password) throws SQLException {
        return bdd.getPatient(login, password);
    }

    public Medecin getMedecin(String login, String password) throws SQLException {
        return bdd.getMedecin(login, password);
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        this.bdd.ajouterUtilisateur(utilisateur);
    }

    public List<String> getSpecialites() throws SQLException {
        return this.bdd.getSpecialites();
    }

    public void ajouterEtablissement(Etablissement etablissement) throws SQLException {
        this.bdd.ajouterEtablissement(etablissement);
    }

    public List<String> getNomsEtablissements() throws SQLException { return this.bdd.getNomsEtablissements(); }

    public Etablissement getEtablissement(String nom) throws SQLException { return this.bdd.getEtablissement(nom); }

    public List<Medecin> getMedecins(String specialite) {
        List<Medecin> resultat = new ArrayList<>();
        try {
            resultat = this.bdd.getMedecins(specialite);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return resultat;
    }

    public void ajouterRendezVous(RendezVous rendezVous) throws SQLException {
        this.bdd.ajouterRendezVous(rendezVous);
    }

    public List<RendezVous> getRendezVous(Utilisateur utilisateur, LocalDate localDate) throws SQLException {
        return this.bdd.getRendezVous(utilisateur, Date.valueOf(localDate));
    }

    public List<RendezVous> getRendezVous(Utilisateur utilisateur) throws SQLException {
        return this.bdd.getRendezVous(utilisateur);
    }

    public PlanningJour getPlanningJour(Medecin medecin, LocalDate jour) throws SQLException {
        return this.bdd.getPlanningJour(medecin, Date.valueOf(jour));
    }

    public void terminerRendezVous(RendezVous rendezVous) throws SQLException {
        this.bdd.mettreAJourRendezVous(rendezVous);
    }
}
