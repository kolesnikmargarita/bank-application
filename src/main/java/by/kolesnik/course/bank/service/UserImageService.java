package by.kolesnik.course.bank.service;

import by.kolesnik.course.bank.dto.UploadResultDto;
import by.kolesnik.course.bank.entity.User;
import by.kolesnik.course.bank.exception.EntityNotFoundException;
import by.kolesnik.course.bank.repository.UserRepository;
import by.kolesnik.course.bank.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserImageService {

    private final Path path = Paths.get("file-storage");
    private final UserRepository userRepository;

    public UserImageService(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException exception) {
            throw new ServerErrorException(
                    "Не удалось создать хранилище", exception
            );
        }
    }

    // сохранить файл
    // получить его имя
    // найти пользователя по id
    // сохранить имя картинки у пользователя
    // вернуть URL

    public UploadResultDto save(Long id, MultipartFile file) {
        final String filename = saveFile(file);

        updateUserImageName(id, filename);

        return new UploadResultDto(filename, null);
    }


    public Resource findByUserId(Long id) {

        final String filename = findUserImageNameByUserId(id);

        final Path file = path.resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Не удалось прочитать файл.");
            }
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Ошибка: " + exception.getMessage());
        }
    }

    private String saveFile(MultipartFile file) {
        final String extension = FileUtil.getFileExtension(file.getOriginalFilename());

        final String filename = UUID.randomUUID() + extension;

        try {
            Files.copy(file.getInputStream(), path.resolve(filename));
        } catch (IOException exception) {
            throw new RuntimeException("Ошибка загрузки файла:" + exception.getMessage());
        }

        return filename;
    }

    private void updateUserImageName(Long id, String filename) {
        final User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователь не найден")
        );

        user.setImageName(filename);
        userRepository.save(user);
    }

    private String findUserImageNameByUserId(Long id) {
        final User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователь не найден")
        );

        return user.getImageName();
    }
}
