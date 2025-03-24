package org.example.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;
//import org.apache.commons.io;

import java.io.FileReader;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "app/src/main/java/org/example/localDb/users.json";

    public UserBookingService(User user1) throws IOException
    {
        this.user = user1;
        loadUsers();
    }

    public UserBookingService() throws IOException{
        loadUsers();
    }

    public List<User> loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
        return List.of();
    }

    public Boolean loginUser(){
        UserServiceUtil util = new UserServiceUtil(); // Initialize utility class
        Optional<User> foundUser = userList.stream().filter(user1 ->
                user1.getName().equals(user.getName()) &&
                        util.checkPassword(user.getPassword(), user1.getHashedPassword())
        ).findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) throws IOException {
        try{
            userList.add(user1);
            saveUserListtoFile();
            return Boolean.TRUE;
        }
        catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListtoFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile,userList);
    }

    public void fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the tickedId to cancel : ");
        ticketId = scanner.next();

        if (ticketId == null || ticketId.isEmpty()){
            System.out.println("Ticket ID cannot be null or empty .");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;  //Because strings are immutable
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }


    public List<Train> getTrains(String source, String destination) throws IOException
    {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);

        }catch(Exception ex){
            System.out.println("Unexpected error: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    FileReader reader = new FileReader("non_existing_file.txt"); // ✅ This throws IOException if file not found
                    reader.close();
                    return true; // Booking successful

                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    public void fetchBookings() {
        user.printTickets();
    }
}
