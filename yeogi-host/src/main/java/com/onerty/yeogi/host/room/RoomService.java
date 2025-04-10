


package com.onerty.yeogi.host.room;

import com.onerty.yeogi.common.room.Accommodation;
import com.onerty.yeogi.common.room.RoomType;
import com.onerty.yeogi.common.user.Host;
import com.onerty.yeogi.host.room.dto.CreateAccommodationRequest;
import com.onerty.yeogi.host.room.dto.CreateRoomTypeRequest;
import com.onerty.yeogi.host.user.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final HostRepository hostRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeStockRepository roomTypeDailyAvailabilityRepository;

    public Accommodation createAccommodation(CreateAccommodationRequest request) {
        Host host = hostRepository.findById(request.hostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 호스트입니다."));

        Accommodation accommodation = Accommodation.builder()
                .name(request.name())
                .location(request.location())
                .host(host)
                .build();

        accommodationRepository.save(accommodation);

        for (CreateRoomTypeRequest roomTypeReq : request.roomTypes()) {
            RoomType roomType = RoomType.builder()
                    .name(roomTypeReq.name())
                    .capacity(roomTypeReq.capacity())
                    .pricePerNight(roomTypeReq.pricePerNight())
                    .description(roomTypeReq.description())
                    .accommodation(accommodation)
                    .build();

            roomTypeRepository.save(roomType);

        }

        return accommodation;
    }

}

