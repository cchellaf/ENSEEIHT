package Modele;

import java.time.LocalDate;
import java.time.Period;

public class Patient extends Utilisateur {

    private String securiteSociale;

    private LocalDate dateNaissance;

    private String taille;

    private String poids;

    public Patient (String login,
                    String password,
                    String nom,
                    String prenom,
                    String telephone,
                    String securiteSociale,
                    LocalDate dateNaissance,
                    String taille,
                    String poids) {
        super(login, password, nom, prenom, telephone);
        this.securiteSociale = securiteSociale;
        this.dateNaissance = dateNaissance;
        this.taille = taille;
        this.poids = poids;
    }

    public Patient (int id,
                    String login,
                    String password,
                    String nom,
                    String prenom,
                    String telephone,
                    String securiteSociale,
                    LocalDate dateNaissance,
                    String taille,
                    String poids) {
        super(id, login, password, nom, prenom, telephone);
        this.securiteSociale = securiteSociale;
        this.dateNaissance = dateNaissance;
        this.taille = taille;
        this.poids = poids;
    }

    public int getAge() {
        Period period = Period.between(dateNaissance, LocalDate.now());
        return period.getYears();
    }

    public String getSecuriteSociale() {
        return securiteSociale;
    }

    public void setSecuriteSociale(String securiteSociale) {
        this.securiteSociale = securiteSociale;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }
}
