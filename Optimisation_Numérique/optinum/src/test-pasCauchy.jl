using Markdown
using Test
using LinearAlgebra
using TestOptinum
using Optinum

include("../src/Algorithme_De_Newton.jl")
include("../src/Gradient_Conjugue_Tronque.jl")
include("../src/Lagrangien_Augmente.jl")
#include("../src/Pas_De_Cauchy.jl")
include("Pas_De_Cauchy.jl")
include("../src/Regions_De_Confiance.jl")

TestOptinum.cacher_stacktrace()

affiche = false
println("affiche = ",affiche)
# Tester l'ensemble des algorithmes
@testset "Test SujetOptinum" begin
	# Tester l'algorithme de Newton
	tester_algo_newton(affiche,Algorithme_De_Newton)

	# Tester l'algorithme du pas de Cauchy
	tester_pas_de_cauchy(affiche,Pas_De_Cauchy)

end
