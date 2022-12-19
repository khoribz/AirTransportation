package com.khoribz.service.dao;

import lombok.AllArgsConstructor;

import com.khoribz.domain.BoardingPass;
import com.khoribz.service.db.SimpleJdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public final class BoardingPassDao {
    private final SimpleJdbcTemplate source;
    private BoardingPass createBoardingPasses(ResultSet resultSet) throws SQLException {
        return new BoardingPass(resultSet.getString("ticketNo"),
                resultSet.getInt("flightId"),
                resultSet.getInt("boardingNo"),
                resultSet.getString("seatNo")
        );
    }

    public void saveBoardingPasses(Collection<BoardingPass> boardingPasses) throws SQLException{
        source.preparedStatement("INSERT INTO BOARDING_PASSES VALUES (?, ?, ?, ?)",
                insertBoardingPass -> {
                    for (BoardingPass boardingPass : boardingPasses) {
                        insertBoardingPass.setString(1, boardingPass.getTicketNo());
                        insertBoardingPass.setLong(2, boardingPass.getFlightId());
                        insertBoardingPass.setInt(3, boardingPass.getBoardingNo());
                        insertBoardingPass.setString(4, boardingPass.getSeatNo());
                        insertBoardingPass.execute();
                    }
                });
    }

    public Set<BoardingPass> getBoardingPasses() throws SQLException {
        return source.statement(stmt -> {
            Set<BoardingPass> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BOARDING_PASSES");
            while (resultSet.next()) {
                result.add(createBoardingPasses(resultSet));
            }
            return result;
        });
    }

    public void downloadCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/boarding_passes.csv");
        ArrayList<BoardingPass> boardingPasses = new ArrayList<>();
        try (BufferedReader reader = new
                BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            int cnt = 0;
            int permissibleDataLines = 7000;
            while ((line = reader.readLine()) != null && cnt++ < permissibleDataLines) {
                String[] data = line.split(",");
                String ticketNo = data[0];
                Integer ticketId = Integer.parseInt(data[1]);
                Integer boardingNo = Integer.parseInt(data[2]);
                String seatNo = data[3];

                boardingPasses.add(new BoardingPass(ticketNo, ticketId, boardingNo, seatNo));
            }
            saveBoardingPasses(boardingPasses);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

}