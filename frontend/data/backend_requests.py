import requests
import streamlit as st
import os


BACKEND_URL = os.getenv("BACKEND_URL", "http://localhost:8090")
ERROR_MESSAGE = "Failed to fetch data from the backend"

def load_seasons():
    response = requests.get(f'{BACKEND_URL}/season/all')
    if response.status_code == 200:
        return response.json()
    else:
        st.error(ERROR_MESSAGE)
        return []


def load_tracks():
    response = requests.get(f'{BACKEND_URL}/track/all')
    if response.status_code == 200:
        return list(map(lambda t: t["name"], response.json()))
    else:
        st.error(ERROR_MESSAGE)
        return []


def load_grands_prix(season_uuid: str):
    response = requests.get(f'{BACKEND_URL}/grandprix/all?seasonUuid={season_uuid}')
    if response.status_code == 200:
        return response.json()
    else:
        st.error(ERROR_MESSAGE)
        return []

def add_grand_prix(grand_prix_data: dict):
    response = requests.post(f'{BACKEND_URL}/grandprix/add', json=grand_prix_data)
    return response

def add_season():
    response = requests.post(f'{BACKEND_URL}/season/add')
    if response.status_code != 200:
        st.error(ERROR_MESSAGE)

def weather_result(grand_prix_uuid: str):
    response = requests.get(f'{BACKEND_URL}/weather/grand-prix?grandPrixUuid={grand_prix_uuid}')
    if response.status_code == 200:
        return response.json()
    elif response.status_code == 404:
        return None
    else:
        st.error(ERROR_MESSAGE)
        return []