package com.ftn.pma.globals;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsParser {

        /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
        public List<List<HashMap<String,String>>> parse(JSONObject jObject){

            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();

                    /** Traversing all legs */
                    for(int j=0;j<jLegs.length();j++){
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }


            return routes;
        }



        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
}

/** This class aims to parse a json object like that:
        * {
        "routes" : [
        {
        "bounds" : {
        "northeast" : {
        "lat" : 50.63918229999999,
        "lng" : 3.0378181
        },
        "southwest" : {
        "lat" : 50.6359351,
        "lng" : 3.027635
        }
        },
        "copyrights" : "Données cartographiques ©2014 Google",
        "legs" : [
        {
        "distance" : {
        "text" : "1,0 km",
        "value" : 1022
        },
        "duration" : {
        "text" : "13 minutes",
        "value" : 761
        },
        "end_address" : "D751, 59000 Lille, France",
        "end_location" : {
        "lat" : 50.6362271,
        "lng" : 3.0378181
        },
        "start_address" : "1-5 D933, 59130 Lambersart, France",
        "start_location" : {
        "lat" : 50.6359351,
        "lng" : 3.027635
        },
        "steps" : [
        {
        "distance" : {
        "text" : "0,5 km",
        "value" : 535
        },
        "duration" : {
        "text" : "6 minutes",
        "value" : 383
        },
        "end_location" : {
        "lat" : 50.63918229999999,
        "lng" : 3.0331989
        },
        "html_instructions" : "Prendre la direction \u003cb\u003enord-est\u003c/b\u003e sur \u003cb\u003eAv. du Colisée/D933\u003c/b\u003e vers \u003cb\u003eRésidence le Clos du Colissee\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003eContinuer de suivre Av. du Colisée\u003c/div\u003e",
        "polyline" : {
        "points" : "sy`tHwinQCOEKGOEMCEOc@GSS_@g@y@S_@KMEQWc@i@eACIk@sAa@{@a@{@k@iAS_@MQGIQWYa@KQS_@k@gA}@gB[s@CKOc@"
        },
        "start_location" : {
        "lat" : 50.6359351,
        "lng" : 3.027635
        },
        "travel_mode" : "WALKING"
        },
        {
        "distance" : {
        "text" : "0,5 km",
        "value" : 487
        },
        "duration" : {
        "text" : "6 minutes",
        "value" : 378
        },
        "end_location" : {
        "lat" : 50.6362271,
        "lng" : 3.0378181
        },
        "html_instructions" : "Prendre \u003cb\u003eà droite\u003c/b\u003e sur \u003cb\u003eAv. de l'Hippodrome/D751\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003eContinuer de suivre D751\u003c/div\u003e\u003cdiv style=\"font-size:0.9em\"\u003eVotre destination se trouvera sur la droite\u003c/div\u003e",
        "maneuver" : "turn-right",
        "polyline" : {
        "points" : "{matHoloQLKb@]JILQDEDCROFGBAHIROJIJILMLIDENMp@_@vAgARWJMPWLUP[HSHUFSFYDQBUFS@IHc@BK?CLw@@APgA@IN{@?E@GTwA"
        },
        "start_location" : {
        "lat" : 50.63918229999999,
        "lng" : 3.0331989
        },
        "travel_mode" : "WALKING"
        }
        ],
        "via_waypoint" : []
        }
        ],
        "overview_polyline" : {
        "points" : "sy`tHwinQQk@a@kA{AgCkAeCmAoCmAeCa@q@Ya@e@s@_AgByA{CSo@p@i@X[f@a@tAgATSp@_@vAgA^e@^m@Zo@Pi@XuAr@iEf@aD"
        },
        "summary" : "Av. du Colisée et D751",
        "warnings" : [
        "Le calcul d'itinéraires piétons est en bêta. Faites attention – Cet itinéraire n'est peut-être pas complètement aménagé pour les piétons."
        ],
        "waypoint_order" : []
        }
        ],
        "status" : "OK"
        }
        */
