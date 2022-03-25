package IHM;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

public class PageRendezVous extends JFrame {

    private RendezVous rendezVous;

    private Modele modele;

    final private JTextArea bilanTextField = new JTextArea(5, 69);

    final private JButton bRetour = new JButton("Retour");

    final private JButton bValider = new JButton("Valider");

    public PageRendezVous(RendezVous rendezVous, Modele modele) {
        super();
        this.rendezVous = rendezVous;
        this.modele = modele;

        this.getContentPane().setLayout(new BorderLayout());

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(1 ,2));
        bottomPanel.add(bRetour);
        bottomPanel.add(bValider);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        bRetour.addActionListener(new ActionBouton());
        bValider.addActionListener(new ActionBouton());

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        // Infos patient
        Patient patient = rendezVous.getPatient();
        JPanel panelPatient = new JPanel(new GridLayout(5, 1));
        String nom = patient.getNom();
        String prenom = patient.getPrenom();
        panelPatient.add(new JLabel("Patient(e) : " + prenom + " " + nom));
        String secu = patient.getSecuriteSociale();
        panelPatient.add(new JLabel("Securite sociale : " + secu));
        int age = patient.getAge();
        panelPatient.add(new JLabel("Age : " + age + " ans"));
        String taille = patient.getTaille();
        panelPatient.add(new JLabel("Taille : " + taille + " cm"));
        String poids = patient.getPoids();
        panelPatient.add(new JLabel("Poids : " + poids + " Kg"));

        centerPanel.add(panelPatient);

        // Saisies medecin
        JPanel panelMedecin = new JPanel(new GridLayout(3, 1));

        JPanel panelMotif = new JPanel(new BorderLayout());
        panelMotif.add(new JLabel("Motif du rendez-vous"), BorderLayout.NORTH);
        panelMotif.add(new JTextArea(), BorderLayout.CENTER);
        panelMedecin.add(panelMotif);

        JPanel panelExamen = new JPanel(new BorderLayout());
        panelExamen.add(new JLabel("Examen clinique"), BorderLayout.NORTH);
        panelExamen.add(new JTextArea(), BorderLayout.CENTER);
        panelMedecin.add(panelExamen);

        JPanel panelPlanDeSoins = new JPanel(new BorderLayout());
        panelPlanDeSoins.add(new JLabel("Plan de soins"), BorderLayout.NORTH);
        panelPlanDeSoins.add(bilanTextField, BorderLayout.CENTER);
        panelMedecin.add(panelPlanDeSoins);

        centerPanel.add(panelMedecin);

        this.pack();
        this.setTitle("Rendez-vous de " +
                rendezVous.getHeure() +
                " du " +
                rendezVous.getJour());
        this.setVisible(true);
        this.setSize(new Dimension(450, 300));
        this.setLocation(500, 500);
    }

    private class ActionBouton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            RendezVous rendezVous = PageRendezVous.this.rendezVous;
            Medecin medecin = rendezVous.getMedecin();
            LocalDate jour = rendezVous.getJour();
            Modele modele = PageRendezVous.this.modele;
            try {
                if (button.getText().equals("Valider")) {
                    String bilan = PageRendezVous.this.bilanTextField.getText();
                    rendezVous.setBilan(bilan);
                    PageRendezVous.this.modele.terminerRendezVous(rendezVous);
                }
                new PagePlanningMedecin(medecin, modele, jour);
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                PageRendezVous.this.dispose();
            }

        }
    }

}
