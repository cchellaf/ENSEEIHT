package Modele;

public abstract class Utilisateur {
    private int id;
    private String login;
    private String hashPassword;
    private String nom;
    private String prenom;
    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Utilisateur(int id,
                       String login,
                       String hashPassword,
                       String nom,
                       String prenom,
                       String telephone) {
        this.id = id;
        this.login = login;
        this.hashPassword = hashPassword;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public Utilisateur(String login,
                       String hashPassword,
                       String nom,
                       String prenom,
                       String telephone) {
        this(0, login, hashPassword, nom, prenom, telephone);
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getLogin() { return this.login; }

    public String getHashPassword() { return this.hashPassword; }

    public String getNom() {
        return nom;
    }

    public void setNom(String Nom) {
        this.nom = nom;
    }


    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String Prenom) {
        this.prenom = prenom;
    }

}