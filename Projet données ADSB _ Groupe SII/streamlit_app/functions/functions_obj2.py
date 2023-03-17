import numpy as np
import ast
import json
import matplotlib.pyplot as plt
import requests
import html_to_json


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


#### Ajouter les colonnes décollage et atterrissage dans la base de données ####

def data_decollage_atterrissage(data):

    list_time = data.timestamp_s
    list_alt = data.altitude
    list_distance = data.distance_aero
    list_rate = data.vertical_rate

    list_decollage = []
    list_atterrissage = []
    for nb in range(len(list_alt)) :
        
        ts = np.array(ast.literal_eval(list_time[nb]))
        distance = np.array(ast.literal_eval(list_distance[nb]))
        roc = np.array(ast.literal_eval(list_rate[nb]))
        #tas_, alt_ = np.meshgrid(spd, alt)

        distance_index = np.argmin(distance)
        distance_min = distance[distance_index]
        sueil = 30    #depend des aeroports alentours
        dict = {}
        if  (distance_min < sueil) : 

            # Calcul des différences entre chaque mesure de vitesse
            diff = np.diff(roc)

            # Recherche des indices des changements de signe (décollage ou atterrissage)
            sign_change = np.where(np.diff(np.sign(diff)))[0]

            # Séparation des intervalles de décollage et d'atterrissage
            intervals_roc = np.split(roc, sign_change)
            intervals_time = np.split(ts, sign_change)

            # Decider si atterrissage ou décollage
            dict["DECOLLAGE"] = []
            dict["ATTERRISSAGE"] = []
            for i, interval in enumerate(intervals_roc):
                try :
                    if (interval[0] > 0) & (len(interval) >=5 ):
                        dict["DECOLLAGE"].append(intervals_time[i].tolist())
                    elif (interval[0] < 0) & (len(interval) >=5 ): 
                        dict["ATTERRISSAGE"].append(intervals_time[i].tolist())
                except : 
                    print("liste vide")

            list_decollage.append(json.dumps(dict["DECOLLAGE"]))
            list_atterrissage.append(json.dumps(dict["ATTERRISSAGE"]))

        else :
            list_decollage.append("[]")       #Pas sur Toulouse
            list_atterrissage.append("[]")


    data.loc[:, 'DECOLLAGE'] = list_decollage
    data.loc[:, 'ATTERRISSAGE'] = list_atterrissage

    data_new = data.loc[(data['ATTERRISSAGE'] != '[]') | (data['DECOLLAGE'] != '[]')]
    return data_new



# returns airecraft age and the number of passenger in normal conditions (tale number and serial number exists) 
# return None if serial number or tale number are not found

def getAge_bis(tale_nb,serial_nb) :
    airecraft_ages = []
    url = f"https://www.airport-data.com/aircraft/{tale_nb}.html"

    response = requests.get(url)
    data = response.text
    output = html_to_json.convert(data)
    f = open("answer.json", "w")
    f.write(json.dumps(output))
    f.close()
    try :
        aircraft_age_table = output["html"][0]["body"][0]["div"][0]["div"][3]["div"][1]["div"]
        for i in range(len(aircraft_age_table)) :
            num_airecraft = i
            airecraft_age = ""
            dib_numb = len(aircraft_age_table[i]["div"]) - 1
            passager_seat_ind = 5
            airecraft_age_from_html = aircraft_age_table[num_airecraft]["div"][dib_numb]["table"][0]["tr"][2]["td"][1]["_value"]
            serial_numb = aircraft_age_table[num_airecraft]["div"][dib_numb]["table"][0]["tr"][3]["td"][1]["_value"]
            check = aircraft_age_table[num_airecraft]["div"][dib_numb]["table"][0]["tr"][5]["td"][0]["b"][0]["_value"]
            if check == "Aircraft Type:"  :
                passager_seat_ind = 6
            nombre_passagers = aircraft_age_table[num_airecraft]["div"][dib_numb]["table"][0]["tr"][passager_seat_ind]["td"][1]["_value"]
            serial_numbers = serial_numb.split("/")
            airecraft_ages.append({"serial_nb" : serial_numb, "age" : airecraft_age_from_html})
            if serial_nb in serial_numbers or serial_nb == serial_numb : 
                print(f"the airecraft age is {airecraft_age_from_html} et le nombre de passagers est {nombre_passagers}")
                airecraft_age = airecraft_age_from_html
        if (airecraft_age == "") :
            return None, None
        return airecraft_age , nombre_passagers
    except :
        print("not found")
        return None, None


# Ajouter l'age d'avion et le nombre de passagers dans la base de données (pour la bdd toulouse)
def age_and_passagers(data_new):
    age_list = []
    nombre_passagers_list = []

    for i in data_new.index : 
        tale_nb = data_new['tale_nb'][i]
        serial_nb = data_new['serial_nb'][i]
        airecraft_age , nombre_passagers = getAge_bis(tale_nb,serial_nb)
        age_list.append(airecraft_age)
        nombre_passagers_list.append(nombre_passagers)

    data_new.loc[:, 'age_avion'] = age_list
    data_new.loc[:, 'nombre_passagers'] = nombre_passagers_list

    data_new.to_csv("data_toulouse.csv")
    data_atterrissage = data_new[data_new['ATTERRISSAGE'] != '[]']
    data_atterrissage2 = data_atterrissage.drop(columns=['DECOLLAGE'])
    data_decollage = data_new[data_new['DECOLLAGE'] != '[]']
    data_decollage2 = data_decollage.drop(columns=['ATTERRISSAGE'])
    data_atterrissage2.to_csv('data_toulouse_atterrisage.csv')
    data_decollage2.to_csv('data_toulouse_decollage.csv')

    return data_new, data_atterrissage2, data_decollage2
