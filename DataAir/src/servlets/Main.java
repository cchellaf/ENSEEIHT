package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.*;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private Facade facade;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		HttpSession session = request.getSession();
		
		if (op.contentEquals("inscrire")) {
			 String nom = request.getParameter("nom");
			 String prenom = request.getParameter("prenom");	
			 String mail = request.getParameter("mail");	
			 String mdp = request.getParameter("mdp");	
			 String remdp = request.getParameter("re-mdp");	
			 String num_telephone = request.getParameter("num_telephone");	
			 String num_passeport = request.getParameter("num_passeport");		
			 String date_naissance = request.getParameter("date_naissance");
			 if (remdp.contentEquals(mdp)) {
				facade.ajoutClient(nom, prenom, mail, mdp, num_telephone, num_passeport, date_naissance);
				request.getRequestDispatcher("index.html").forward(request, response);
			 } else {
				 request.getRequestDispatcher("PageErreurInscription.html").forward(request, response);
			 }
			
		}else if (op.contentEquals("connexion")) {
			String mail = request.getParameter("mail");	
			String mdp = request.getParameter("mdp");
			Collection <Utilisateur> c = facade.connexion(mail, mdp);
			if (c==null) {
				request.getRequestDispatcher("PageErreur.html").forward(request, response);
			} else {
				session.setAttribute("client", ((ArrayList ) c).get(0));
			    request.setAttribute("client", ((ArrayList ) c).get(0));
			    if(mail.endsWith("@dataair.com")) {
					request.getRequestDispatcher("pagePersoAdmin.jsp").forward(request, response);
				}else {
					request.getRequestDispatcher("pagePerso.jsp").forward(request, response);
				}
			}
			
		}else if (op.contentEquals("pagePerso")) {
			Client c = (Client) session.getAttribute("client");
			request.setAttribute("client", c);
			if(c.getMail().endsWith("@dataair.com")) {
				request.getRequestDispatcher("pagePersoAdmin.jsp").forward(request, response);
			}else {
				request.getRequestDispatcher("pagePerso.jsp").forward(request, response);
			}
			
		}else if (op.contentEquals("modifierMDP")) {
			Client c = (Client) session.getAttribute("client");
			String mdp = request.getParameter("mdp");
			int id = c.getId();
			facade.modifierMDP(id, mdp);
			c.setMdp(mdp);
			request.setAttribute("client", c);
			if(c.getMail().endsWith("@dataair.com")) {
				request.getRequestDispatcher("pagePersoAdmin.jsp").forward(request, response);
			}else {
				request.getRequestDispatcher("pagePerso.jsp").forward(request, response);
			}
			
		}else if(op.contentEquals("voirMesVols")) {
			Client c = (Client) session.getAttribute("client");
			//Collection<Vol> vols = c.getListe_vols();
			Collection<Vol> vols = facade.listeVolsClient(c.getId());
			if (vols.size()==0) {
				request.getRequestDispatcher("PageAucunVol.html").forward(request, response);
			}else {
				request.setAttribute("client", c);
				request.getRequestDispatcher("mesVols.jsp").forward(request, response);
			}
			
		}else if(op.contentEquals("AjouterVol")) {
			String num_vol = request.getParameter("num_vol");
			String depart = request.getParameter("depart");
			String arrivee = request.getParameter("arrivee");
			String date = request.getParameter("date");
			String heure = request.getParameter("heure");
			String place_dispo = request.getParameter("place_dispo");
			facade.ajoutVol(num_vol, depart, arrivee, date, heure, Integer.parseInt(place_dispo));
			Client c = (Client) session.getAttribute("client");
			request.setAttribute("client", c);
			request.getRequestDispatcher("pagePersoAdmin.jsp").forward(request, response);	
			
		}else if (op.contentEquals("choixVol")) {
			String date = request.getParameter("date");
			String arrivee = request.getParameter("arrivee");
			String depart = request.getParameter("depart");
			Collection<Vol> list = (Collection<Vol>) facade.listeVols_selection(depart,arrivee,date);
			if (list.size()==0) {
				request.getRequestDispatcher("PageAucunVolDate.html").forward(request, response);	
			}else {
				request.setAttribute("listeVols", list);
				request.getRequestDispatcher("ListerVols.jsp").forward(request,response);
			}
			
		}else if(op.contentEquals("reserver")){
			String idVol = request.getParameter("IdVol");
			Vol v = facade.trouverVol(Integer.parseInt(idVol));
			session.setAttribute("vol", v);
			request.setAttribute("vol", v);
			request.getRequestDispatcher("paiement.html").forward(request,response);
			
		}else if (op.contentEquals("payer")) {
			Client c = (Client) session.getAttribute("client");
			Vol vol = (Vol) session.getAttribute("vol");
			int a = facade.reserver(c.getId(), vol.getId());
			if (a==1) {
				request.getRequestDispatcher("confirmation.html").forward(request,response);
			}else {
				request.getRequestDispatcher("PageVolPlein.html").forward(request, response);
			}
			
		}else if (op.contentEquals("billet")) {
			Client c = (Client) session.getAttribute("client");
			Vol vol = (Vol) session.getAttribute("vol");
			request.setAttribute("client", c);
			request.setAttribute("vol", vol);
			request.getRequestDispatcher("billet.jsp").forward(request, response);
			
		}else {
			request.getRequestDispatcher("index.html").forward(request, response);
		}
		
		
	}

}