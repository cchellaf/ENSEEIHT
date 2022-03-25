package IHM;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

public class PageResultat extends JFrame {

    private Utilisateur utilisateur;

    private Modele modele;

    private java.util.List<Medecin> medecins;

    private ButtonGroup buttonGroup;

    final private JButton bRetour = new JButton("Retour");

    final private JButton bQuitter = new JButton("Quitter");

    final private JButton bConsulter = new JButton("Consulter Disponibilités");



    public PageResultat(Utilisateur utilisateur, Modele modele, String specialite) {
        super();
        this.utilisateur = utilisateur;
        this.modele = modele;
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(bConsulter, BorderLayout.NORTH);
        bConsulter.addActionListener(new ActionConsulter());
        bConsulter.setEnabled(false);
        this.getContentPane().add(new JLabel("Aucun resultat"), BorderLayout.CENTER);

        medecins = this.modele.getMedecins(specialite);
        if (medecins.size() > 0) {
            JPanel innerCenterPanel = new JPanel();
            innerCenterPanel.setLayout(new GridLayout(medecins.size(), 1));

            buttonGroup = new ButtonGroup();
            for (int i = 0; i < medecins.size(); i++) {
                Medecin medecin = medecins.get(i);

                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());

                JTextArea textArea = new JTextArea(6, 5);
                textArea.setSize(new Dimension(100, 200));
                textArea.append(medecin.getPrenom() + " " + medecin.getNom() + "\n");
                textArea.append(medecin.getSpecialite() + "\n");
                textArea.append(medecin.getEtablissement().getAdresseComplete() + "\n");
                textArea.append("----------------------------------------------------------------\n");
                textArea.setEditable(false);

                jPanel.add(textArea, BorderLayout.CENTER);

                JRadioButton jRadioButton = new JRadioButton();
                jRadioButton.setActionCommand(String.valueOf(i));
                jRadioButton.addActionListener(new ActionBouton());
                jPanel.add(jRadioButton, BorderLayout.EAST);
                buttonGroup.add(jRadioButton);

                innerCenterPanel.add(jPanel);
            }

            JScrollPane centerPanel = new JScrollPane(innerCenterPanel);
            this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        }

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));
        southPanel.add(bRetour);
        southPanel.add(bQuitter);
        bRetour.addActionListener(new ActionRetour());
        bQuitter.addActionListener(new ActionQuitter());
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.pack();
        this.setTitle("Résultats de la Recherche");
        this.setVisible(true);
        this.setSize(new Dimension(350, 300));
        this.setLocation(500, 500);
    }

    private class ActionQuitter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new PageLogin(PageResultat.this.modele);
            PageResultat.this.dispose();
        }
    }

    private class ActionRetour implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Utilisateur utilisateur = PageResultat.this.utilisateur;
            Modele modele = PageResultat.this.modele;
            new PageRecherche(utilisateur, modele);
            PageResultat.this.dispose();
        }
    }

    private class ActionConsulter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ButtonModel buttonModel = buttonGroup.getSelection();
            int indice = Integer.valueOf(buttonModel.getActionCommand());
            Medecin medecin = medecins.get(indice);

            // Lancer PageAgenda du medecin
            try {
                Patient patient = (Patient) PageResultat.this.utilisateur;
                Modele modele = PageResultat.this.modele;
                new PagePlanningPatient(patient, medecin, modele, LocalDate.now());
                PageResultat.this.dispose();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
    }

    private class ActionBouton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PageResultat.this.bConsulter.setEnabled(true);
        }
    }

}
