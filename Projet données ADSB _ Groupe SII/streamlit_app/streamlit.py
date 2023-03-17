import matplotlib.pyplot as plt
import pandas as pd
import streamlit as st
import plotly.graph_objs as go
import datetime
from functions import functions_obj1 as f1
from functions import functions_obj3 as f3
from functions import functions_obj6_sec as f6
from functions import streamlit_func4 as f4
from parts_data_view import Aterrissage_decollage as d1
from parts_data_view import age_distribution as age_d
from parts_data_view import type_repartition as type_r
from parts_data_view import bruit as b
from parts_data_view import search as s


page = st.sidebar.selectbox("Selectionnez une page", [
                            "Introduction", "Données des vols"])

data = pd.read_csv("data/merged_toulouse_11/merged_toulouse_11.csv")
data_atterrissage = pd.read_csv("data/merged_toulouse_11/merged_toulouse_11_att.csv")
data_decollage = pd.read_csv("data/merged_toulouse_11/merged_toulouse_11_dec.csv") 

#dictionnaire contenant les dates avec leurs dataframe correspondantes
data_dictio = {}
data_atter_dic = {}
data_decoll_dic = {}
#dictionnaire contenant les dates des vols
date_vols = {}
def list_of_time(table) :
    date_vols[table.split("('")[1].split(" ")[0]] = ""
    return table.split("('")[1].split(" ")[0]


data["datetime"] = data["datetime"].apply(list_of_time)
data_atterrissage["datetime"] = data_atterrissage["datetime"].apply(list_of_time)
data_decollage["datetime"] = data_decollage["datetime"].apply(list_of_time)

for i in date_vols.keys() :
    data_dictio[i] = data[data["datetime"] == str(i)].reset_index()
    data_atter_dic[i] = data_atterrissage[data_atterrissage["datetime"] == str(i)].reset_index()
    data_decoll_dic[i] = data_decollage[data_decollage["datetime"] == str(i)].reset_index()
    
def date_order_func(date_str):
    date_obj = datetime.datetime.strptime(date_str, '%Y-%m-%d')
    return date_obj
data_dictio = dict(sorted(data_dictio.items(), key=lambda x: date_order_func(x[0])))
if page == "Introduction" :
    st.header("Interface graphique du projet long pour les données ADSB")
    st.header("Introdution")
    # Add content for homepage here
elif page == "Données des vols":

    choice = st.sidebar.selectbox("Choisir une option", ["Atterrissage et décollage", "Chercher un vol en detail" , "Trajectoire d'avion",  "Classification selon les indicateurs environnementaux",
                                  "Classification selon le bruit", "Distribution de l'âge des avions", "Répartition des types d'avion", "Nombre de Jets privés detéctés à Toulouse"])

    if (choice == "Atterrissage et décollage"):
        d1.show_view_ad(data_atter_dic, data_decoll_dic)
    elif (choice == "Distribution de l'âge des avions"):
        age_d.show_age_distribution(data)
    elif (choice == "Répartition des types d'avion"):
        day = st.sidebar.selectbox("Selectionnez un jour", data_dictio.keys())
        type_r.show_type_repartition(data_dictio[day])
    elif (choice == "Classification selon le bruit"):
        #unzip file all_data_toulouse.zip in the folder Data
        data_b = pd.read_csv("data/all_data_toulouse.csv")
        b.run_app(data_b)
    elif (choice == "Trajectoire d'avion"):
        st.header(":green[Trajectoire d'avion :] 🛫 ")
        day = st.sidebar.selectbox("Selectionnez un jour", data_dictio.keys())
        nb_ligne = st.sidebar.slider(
            "Sélectionnez le numéro de ligne pour visualiser la trajectoire de l'avion:", 0, len(data_dictio[day])-1, 1)
        f3.traj_avion(data_dictio[day], nb_ligne)
    elif (choice == "Nombre de Jets privés detéctés à Toulouse"):
        st.header(f':green[Nombre de Jets privés detéctés à Toulouse ] 👨‍✈️ ')
        st.text(f'💡💡 La periode concernée est de {list(data_dictio.keys())[0]} à {list(data_dictio.keys())[-1]} 💡💡')
        f6.jetprive(data)
    elif (choice == "Classification selon les indicateurs environnementaux"):
        st.header(
            ":green[Classification selon les indicateurs environnementaux :] 🌿 ")
        choice_ind = st.sidebar.selectbox("Sélectionnez l'indicateur selon lequel vous voulez classifier", [
                                          "CO", "CO2", "H2O", "HC", "NOX", "SOX", "Carburant"])
        f4.app(choice_ind)
    elif (choice == "Chercher un vol en detail"):
        s.show_search(data)
    else:
        print("")

