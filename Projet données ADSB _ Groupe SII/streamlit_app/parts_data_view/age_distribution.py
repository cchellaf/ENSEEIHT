import matplotlib.pyplot as plt
import pandas as pd
import streamlit as st
import plotly.graph_objs as go
from functions import functions_obj1 as f1
from folium.plugins import MarkerCluster
from datetime import date
import numpy as np



def show_age_distribution(data):
    st.header(":green[Distribution de l'âge des avions :] ")
    data['age_avion'] = data['age_avion'].fillna(-1)
    #data['age'] = data['age_avion'].apply(lambda x: -1 if np.isnan(x) else date.today().year - x)
    
    # Create a new column that maps the age of each aircraft to a decade
    def map_to_decade(x):
        if x < 10:
            return 0
        else:
            return (( -1 if np.isnan(x) else date.today().year - x) // 10) * 10

    data['decade'] = data['age_avion'].apply(map_to_decade)

    # Calculate the count of aircraft by decade
    count_by_decade = data['decade'].value_counts(dropna=False)


    def label_age_avion(decade):
        if decade == 0:
            return "0-10 ans"
        if decade == 10:
            return "10-20 ans"
        if decade == 20:
            return "20-30 ans"
        else:
            return "Plus de 30 ans"

    fig, ax = plt.subplots(figsize=(10, 7))
    labels = count_by_decade.index.map(label_age_avion)
    ax.pie(count_by_decade, labels=labels, autopct='%1.1f%%')
    

    # Show the plot
    st.pyplot(fig)
