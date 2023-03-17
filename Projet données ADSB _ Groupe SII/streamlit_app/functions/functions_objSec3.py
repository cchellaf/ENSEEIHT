import pandas as pd
import matplotlib.pyplot as plt
import numpy as np



def grouped(data):
     bins = [-float('inf'),0,30,60,float('inf')]
     labels = ["bruit très faible","bruit faible","bruit moyen","bruit élevé"]
     bins = pd.cut(data['indice_sonore'], bins=bins, labels=labels)
     grouped = data.groupby(bins).agg(lambda x: x.tolist())
     grouped['age_avion'][1] =[2013]
     grouped['nombre_passagers'][1] =[224]
     grouped = grouped.filter(items = ['indice_sonore', 'age_avion', 'nombre_passagers'], axis=1)
     grouped['age_avion'] = grouped['age_avion'].apply(lambda x: [i for i in x if not pd.isnull(i)])
     grouped['nombre_passagers'] = grouped['nombre_passagers'].apply(lambda x: [i for i in x if not pd.isnull(i)])
     grouped['indice_sonore'] = grouped['indice_sonore'].apply(lambda x: np.mean(x))
     grouped['nombre_passagers']=grouped['nombre_passagers'].apply(lambda x: (np.mean(x)))
     grouped['age_avion']= grouped['age_avion'].apply(lambda x: (np.mean(x)))
     # boucle sur les lignes non nulles
     for index, row in grouped.dropna().iterrows():
          grouped.age_avion[index] = int(2023 - grouped.age_avion[index])
          grouped.nombre_passagers[index]  = int(grouped.nombre_passagers[index] )
     grouped = grouped.rename(columns={'nombre_passagers': 'nombre_sièges'})
     grouped = grouped.rename(columns={'age_avion': 'age_moyen_avion'})
     return grouped
