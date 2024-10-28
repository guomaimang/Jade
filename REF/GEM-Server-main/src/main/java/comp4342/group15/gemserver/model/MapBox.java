package comp4342.group15.gemserver.model;

import java.util.List;
import java.util.Map;

public final class MapBox {
    private static final char[] locTemplate = """
            https://restapi.amap.com/v3/geocode/regeo?key=5d89340cbb3df5b10f110c35142985e2&location=~X,~Y&poitype=&radius=10&extensions=base&batch=false&roadlevel=1
            """.toCharArray();
    private static final char[] mapTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="utf-8">
            <title>Add custom icons with Markers</title>
            <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no">
            <link href="https://api.mapbox.com/mapbox-gl-js/v2.11.0/mapbox-gl.css" rel="stylesheet">
            <script src="https://api.mapbox.com/mapbox-gl-js/v2.11.0/mapbox-gl.js"></script>
            <style>
            body { margin: 0; padding: 0; }
            #map { position: absolute; top: 0; bottom: 0; width: 100%; }
            </style>
            </head>
            <body>
            <style>
            .marker {
            display: block;
            border: none;
            border-radius: 50%;
            cursor: pointer;
            padding: 0;
            }
            </style>
            \s
            <div id="map"></div>
            \s
            <script>
            (function(){
            window.alert = function(name){
            var iframe = document.createElement("IFRAME");
            iframe.style.display="none";
            iframe.setAttribute("src", 'data:text/plain');
            document.documentElement.appendChild(iframe);
            window.frames[0].window.alert(name);
            iframe.parentNode.removeChild(iframe);
            }
            })();
            	mapboxgl.accessToken = 'pk.eyJ1IjoiaGFuamlhbWluZyIsImEiOiJjbDYzZ29hZWIwY2l5M29uam5taTc0MjJqIn0.tcjuacfyZfjsHR3B3aIQyA';
            const geojson = {
            'type': 'FeatureCollection',
            'features': [
            {
            'type': 'Feature',
            'properties': {
            'message': 'Current Location',
            'iconSize': [60, 60],
            'pic_name': 'coordinate.png'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': [~UX, ~UY]
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            },
            {
            'type': 'Feature',
            'properties': {
            'message': '~PM',
            'iconSize': [60, 60],
            'pic_name': '~PN'
            },
            'geometry': {
            'type': 'Point',
            'coordinates': ~PC
            }
            }
            ]
            };
            \s
            const map = new mapboxgl.Map({
            container: 'map',
            // Choose from Mapbox's core styles, or make your own style with Mapbox Studio
            style: 'mapbox://styles/mapbox/streets-v12',
            center: [~UX, ~UY],
            zoom: 15
            });
            \s
            // Add markers to the map.
            for (const marker of geojson.features) {
            // Create a DOM element for each marker.
            const el = document.createElement('div');
            const name = marker.properties.pic_name;
            el.className = 'marker';
            el.style.backgroundImage = `url(https://comp4342.hjm.red/public/thumbnail/${name})`;
            el.style.width = `60px`;
            el.style.height = `60px`;
            el.style.backgroundSize = '100%';
            \s
            el.addEventListener('click', () => {
            window.alert(marker.properties.message);
            });
            \s
            // Add markers to the map.
            new mapboxgl.Marker(el)
            .setLngLat(marker.geometry.coordinates)
            .addTo(map);
            }
            </script>
            \s
            </body>
            </html>
            """.toCharArray();

    public static String getHTML(String x, String y, List<Map<String, Object>> postList) {
        StringBuilder builder = new StringBuilder();
        int cnt = 0;
        for (int index = 0; index < mapTemplate.length; index++) {
            if (mapTemplate[index] == '~') {
                if (mapTemplate[index + 1] == 'U') {
                    if (mapTemplate[index + 2] == 'X')
                        builder.append(x);
                    else if (mapTemplate[index + 2] == 'Y')
                        builder.append(y);
                    index += 2;
                } else if (mapTemplate[index + 1] == 'P' && cnt < postList.size()) {
                    Map<String, Object> post = postList.get(cnt);
                    if (mapTemplate[index + 2] == 'M') {
                        String message = String.valueOf(post.get("message"));
                        message = message.replace("<", "&lt;");
                        message = message.replace("\"", "");
                        message = message.replace("'", "");
                        message = message.replace("\\", "\\\\");
                        message = message.replace("\n", "\\n");
                        message = message.replace("\r", "\\r");
                        builder.append(message);
                    } else if (mapTemplate[index + 2] == 'N')
                        builder.append(post.get("pic_name"));
                    else if (mapTemplate[index + 2] == 'C') {
                        builder.append(post.get("location"));
                        cnt++;
                    }
                    index += 2;
                }
            } else builder.append(mapTemplate[index]);
        }
        return builder.toString();
    }

    public static String getLocation(String x, String y) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < locTemplate.length; index++) {
            if (locTemplate[index] == '~') {
                if (locTemplate[index + 1] == 'X')
                    builder.append(x);
                else if (locTemplate[index + 1] == 'Y')
                    builder.append(y);
                index++;
            } else builder.append(locTemplate[index]);
        }
        return builder.toString();
    }
}
