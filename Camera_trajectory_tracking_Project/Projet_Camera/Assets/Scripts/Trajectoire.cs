using System.Collections;
using System.Collections.Generic;
using UnityEngine;


using System.Linq;
using System;
using UnityEngine.UI;

public class Trajectoire : MonoBehaviour
{


    List<float> T = new List<float>(); 
    List<float> X = new List<float>();      //Recuperer la position selon l'axe X
    List<float> Y = new List<float>();      //Recuperer la position selon l'axe Y
    List<float> Z = new List<float>();      //Recuperer la position selon l'axe Z

    List<Quaternion> rotation = new List<Quaternion>();    //Recuperer la rotation 
    
    List<float> Xres = new List<float>();           //La liste des nouveaux points interpoles selon l'axe des X
    List<float> Yres = new List<float>();           //La liste des nouveaux points interpoles selon l'axe des Y
    List<float> Zres = new List<float>();           //La liste des nouveaux points interpoles selon l'axe des Z


    private List<Vector3> P2DRAW = new List<Vector3>();

    List<float> tToEval = new List<float>(); 
    public float pas = 1.0f/100;
    int i = 0;
    int j = 0;

    float t = 0;

    // Pour ralentir le mouvement de la camera 
    float compteur = 1.0f;       
    int v = 5;                  


    GameObject[] camera;

    public enum EParametrisationType { Reguliere, Tchebytcheff};
    public EParametrisationType ParametrisationType = EParametrisationType.Tchebytcheff;



    //Parametrisation 
    (List<float>, List<float>) buildParametrisationTchebycheff(int nbElem, float pas)
    {
        // Vecteur des pas temporels
        List<float> T = new List<float>();
        // Echantillonage des pas temporels
        List<float> tToEval = new List<float>();

        // Construction des pas temporels
        for (int i = nbElem - 1; i>=0 ; i--){
            T.Add((float)Math.Cos((2*i + 1)*(float)Math.PI/(2*(nbElem-1) + 2)));
        }

        // Construction des échantillons
         float elt = T.Min();
         tToEval.Add(elt);
         while (tToEval.Last() < T.Max()) {
             elt = elt + pas; 
             tToEval.Add(elt);
         }

        return (T, tToEval);
    }


    (List<float>, List<float>) buildParametrisationReguliere(int nbElem, float pas)
    {
        // Vecteur des pas temporels
        List<float> T = new List<float>();
        // Echantillonage des pas temporels
        List<float> tToEval = new List<float>();

        // Construction des pas temporels
        for (int i = 0; i<nbElem ; i++){
            T.Add(i);
        }

        // Construction des échantillons
         float elt = T.Min();
         tToEval.Add(elt);
         while (tToEval.Last() < T.Max()) {
             elt = elt + pas; 
             tToEval.Add(elt);
         }

        return (T, tToEval);
    }

    

     private float lagrange(float x, List<float> X, List<float> Y)
    {
        float somme = 0;
	  
        for (int i=0;i<Y.Count;i++) {
		float  produit = 1;
		for (int j = 0; j<Y.Count;j++) {
            if (i != j) {
			produit = produit * ((x - X[j])/(X[i]-X[j]));
        }
		}
		somme += produit* Y[i];
		}
        return somme;
    }



    (List<float>,List<float>,List<float>) applyLagrangeParametrisation(List<float> X, List<float> Y, List<float> Z, List<float> T, List<float> tToEval)
    {
        List<float> Xnew = new List<float>();
        List<float> Ynew = new List<float>();
        List<float> Znew = new List<float>();

        for (int i = 0; i < tToEval.Count; ++i) {
       
            float xpoint = lagrange(tToEval[i], T, X);
            float ypoint = lagrange(tToEval[i], T, Y);
            float zpoint = lagrange(tToEval[i], T, Z);
            Vector3 pos = new Vector3(xpoint, ypoint, zpoint);    
            Xnew.Add(xpoint);
            Ynew.Add(ypoint);
            Znew.Add(zpoint);
            P2DRAW.Add(pos);
        }  

        return (Xnew,Ynew,Znew); 
    }



    // Start is called before the first frame update
    void Start()
    {
        // Récupération des points de la trajectoire
        GameObject[] listePoints = GameObject.FindGameObjectsWithTag("Point");

        // Récupération des objets caméra
        camera = GameObject.FindGameObjectsWithTag("MainCamera");
        
        // Remplissage des listes X,Y,Z, rotation par les coordonnées des points de la trajectoire
        for (int i = 0; i < listePoints.Count() ; i++) {
            X.Add(listePoints[i].transform.position[0]);  
            Y.Add(listePoints[i].transform.position[1]);
            Z.Add(listePoints[i].transform.position[2]);
            rotation.Add(listePoints[i].transform.rotation);
        }

        // Ajout du premier point pour avoir une courbe fermée
        X.Add(listePoints[0].transform.position[0]);
        Y.Add(listePoints[0].transform.position[1]);
        Z.Add(listePoints[0].transform.position[2]);
        rotation.Add(listePoints[0].transform.rotation);

                
        switch (ParametrisationType)
        {
            case EParametrisationType.Reguliere:
                (T, tToEval) = buildParametrisationReguliere(X.Count, pas);
                (Xres,Yres,Zres)  = applyLagrangeParametrisation(X,Y,Z,T,tToEval);
                break;
    
            case EParametrisationType.Tchebytcheff:
                (T, tToEval) = buildParametrisationTchebycheff(X.Count, pas);
                (Xres,Yres,Zres)  = applyLagrangeParametrisation(X,Y,Z,T,tToEval);
                break;
        }
     
    }

    // Update is called once per frame
    void Update()
    {

       /** A DECOMMENTER : Si vous voulez voir le suivi de la camera en ralenti */
       //compteur = compteur - Time.deltaTime*v;
        //if (X.Count > 0 && compteur < 0)  {

            // La camera suit les positions des points 
            Vector3 p = new Vector3(Xres[i], Yres[i],Zres[i]);
            foreach (GameObject c in camera) {
                c.transform.position = p;
            }
            i = (i+1)%(tToEval.Count);


            // La camera suit la rotation des points
            t = (tToEval[i]-T[j])/(T[j+1]-T[j]);
            if (t > 1) {
                t = 1;
            }
            Quaternion q = Quaternion.Slerp(rotation[j], rotation[j+1],t);

            if (tToEval[i] >= T[j+1]) {
                j = (j+1)%(T.Count-1);
            }
            foreach (GameObject c in camera) {
                c.transform.rotation = q;
            }

           // compteur = 1;
        // }

    } 


    // Tracer la trajectoire en bleu 
    void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.blue;
        for(int i = 0; i < P2DRAW.Count - 1; ++i)
        {
            Gizmos.DrawLine(P2DRAW[i], P2DRAW[i+1]);

        }

        if (P2DRAW.Count > 0) {
            Gizmos.DrawLine(P2DRAW[P2DRAW.Count-1], P2DRAW[0]);
        }

    }
    



}
