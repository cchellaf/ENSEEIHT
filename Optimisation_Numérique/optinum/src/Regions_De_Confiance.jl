@doc doc"""
Minimise une fonction en utilisant l'algorithme des régions de confiance avec
    - le pas de Cauchy
ou
    - le pas issu de l'algorithme du gradient conjugue tronqué

# Syntaxe
```julia
xk, nb_iters, f(xk), flag = Regions_De_Confiance(algo,f,gradf,hessf,x0,option)
```

# Entrées :

   * **algo**        : (String) string indicant la méthode à utiliser pour calculer le pas
        - **"gct"**   : pour l'algorithme du gradient conjugué tronqué
        - **"cauchy"**: pour le pas de Cauchy
   * **f**           : (Function) la fonction à minimiser
   * **gradf**       : (Function) le gradient de la fonction f
   * **hessf**       : (Function) la hessiene de la fonction à minimiser
   * **x0**          : (Array{Float,1}) point de départ
   * **options**     : (Array{Float,1})
     * **deltaMax**      : utile pour les m-à-j de la région de confiance
                      ``R_{k}=\left\{x_{k}+s ;\|s\| \leq \Delta_{k}\right\}``
     * **gamma1,gamma2** : ``0 < \gamma_{1} < 1 < \gamma_{2}`` pour les m-à-j de ``R_{k}``
     * **eta1,eta2**     : ``0 < \eta_{1} < \eta_{2} < 1`` pour les m-à-j de ``R_{k}``
     * **delta0**        : le rayon de départ de la région de confiance
     * **max_iter**      : le nombre maximale d'iterations
     * **Tol_abs**       : la tolérence absolue
     * **Tol_rel**       : la tolérence relative

# Sorties:

   * **xmin**    : (Array{Float,1}) une approximation de la solution du problème : ``min_{x \in \mathbb{R}^{n}} f(x)``
   * **fxmin**   : (Float) ``f(x_{min})``
   * **flag**    : (Integer) un entier indiquant le critère sur lequel le programme à arrêter
      - **0**    : Convergence
      - **1**    : stagnation du ``x``
      - **2**    : stagnation du ``f``
      - **3**    : nombre maximal d'itération dépassé
   * **nb_iters** : (Integer)le nombre d'iteration qu'à fait le programme

# Exemple d'appel
```julia
algo="gct"
f(x)=100*(x[2]-x[1]^2)^2+(1-x[1])^2
gradf(x)=[-400*x[1]*(x[2]-x[1]^2)-2*(1-x[1]) ; 200*(x[2]-x[1]^2)]
hessf(x)=[-400*(x[2]-3*x[1]^2)+2  -400*x[1];-400*x[1]  200]
x0 = [1; 0]
options = []
xmin, fxmin, flag,nb_iters = Regions_De_Confiance(algo,f,gradf,hessf,x0,options)
```
"""
function Regions_De_Confiance(algo,f::Function,gradf::Function,hessf::Function,x0,options)

    if options == []
        deltaMax = 10
        gamma1 = 0.5
        gamma2 = 2.00
        eta1 = 0.25
        eta2 = 0.75
        delta0 = 2
        max_iter = 1000
        Tol_abs = sqrt(eps())
        Tol_rel = 1e-15
    else
        deltaMax = options[1]
        gamma1 = options[2]
        gamma2 = options[3]
        eta1 = options[4]
        eta2 = options[5]
        delta0 = options[6]
        max_iter = options[7]
        Tol_abs = options[8]
        Tol_rel = options[9]
    end

    n = length(x0)
    xmin = zeros(n)
    fxmin = f(xmin)
    flag = 0
    nb_iters = 0
    xk = x0
    xkplus1 = x0
    sk = 0
    delta_k = delta0
    delta_kplus1 = delta0
    
    conditionArret = false 
    
    if norm(gradf(xk)) == 0 
	xmin = x0
	nb_iters = 0
    else 
    
	while !conditionArret
            
            gk = gradf(xk)
            Hk = hessf(xk)
            
	    if algo == "cauchy"
    	 	sk, ek = Pas_De_Cauchy(gk,Hk,delta_k)
    	    elseif algo == "gct" 
    		sk = Gradient_Conjugue_Tronque(gk,Hk,[delta_k, max_iter, Tol_rel])     
    	    else 
    		println("aucun algo ne correspond"); 
    	    end
    	   
	    R_k = (f(xk) - f(xk + sk)) / -(gk'*sk + 0.5*sk'*Hk*sk)
	    
	    if (R_k >= eta1) 
    	    	xkplus1 = xk + sk
    	    else 
    	    	xkplus1 = xk
    	    end 
    	    
	    if (R_k >= eta2) 
	    	delta_kplus1 = min(gamma2*delta_k, deltaMax)
	    elseif ((R_k >= eta1) && (R_k <= eta2))
	    	delta_kplus1 = delta_k
	    else 
	    	delta_kplus1 = gamma1*delta_k
	    end 
		 
	    convergence = norm(gradf(xkplus1)) <= max(Tol_rel*(norm(gradf(x0))), Tol_abs) 
	    stagnation_xk = norm(sk) <= max(Tol_rel*(norm(xk)), Tol_abs) 
	    stagnation_f = norm(f(xk + sk) - f(xk)) <= max(Tol_rel*(norm(f(xk))), Tol_abs) 
	    maxIter = (nb_iters >= max_iter)
	    
	    if (convergence) 
		flag = 0 
		conditionArret = true
	    elseif (stagnation_xk) 
		flag = 1
		conditionArret = true
	    elseif (stagnation_f) 
		flag = 2 
		conditionArret = true
	    elseif (maxIter) 
		flag = 3
		conditionArret = true
            end
	    
	    nb_iters = nb_iters + 1
	    xk = xkplus1
            delta_k = delta_kplus1
	   
	end
	
	xmin = xkplus1
	
    end
    fxmin = f(xmin)

    return xmin, fxmin, flag, nb_iters
end
