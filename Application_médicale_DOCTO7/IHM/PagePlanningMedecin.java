package IHM;

import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PagePlanningMedecin extends JFrame {

    private Medecin medecin;

    private List<Patient> patientsDuJour = new ArrayList<>();

    private Modele modele;

    private PlanningJour planningJour;

    private ButtonGroup buttonGroup;

    final private JButton bDate;

    final private JButton bDateSuivante;

    final private JButton bDatePrecedente;

    final private JButton bRendezVous = new JButton("Initier Rendez-Vous");

    final private JButton bQuitter = new JButton("Quitter");

    private JDialog jDialog;

    final private JButton bValiderDialog = new JButton("Valider");

    final private JTextField textFieldDialog = new JTextField();



    public PagePlanningMedecin(Medecin medecin, Modele modele, LocalDate jour) throws SQLException {
        super();
        this.medecin = medecin;
        this.modele = modele;
        this.planningJour = this.modele.getPlanningJour(this.medecin, jour);

        this.bValiderDialog.addActionListener(new ActionValiderDate());
        this.textFieldDialog.setText(jour.toString());

        this.getContentPane().setLayout(new BorderLayout());
        this.bDate = new JButton(jour.toString());
        this.bDate.addActionListener(new ActionDate());

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


        this.getContentPane().add(jPanelNorth, BorderLayout.NORTH);
        bRendezVous.addActionListener(new PagePlanningMedecin.ActionRendezVous());
        bRendezVous.setEnabled(false);

        if (planningJour.nbCreneaux() > 0) {
            JPanel innerCenterPanel = new JPanel();
            innerCenterPanel.setLayout(new GridLayout(planningJour.nbCreneaux(), 1));

            LocalDate dateRdv = jour;
            List<RendezVous> listeRdv = this.modele.getRendezVous(medecin, dateRdv);
            int indiceRdv = 0;

            buttonGroup = new ButtonGroup();
            for (int i = 0; i < planningJour.nbCreneaux(); i++) {

                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());

                JTextArea textArea = new JTextArea(6, 5);
                textArea.setSize(new Dimension(30, 200));

                JRadioButton jRadioButton = new JRadioButton();
                jRadioButton.setEnabled(false);
                String statutCreneau = "LIBRE";
                if (!planningJour.estLibre(i)) {
                    jRadioButton.setActionCommand(i + " " + indiceRdv);
                    Patient patient = listeRdv.get(indiceRdv++).getPatient();
                    patientsDuJour.add(patient);
                    statutCreneau = patient.getNom() + " " + patient.getPrenom();
                    jRadioButton.setEnabled(true);
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


                jRadioButton.addActionListener(new PagePlanningMedecin.ActionBouton());
                jPanel.add(jRadioButton, BorderLayout.EAST);
                buttonGroup.add(jRadioButton);

                innerCenterPanel.add(jPanel);
            }

            JScrollPane centerPanel = new JScrollPane(innerCenterPanel);
            this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        }

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 1));
        southPanel.add(bQuitter);
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

    private class ActionDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            PagePlanningMedecin.this.jDialog = new JDialog();
            jDialog.getContentPane().setLayout(new BorderLayout());
            jDialog.getContentPane().add(new JLabel("Date au format YYYY-MM-JJ: "), BorderLayout.NORTH);
            jDialog.getContentPane().add(PagePlanningMedecin.this.textFieldDialog, BorderLayout.CENTER);
            jDialog.getContentPane().add(PagePlanningMedecin.this.bValiderDialog, BorderLayout.SOUTH);

            jDialog.pack();
            jDialog.setVisible(true);
            jDialog.setSize(new Dimension(300, 150));
            jDialog.setLocation(500, 500);

        }
    }

    private class ActionValiderDate implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            LocalDate jour = PagePlanningMedecin.this.planningJour.getJour();

            JButton jButtonSource = (JButton) e.getSource();
            if (jButtonSource == PagePlanningMedecin.this.bDatePrecedente) {
                jour = jour.minusDays(1);
            } else if (jButtonSource == PagePlanningMedecin.this.bDateSuivante) {
                jour = jour.plusDays(1);
            } else {
                // ATTENTION : BOUTON ACTIF MEME SI CHAMP DATE VIDE
                String stringJour = PagePlanningMedecin.this.textFieldDialog.getText();
                jour = LocalDate.parse(stringJour);
            }

            try {
                Medecin medecin = PagePlanningMedecin.this.medecin;
                Modele modele = PagePlanningMedecin.this.modele;
                new PagePlanningMedecin(medecin, modele, jour);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            PagePlanningMedecin.this.dispose();
            if (PagePlanningMedecin.this.jDialog != null) {
                PagePlanningMedecin.this.jDialog.dispose();
                PagePlanningMedecin.this.jDialog = null; // Utile ?
            }

        }
    }

    private class ActionBouton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PagePlanningMedecin.this.bRendezVous.setEnabled(true);
        }
    }

    private class ActionRendezVous implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ButtonModel buttonModel = buttonGroup.getSelection();
            String[] stringIndices = buttonModel.getActionCommand().split(" ");
            int indiceCreneau = Integer.valueOf(stringIndices[0]);
            int indicePatient = Integer.valueOf(stringIndices[1]);

            Patient patient = PagePlanningMedecin.this.patientsDuJour.get(indicePatient);
            LocalTime heureRdv = PagePlanningMedecin.this.planningJour.getHeureCreneau(indiceCreneau);
            LocalDate dateRdv = PagePlanningMedecin.this.planningJour.getJour();
            RendezVous rendezVous = new RendezVous(dateRdv, heureRdv, medecin, patient);

            new PageRendezVous(rendezVous, PagePlanningMedecin.this.modele);
            PagePlanningMedecin.this.dispose();
        }
    }

    private class ActionQuitter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new PageLogin(PagePlanningMedecin.this.modele);
            PagePlanningMedecin.this.dispose();
        }
    }

}
