package IHM;

import Modele.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

public class PageLogin extends JFrame {

    private Modele modele;

    final private JTextField loginTextField = new JTextField();
    final private JPasswordField passwordTextField = new JPasswordField();
    final private JButton bCreate = new JButton("Créer un compte");
    final private JButton bLogin = new JButton("Se connecter");
    final private JToggleButton bStatut = new JToggleButton("Particulier");


    public PageLogin(Modele modele) {
        super();
        this.modele = modele;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2));
        centerPanel.add(new JLabel("Login"));
        centerPanel.add(loginTextField);
        centerPanel.add(new JLabel("Password"));
        passwordTextField.setEchoChar('*');
        centerPanel.add(passwordTextField);
        centerPanel.add(new JLabel("Statut"));
        centerPanel.add(bStatut);
        bStatut.addItemListener(new ActionStatut());
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(bLogin);
        southPanel.add(bCreate);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);

        this.bCreate.addActionListener(new ActionCreer());
        this.bLogin.addActionListener(new ActionLogin());

        this.pack();
        this.setTitle("Docto7");
        this.setVisible(true);
        this.setSize(new Dimension(300, 300));
        this.setLocation(500, 500);

    }

    private class ActionLogin implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            String login = PageLogin.this.loginTextField.getText();
            String password = PageLogin.this.passwordTextField.getText();
            String hashPassword = Securite.hashMD5(password);
            boolean statutPro = PageLogin.this.bStatut.isSelected();
            Utilisateur utilisateur = null;
            try {
                if (statutPro) {
                    utilisateur = PageLogin.this.modele.getMedecin(login, hashPassword);
                } else {
                    utilisateur = PageLogin.this.modele.getPatient(login, hashPassword);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            if (utilisateur != null && !statutPro) {
                new PageEspacePersonnel(utilisateur, PageLogin.this.modele);
                PageLogin.this.dispose();
            } else if (utilisateur != null && statutPro) {
                try {
                    new PagePlanningMedecin((Medecin) utilisateur, PageLogin.this.modele, LocalDate.now());
                    PageLogin.this.dispose();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } else {
                System.out.println("login ou mot de passe invalide");
            }
        }
    }

    private class ActionCreer implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            boolean statutPro = PageLogin.this.bStatut.isSelected();
            if (statutPro) {
                new PageSignUpPro(PageLogin.this.modele);
            } else {
                new PageSignUp(PageLogin.this.modele);
            }
            PageLogin.this.dispose();
        }
    }

    private class ActionStatut implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JToggleButton bouton = (JToggleButton) e.getItem();
            if (bouton.isSelected()) {
                bouton.setText("Professionnel");
            } else {
                bouton.setText("Particulier");
            }
        }
    }
}
