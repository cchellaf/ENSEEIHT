package Modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVous {

    private LocalDate jour;

    private LocalTime heure;

    private Medecin medecin;

    private Patient patient;

    private String bilan;

    public RendezVous(LocalDate jour,
                      LocalTime heure,
                      Medecin medecin,
                      Patient patient,
                      String bilan) {
        this.jour = jour;
        this.heure = heure;
        this.medecin = medecin;
        this.patient = patient;
        this.bilan = bilan;
    }

    public RendezVous(LocalDate jour,
                      LocalTime heure,
                      Medecin medecin,
                      Patient patient) {
        this(jour, heure, medecin, patient, "");
    }

    public LocalDate getJour() {
        return jour;
    }

    public void setJour(LocalDate jour) {
        this.jour = jour;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    @Override
    public String toString() {
        String string = this.jour + "-"
                + this.heure + "-"
                + this.medecin.getNom() + "-"
                + this.patient.getNom();
        return string;
    }
}
