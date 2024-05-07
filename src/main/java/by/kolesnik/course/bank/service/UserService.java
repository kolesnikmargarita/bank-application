package by.kolesnik.course.bank.service;

import by.kolesnik.course.bank.dto.CreateUserDto;
import by.kolesnik.course.bank.dto.UpdateUserDto;
import by.kolesnik.course.bank.dto.UserDto;
import by.kolesnik.course.bank.entity.User;
import by.kolesnik.course.bank.exception.EntityNotFoundException;
import by.kolesnik.course.bank.mapper.UserMapper;
import by.kolesnik.course.bank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto findDtoById(Long id) {


        return userMapper.toDto(findUserById(id));
    }

    public User findUserById(Long id) {
        final Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден");
        }

        return optionalUser.get();
    }

    // найти всех
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto).toList();
    }

    // обновим всю информацию о пользователе
    // обновим часть информации о пользователе
    public UserDto createUser(CreateUserDto dto) {
        final User user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }


    // удалим пользователя
    public void removeById(Long id) {
        userRepository.removeById(id);
    }
}
