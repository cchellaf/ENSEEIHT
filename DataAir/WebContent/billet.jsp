<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import= "java.util.Collection" import="entities.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DataAir - Compagnie Aerienne</title>

    <meta name="description" content="Description">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=2">
        
    <link rel="icon" href="assets/img/favicon/logo12.png" type="image/logo12">
    <link rel="stylesheet" href="assets/css/libs.css">
<link rel="stylesheet" type="text/css" href="assets/css/style.css "/>
</head>
<body>

<main class="main">


<header class="header"> 
      
    <!-- Begin header fixed -->
    <nav class="header-fixed">
        <div class="container">
            <div class="row flex-nowrap align-items-center justify-content-between">

                <div class="col-auto header-fixed-col">
                    <!-- Begin logo -->
                    <a href="/" class="logo" title="DataAir">
                        <img src="assets/img/favicon/logo12.png" width="115" height="70" alt="DataAir">
                    </a><!-- End logo -->
                </div>
                <div class="col-auto header-fixed-col d-none d-lg-block col-static">
                    <!-- Begin main menu -->
<nav class="main-mnu">
    <ul class="main-mnu-list">
        <li><a href="index.html" data-title="Accueil"><span>Accueil</span></a></li>
        <li><a href="about-us.html" data-title="A propos de nous"><span>A propos de nous</span></a></li>
        <li><a href="inscription.html" data-title="Inscription"><span>Inscription</span></a></li>       <!-- Changer le lien -->
        <li><a href="connexion.html" data-title="Connexion"><span>Connexion</span></a></li>               <!-- Changer le lien -->
        <li><a href="contact-us.html" data-title="Contactez-Nous"><span>Contactez-Nous</span></a></li>
    </ul>
</nav>                    
<!-- End main menu -->
                </div>
            </div> 
        </div>
    </nav><!-- End header fixed -->
</header><!-- End header -->
<p> </p>
<div class="section-bgc intro">
    <div class="intro-slider">
        <div class="intro-item" style="background-image: url(assets/img/favicon/ticket.png) ;" > 
            <div class="container">
                <div class="row">
                    <div class="col">
                        <div class="intro-content">
                            <div class="section-heading shm-none">
                                <h1>Billet du Passager </h1> <br>
                                
                            </div>
<!--  -->                    <form method="get" action="Main" style="margin: 0 65px; text-align: justify;">
                             <% Client c = (Client) request.getAttribute("client");
	                             Vol v = (Vol) request.getAttribute("vol"); 
	                             out.println("<br>");
	                             out.println(" Le vol numero : " + v.getNum_vol() + "<br>");
	                             out.println(" Depart : " + v.getDepart() + "<br>");
	                             out.println(" Arrivee : " + v.getArrivee() + "<br>");
	                             out.println(" Date du vol : " + v.getDate() + "<br>");
		                         out.println(" Nom du passager : " + c.getNom() + "<br>"); 
		                         out.println(" Prenom du passager : " + c.getPrenom() + "<br>"); 
	                         %> 
	                         <br>
                             </form>
                             
                             <div class="section-heading heading-center">
								<p> Pour revenir a votre compte, <a href="/DataAir/Main?op=pagePerso"> cliquez ici. </a> </p>
							</div>
                             
                             </div>
                             </div>
                             </div>
                             </div>
                             </div>
                             </div>
                             </div>
                             
                             
 <footer class="footer">
    <div class="footer-main">
        <div class="container">
            <div class="row items">
                <div class="col-xl-3 col-lg-3 col-md-5 col-12 item">
                    <!-- Begin company info -->
                    <div class="footer-company-info">
                        <div class="footer-company-top">
                            <a href="/" class="logo" title="DataAir">
                                <img data-src="assets/img/favicon/logo12.png" width="150" height="150" class="lazy" src="data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=" alt="PathSoft">
                            </a>
                            <div class="footer-company-desc">
                                <p>DataAir offre ses meilleurs services aux clients. La securite des passagers est notre priorite. Nous vous attendons avec impatience a bord de nos avions.</p>
                            </div> 
                        </div>
                        
                    </div>
                    <!-- End company info -->
                </div>
                <div class="col-xl-2 offset-xl-1 col-md-3 col-5 col-lg-2 item">
                    <div class="footer-item">
                        <p class="footer-item-heading">Menu</p>
                        <nav class="footer-nav">
                            <ul class="footer-mnu">
                                <li><a href="index.html" class="hover-link" data-title="Accueil"><span>Accueil</span></a></li>
                                <li><a href="about-us.html" class="hover-link" data-title="A propos de nous"><span>A propos de nous</span></a></li>
                                <li><a href="/DataAir/Inscription?op=inscrire" class="hover-link" data-title="Inscription"><span>Inscription</span></a></li>
                                <li><a href="/DataAir/Connexion" data-title="Connexion"><span>Connexion</span></a></li>
                                <li><a href="contact-us.html" class="hover-link" data-title="Contactez-Nous"><span>Contactez-Nous</span></a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
                
                <div class="col-xs-4 col-lg-4 col-12 item">
                    <div class="footer-item">
                        <p class="footer-item-heading">Contact</p>
                        <ul class="footer-contacts">
                            <li>
                                <i class="material-icons md-22">location_on</i>
                                <div class="footer-contact-info">
                                     2 Rue Charles Camichel, 31000 Toulouse, FRANCE
                                </div>
                            </li>
                            <li>
                                <i class="material-icons md-22 footer-contact-tel">smartphone</i>
                                <div class="footer-contact-info">
                                    <a href="#!" class="formingHrefTel">+33 5 34 32 20 00</a>
                                </div>
                            </li>
                            <li>
                                <i class="material-icons md-22 footer-contact-email">email</i>
                                <div class="footer-contact-info">
                                    <a href="mailto:chaimae.chellafelhammoud@etu.inp-n7.fr">chaimae.chellafelhammoud@etu.inp-n7.fr</a>, <a href="mailto:aya.azhari@etu.toulouse-inp.fr">aya.azhari@etu.toulouse-inp.fr</a>, <a href="mailto:nezha.akouz@etu.toulouse-inp.fr">nezha.akouz@etu.toulouse-inp.fr</a>, <a href="mailto:meriem.boultam@etu.toulouse-inp.fr">meriem.boultam@etu.toulouse-inp.fr</a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</footer><!-- End footer -->

	</main><!-- End main -->

<script src="assets/libs/jquery/jquery.min.js"></script>
<script src="assets/libs/lozad/lozad.min.js"></script>
<script src="assets/libs/device/device.js"></script>
<script src="assets/libs/ScrollToFixed/jquery-scrolltofixed-min.js"></script>
<script src="assets/libs/spincrement/jquery.spincrement.min.js"></script>
<script src="assets/libs/jquery-validation-1.19.3/jquery.validate.min.js"></script>
<script src="assets/js/custom.js"></script>                              
</body>
</html>