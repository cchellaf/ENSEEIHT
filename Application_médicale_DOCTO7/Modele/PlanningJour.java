package Modele;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class PlanningJour {

    private LocalDate jour;

    private LocalTime heureDepart = LocalTime.parse("08:00");

    private int dureeCreneau = 30;

    private int creneaux[];

    public PlanningJour(LocalDate jour, int nbCreneaux) {
        this.jour = jour;
        this.creneaux = new int[nbCreneaux];
        for (int i = 0; i < this.creneaux.length; i++) {
            this.creneaux[i] = 0;
        }
    }

    private boolean estCreneauValide(int numeroCreneau) {
        return this.nbCreneaux() > 0
                && numeroCreneau >= 0
                && numeroCreneau < this.nbCreneaux();
    }

    public boolean estLibre(int numeroCreneau) {
        boolean libre = false;
        if (this.estCreneauValide(numeroCreneau)) {
            libre = this.creneaux[numeroCreneau] == 0;
        }
        return libre;
    }

    public void setOccupe(int numeroCreneau) {
        if (this.estCreneauValide(numeroCreneau)) {
            this.creneaux[numeroCreneau] = 1;
        }
    }

    public void setLibre(int numeroCreneau) {
        if (this.estCreneauValide(numeroCreneau)) {
            this.creneaux[numeroCreneau] = 0;
        }
    }

    public int getNumeroCreneau(Duration duration) {
        duration = duration.abs();
        long dureeMinutes = duration.toMinutes();
        return  (int) dureeMinutes / this.getDureeCreneau();

    }

    public LocalTime getHeureCreneau(int numeroCreneau) {
        return this.heureDepart.plusMinutes(numeroCreneau * dureeCreneau);
    }

    public LocalDate getJour() {
        return jour;
    }

    public void setJour(LocalDate jour) {
        this.jour = jour;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public int getDureeCreneau() {
        return dureeCreneau;
    }

    public void setDureeCreneau(int dureeCreneau) {
        this.dureeCreneau = dureeCreneau;
    }

    public int nbCreneaux() {
        return this.creneaux.length;
    }
}


