import React, {useLayoutEffect, useRef, useState} from 'react';
import './App.css';
import GoogleLogo from "./powered_by_google_on_non_white_hdpi.png";

import * as am4core from "@amcharts/amcharts4/core";
import * as am4maps from "@amcharts/amcharts4/maps";
import am4geodata_worldLow from "@amcharts/amcharts4-geodata/worldLow";
import am4themes_spiritedaway from "@amcharts/amcharts4/themes/spiritedaway";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";

/* Chart code */
// Themes begin
am4core.useTheme(am4themes_spiritedaway);
am4core.useTheme(am4themes_animated);
// Themes end

const renderChart = (locations) => {
  // Create map instance
  let chart = am4core.create("chartdiv", am4maps.MapChart);

// Set map definition
  chart.geodata = am4geodata_worldLow;

// Set projection
  chart.projection = new am4maps.projections.Miller();

// Create map polygon series
  let polygonSeries = chart.series.push(new am4maps.MapPolygonSeries());

// Exclude Antartica
  polygonSeries.exclude = ["AQ"];

// Make map load polygon (like country names) data from GeoJSON
  polygonSeries.useGeodata = true;

// Configure series
  let polygonTemplate = polygonSeries.mapPolygons.template;
  polygonTemplate.tooltipText = "{name}";
  polygonTemplate.polygon.fillOpacity = 0.6;

// Create hover state and set alternative fill color
  let hs = polygonTemplate.states.create("hover");
  hs.properties.fill = chart.colors.getIndex(0);

// Add image series
  let imageSeries = chart.series.push(new am4maps.MapImageSeries());
  imageSeries.mapImages.template.propertyFields.longitude = "longitude";
  imageSeries.mapImages.template.propertyFields.latitude = "latitude";
  imageSeries.mapImages.template.tooltipText = "{query}";
  imageSeries.mapImages.template.propertyFields.url = "url";

  let circle = imageSeries.mapImages.template.createChild(am4core.Circle);
  circle.radius = 3;
  circle.propertyFields.fill = "color";

  let circle2 = imageSeries.mapImages.template.createChild(am4core.Circle);
  circle2.radius = 3;
  circle2.propertyFields.fill = "color";


  circle2.events.on("inited", function (event) {
    animateBullet(event.target);
  });


  function animateBullet(circle) {
    let animation = circle.animate([{property: "scale", from: 1, to: 5}, {
      property: "opacity",
      from: 1,
      to: 0
    }], 1000, am4core.ease.circleOut);
    animation.events.on("animationended", function (event) {
      animateBullet(event.target.object);
    })
  }


  imageSeries.data = locations;

  return chart;
};

const colorSet = new am4core.ColorSet();

const initialLocations = [{
  "query": "Brussels",
  "latitude": 50.8371,
  "longitude": 4.3676,
  "color": colorSet.next()
}, {
  "query": "Copenhagen",
  "latitude": 55.6763,
  "longitude": 12.5681,
  "color": colorSet.next()
}, {
  "query": "bermuda triangle",
  "latitude": 25,
  "longitude": -71,
  "color": colorSet.next()
}];

function App() {
  let chart = useRef(null).current;
  const [locations, setLocations] = useState(initialLocations);
  const [queries, setQueries] = useState(initialLocations.map(item => item.query));

  const refreshMap = () => {
    let newLocations = Object.assign([], locations);
    newLocations = newLocations.filter(it => queries.includes(it.query));

    queries.forEach(query => {
          if (query.length > 0 && !newLocations.map(it => it.query).includes(query)) {
            fetch('/coordinates?query=' + encodeURIComponent(query))
                .then(response => response.json())
                .then(response => {
                  if (response.length === 0) return;
                  let location = response[0];
                  newLocations.push({
                    query: location.query,
                    latitude: location.latitude,
                    longitude: location.longitude,
                    color: colorSet.next()
                  });
                  setLocations(newLocations)
                })
          }
        }
    )
  };

  useLayoutEffect(() => {
    chart = renderChart(locations);
    return () => {
      chart.dispose();
    };
  }, [locations]);
  return (<>
        <div style={{padding: "2rem"}}>
          <p>This service forwards geocoding requests and caches the results according to the <a
              href="https://cloud.google.com/maps-platform/terms/maps-service-terms">Maps Api Terms of Service</a> which
            at the time of writing this allows latitude and longitude values to be cached up to 30 days, and placeId
            indefinitely. Source on <a href="https://github.com/globalworming/maps-geocoding-caching">GitHub</a></p>
          <p>TODO: make this page compliant to compliance to
            https://developers.google.com/maps/documentation/geocoding/policies#terms-privacy</p>
          <p>Usage is pretty simple: get a <a
              href="https://developers.google.com/maps/documentation/geocoding/get-api-key">maps geocoding API key</a>,
            then</p>
          <pre style={{
            padding: "0.5rem",
            background: "#CCC",
            fontFamily: "monospace"
          }}>REQUEST GET /coordinates?query=North%20Pole&apiKey=YOUR_API_KEY<br/>
            <br/>RESPONSE:<br/>
            {JSON.stringify([
              {
                "query": "North Pole",
                "placeId": "ChIJUbkVldpEk08RdojG1cTQGEU",
                "latitude": 89.99999989999999,
                "longitude": -135
              }
            ], undefined, 2)}
          </pre>
          <p>your API key is sent securely (check https) and may be stored in memory for faster service. We don't do
            anything else with it than forwarding it to the google maps api. See also: <a
                href="https://developers.google.com/maps/api-key-best-practices#best_practice_list">maps/api-key-best-practices#best_practice_list</a>
          </p>
        </div>

        <div className="App">
          <header className="App-header">
            <div id="chartdiv"
                 style={{width: "90%", height: "500px", maxWidth: "900px", border: "1px solid #CCC"}}></div>
            <img src={GoogleLogo} alt=""/>
            <button onClick={() => refreshMap()}>refresh map</button>
            <textarea style={{minHeight: "4rem"}} value={queries.join("\n")}
                      onChange={(event => setQueries(event.target.value.split("\n")))}/>
          </header>
        </div>
      </>
  );
}

export default App;


