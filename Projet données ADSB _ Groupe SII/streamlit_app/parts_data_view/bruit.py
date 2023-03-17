import matplotlib.pyplot as plt
import pandas as pd
import streamlit as st
from functions import functions_objSec3 as fs3

def run_app(data):
        #fs3.classification_real(data,target)
        grouped = fs3.grouped(data)
        st.markdown("### Classification des avions selon le bruit sonore et l'age")
        st.write('Voici le dataframe pour la classification:')
        st.dataframe(grouped)
        colors = {"bruit faible": ('g'), "bruit moyen": ('b'), "bruit élevé": ('r')}

        fig, ax = plt.subplots()
        for index,row in grouped.dropna().iterrows():
               ax.bar(grouped.age_moyen_avion[index], grouped.indice_sonore[index],color = colors[index], label=index)

        fig.set_size_inches(3, 3)
        ax.set_xlabel('Age moyen des avions')
        ax.set_ylabel('Indice sonore')
        ax.set_title(" Classification des avions selon le bruit sonore")
        ax.legend()
        st.pyplot(fig)

  