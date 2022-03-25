package Modele;

public class Medecin extends Utilisateur {

    private String specialite;

    private Etablissement etablissement;

    public Medecin (String login,
                       String password,
                       String nom,
                       String prenom,
                       String telephone,
                       String specialite,
                       Etablissement etablissement) {
        super(login, password, nom, prenom, telephone);
        this.specialite = specialite;
        this.etablissement = etablissement;
    }

    public Medecin (int id,
                    String login,
                    String password,
                    String nom,
                    String prenom,
                    String telephone,
                    String specialite,
                    Etablissement etablissement) {
        super(id, login, password, nom, prenom, telephone);
        this.specialite = specialite;
        this.etablissement = etablissement;
    }

    public String getSpecialite() { return this.specialite; }

    public Etablissement getEtablissement() { return this.etablissement; }

}
