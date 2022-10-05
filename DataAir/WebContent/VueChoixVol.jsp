<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ page import="java.util.*, entities.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>choix vol</title>
<link rel="stylesheet" href="assets/css/libs.css">
<link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

<main class="main">
		
		<div class="main-inner">

<!-- Begin header -->
 <header class="header"> 
    
     
    <!-- Begin header fixed -->
    <nav class="header-fixed">
        <div class="container">
            <div class="row flex-nowrap align-items-center justify-content-between">
                <div class="col-auto d-block d-lg-none header-fixed-col">
                    <div class="main-mnu-btn">
                        <span class="bar bar-1"></span>
                        <span class="bar bar-2"></span>
                        <span class="bar bar-3"></span>
                        <span class="bar bar-4"></span>
                    </div>
                </div>
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
</nav><!-- End main menu -->
                </div>
            </div> 
        </div>
    </nav><!-- End header fixed -->
</header><!-- End header -->


			<!-- Begin bread crumbs -->
			<nav class="bread-crumbs">
				<div class="container">
					<div class="row">
						<div class="col-12">
							<ul class="bread-crumbs-list">
								<li>
									<a href="index.html">Accueil</a>
									<i class="material-icons md-18">chevron_right</i>
								</li>
								<li><a href="#!">ChoixVol</a></li>
							</ul>
						</div>
					</div>
				</div>
			</nav><!-- End bread crumbs -->

			<div class="section">
			
			<div  style="width:31%; margin: 0px auto;font-size: 22px;">
			
<form method="get" action="Main" >
depart<input type="text" name="depart"><br/>
arrivee<input type="text" name="arrivee"><br/>
date<input type="text" name="date"><br/>

<input style="	padding: 5px 17px;font-size: 20px;
background-color: #87CEEB;
color: #282727;
border: none;
border-radius: 3px;" type="submit" name="op"  value="choixVol">

</form>

</div>
		</div>
		
<div class="section-heading heading-center">
	<p> Pour revenir a votre compte, <a href="/DataAir/Main?op=pagePerso"> cliquez ici. </a> </p>
</div>

	</div>




<!-- Begin footer -->
<footer class="footer">
    <div class="footer-main">
        <div class="container">
            <div class="row items">
                <div class="col-xl-3 col-lg-3 col-md-5 col-12 item">
                    <!-- Begin company info -->
                    <div class="footer-company-info">
                        <div class="footer-company-top">
                            <a href="/" class="logo" title="DataAir">
                                <img data-src="assets/img/logo12.png" width="150" height="150" class="lazy" src="data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=" alt="PathSoft">
                            </a>
                            <div class="footer-company-desc" style="text-align: justify">
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
                                <li><a href="inscription.html" class="hover-link" data-title="Inscription"><span>Inscription</span></a></li>
                                <li><a href="connexion.html" data-title="Connexion"><span>Connexion</span></a></li>
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