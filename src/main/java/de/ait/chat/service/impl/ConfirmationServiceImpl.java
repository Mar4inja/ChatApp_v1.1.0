package de.ait.chat.service.impl;
import de.ait.chat.entity.ConfirmationCode;
import de.ait.chat.entity.User;
import de.ait.chat.exceptions.ConfirmationCodeExpiredException;
import de.ait.chat.exceptions.ConfirmationCodeNotFoundException;
import de.ait.chat.repository.ConfirmationCodeRepository;
import de.ait.chat.repository.UserRepository;
import de.ait.chat.service.ConfirmationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository confirmRepository;
    private final UserRepository userRepository;


    public ConfirmationServiceImpl(ConfirmationCodeRepository confirmationCodeRepository, UserRepository userRepository) {

        this.confirmRepository = confirmationCodeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateConfirmationCode(User user) {

        LocalDateTime expired = LocalDateTime.now().plusMinutes(1440);
        String code = UUID.randomUUID().toString();
        ConfirmationCode entity = new ConfirmationCode(code, expired, user);
        confirmRepository.save(entity);
        return code;

    }


    public void saveConfirmationCode(String activationCode) {
        ConfirmationCode code = new ConfirmationCode();
        code.setCode(activationCode);
        confirmRepository.save(code);
    }

    @Override
    public boolean activateUser(String code) {

        ConfirmationCode confirmationCode = confirmRepository.findByCode(code);

        if (confirmationCode == null) {
            throw new ConfirmationCodeNotFoundException("Confirmation code not found.");
        }

        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())) {
            throw new ConfirmationCodeExpiredException("Confirmation code has expired.");
        }

            User user = confirmationCode.getUser();
            user.setActive(true);
            confirmRepository.delete(confirmationCode);
            return true;

    }


}
