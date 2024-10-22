package de.ait.chat.service;

import de.ait.chat.entity.Room;

import java.util.List;

public interface RoomService {

    Room createRoom(String roomName);

    Room getRoomById(Long roomId);

    void deleteRoom(Long roomId);

    List<Room> getAllRooms();

    Room selectRoom(Long roomId);
}
