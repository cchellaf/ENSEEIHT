from openap import prop
import ast
import numpy as np
import matplotlib.pyplot as plt
from openap import Emission, FuelFlow, prop
from mpl_toolkits.mplot3d import Axes3D
import pandas as pd
import json
import math


#### Calcul de la distance entre deux points à partir de la latitude et longitude

def haversine_distance(lat1, long1, lat2, long2):
    R = 6371  # radius of Earth in kilometers
    lat1, long1, lat2, long2 = map(math.radians, [lat1, long1, lat2, long2])
    dlat = lat2 - lat1
    dlong = long2 - long1
    a = (math.sin(dlat/2)**2) + math.cos(lat1) * math.cos(lat2) * (math.sin(dlong/2)**2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    d = R * c
    return d

###### Nettoyage des données #####

def sum_sonore(x):
    return 10**(x/10)

def clean_data(data):
    alt_list = []
    lat_list = []
    long_list = []
    distance_aero_list = []
    surf_list = []
    rate_list = []
    indice_list = []
    flightid_list = []
    sonore_list = []

    latitude_aero = 43.6293
    longitude_aero = 1.3676 

    data['datetime'] = pd.to_datetime(data['timestamp_s'], unit='s')

    for i in range (len(data)) :
        icao_addr = data.icao_adress_hex[i]
        jsn = data.data[i]
        jsn2 = json.loads(jsn)
        try:
            alt = jsn2["05_Altitude (ft)"]
            long = jsn2["05_Airborne Longitude (°)"]
            lat = jsn2["05_Airborne Latitude (°)"]
            vitesse_surf = jsn2["09_Surface Speed Calculated (knots)"]
            vitesse_rate = jsn2["09_sub1_Vertical Rate_Meaning"]
            flight_id = jsn2["08_Flight_ID"]
            air_sol = jsn2["AIR_SOL"]
            if (air_sol == 'SOL') : 
                indice_list.append(icao_addr)

            #calcul de la distance
            distance = haversine_distance(latitude_aero, longitude_aero, lat, long)
            
            #calcul du niveau sonore
            niveau = 57 + 20 * math.log10(vitesse_surf) - 15 * math.log10(alt) - 2 * math.log10(distance)

            if (vitesse_rate == "ZERO") : 
                rate_list.append(0)
            else :
                vitesse_split = vitesse_rate.split()
                if (vitesse_split[0] == "-") : 
                    rate_list.append(-int(vitesse_split[1]))
                else : 
                    rate_list.append(int(vitesse_split[1]))


            alt_list.append(alt)
            long_list.append(long)
            lat_list.append(lat)
            distance_aero_list.append(distance)
            surf_list.append(int(vitesse_surf))
            flightid_list.append(flight_id)
            sonore_list.append(niveau)
            
        except:
            #print("erreur")
            alt_list.append(-100)
            long_list.append(-100)
            lat_list.append(-100)
            surf_list.append(-100)
            rate_list.append(-100)
            distance_aero_list.append(-100)
            sonore_list.append(-100)
            flightid_list.append("")     # A verifier


    data.loc[:, 'altitude'] = alt_list
    data.loc[:, 'longitude'] = long_list
    data.loc[:, 'latitude'] = lat_list
    data.loc[:, 'distance_aero'] = distance_aero_list
    data.loc[:, 'TAS'] = surf_list
    data.loc[:, 'vertical_rate'] = rate_list
    data.loc[:, 'Flight_ID'] = flightid_list
    data.loc[:, 'niveau_sonore'] = sonore_list

    data.drop(data[data['altitude']==-100].index,  inplace = True)
    data = data.dropna(subset=['longitude','latitude'])

    indice_list = [*set(indice_list)]
    data = data[ data['icao_adress_hex'].isin(indice_list) ]

    data_filter = data.filter( items = ["icao_adress_hex", "Flight_ID", "timestamp_s", "datetime", "altitude", "longitude", "latitude", "distance_aero", "TAS", "vertical_rate", "niveau_sonore"])
    data_group = data_filter.groupby(['icao_adress_hex', 'Flight_ID']).agg(lambda x: x.tolist())

    #calcul de l'indice de performance sonore
    list_indice = []
    for i in range (len(data_group)) :
    
        list_niv = list(map(sum_sonore, data_group['niveau_sonore'][i]))
        indice_perfo = 10*math.log10(sum(list_niv))
        list_indice.append(indice_perfo)

    data_group.loc[:, 'indice_sonore'] = list_indice
    data_group = data_group.drop(columns=['niveau_sonore'])

    return data_group


def jointure_data(data_join, data_group):
    data_new = data_join.set_index('icao_address').join(data_group.set_index('icao_adress_hex'))
    data_new_na = data_new.dropna(subset=['timestamp_s','altitude', 'TAS', 'vertical_rate'])
    data_new_na = data_new_na.loc[:, ~data_new_na.columns.str.contains('^Unnamed')]
    data_new_na.to_csv('data_join.csv')
    #data_new_na.to_csv('data_join_exemple.csv')


#### Indicateurs environnementaux ####

def indicateurs_env(ac, tas, alt) : 
        
    aircraft = prop.aircraft(ac)
    fuelflow = FuelFlow(ac=ac)
    emission = Emission(ac=ac)

    tas_, alt_ = np.meshgrid(tas, alt)
    mass = aircraft["limits"]["MTOW"] * 0.85


    ff = fuelflow.enroute(mass=mass, tas=tas_, alt=alt_, path_angle=0)

    co2 = emission.co2(ff)
    h2o = emission.h2o(ff)
    sox = emission.sox(ff)
    nox = emission.nox(ff, tas=tas_, alt=alt_)
    co = emission.co(ff, tas=tas_, alt=alt_)
    hc = emission.hc(ff, tas=tas_, alt=alt_)

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, ff)
    plt.title("fuel flow (kg/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, h2o)
    plt.title("H2O (g/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, co2)
    plt.title("CO2 (kg/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, sox)
    plt.title("SOx (g/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, nox)
    plt.title("NOx (g/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, co)
    plt.title("CO (g/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()

    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(tas_, alt_, hc)
    plt.title("HC (g/s)")
    plt.xlabel("TAS (kt)")
    plt.ylabel("Altitude (ft)")
    plt.show()


def indicateurs_env_ligne(data, nb):     

    list_type = data.type_avion
    list_tas = data.TAS
    list_alt = data.altitude

    ac = list_type[nb]
    tas = ast.literal_eval(list_tas[nb])
    alt = ast.literal_eval(list_alt[nb])

    indicateurs_env_streamlit(ac, tas, alt)


def indicateurs_env_streamlit(ac, tas, alt) : 
        
    aircraft = prop.aircraft(ac)
    fuelflow = FuelFlow(ac=ac)
    emission = Emission(ac=ac)

    tas_, alt_ = np.meshgrid(tas, alt)
    mass = aircraft["limits"]["MTOW"] * 0.85


    ff = fuelflow.enroute(mass=mass, tas=tas_, alt=alt_, path_angle=0)

    co2 = emission.co2(ff)
    h2o = emission.h2o(ff)
    sox = emission.sox(ff)
    nox = emission.nox(ff, tas=tas_, alt=alt_)
    co = emission.co(ff, tas=tas_, alt=alt_)
    hc = emission.hc(ff, tas=tas_, alt=alt_)

    return ff,co2, h2o, sox, nox, co, hc, tas_, alt_


def indicateurs_env_ligne(data, nb):     

    list_type = data.type_avion
    list_tas = data.TAS
    list_alt = data.altitude

    ac = list_type[nb]
    tas = ast.literal_eval(list_tas[nb])
    alt = ast.literal_eval(list_alt[nb])

    ff, co2, h2o, sox, nox, co, hc, tas_, alt_ = indicateurs_env_streamlit(ac, tas, alt)

    return ff, co2, h2o, sox, nox, co, hc, tas_, alt_


#### Générer les dataframes consommation et emission (pour classification) ####


def indicateurs(ac, tas, alt) : 
        
    aircraft = prop.aircraft(ac)
    fuelflow = FuelFlow(ac=ac)
    emission = Emission(ac=ac)

    tas_, alt_ = np.meshgrid(tas, alt)
    mass = aircraft["limits"]["MTOW"] * 0.85


    ff = fuelflow.enroute(mass=mass, tas=tas_, alt=alt_, path_angle=0)
    ff = np.array(ff)

    co2 = emission.co2(ff)
    h2o = emission.h2o(ff)
    sox = emission.sox(ff)
    nox = emission.nox(ff, tas=tas_, alt=alt_)
    co = emission.co(ff, tas=tas_, alt=alt_)
    hc = emission.hc(ff, tas=tas_, alt=alt_)

    return np.mean(ff), np.mean(co2), np.mean(h2o), np.mean(sox), np.mean(nox), np.mean(co), np.mean(hc)


def construct_conso_emission(data):

    list_type = data.type_avion
    list_tas = data.TAS
    list_alt = data.altitude
    
    #créer une dataframe :
    df_conso = pd.DataFrame(columns=['type_avion', 'consommation_carburant'])
    df_emission = pd.DataFrame(columns=['type_avion', 'emission_co2', 'emission_h2o', 'emission_sox', 'emission_nox', 'emission_co', 'emission_hc'])

    for i in range (len(list_type)) : 
        try : 

            ac = list_type[i]
            tas = ast.literal_eval(list_tas[i])
            alt = ast.literal_eval(list_alt[i])

            ff, co2, h2o, sox, nox, co, hc = indicateurs(ac, tas, alt)

            df_conso.loc[i] = [ac, ff]
            df_emission.loc[i] = [ac, co2, h2o, sox, nox, co, hc]

        except:
            print('erreur')

    df_conso.to_csv('aircraft_consommations_data.csv')
    df_emission.to_csv('aircraft_emissions_data.csv')

    return df_conso, df_emission