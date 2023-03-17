import matplotlib.pyplot as plt
import pandas as pd
import streamlit as st
import plotly.graph_objs as go
from functions import functions_obj1 as f1
from folium.plugins import MarkerCluster



def show_type_repartition(data):
    st.header(":green[Répartition des types d'avion :] ")
    #from PIL import Image            
    #image2 = Image.open('repartition_type.png')
    #st.image(image2)
    # Compute the count of aircraft by type
    count_by_type = data.groupby('type_avion')['type_avion'].count()

    # Get the list of types with count greater than 2% of total count
    types = count_by_type[count_by_type / count_by_type.sum() > 0.02].index.tolist()

    # Compute the count of aircraft for the "other" category
    other_count = count_by_type[count_by_type / count_by_type.sum() <= 0.02].sum()

    # Add the "other" category to the types list
    types.append('Autres')

    # Create a new Series with the count of aircraft by type, grouped by the types list
    count_by_type_grouped = data['type_avion'].apply(lambda x: x if x in types else 'Autres').value_counts()

    # Create the pie chart
    fig, ax = plt.subplots(figsize=(10, 7))
    ax.pie(count_by_type_grouped, labels=count_by_type_grouped.index.tolist(), autopct='%1.1f%%')

    # Show the plot
    st.pyplot(fig)