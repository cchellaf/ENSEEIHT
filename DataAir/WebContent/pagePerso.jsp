<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.*, entities.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Page Client</title>

<meta name="description" content="Description">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=2">
        
    <link rel="icon" href="assets/img/favicon/logo12.png" type="image/logo12"> 

    <link rel="stylesheet" href="assets/css/libs.css"> 
<link rel="stylesheet" href="assets/css/style.css">
    <link rel="preload" href="assets/fonts/istok-web-v15-latin/istok-web-v15-latin-regular.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preload" href="assets/fonts/istok-web-v15-latin/istok-web-v15-latin-700.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preload" href="assets/fonts/montserrat-v15-latin/montserrat-v15-latin-700.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preload" href="assets/fonts/montserrat-v15-latin/montserrat-v15-latin-600.woff2" as="font" type="font/woff2" crossorigin>

<link rel="preload" href="assets/fonts/material-icons/material-icons.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preload" href="assets/fonts/material-icons/material-icons-outlined.woff2" as="font" type="font/woff2" crossorigin>


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
        <li><a href="index.html" data-title="Accueil"><span>Deconnexion</span></a></li>
        <li><a href="about-us.html" data-title="A propos de nous"><span>A propos de nous</span></a></li>          
        <li><a href="contact-us.html" data-title="Contactez-Nous"><span>Contactez-Nous</span></a></li>
    </ul>
</nav><!-- End main menu -->
                </div>
            </div> 
        </div>
    </nav><!-- End header fixed -->
</header><!-- End header -->



<%

Client c = (Client) request.getAttribute("client");
%>

<div class="section">
	<div class="container">
		<div class="row content-items">
			<div class="col-12">
				<div class="section-heading">
					<div class="section-subheading">Information personnelle - Client</div>
							</div>
						</div>
						<div class="col-xl-4 col-md-5 content-item">
							<div class="contact-info section-bgc">
							
								<div class="signup-image" style="margin: 0 50px;" >
                 					<figure><img src="assets/img/favicon/logo12.png" alt="sing up image" width="200" height="200"></figure>
             					</div>
             					
             					
							</div>
						</div>
		

<div class="col-xl-4 col-sm-6 col-12">	
<p>			
<h6><%out.println("Nom : " + c.getNom() );%><br>
<%out.println("Prenom : " + c.getPrenom() );%><br>
<%out.println("Adresse Mail : " + c.getMail() );%><br>
<%out.println("Mot de passe : " + c.getMdp() );%><br>
<%out.println("Numero de telephone : " + c.getNum_telephone() );%><br>
<%out.println("Numero de passeport : " + c.getNum_passeport());%><br>
<%out.println("Date de naissance : " + c.getDate_naissance() );%><br>


</h6>
</p>
</div>

<div class="col-xl-4 col-sm-6 col-12">	
          <a href="changementMDP.jsp" class="btn btn-with-icon btn-small ripple">     <!-- Changer le lien -->
                     <span style="font-weight: bold">Modifier le mot de passe</span>
                     <svg class="btn-icon-right" viewBox="0 0 13 9" width="13" height="9"><use xlink:href="assets/img/sprite.svg#arrow-right"></use></svg>
                         </a><br>
                         <br>
             <a href="VueChoixVol.jsp" class="btn btn-with-icon btn-small ripple">     <!-- Changer le lien -->
           <span  style="font-weight: bold">Reserver un vol</span>
               <svg class="btn-icon-right" viewBox="0 0 13 9" width="13" height="9"><use xlink:href="assets/img/sprite.svg#arrow-right"></use></svg>
                  </a>
                  <br>
                  <br>
                  
            <a href="/DataAir/Main?op=voirMesVols" class="btn btn-with-icon btn-small ripple">     <!-- Changer le lien -->
           <span  style="font-weight: bold">Voir mes reservations</span>
               <svg class="btn-icon-right" viewBox="0 0 13 9" width="13" height="9"><use xlink:href="assets/img/sprite.svg#arrow-right"></use></svg>
                  </a>
</div>

</div>
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