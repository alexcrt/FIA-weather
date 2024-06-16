import numpy as np
import streamlit as st

from data import backend_requests, utils
from sidebar import modules as sidebar_modules


st.set_page_config(page_title = "FIA - Weather Application")
# Streamlit interface
st.title("Weather metrics")

weather_result = None

with st.sidebar:
    if st.subheader(":racing_car: Select Grand Prix", divider='red'):
       selected_grand_prix = sidebar_modules.select_grand_prix()
       if selected_grand_prix:
        if st.button("Fetch weather data", type="primary", use_container_width=True):
            weather_result = backend_requests.weather_result(selected_grand_prix)["grandPrixWeatherResult"]

    st.subheader('Grand prix not found ? :checkered_flag:', divider='red')
    if st.button("Add a Grand Prix", use_container_width=True):
       sidebar_modules.add_grand_prix()

    if "new_grand_prix_data" in st.session_state:
        grand_prix_data = st.session_state["new_grand_prix_data"]
        del st.session_state["new_grand_prix_data"]
        response = backend_requests.add_grand_prix(grand_prix_data)
        if response.status_code == 200:
            st.success("Grand prix successfully added")
        else:
            st.error("Error while adding grand prix")

if weather_result:    
    st.header(f"{weather_result['trackName']}")

    col1, col2, col3 = st.columns(3)
    with col1:
        st.metric("Zone offset", f"{weather_result['zoneOffset']} (UTC)")
    with col2:
        st.metric("Latitude", weather_result['latitude'])
    with col3:
        st.metric("longitude", weather_result['longitude'])

    prev_metrics_mean_values = {}

    for session_name, weather_data_report in weather_result["weatherDataReportList"].items():
        with st.expander(session_name):
            metrics, chart = utils.create_metrics_info_and_plotly_chart(weather_data_report["weatherData"])
            if metrics is None or chart is None:
                st.info('Still fetching data...')
            else:
                col_size = 4
                for i in range(0, len(metrics), col_size):
                    chunk = metrics[i:i + col_size]
                    for i, col in enumerate(st.columns(len(chunk))):
                        metric_title = chunk[i][0]
                        metric_unit = chunk[i][1]
                        metric_mean_value = chunk[i][2]
                        metric_mean_diff_value = 0

                        if metric_title in prev_metrics_mean_values:
                            metric_mean_diff_value = np.round(metric_mean_value - prev_metrics_mean_values[metric_title], 3)

                        prev_metrics_mean_values[metric_title] = metric_mean_value
                        col.metric(f"{metric_title} ({metric_unit})", metric_mean_value, str(metric_mean_diff_value))

                st.divider()
                st.plotly_chart(chart)