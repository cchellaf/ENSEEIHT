package IHM;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class PagePlanningPatient extends JFrame {

    private Patient patient;

    private Medecin medecin;

    private Modele modele;

    private PlanningJour planningJour;

    private ButtonGroup buttonGroup;

    final private JButton bRetour = new JButton("Retour");

    final private JButton bQuitter = new JButton("Quitter");

    final private JButton bRendezVous = new JButton("Prendre Rendez-Vous");

    final private JButton bDate;

    final private JButton bDateSuivante;

    final private JButton bDatePrecedente;

    private JDialog jDialog;

    final private JButton bValiderDialog = new JButton("Valider");

    final private JTextField textFieldDialog = new JTextField();



    public PagePlanningPatient(Patient patient, Medecin medecin, Modele modele, LocalDate jour) throws SQLException {
        super();
        this.patient = patient;
        this.medecin = medecin;
        this.modele = modele;

        this.bValiderDialog.addActionListener(new ActionValiderDate());
        this.textFieldDialog.setText(jour.toString());

        this.bDate = new JButton(jour.toString());
        bDate.addActionListener(new ActionDate());

        this.bDateSuivante = new JButton(">>");
        this.bDateSuivante.addActionListener(new ActionValiderDate());
        this.bDatePrecedente = new JButton("<<");
        this.bDatePrecedente.addActionListener(new ActionValiderDate());

        JPanel jPanelNorthTop = new JPanel(new BorderLayout());
        jPanelNorthTop.add(bDate, BorderLayout.CENTER);
        jPanelNorthTop.add(bDatePrecedente, BorderLayout.WEST);
        jPanelNorthTop.add(bDateSuivante, BorderLayout.EAST);

        JPanel jPanelNorth = new JPanel(new GridLayout(2, 1));
        jPanelNorth.add(jPanelNorthTop);
        jPanelNorth.add(bRendezVous);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(jPanelNorth, BorderLayout.NORTH);
        bRendezVous.addActionListener(new ActionRendezVous());
        bRendezVous.setEnabled(false);
        this.getContentPane().add(new JLabel("Aucun resultat"), BorderLayout.CENTER);

        this.planningJour = this.modele.getPlanningJour(this.medecin, jour);

        if (planningJour.nbCreneaux() > 0) {
            JPanel innerCenterPanel = new JPanel();
            innerCenterPanel.setLayout(new GridLayout(planningJour.nbCreneaux(), 1));

            buttonGroup = new ButtonGroup();
            for (int i = 0; i < planningJour.nbCreneaux(); i++) {

                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());

                JTextArea textArea = new JTextArea(6, 5);
                textArea.setSize(new Dimension(30, 200));

                JRadioButton jRadioButton = new JRadioButton();
                String statutCreneau = "LIBRE";
                if (!planningJour.estLibre(i)) {
                    statutCreneau = "OCCUPE";
                    jRadioButton.setEnabled(false);
                }
                LocalTime heureCreneau = planningJour.getHeureCreneau(i);
                textArea.append("                       " +
                        "                " + heureCreneau + "\n\n");
                textArea.append("                    " +
                        "                   " + statutCreneau + "\n\n");
                textArea.append("---------------------------" +
                        "---------------------------------------------\n");
                textArea.setEditable(false);

                jPanel.add(textArea, BorderLayout.CENTER);

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
        this.setTitle("Planning du Dr " +
                medecin.getPrenom() + " " +
                medecin.getNom());
        this.setVisible(true);
        this.setSize(new Dimension(350, 300));
        this.setLocation(500, 500);
    }

    private class ActionQuitter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new PageLogin(PagePlanningPatient.this.modele);
            PagePlanningPatient.this.dispose();
        }
    }

    private class ActionRetour implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Utilisateur utilisateur = PagePlanningPatient.this.patient;
            Modele modele = PagePlanningPatient.this.modele;
            new PageRecherche(utilisateur, modele);
            PagePlanningPatient.this.dispose();
        }
    }

    private class ActionRendezVous implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ButtonModel buttonModel = buttonGroup.getSelection();
            int indice = Integer.valueOf(buttonModel.getActionCommand());
            LocalTime heureRdv = PagePlanningPatient.this.planningJour.getHeureCreneau(indice);

            LocalDate dateRdv = PagePlanningPatient.this.planningJour.getJour();
            RendezVous rendezVous = new RendezVous(dateRdv, heureRdv, medecin, patient);
            try {
                PagePlanningPatient.this.modele.ajouterRendezVous(rendezVous);
                new PageEspacePersonnel(patient, PagePlanningPatient.this.modele);
                PagePlanningPatient.this.dispose();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
    }

    private class ActionBouton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PagePlanningPatient.this.bRendezVous.setEnabled(true);
        }
    }

    private class ActionDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            PagePlanningPatient.this.jDialog = new JDialog();
            jDialog.getContentPane().setLayout(new BorderLayout());
            jDialog.getContentPane().add(new JLabel("Date au format YYYY-MM-JJ: "), BorderLayout.NORTH);
            jDialog.getContentPane().add(PagePlanningPatient.this.textFieldDialog, BorderLayout.CENTER);
            jDialog.getContentPane().add(PagePlanningPatient.this.bValiderDialog, BorderLayout.SOUTH);

            jDialog.pack();
            jDialog.setVisible(true);
            jDialog.setSize(new Dimension(300, 150));
            jDialog.setLocation(500, 500);

        }
    }

    private class ActionValiderDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            LocalDate jour = PagePlanningPatient.this.planningJour.getJour();

            JButton jButtonSource = (JButton) e.getSource();
            if (jButtonSource == PagePlanningPatient.this.bDatePrecedente) {
                jour = jour.minusDays(1);
            } else if (jButtonSource == PagePlanningPatient.this.bDateSuivante) {
                jour = jour.plusDays(1);
            } else {
                // ATTENTION : BOUTON ACTIF MEME SI CHAMP DATE VIDE
                String stringJour = PagePlanningPatient.this.textFieldDialog.getText();
                jour = LocalDate.parse(stringJour);
            }

            try {
                Patient patient  = PagePlanningPatient.this.patient;
                Medecin medecin = PagePlanningPatient.this.medecin;
                Modele modele = PagePlanningPatient.this.modele;
                new PagePlanningPatient(patient, medecin, modele, jour);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            PagePlanningPatient.this.dispose();
            if (PagePlanningPatient.this.jDialog != null) {
                PagePlanningPatient.this.jDialog.dispose();
                PagePlanningPatient.this.jDialog = null; // Utile ?
            }

        }
    }


}
