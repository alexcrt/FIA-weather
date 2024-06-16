import datetime
import streamlit as st

from data import backend_requests, utils

@st.experimental_dialog("Add a Grand Prix 🏎")
def add_grand_prix():
    track_options = backend_requests.load_tracks()
    track_options += ["Other"]
    track_selected = st.selectbox("Track", track_options)

    is_new_track = track_selected == "Other"

    if is_new_track:
        col1, col2 = st.columns(2)
        with col1:
            track_selected = st.text_input("Track name")
        with col2:
            track_zone_offset_hours = st.number_input("Zone offset hours", value=0, step=1)
        col1, col2 = st.columns(2)
        with col1:
            latitude = st.number_input("Latitude", value=None)
        with col2:
            longitude = st.number_input("Longitude", value=None)

        st.divider()

    is_sprint_weekend = st.toggle("Is sprint weekend ?", value=False)

    if is_sprint_weekend == True:
        with st.expander("Free practice"):
            st.text("FP1")
            col1, col2, col3 = st.columns(3)
            with col1:
                fp1_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="fp1_start_date")
            with col2:
                fp1_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="fp1_start_time")
            with col3:
                fp1_duration = st.number_input("Duration (mins)", value=15, key="fp1_duration")

        with st.expander("Sprint qualifying"):
            st.text("SQ1")
            col1, col2, col3 = st.columns(3)
            with col1:
                sq1_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="sq1_start_date")
            with col2:
                sq1_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="sq1_start_time")
            with col3:
                sq1_duration = st.number_input("Duration (mins)", value=15, key="sq1_duration")

            st.text("SQ2")
            col1, col2, col3 = st.columns(3)
            with col1:
                sq2_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="sq2_start_date")
            with col2:
                sq2_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="sq2_start_time")
            with col3:
                sq2_duration = st.number_input("Duration (mins)", value=15, key="sq2_duration")

            st.text("SQ3")
            col1, col2, col3 = st.columns(3)
            with col1:
                sq3_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="sq3_start_date")
            with col2:
                sq3_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="sq3_start_time")
            with col3:
                sq3_duration = st.number_input("Duration (mins)", value=15, key="sq3_duration")

        with st.expander("Sprint race"):
            col1, col2, col3 = st.columns(3)
            with col1:
                sp_race_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="sp_race_start_date")
            with col2:
                sp_race_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="sp_race_start_time")
            with col3:
                sp_race_duration = st.number_input("Duration (mins)", value=15, key="sp_race_duration")

    else:
        with st.expander("Free practice"):
            st.text("FP1")
            col1, col2, col3 = st.columns(3)
            with col1:
                fp1_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="fp1_start_date")
            with col2:
                fp1_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="fp1_start_time")
            with col3:
                fp1_duration = st.number_input("Duration (mins)", value=15, key="fp1_duration")

            st.text("FP2")
            col1, col2, col3 = st.columns(3)
            with col1:
                fp2_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="fp2_start_date")
            with col2:
                fp2_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="fp2_start_time")
            with col3:
                fp2_duration = st.number_input("Duration (mins)", value=15, key="fp2_duration")

            st.text("FP3")
            col1, col2, col3 = st.columns(3)
            with col1:
                fp3_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="fp3_start_date")
            with col2:
                fp3_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="fp3_start_time")
            with col3:
                fp3_duration = st.number_input("Duration (mins)", value=15, key="fp3_duration")

    with st.expander("Qualifying"):
        st.text("Q1")
        col1, col2, col3 = st.columns(3)
        with col1:
            q1_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="q1_start_date")
        with col2:
            q1_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="q1_start_time")
        with col3:
            q1_duration = st.number_input("Duration (mins)", value=15, key="q1_duration")

        st.text("Q2")
        col1, col2, col3 = st.columns(3)
        with col1:
            q2_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="q2_start_date")
        with col2:
            q2_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="q2_start_time")
        with col3:
            q2_duration = st.number_input("Duration (mins)", value=15, key="q2_duration")

        st.text("Q3")
        col1, col2, col3 = st.columns(3)
        with col1:
            q3_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="q3_start_date")
        with col2:
            q3_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="q3_start_time")
        with col3:
            q3_duration = st.number_input("Duration (mins)", value=15, key="q3_duration")

    with st.expander("Race"):
        col1, col2, col3 = st.columns(3)
        with col1:
            race_start_date = st.date_input('Start date', value=datetime.datetime.now(), key="race_start_date")
        with col2:
            race_start_time = st.time_input('Start time', datetime.time(14, 30), step=60, key="race_start_time")
        with col3:
            race_duration = st.number_input("Duration (mins)", value=15, key="race_duration")

    if st.button("Submit", use_container_width=True):
        new_grand_prix_data = {
            "trackName": track_selected,
            "fp1StartingDateTime": utils.convert_datetime_to_ISO_8601(fp1_start_date, fp1_start_time),
            "fp1Duration": fp1_duration,
            "q1StartingDateTime": utils.convert_datetime_to_ISO_8601(q1_start_date, q1_start_time),
            "q1Duration": q1_duration,
            "q2StartingDateTime": utils.convert_datetime_to_ISO_8601(q2_start_date, q2_start_time),
            "q2Duration": q2_duration,
            "q3StartingDateTime": utils.convert_datetime_to_ISO_8601(q3_start_date, q3_start_time),
            "q3Duration": q3_duration,
            "raceStartingDateTime": utils.convert_datetime_to_ISO_8601(race_start_date, race_start_time),
            "raceDuration": race_duration,
        }

        if is_new_track:
            new_grand_prix_data  = new_grand_prix_data | {
                "trackLatitude": str(latitude), 
                "trackLongitude": str(longitude),
                "trackZoneOffsetHours": track_zone_offset_hours
            }

        if is_sprint_weekend:
            new_grand_prix_data  = new_grand_prix_data | {
                "isSprint": True,
                "sq1StartingDateTime": utils.convert_datetime_to_ISO_8601(sq1_start_date, sq1_start_time),
                "sq1Duration": sq1_duration,
                "sq2StartingDateTime": utils.convert_datetime_to_ISO_8601(sq2_start_date, sq2_start_time),
                "sq2Duration": sq2_duration,
                "sq3StartingDateTime": utils.convert_datetime_to_ISO_8601(sq3_start_date, sq3_start_time),
                "sq3Duration": sq3_duration,
                "sprintRaceStartingDateTime": utils.convert_datetime_to_ISO_8601(sp_race_start_date, sp_race_start_time),
                "sprintRaceDuration": sp_race_duration,
            }
        else:
            new_grand_prix_data  = new_grand_prix_data | {
                "isSprint": False,
                "fp2StartingDateTime": utils.convert_datetime_to_ISO_8601(fp2_start_date, fp2_start_time),
                "fp2Duration": fp2_duration,
                "fp3StartingDateTime": utils.convert_datetime_to_ISO_8601(fp3_start_date, fp3_start_time),
                "fp3Duration": fp3_duration,
            }

        st.session_state.new_grand_prix_data = new_grand_prix_data
        st.rerun()


def select_grand_prix():
    seasons = backend_requests.load_seasons()
    if len(seasons) > 0:
        # Create a list of options for the seasons dropdown
        season_options = [f"{season['year']}" for season in seasons]
        season_by_uuid = {f"{season['year']}": season['uuid'] for season in seasons}
        selectbox_season = st.selectbox("Season", season_options)
        selected_season = season_by_uuid[selectbox_season]

    # Create a list of options for the grands prix dropdown
    selected_grand_prix = None
    grands_prix = backend_requests.load_grands_prix(selected_season)
    if len(grands_prix) > 0:
        grands_prix_options = [f"{grand_prix['name']}" for grand_prix in grands_prix]
        grands_prix_by_uuid = {f"{grand_prix['name']}": grand_prix['uuid'] for grand_prix in grands_prix}
        selectbox_grand_prix = st.selectbox("Race", grands_prix_options)
        selected_grand_prix = grands_prix_by_uuid[selectbox_grand_prix]

    return selected_grand_prix