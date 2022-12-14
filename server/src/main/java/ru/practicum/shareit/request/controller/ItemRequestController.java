package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService service;

    /**
     * Внесение нового запроса на добавление вещи.
     *
     * @param requesterId - id пользователя, публикующего запрос.
     * @param requestDto  - передаваемый объект запроса, должен содержать текст.
     * @return DTO добавленного запроса.
     * @throws UserNotFoundException - если пользователя с указанным id не существует.
     */
    @PostMapping
    public ResponseEntity<ItemRequestDto> addItemRequest(@RequestHeader(name = "X-Sharer-User-Id") Long requesterId,
                                                         @RequestBody ItemRequestDto requestDto) {
        return ResponseEntity.ok(service.addRequest(requestDto, requesterId));
    }

    /**
     * Получение существующего запроса на добавление вещи.
     *
     * @param requesterId - id пользователя, запросившего информацию.
     * @param requestId   - идентификатор запроса.
     * @return DTO существующего запроса.
     * @throws UserNotFoundException    - если пользователя с указанным id не существует.
     * @throws RequestNotFoundException - если запроса с указанным id не существует.
     */
    @GetMapping(path = "/{requestId}")
    public ResponseEntity<ItemRequestDto> getItemRequest(@RequestHeader(name = "X-Sharer-User-Id") Long requesterId,
                                                         @PathVariable Long requestId) {
        return ResponseEntity.ok(service.getRequestDto(requestId, requesterId));
    }

    /**
     * Получение пользователем собственных запросов на добавление вещей.
     *
     * @param requesterId - идентификатор пользователя.
     * @return Список с запросами пользователя.
     * @throws UserNotFoundException - если пользователя с указанным id не существует.
     */
    @GetMapping
    public ResponseEntity<Collection<ItemRequestDto>> getOwnItemRequests(
            @RequestHeader(name = "X-Sharer-User-Id") Long requesterId) {

        return ResponseEntity.ok(service.getOwnItemRequests(requesterId));
    }

    /**
     * Получением пользователем запросов на добавление вещей других участников сервиса с возможностью использования
     * пагинации.
     *
     * @param requesterId - идентификатор пользователя.
     * @param from        - индекс первого получаемого объекта из списка.
     * @param size        - размер получаемого списка.
     * @return список запросов других пользователей.
     * @throws UserNotFoundException - если пользователя с указанным id не существует.
     */
    @GetMapping(path = "/all")
    public ResponseEntity<Collection<ItemRequestDto>> getOtherUsersRequests(
            @RequestHeader(name = "X-Sharer-User-Id") Long requesterId,
            @RequestParam Integer from,
            @RequestParam Integer size) {

        return ResponseEntity.ok(service.getOtherUsersRequests(requesterId, from, size));
    }
}
