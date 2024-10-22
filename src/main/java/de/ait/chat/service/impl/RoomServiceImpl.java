package de.ait.chat.service.impl;

import de.ait.chat.entity.Room;
import de.ait.chat.repository.RoomRepository;
import de.ait.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public Room createRoom(String roomName) {
        if (roomName == null || roomName.isEmpty()) {
            throw new IllegalArgumentException("Имя комнаты не может быть пустым");
        }

        Room room = new Room();
        room.setRoomTitle(roomName);
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        return room.orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));
    }

    @Override
    public void deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Комната не найдена, удаление невозможно");
        }
        roomRepository.deleteById(roomId);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Метод для выбора комнаты пользователем
    public Room selectRoom(Long roomId) {
        return getRoomById(roomId);  // Проверяем, существует ли комната, и возвращаем её
    }
}
