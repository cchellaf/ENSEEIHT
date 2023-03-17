import pandas as pd
import numpy as np
import ast
import streamlit as st
from openap import FlightPhase
import matplotlib.pyplot as plt


def test_segment(labels, ts, alt, spd, roc):

    phasecolors = {
        'GND': 'black',     #ground
        'CL': 'green',      #climb
        'DE': 'blue',       #descend
        'LVL': 'cyan',      #level
        'CR': 'purple',     #cruise (vitesse cste ou alt cste)
        'NA': 'red'         #not available
    }

    colors = [phasecolors[lbl] for lbl in labels]

    plt.subplot(311)
    plt.scatter(ts, alt, marker='.', c=colors, lw=0)
    plt.ylabel('altitude (ft)')

    plt.subplot(312)
    plt.scatter(ts, spd, marker='.', c=colors, lw=0)
    plt.ylabel('speed (kt)')

    plt.subplot(313)
    plt.scatter(ts, roc, marker='.', c=colors, lw=0)
    plt.ylabel('roc (fpm)')

    plt.show()


def trajectory(data, line_nb):
    list_time = data.timestamp_s
    list_tas = data.TAS
    list_alt = data.altitude
    list_rate = data.vertical_rate

    ts = np.array(ast.literal_eval(list_time[line_nb]))
    spd = np.array(ast.literal_eval(list_tas[line_nb]))
    alt = np.array(ast.literal_eval(list_alt[line_nb]))
    roc = np.array(ast.literal_eval(list_rate[line_nb]))

    fp = FlightPhase()
    fp.set_trajectory(ts, alt, spd, roc)
    labels = fp.phaselabel()
    return labels, ts, alt, spd, roc
    #test_segment(labels, ts, alt, spd, roc)


def traj_avion(data, nb_ligne):

    type_avion = data['type_avion'][nb_ligne]
    flight_id = data['Flight_ID'][nb_ligne]
    
    labels, ts, alt, spd, roc = trajectory(data, nb_ligne)
    
    phasecolors = {
    'GND': 'black',     #ground
    'CL': 'green',      #climb
    'DE': 'blue',       #descend
    'LVL': 'cyan',      #level
    'CR': 'purple',     #cruise (vitesse cste ou alt cste)
    'NA': 'red'         #not available
    }

    colors = [phasecolors[lbl] for lbl in labels]
    
    st.subheader("Trajectoire de l'avion type : " + type_avion + " , numÃ©ro de vol : " + flight_id)

    # Affichage des graphiques
    fig1, ax1 = plt.subplots()
    ax1.scatter(ts, alt, marker='.', c=colors, lw=0)
    ax1.set_ylabel('altitude (ft)')

    fig2, ax2 = plt.subplots()
    ax2.scatter(ts, spd, marker='.', c=colors, lw=0)
    ax2.set_ylabel('speed (kt)')

    fig3, ax3 = plt.subplots()
    ax3.scatter(ts, roc, marker='.', c=colors, lw=0)
    ax3.set_ylabel('roc (fpm)')

    # Afficher les graphiques dans l'application web
    st.pyplot(fig1)
    st.pyplot(fig2)
    st.pyplot(fig3)


## TEST : 
#data_new_na = pd.read_csv('data_join.csv')
#trajectory(data_new_na, 11)