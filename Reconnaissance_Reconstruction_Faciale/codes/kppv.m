%--------------------------------------------------------------------------
% ENSEEIHT - 1SN - Analyse de donnees
% TP4 - Reconnaissance de chiffres manuscrits par k plus proches voisins
% fonction kppv.m
%
% Données :
% DataA      : les données d'apprentissage (connues)
% LabelA     : les labels des données d'apprentissage
%
% DataT      : les données de test (on veut trouver leur label)
% Nt_test    : nombre de données tests qu'on veut labelliser
%
% K          : le K de l'algorithme des k-plus-proches-voisins
% ListeClass : les classes possibles (== les labels possibles)
%
% Résultat :
% Partition : pour les Nt_test données de test, le label calculé
%
%--------------------------------------------------------------------------
function Partition= kppv(DataA, labelA, DataT, Nt_test, K, ListeClass)

[Na,~] = size(DataA);

% Initialisation du vecteur d'étiquetage des images tests
Partition = zeros(Nt_test,1);

% Boucle sur les vecteurs test de l'ensemble de l'évaluation
for i = 1:Nt_test   

    % Calcul des distances entre les vecteurs de test 
    % et les vecteurs d'apprentissage (voisins)
    distances = sqrt(sum((repmat(DataT(i,:), Na, 1) - DataA).^2, 2));
    
    % On ne garde que les indices des K + proches voisins
    % Tri des distances
    [~, indices] = sort(distances);
    % Tri des labels des données d'apprentissage
    labels_tries = labelA(indices);
    kpp = labels_tries(1:K);
      
    % Comptage du nombre de voisins appartenant à chaque classe
    nb_voisins = zeros(1, length(ListeClass));
        
    for j = 1:length(ListeClass)
        nb_voisins(j) = sum(kpp == ListeClass(j));    
    end
    
    % Recherche de la classe contenant le maximum de voisins
    [~, indice_max] = max(nb_voisins);
    
    % Si l'image test a le plus grand nombre de voisins dans plusieurs  
    % classes différentes, alors on lui assigne celle du voisin le + proche,
    % sinon on lui assigne l'unique classe contenant le plus de voisins 
    if length(indice_max)> 1
        classe_max = kpp(1);
    else
        classe_max = indice_max;
    end
    
    % Assignation de l'étiquette correspondant à la classe trouvée au point 
    % correspondant à la i-ème image test dans le vecteur "Partition" 
    Partition(i) = classe_max;
        
end

