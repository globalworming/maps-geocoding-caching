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
  console.log("render chart", locations)
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
  imageSeries.mapImages.template.tooltipText = "{title}";
  imageSeries.mapImages.template.propertyFields.url = "url";

  let circle = imageSeries.mapImages.template.createChild(am4core.Circle);
  circle.radius = 3;
  circle.propertyFields.fill = "color";

  let circle2 = imageSeries.mapImages.template.createChild(am4core.Circle);
  circle2.radius = 3;
  circle2.propertyFields.fill = "color";


  circle2.events.on("inited", function(event){
    animateBullet(event.target);
  })


  function animateBullet(circle) {
    let animation = circle.animate([{ property: "scale", from: 1, to: 5 }, { property: "opacity", from: 1, to: 0 }], 1000, am4core.ease.circleOut);
    animation.events.on("animationended", function(event){
      animateBullet(event.target.object);
    })
  }


  imageSeries.data = locations;

  return chart;
}

const colorSet = new am4core.ColorSet();

const initialLocations = [ {
  "title": "Brussels",
  "latitude": 50.8371,
  "longitude": 4.3676,
  "color":colorSet.next()
}, {
  "title": "Copenhagen",
  "latitude": 55.6763,
  "longitude": 12.5681,
  "color":colorSet.next()
} ];

function App() {
  const chart = useRef(null);
  const [locations, setLocations] = useState(initialLocations)
  const [titles, setTitles] = useState(initialLocations.map(item => item.title))

  const refreshMap = () => {
    let newLocations = Object.assign([], locations)
    newLocations = newLocations.filter(it => titles.includes(it.title))

    titles.forEach(title => {
          if (title.length > 0 && !newLocations.map(it => it.title).includes(title)) {
            fetch('/coordinates?query=' + encodeURIComponent(title))
                .then(response => response.json())
                .then(response => {
                  if (response.length === 0) return;
                  let location = response[0];
                  newLocations.push({title: location.query, latitude:location.latitude, longitude: location.longitude, color: colorSet.next()})
                  setLocations(newLocations)
                })
          }
        }

    )

    //return {title: it, latitude: location.lat, longitude: location.lng, color: colorSet.next()}
    //console.log(JSON.stringify(newLocations, null, 2));
    //setLocations(newLocations)
  }

  useLayoutEffect(() => {
    chart.current = renderChart(locations);
    return () => {
      chart.current.dispose();
    };
  }, [locations]);
  return (
      <div className="App">
        <span>This service forwards geocoding requests and caches the results according to the <a href="https://cloud.google.com/maps-platform/terms/maps-service-terms">Maps Api Terms of Service</a></span>
        <div>TODO, compliance to https://developers.google.com/maps/documentation/geocoding/policies#terms-privacy</div>
        <header className="App-header">
          <div id="chartdiv" style={{width: "100%", height: "500px"}}></div>
          <img src={GoogleLogo} alt=""/>
          <button onClick={() => refreshMap()}>refresh map</button>
          <textarea value={titles.join("\n")} onChange={(event => setTitles(event.target.value.split("\n"))) }/>
        </header>
      </div>
  );
}

export default App;


