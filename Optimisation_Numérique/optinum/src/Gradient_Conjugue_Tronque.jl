@doc doc"""
Minimise le problème : ``min_{||s||< \delta_{k}} q_k(s) = s^{t}g + (1/2)s^{t}Hs``
                        pour la ``k^{ème}`` itération de l'algorithme des régions de confiance

# Syntaxe
```julia
sk = Gradient_Conjugue_Tronque(fk,gradfk,hessfk,option)
```

# Entrées :   
   * **gradfk**           : (Array{Float,1}) le gradient de la fonction f appliqué au point xk
   * **hessfk**           : (Array{Float,2}) la Hessienne de la fonction f appliqué au point xk
   * **options**          : (Array{Float,1})
      - **delta**    : le rayon de la région de confiance
      - **max_iter** : le nombre maximal d'iterations
      - **tol**      : la tolérance pour la condition d'arrêt sur le gradient


# Sorties:
   * **s** : (Array{Float,1}) le pas s qui approche la solution du problème : ``min_{||s||< \delta_{k}} q(s)``

# Exemple d'appel:
```julia
gradf(x)=[-400*x[1]*(x[2]-x[1]^2)-2*(1-x[1]) ; 200*(x[2]-x[1]^2)]
hessf(x)=[-400*(x[2]-3*x[1]^2)+2  -400*x[1];-400*x[1]  200]
xk = [1; 0]
options = []
s = Gradient_Conjugue_Tronque(gradf(xk),hessf(xk),options)
```
"""
function Gradient_Conjugue_Tronque(gradfk,hessfk,options)

    "# Si option est vide on initialise les 3 paramètres par défaut"
    if options == []
        deltak = 2
        max_iter = 100
        tol = 1e-6
    else
        deltak = options[1]
        max_iter = options[2]
        tol = options[3]
    end

   n = length(gradfk)
   s = zeros(n)

   g = gradfk 
   H = hessfk 
   q(s) = g'*s + 0.5*s'*H*s
   
   g0 = g
   gj = g0
   pj = -g
   sj = s
   
   if norm(g) != 0 
   
	   for j = 0:max_iter
	   	
	   	Kj = pj'*H*pj
	   	
	   	if Kj <= 0 
	   		#racine de norm(sj + sigma_j*pj) = deltak 
	   		#on écrit norm(sj + sigma_j*pj)=deltak sous la forme ax²+bx+c=0
	   		a = norm(pj)^2
	   		b = 2*sj'*pj
	   		c = norm(sj)^2 - deltak^2
	   		d = sqrt(b^2 - 4*a*c)
	   		r1 = (-b-d)/(2*a)
	   		r2 = (-b+d)/(2*a)
	   		
	   		if q(sj + r1*pj) > q(sj + r2*pj) 
	   			sigma_j = r2
	   		else 
	   			sigma_j = r1
	   		end
	   		s = sj + sigma_j*pj
	   		
	   		break
	   	end 
	   		
		alpha_j = gj'*gj / Kj
		
		if norm(sj + alpha_j*pj) >= deltak 
			#racine positive de norm(sj + sigma_j*pj) = deltak
			a = norm(pj)^2
	   		b = 2*sj'*pj
	   		c = norm(sj)^2 - deltak^2
	   		d = sqrt(b^2 - 4*a*c)
	   		alpha1 = (-b-d)/(2*a)
	   		alpha2 = (-b+d)/(2*a) 
	   		
	   		if alpha1 > 0
	   			alpha = alpha1
	   		elseif alpha2 > 0 
	   			alpha = alpha2
	   		end
			s = sj + alpha*pj
			break
		end 

		sj = sj + alpha_j*pj
		g_jplus1 = gj + alpha_j*H*pj
		beta_j = (g_jplus1'*g_jplus1) / (gj'*gj)
		pj = -g_jplus1 + beta_j*pj
			
		if norm(g_jplus1) <= tol*norm(g0) 
			s = sj
			break
		end  
	 
	   	gj = g_jplus1
	   
	   end
	   
   end
   
   return s
   
end
