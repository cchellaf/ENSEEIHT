@doc doc"""
Approximation de la solution du problème ``\min_{x \in \mathbb{R}^{n}} f(x)`` en utilisant l'algorithme de Newton

# Syntaxe
```julia
xk,f_min,flag,nb_iters = Algorithme_de_Newton(f,gradf,hessf,x0,option)
```

# Entrées :
   * **f**       : (Function) la fonction à minimiser
   * **gradf**   : (Function) le gradient de la fonction f
   * **hessf**   : (Function) la Hessienne de la fonction f
   * **x0**      : (Array{Float,1}) première approximation de la solution cherchée
   * **options** : (Array{Float,1})
       * **max_iter**      : le nombre maximal d'iterations
       * **Tol_abs**       : la tolérence absolue
       * **Tol_rel**       : la tolérence relative

# Sorties:
   * **xmin**    : (Array{Float,1}) une approximation de la solution du problème  : ``\min_{x \in \mathbb{R}^{n}} f(x)``
   * **f_min**   : (Float) ``f(x_{min})``
   * **flag**     : (Integer) indique le critère sur lequel le programme à arrêter
      * **0**    : Convergence
      * **1**    : stagnation du xk
      * **2**    : stagnation du f
      * **3**    : nombre maximal d'itération dépassé
   * **nb_iters** : (Integer) le nombre d'itérations faites par le programme

# Exemple d'appel
```@example
using Optinum
f(x)=100*(x[2]-x[1]^2)^2+(1-x[1])^2
gradf(x)=[-400*x[1]*(x[2]-x[1]^2)-2*(1-x[1]) ; 200*(x[2]-x[1]^2)]
hessf(x)=[-400*(x[2]-3*x[1]^2)+2  -400*x[1];-400*x[1]  200]
x0 = [1; 0]
options = []
xmin,f_min,flag,nb_iters = Algorithme_De_Newton(f,gradf,hessf,x0,options)
```
"""
function Algorithme_De_Newton(f::Function,gradf::Function,hessf::Function,x0,options)

    "# Si options == [] on prends les paramètres par défaut"
    if options == []
        max_iter = 100
        Tol_abs = sqrt(eps())
        Tol_rel = 1e-15
    else
        max_iter = options[1]
        Tol_abs = options[2]
        Tol_rel = options[3]
    end

        n = length(x0)
        xmin = zeros(n)
        f_min = 0
        flag = 0
        nb_iters = 0
        xk = x0
        xkplus1 = x0
        
	conditionArret = false
	
	if norm(gradf(xk)) == 0 
		xmin = x0
	else 
		 while !conditionArret
		 
			xk = xkplus1
			nb_iters = nb_iters + 1
			dk = hessf(xk) \ -gradf(xk)
        		xkplus1 = xk + dk
        
			convergence = norm(gradf(xkplus1)) <= max(Tol_rel*(norm(gradf(x0))), Tol_abs) 
			stagnation_xk = norm(xkplus1-xk) <= max(Tol_rel*(norm(xk)), Tol_abs) 
			stagnation_f = norm(f(xkplus1) - f(xk)) <= max(Tol_rel*(norm(f(xk))), Tol_abs) 
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
		
			
		end
		
		xmin = xkplus1

	end
   
        f_min = f(xmin) 
        
        return xmin,f_min,flag,nb_iters
end
