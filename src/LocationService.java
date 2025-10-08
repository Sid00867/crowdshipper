import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LocationService {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private static final String USER_AGENT = "CrowdShipping-App/1.0";

    /**
     * Gets latitude and longitude coordinates for a given address
     * 
     * @param address The address to geocode
     * @return Array containing [latitude, longitude] or null if not found
     */
    public static double[] getCoordinates(String address) {
        try {
            // Encode the address for URL
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String requestUrl = NOMINATIM_URL + "?q=" + encodedAddress + "&format=json&limit=1&addressdetails=1";

            // Create HTTP connection
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setConnectTimeout(10000); // 10 seconds timeout
            connection.setReadTimeout(10000);

            // Respect Nominatim usage policy - add delay
            Thread.sleep(1000);

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.err.println("HTTP Error: " + responseCode);
                return null;
            }

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            // Parse JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                JSONObject location = jsonArray.getJSONObject(0);
                double lat = location.getDouble("lat");
                double lon = location.getDouble("lon");

                System.out.println("Geocoded '" + address + "' to: " + lat + ", " + lon);
                return new double[] { lat, lon };
            } else {
                System.err.println("No coordinates found for address: " + address);
            }

        } catch (Exception e) {
            System.err.println("Error geocoding address '" + address + "': " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Calculates distance between two points using Haversine formula
     * 
     * @param lat1 Latitude of first point
     * @param lon1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lon2 Longitude of second point
     * @return Distance in kilometers
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371; // Earth's radius in kilometers

        // Convert latitude and longitude from degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        // Haversine formula
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;
        return Math.round(distance * 1000.0) / 1000.0; // Round to 3 decimal places
    }

    /**
     * Validates if coordinates are within reasonable bounds
     * 
     * @param lat Latitude
     * @param lng Longitude
     * @return true if valid, false otherwise
     */
    public static boolean isValidCoordinates(double lat, double lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }
}
