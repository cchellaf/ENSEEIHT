package Modele;

public class Etablissement {

    private int id;

    private String nom;

    private String adresse;

    private String codePostal;

    private String ville;

    public Etablissement(int id, String nom, String adresse, String codePostal, String ville) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Etablissement(String nom, String adresse, String codePostal, String ville) {
        this(0, nom, adresse, codePostal, ville);
    }

    public String getAdresseComplete() {
        return this.nom + "\n"
                + this.adresse + "\n"
                + this.codePostal + " "
                + this.ville;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getNom() { return this.nom; }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || ! (o instanceof Etablissement)) {
            return false;
        } else {
            Etablissement e = (Etablissement) o;
            return e.nom.equals(this.nom)
                    && e.adresse.equals(this.adresse)
                    && e.codePostal.equals(this.codePostal)
                    && e.ville.equals(this.ville);
        }
    }
}
