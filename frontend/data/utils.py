import numpy as np
import pandas as pd
import plotly.express as px
import streamlit as st

from datetime import datetime

METRICS_INFO = [
    ("temperature_2m", {"unit": "°C", "title": "Temperature 2m"}),
    ("relative_humidity_2m", {"unit": "%", "title": "Relative Humidity 2m"}),
    ("rain", {"unit": "mm", "title": "Rain"}),
    ("surface_pressure", {"unit": "hPa", "title": "Surface Pressure"}),
    ("wind_speed_10m", {"unit": "km/h", "title": "Wind Speed 10m"}),
    ("wind_speed_100m", {"unit": "km/h", "title": "Wind Speed 100m"}),
    ("wind_direction_10m", {"unit": "°", "title": "Wind Direction 10m"}),
    ("wind_direction_100m", {"unit": "°", "title": "Wind Direction 100m"})
]

def create_metrics_info_and_plotly_chart(weather_data_report):
	dataframes = []
	metrics = []

	if len(weather_data_report) == 0:
		return None, None

	for (metric_id, metric_info) in METRICS_INFO:
		data = weather_data_report[metric_id]
		metrics.append((metric_info["title"], metric_info["unit"], np.round(np.mean(data["values"]), 2)))
		df = pd.DataFrame({
			'Metric': [metric_info["title"]]*len(data["timestamps"]),
			'Datetime UTC': data["timestamps"],
			'Value': data["values"]
		})
		dataframes.append(df)

	return metrics, px.line(pd.concat(dataframes), x = 'Datetime UTC', y = 'Value', color = 'Metric', markers = True)


def convert_datetime_to_ISO_8601(date, time):
	combined_datetime = datetime.combine(date, time)
	return combined_datetime.strftime('%Y-%m-%dT%H:%M:%S')

def compute_grand_prix_date_range(starting_date, ending_date):
	starting_month, starting_day = datetime.strptime(starting_date, '%Y-%m-%d').strftime("%B %d").split(" ")
	ending_month, ending_day = datetime.strptime(ending_date, '%Y-%m-%d').strftime("%B %d").split(" ")

	months = ""
	days = f"{starting_day}-{ending_day}"
	if starting_month == ending_month:
	    months = starting_month[:3]
	else:
	    months = f"{starting_month[:3]} - {ending_month[:3]}"

	return months, days