package IHM;

import Modele.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

public class PageSignUpPro extends JFrame {

    private Modele modele;

    final private JTextField loginTextField = new JTextField();
    final private JTextField passwordTextField = new JTextField();
    final private JTextField nomTextField = new JTextField();
    final private JTextField prenomTextField = new JTextField();
    final private JTextField telephoneTextField = new JTextField();

    final private JTextField specialiteTextField = new JTextField();
    final private JComboBox comboBoxEtablissements;
    final private JButton bEtablissement = new JButton("+");

    final private JButton bValider = new JButton("Valider");
    final private JButton bAnnuler = new JButton("Annuler");


    public PageSignUpPro(Modele modele) {
        super();
        this.modele = modele;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(7, 2));
        centerPanel.add(new JLabel("Login"));
        centerPanel.add(loginTextField);
        centerPanel.add(new JLabel("Password"));
        centerPanel.add(passwordTextField);
        centerPanel.add(new JLabel("Prenom"));
        centerPanel.add(prenomTextField);
        centerPanel.add(new JLabel("Nom"));
        centerPanel.add(nomTextField);
        centerPanel.add(new JLabel("Telephone"));
        centerPanel.add(telephoneTextField);

        centerPanel.add(new JLabel("Specialite"));
        centerPanel.add(specialiteTextField);

        centerPanel.add(new JLabel("Etablissement"));

        JPanel jPanelEtablissement = new JPanel(new BorderLayout());
        this.bEtablissement.addActionListener(new ActionEtablissement());
        jPanelEtablissement.add(this.bEtablissement, BorderLayout.WEST);

        java.util.List<String> listeNomsEtablissements = new ArrayList<>();
        try {
            listeNomsEtablissements = this.modele.getNomsEtablissements();

        } catch (SQLException exception) {
            exception.printStackTrace();
        };
        comboBoxEtablissements = new JComboBox(listeNomsEtablissements.toArray());
        jPanelEtablissement.add(comboBoxEtablissements, BorderLayout.CENTER);
        centerPanel.add(jPanelEtablissement);

        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(bValider);
        southPanel.add(bAnnuler);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.bValider.addActionListener(new ActionSignUp());
        this.bAnnuler.addActionListener(new ActionSignUp());

        this.pack();
        this.setTitle("Docto7");
        this.setVisible(true);
        this.setSize(new Dimension(500, 300));
        this.setLocation(500, 500);
    }

    private class ActionSignUp implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            JButton bouton = (JButton) evt.getSource();
            if (bouton == PageSignUpPro.this.bValider) {
                String login = PageSignUpPro.this.loginTextField.getText();
                String password = PageSignUpPro.this.passwordTextField.getText();
                String hashPassword = Securite.hashMD5(password);
                String nom = PageSignUpPro.this.nomTextField.getText();
                String prenom = PageSignUpPro.this.prenomTextField.getText();
                String telephone = PageSignUpPro.this.telephoneTextField.getText();
                String specialite = PageSignUpPro.this.specialiteTextField.getText();
                String nomEtablissement = (String) PageSignUpPro.this.comboBoxEtablissements.getSelectedItem();

                Etablissement etablissement = null;
                try {
                    etablissement = PageSignUpPro.this.modele.getEtablissement(nomEtablissement);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                Utilisateur utilisateur = new Medecin(login, hashPassword, nom, prenom, telephone,
                        specialite, etablissement);
                try {
                    PageSignUpPro.this.modele.ajouterUtilisateur(utilisateur);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

            new PageLogin(PageSignUpPro.this.modele);
            PageSignUpPro.this.dispose();
        }
    }

    private class ActionEtablissement implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            new PageEtablissement(PageSignUpPro.this.modele);
            PageSignUpPro.this.dispose();
        }
    }
}
