import matplotlib.pyplot as plt
import pandas as pd
import streamlit as st
import plotly.graph_objs as go
from functions import functions_obj1 as f1
import ast
import folium
from folium.plugins import MarkerCluster

timedict = {}

def extract_first_element(table) :
    list = ast.literal_eval(table)
    return list[0]

def list_of_time(table) :
    timedict[table.split("('")[1].split(" ")[0]] = ""
    return table.split("('")[1].split(" ")[0]

def show_view_ad(data_atter, data_decoll):
    st.header(":blue[Atterrissage et décollage :] ✈️ ")
    #data_atter["datetime"] = data_atter["datetime"].apply(list_of_time)
    day = st.selectbox("Selectionnez un jour", data_atter.keys())
    #data_decoll["datetime"] = data_decoll["datetime"].apply(list_of_time)
    show_view_ad_bis(data_atter[day], data_decoll[day], day)

def show_view_ad_bis(data_atte, data_decol,day):
    data_atter = data_atte[data_atte.datetime == str(day)]
    data_decoll = data_decol[data_decol.datetime == str(day)]
    # partie atterissage :
    st.text("Partie I : Atterissage à Toulouse")
    st.text("Representation dans maps lors de la première detection dans le radar :")
    data_atter.rename(columns={"Unnamed: 0": "icao_address"}, inplace=True)
    data_atter['age_avion'] = data_atter['age_avion'].fillna("Unknown")
    data_atter['nombre_passagers'] = data_atter['nombre_passagers'].fillna(
        "Unknown")
    data_atter.astype({'age_avion': 'string', 'nombre_passagers': 'string'})
    data_atter['longitude'] = data_atter['longitude'].apply(
        extract_first_element)
    data_atter['latitude'] = data_atter['latitude'].apply(
        extract_first_element)
    data_atter.rename(columns={"longitude": "lon",
                      "latitude": "lat"}, inplace=True)
    # create a map object
    m_atterissage = folium.Map(location=[43.604652, 1.444209], zoom_start=5)
    # add markers to the map for each plane
    for _, row in data_atter.iterrows():
        age = str(row['age_avion'])
        nbp = str(row['nombre_passagers'])
        if str(row['age_avion']) != "Unknown" :
            age = int(row['age_avion'])
        if str(row['nombre_passagers']) != "Unknown" :
            nbp = int(row['nombre_passagers'])
        html = f"""
            <p> Information sur le vol : </p>
            <ul>
                <li>ID : {str(row['Flight_ID'])} </li>
                <li>Type : {str(row['type_avion'])} </li>
                <li>Nombre de sièges : {str(nbp)} </li>
                <li>Année de construction : {str(age)} </li>
                <li>date du vol : {str(row['datetime'])} </li>
            </ul>
            """
        iframe = folium.IFrame(html=html, width=270, height=150)
        popup = folium.Popup(iframe, max_width=2650)
        folium.Marker(location=[row['lat'], row['lon']],
                    tooltip=row['Flight_ID'], popup=popup).add_to(m_atterissage)

    # generate HTML code for the map
    html_string = m_atterissage._repr_html_()

    # partie decollage
    # display the map in Streamlit
    st.components.v1.html(html_string, height=600)
    
    st.text("Partie II : Decollage à Toulouse")
    st.text("Representation dans maps lors de la première detection dans le radar :")

    # adaptation de la dataframe
    data_decoll['longitude'] = data_decoll['longitude'].apply(
        extract_first_element)
    data_decoll['latitude'] = data_decoll['latitude'].apply(
        extract_first_element)
    data_decoll.rename(columns={"longitude": "lon",
                       "latitude": "lat"}, inplace=True)
    data_decoll.rename(columns={"Unnamed: 0": "icao_address"}, inplace=True)
    data_decoll['age_avion'] = data_decoll['age_avion'].fillna("Unknown")
    data_decoll['nombre_passagers'] = data_decoll['nombre_passagers'].fillna(
        "Unknown")
    data_decoll.astype({'age_avion': 'string', 'nombre_passagers': 'string'})

    
    # create a map object
    m_decollage = folium.Map(location=[43.604652, 1.444209], zoom_start=5)
    # add markers to the map for each plane
    for _, row in data_decoll.iterrows():
        age = str(row['age_avion'])
        nbp = str(row['nombre_passagers'])
        if str(row['age_avion']) != "Unknown" :
            age = int(row['age_avion'])
        if str(row['nombre_passagers']) != "Unknown" :
            nbp = int(row['nombre_passagers'])
        html = f"""
            <p> Information sur le vol : </p>
            <ul>
                <li>ID : {str(row['Flight_ID'])} </li>
                <li>Type : {str(row['type_avion'])} </li>
                <li>Nombre de sièges : {str(nbp)} </li>
                <li>Année de construction : {str(age)} </li>
                <li>date du vol : {str(row['datetime'])} </li>
            </ul>
            """
        iframe = folium.IFrame(html=html, width=270, height=150)
        popup = folium.Popup(iframe, max_width=2650)
        folium.Marker(location=[row['lat'], row['lon']],
                    tooltip=row['Flight_ID'], popup=popup).add_to(m_decollage)

    # generate HTML code for the map
    html_string = m_decollage._repr_html_()

    # display the map in Streamlit
    st.components.v1.html(html_string, height=600)
