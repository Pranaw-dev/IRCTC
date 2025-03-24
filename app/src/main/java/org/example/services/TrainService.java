package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.spi.ToolProvider.findFirst;

@SuppressWarnings("CallToPrintStackTrace")
public class TrainService {

    private final List<Train> trainList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "../localDB/trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAIN_DB_PATH);
        trainList = objectMapper.readValue(trains, new ListTypeReference());
    }

    public List<Train> searchTrains(String source, String destination) {
        Train collect = (Train) trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
        return Collections.singletonList(collect);
    }

    public void addTrain(Train newTrain) {
        // Check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            Train set = trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }

    private void findFirst() {

    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your application's requirements
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    private static class ListTypeReference extends TypeReference<List<Train>> {

    }
}