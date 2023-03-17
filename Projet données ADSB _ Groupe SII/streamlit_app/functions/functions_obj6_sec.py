import pandas as pd
import numpy as np
import ast
import streamlit as st
from openap import FlightPhase
import matplotlib.pyplot as plt


def jetprive(data_atterrissage):

    list_jet = data_atterrissage.jet_prive
    list_sans_redd = []
    list_id = []
    nb_jets = 0
    for i in range(len(list_jet)) :
        if (list_jet[i].__contains__('Private') and (data_atterrissage['Flight_ID'][i] not in list_id)) : 
            nb_jets += 1
            list_id.append(data_atterrissage['Flight_ID'][i])
    
        elif ( (not (list_jet[i].__contains__('Unknown'))) and (list_jet[i] not in list_sans_redd)) : 
            list_sans_redd.append(list_jet[i])

    col1, col2 = st.columns(2)
    col1.metric("Nombre de Jets privés", nb_jets)
    col2.metric("Pourcentage", str(round(100*nb_jets/len(list_jet),2)) + "%")

    type1 = st.selectbox("Selectionnez un autre type d'avion pour voir combien atterrissent à Toulouse", list_sans_redd)
    nb_vols = 0
    list_id2 = []
    for i in range(len(list_jet)) :
        if (list_jet[i].__contains__(type1) and (data_atterrissage['Flight_ID'][i] not in list_id2)) : 
            nb_vols += 1
            list_id2.append(data_atterrissage['Flight_ID'][i])

    col11, col21 = st.columns(2)
    col11.metric("Nombre d'avions " + type1, nb_vols)
    col21.metric("Pourcentage", str(round(100*nb_vols/len(list_jet),2)) + "%")