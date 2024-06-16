import influxdb_client
import os
import pendulum
import requests
import pandas as pd

from airflow.decorators import dag, task
from airflow.models.param import Param
from datetime import datetime, timezone
from influxdb_client.client.write_api import SYNCHRONOUS


INFLUXDB_ORG = os.getenv("AIRFLOW_VAR_INFLUXDB_ORG")
INFLUXDB_BUCKET = os.getenv("AIRFLOW_VAR_INFLUXDB_BUCKET")
INFLUXDB_URL = os.getenv("AIRFLOW_VAR_INFLUXDB_URL")
INFLUXDB_ACCESS_TOKEN = os.getenv("AIRFLOW_VAR_INFLUXDB_ACCESS_TOKEN")

OPEN_METEO_METRICS_KEY = [
    "temperature_2m", "relative_humidity_2m", "rain", "surface_pressure", 
    "wind_speed_10m", "wind_speed_100m", "wind_direction_10m", "wind_direction_100m"
]


# Interpolate data points every minute linearly
def interpolate(data_points):
    df = pd.DataFrame(data_points)
    df['time'] = df['time'].map(lambda timestamp: datetime.fromtimestamp(timestamp, tz=timezone.utc))
    df.set_index('time', inplace=True)
    
    df_resampled = df.resample('1min').interpolate(method='linear')
    interpolated_data_points = df_resampled.reset_index()
    interpolated_data_points['time'] = interpolated_data_points['time'].map(lambda datetime: int(datetime.timestamp()))

    return interpolated_data_points.to_dict(orient='records')

@dag(
    tags=['open-meteo-etl'],
    start_date=pendulum.datetime(2015, 12, 1, tz="UTC"),
    params={
        "open_meteo_url": Param("", type="string"),
        "influxdb_series_key": Param("", type="string"),
        "latitude": Param("", type="string"),
        "longitude": Param("", type="string"),
        "start_date": Param("", type="string"),
        "end_date": Param("", type="string")
    },
    catchup=False
)
def open_meteo_weather_etl():
    @task()
    def extract(**context):
        params = context["params"]

        url_params = {
            "latitude": params["latitude"],
            "longitude": params["longitude"],
            "start_date": params["start_date"],
            "end_date": params["end_date"],
            "timeformat": "unixtime",
            "timezone": "GMT",
            "hourly": ",".join(OPEN_METEO_METRICS_KEY)
        }
        
        response = requests.get(params["open_meteo_url"], headers={}, params=url_params)
        return response.status_code, response.json()

    @task()
    def transform(response, **context):
        status_code, data = response
        if status_code != 200:
            return None

        data_points = []

        for i, time in enumerate(data["hourly"]["time"]):
            data_point = {"time": time}
            data_point.update({key: data["hourly"][key][i] for key in OPEN_METEO_METRICS_KEY})
            data_points.append(data_point)

        interpolated_data = interpolate(data_points)
        return interpolated_data

    @task()
    def load(transformed_data: dict, **context):
        if transformed_data is None:
            return None

        params = context["params"]

        data_points = transformed_data

        points = []
        for data_point in data_points:
            point = influxdb_client.Point(params["influxdb_series_key"]).tag("latitude", params["latitude"]).tag("longitude", params["longitude"])
            for metric_key in OPEN_METEO_METRICS_KEY:
                point = point.field(metric_key, data_point[metric_key])
            point = point.time(data_point["time"], influxdb_client.WritePrecision.S)
            points.append(point)
        
        with influxdb_client.InfluxDBClient(url=INFLUXDB_URL, token=INFLUXDB_ACCESS_TOKEN, org=INFLUXDB_ORG) as client:
            write_api = client.write_api(write_options=SYNCHRONOUS)
            write_api.write(bucket=INFLUXDB_BUCKET, org=INFLUXDB_ORG, record=points)

    # Define the dag
    raw_data = extract()
    transformed_data = transform(raw_data)
    load(transformed_data)

open_meteo_weather_dag = open_meteo_weather_etl()