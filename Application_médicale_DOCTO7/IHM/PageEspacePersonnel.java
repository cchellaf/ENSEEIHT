package IHM;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class PageEspacePersonnel extends JFrame {

    private Utilisateur utilisateur;

    private Modele modele;

    private java.util.List<RendezVous> listeRendezVous = null;

    final private JButton bNouveauRendezVous = new JButton("Nouveau Rendez-Vous");

    final private JButton bQuitter = new JButton("Quitter");

    public PageEspacePersonnel(Utilisateur utilisateur, Modele modele) {
        super();
        this.utilisateur = utilisateur;
        this.modele = modele;
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(new JLabel("                                  " +
                "Aucun rendez-vous"), BorderLayout.CENTER);
        this.getContentPane().add(bNouveauRendezVous, BorderLayout.NORTH);

        try {
            listeRendezVous = this.modele.getRendezVous(this.utilisateur);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        if (listeRendezVous != null && listeRendezVous.size() > 0) {
            JPanel innerCenterPanel = new JPanel();
            innerCenterPanel.setLayout(new GridLayout(listeRendezVous.size(), 1));

            for (int i = 0; i < listeRendezVous.size(); i++) {
                RendezVous rendezVous = listeRendezVous.get(i);
                LocalDate jourRdv = rendezVous.getJour();
                LocalTime heureRdv = rendezVous.getHeure();
                Medecin medecin = rendezVous.getMedecin();
                String bilan = rendezVous.getBilan();
                String prenomNom = medecin.getPrenom() + " " + medecin.getNom();
                String specialite = medecin.getSpecialite();

                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());

                JTextArea textArea = new JTextArea(20, 35);
                //textArea.setSize(new Dimension(30, 100));

                textArea.append("                       " +
                        "                " + jourRdv + "\n\n");
                textArea.append("                       " +
                        "                " + heureRdv + "\n\n");
                textArea.append("                       " +
                        "                " + specialite + "\n\n");
                textArea.append("                       " +
                        "                " + prenomNom + "\n\n");

                textArea.append("                       " +
                        "                Plan de soin :\n\n");

                String[] lignes = bilan.split("\n");
                for (String ligne : lignes) {
                    textArea.append("                       " +
                            "                " + ligne + "\n");
                }

                textArea.append("\n---------------------------" +
                        "---------------------------------------------\n");
                textArea.setEditable(false);

                jPanel.add(textArea, BorderLayout.CENTER);

                innerCenterPanel.add(jPanel);
            }

            JScrollPane centerPanel = new JScrollPane(innerCenterPanel);
            this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        }

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 1));
        //southPanel.add(bNouveauRendezVous);
        southPanel.add(bQuitter);
        bNouveauRendezVous.addActionListener(new ActionRechercher());
        bQuitter.addActionListener(new ActionQuitter());
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.pack();
        this.setTitle("Espace Personnel de " +
                utilisateur.getPrenom() + " " +
                utilisateur.getNom());
        this.setVisible(true);
        this.setSize(new Dimension(350, 300));
        this.setLocation(500, 500);
    }

    private class ActionQuitter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new PageLogin(PageEspacePersonnel.this.modele);
            PageEspacePersonnel.this.dispose();
        }
    }

    private class ActionRechercher implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Utilisateur utilisateur = PageEspacePersonnel.this.utilisateur;
            Modele modele = PageEspacePersonnel.this.modele;
            new PageRecherche(utilisateur, modele);
            PageEspacePersonnel.this.dispose();
        }
    }

}
