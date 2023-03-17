from matplotlib import pyplot as plt
import pandas as pd
import streamlit as st
from sklearn.cluster import KMeans
import numpy as np


data_consommation = pd.read_csv("data/merged_consommation.csv")
data_emission = pd.read_csv("data/merged.csv")

def classification_type(data,ind,facteur):
    if(facteur == 'type_avion'):
        data[facteur] = data[facteur].apply(str.upper)
        
    grouped_data = data.groupby(facteur)[ind].mean().reset_index()
    
    kmeans = KMeans(n_clusters=3)
    kmeans.fit(grouped_data[[ind]])
    labels = kmeans.labels_

    colors = ['g', 'b', 'r']
    # Assign each label to a color based on its rank (i.e., the label with the highest mean value gets the first color, etc.)
    mean_values = grouped_data.groupby(labels)[ind].mean().sort_values()
    color_map = {label: colors[i] for i, label in enumerate(mean_values.index)}

    # Plot the results with bar color based on the cluster label
    fig, ax = plt.subplots(figsize=(8, 6))
    for i, label in enumerate(labels):
        color = color_map[label]
        ax.bar(grouped_data[facteur][i], grouped_data[ind][i], color=color)
    legend_handles = [plt.scatter([], [], marker='o', s=50, c=color, label=f'Cluster {label+1}') 
                      for label, color in color_map.items()]
    ax.legend(handles=legend_handles)
    ax.set_xlabel(facteur)
    ax.set_ylabel('Moyenne '+ind)
    ax.set_title('Classification selon '+ind)
    plt.show()
    st.pyplot(fig)

def boxplot_emission(df3,i):
    df3['type_avion'] = df3['type_avion'].apply(str.upper)
    grouped_data = df3.groupby('type_avion')
    col = df3.columns[i]
    fig, ax = plt.subplots()
    ax.boxplot([grouped_data.get_group(name)[col] for name in grouped_data.groups.keys()])
    ax.set_xticklabels(grouped_data.groups.keys())
    ax.set_xlabel('Typde d\'avion')
    ax.set_ylabel(col)
    st.pyplot(fig)
    

    

def app(choice_ind) :
    if (choice_ind == "CO"):
        st.title("CO")
        classification_type(data_emission, 'emission_co', 'type_avion')
        
        st.title("Boxplot des données d'émissions")
        boxplot_emission(data_emission,6)
        
    elif(choice_ind == "CO2"):
        st.title("CO2")
        classification_type(data_emission, 'emission_co2', 'type_avion')
        st.title("Boxplot des données d'émissions")
        boxplot_emission(data_emission,2)
    elif(choice_ind == "H2O"):
        st.title("H2O")
        classification_type(data_emission, 'emission_h2o', 'type_avion')
        boxplot_emission(data_emission,3)
    elif(choice_ind == "HC"):
        st.title("HC")
        classification_type(data_emission, 'emission_hc', 'type_avion')
        boxplot_emission(data_emission,7)
        
    elif(choice_ind == "NOX"):
        st.title("NOX")
        classification_type(data_emission, 'emission_nox', 'type_avion')
        boxplot_emission(data_emission,5)
    elif(choice_ind == "SOX"):
        st.title("SOX")
        classification_type(data_emission, 'emission_sox', 'type_avion')
        boxplot_emission(data_emission,4)
    else:
        #type
        st.title("classifictation de la consommation de carburant par type d'avion")
        classification_type(data_consommation, 'consommation_carburant', 'type_avion')
        #age
        st.title("classifictation de la consommation de carburant par age d'avion")
        df_age = data_consommation.filter(items = ['age_avion','consommation_carburant'] )
        df_age.dropna(subset=['age_avion'], inplace=True) 
        masque = df_age['age_avion'] == 0000  
        df_age = df_age.drop(df_age[masque].index)
        classification_type(df_age, 'consommation_carburant', 'age_avion')
        #passagers
        st.title("classifictation de la consommation de carburant par nombre de passagers")
        df_passagers = data_consommation.filter(items = ['nombre_passagers','consommation_carburant'] )
        df_passagers.dropna(subset=['nombre_passagers'], inplace=True)   
        classification_type(df_passagers, 'consommation_carburant', 'nombre_passagers')
        
        



        

        


