import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MatchingService {
    private static final double RADIUS_KM = 0.5; // 500 meters matching radius

    /**
     * Finds and creates matches between travelers and senders within radius
     */
    public static void findMatches() {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection conn = DatabaseConnection.getConnection();

                // Find potential matches within 500m radius for both start and end points
                String sql = """
                        SELECT t.id as traveler_id, t.name as traveler_name,
                               t.start_location as t_start, t.end_location as t_end,
                               t.start_lat as t_start_lat, t.start_lng as t_start_lng,
                               t.end_lat as t_end_lat, t.end_lng as t_end_lng,
                               s.id as sender_id, s.name as sender_name,
                               s.start_location as s_start, s.end_location as s_end,
                               s.start_lat as s_start_lat, s.start_lng as s_start_lng,
                               s.end_lat as s_end_lat, s.end_lng as s_end_lng
                        FROM travelers t
                        CROSS JOIN senders s
                        WHERE t.id NOT IN (
                            SELECT COALESCE(traveler_id, 0) FROM matches WHERE traveler_id = t.id
                        )
                        AND s.id NOT IN (
                            SELECT COALESCE(sender_id, 0) FROM matches WHERE sender_id = s.id
                        )
                        ORDER BY t.travel_time DESC, s.request_time DESC
                        """;

                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();

                int matchCount = 0;

                while (rs.next()) {
                    // Calculate distances between start and end points
                    double distanceStart = LocationService.calculateDistance(
                            rs.getDouble("t_start_lat"), rs.getDouble("t_start_lng"),
                            rs.getDouble("s_start_lat"), rs.getDouble("s_start_lng"));

                    double distanceEnd = LocationService.calculateDistance(
                            rs.getDouble("t_end_lat"), rs.getDouble("t_end_lng"),
                            rs.getDouble("s_end_lat"), rs.getDouble("s_end_lng"));

                    // Check if both start and end points are within radius
                    if (distanceStart <= RADIUS_KM && distanceEnd <= RADIUS_KM) {
                        // Create match entry in database
                        String insertMatch = "INSERT INTO matches (traveler_id, sender_id, distance_km) VALUES (?, ?, ?)";
                        try (PreparedStatement matchStmt = conn.prepareStatement(insertMatch)) {
                            matchStmt.setInt(1, rs.getInt("traveler_id"));
                            matchStmt.setInt(2, rs.getInt("sender_id"));
                            matchStmt.setDouble(3, Math.round(((distanceStart + distanceEnd) / 2) * 1000.0) / 1000.0);
                            matchStmt.executeUpdate();
                        }

                        matchCount++;

                        // Extract package details from sender name if present
                        String senderName = rs.getString("sender_name");
                        String packageInfo = "";
                        if (senderName.contains("[") && senderName.contains("]")) {
                            int startBracket = senderName.indexOf("[");
                            int endBracket = senderName.lastIndexOf("]");
                            packageInfo = "\nPackage: " + senderName.substring(startBracket + 1, endBracket);
                            senderName = senderName.substring(0, startBracket).trim();
                        }

                        // Show match notification with enhanced formatting
                        String message = String.format(
                                "🎉 NEW MATCH FOUND! 🎉\n\n" +
                                        "TRAVELER DETAILS:\n" +
                                        "👤 Name: %s\n" +
                                        "🗺️ Route: %s → %s\n\n" +
                                        "SENDER DETAILS:\n" +
                                        "👤 Name: %s%s\n" +
                                        "🗺️ Route: %s → %s\n\n" +
                                        "📍 Match Distance: %.3f km\n" +
                                        "📋 Start Points Distance: %.3f km\n" +
                                        "📋 End Points Distance: %.3f km\n\n" +
                                        "Both parties will be notified to coordinate the delivery.",
                                rs.getString("traveler_name"),
                                rs.getString("t_start"),
                                rs.getString("t_end"),
                                senderName,
                                packageInfo,
                                rs.getString("s_start"),
                                rs.getString("s_end"),
                                (distanceStart + distanceEnd) / 2,
                                distanceStart,
                                distanceEnd);

                        JOptionPane.showMessageDialog(
                                null,
                                message,
                                "CrowdShipping Match Found!",
                                JOptionPane.INFORMATION_MESSAGE);

                        System.out.println("Match created: Traveler ID " + rs.getInt("traveler_id") +
                                " <-> Sender ID " + rs.getInt("sender_id"));
                    }
                }

                rs.close();
                pstmt.close();
                DatabaseConnection.closeConnection(conn);

                if (matchCount == 0) {
                    System.out.println("No new matches found within " + (RADIUS_KM * 1000) + "m radius.");
                } else {
                    System.out.println("Total matches found and created: " + matchCount);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Error finding matches: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Gets all existing matches for display
     * 
     * @return ResultSet containing match information
     */
    public static ResultSet getAllMatches() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sql = """
                SELECT m.id, m.distance_km, m.match_time,
                       t.name as traveler_name, t.start_location as t_start, t.end_location as t_end,
                       s.name as sender_name, s.start_location as s_start, s.end_location as s_end
                FROM matches m
                JOIN travelers t ON m.traveler_id = t.id
                JOIN senders s ON m.sender_id = s.id
                ORDER BY m.match_time DESC
                """;

        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    /**
     * Removes a match by ID
     * 
     * @param matchId The ID of the match to remove
     * @return true if successfully removed, false otherwise
     */
    public static boolean removeMatch(int matchId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM matches WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, matchId);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
