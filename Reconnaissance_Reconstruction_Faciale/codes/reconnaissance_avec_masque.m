clear;
close all;

load eigenfaces;

% Tirage aleatoire d'une image de test :
personne = randi(nb_personnes)
posture = randi(nb_postures)
% si on veut tester/mettre au point, on fixe l'individu
%personne = 1
%posture = 3

ficF = strcat('./Data/', liste_personnes{personne}, liste_postures{posture}, '-300x400.gif')
img = imread(ficF);
% Degradation de l'image
img(ligne_min:ligne_max,colonne_min:colonne_max) = 0;
image_test = double(transpose(img(:)));

% Pourcentage d'information 
per = 0.95;

% Nombre N de composantes principales a prendre en compte 
% [dans un second temps, N peut etre calcule pour atteindre le pourcentage
% d'information avec N valeurs propres (contraste)] :
N = 1; 
somme_vp = sum(D_masque); 
Contraste_avec_masque = 0;
while (Contraste_avec_masque < per) 
    N = N + 1; 
    somme_vN = sum(D_masque(1:N));
    Contraste_avec_masque = somme_vN/somme_vp;
end 

% Composantes principales des donnees d'apprentissage
C = X_centre_masque*W_masque;	
C2 = C(:, 1:N);

C_test = (image_test-individu_moyen_masque)*W_masque;
C_test = C_test(:, 1:N);

DataA = C2;
DataT = C_test;
labelA = repmat(1:nb_personnes_base, nb_postures_base, 1);
labelA = labelA(:)';
ListeClass = 1:nb_personnes_base;
K = 1;
Nt_test = 1;

% Reconnaissance de la personne
reconnaissance_pers = kppv(DataA, labelA, DataT, Nt_test, K, ListeClass)

% Reconnaissance de la posture
labelA = repmat(1:nb_postures_base, nb_personnes_base, 1)';
labelA = labelA(:)';
ListeClass = 1:nb_postures_base; 
reconnaissance_post = kppv(DataA, labelA, DataT, Nt_test, K, ListeClass)

% pour l'affichage (A CHANGER)
personne_proche = reconnaissance_pers;
posture_proche = reconnaissance_post;

figure('Name','Image tiree aleatoirement','Position',[0.2*L,0.2*H,0.8*L,0.5*H]);

subplot(1, 2, 1);
% Affichage de l'image de test :
colormap gray;
imagesc(img);
title({['Individu de test : posture ' num2str(posture) ' de ', liste_personnes{personne}]}, 'FontSize', 20);
axis image;


ficF = strcat('./Data/', liste_personnes_base{personne_proche}, liste_postures{posture_proche}, '-300x400.gif')
img = imread(ficF);
% Degradation de l'image
img(ligne_min:ligne_max,colonne_min:colonne_max) = 0;
        
subplot(1, 2, 2);
imagesc(img);
title({['Individu la plus proche : posture ' num2str(posture_proche) ' de ', liste_personnes_base{personne_proche}]}, 'FontSize', 20);
axis image;
