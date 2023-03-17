import matplotlib.pyplot as plt
import pandas as pd
import streamlit as st
import plotly.graph_objs as go
from functions import functions_obj1 as f1
import ast
import folium
import os
from folium.plugins import MarkerCluster
from folium.plugins import PolyLineTextPath



def get_lat_long_list(lat_ls,long_ls):
    lat_long_l = []
    lat_l = ast.literal_eval(lat_ls)
    long_l = ast.literal_eval(long_ls)
    for i in range(min(len(lat_l),len(long_l))) :
        lat_long_l.insert(0,[lat_l[i],long_l[i]])
    return lat_long_l


def search_data(data,search) : 
    for _,row in data.iterrows() :
        if (str(row['datetime']) == search.split(" ")[1]) :
            if search.split(" ")[0] == str(row['Flight_ID']) :
                st.write(f'Id du vol : {str(row["Flight_ID"])}')
                st.write(f'date du vol : {str(row["datetime"])}')
                st.write(f'Type d\'avion : {str(row["type_avion"])}')
                return row
    

def show_search(data):
    # partie atterissage :
    st.header(":blue[Chercher un vol :] ✈️ ")
    search_term = st.text_input('Chercher un vol par son Flight_ID et sa date dans cet format : Flight_ID yy-mm-dd')
    if search_term != "" :
        #search_results = data.loc[data['Flight_ID'].str.contains(search_term, case=False)]
        data.rename(columns={"Unnamed: 0": "icao_address"}, inplace=True)
        data_s = search_data(data,search_term)

        lat_ls = data_s.latitude
        long_ls = data_s.longitude
        # Create map object
        m = folium.Map(location=[43.604652, 1.444209], zoom_start=7)

        # Create list of coordinates
        coordinates = get_lat_long_list(lat_ls,long_ls)

        wind_line = folium.PolyLine(coordinates,weight=7,color = '#FFFFFFF').add_to(m)
        
        attr = {'fill' : '#007DEF' , 'font-weight' : 'bold' , 'font-size' : '20'}
        
        # Create PolyLineTextPath object
        PolyLineTextPath(
            wind_line,
            '✈︎',
            repeat = True,
            offset=7,
            attributes=attr
        ).add_to(m)

        # Add PolyLineTextPath object to map
    #line.add_to(m)
    
        # generate HTML code for the map
        html_string = m._repr_html_()

        # display the map in Streamlit
        st.components.v1.html(html_string, height=600)

