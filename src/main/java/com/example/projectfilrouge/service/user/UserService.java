package com.example.projectfilrouge.service.user;


import com.example.projectfilrouge.dto.AllTicketDto;
import com.example.projectfilrouge.dto.TicketDto;
import com.example.projectfilrouge.dto.UserDto;
import com.example.projectfilrouge.entity.ConfirmationToken;
import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.entity.UserEntity;
import com.example.projectfilrouge.entity.UserRole;
import com.example.projectfilrouge.exception.NotAllowedException;
import com.example.projectfilrouge.exception.NotFoundException;
import com.example.projectfilrouge.repository.TicketRepository;
import com.example.projectfilrouge.repository.UserRepository;
import com.example.projectfilrouge.service.email.EmailSender;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    //loadUserByUsername est une fonction à l'intérieur de UserDetailsService que tu surcharges pour donner
    // l'info concernant l'endroit où on retrouve le userName
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void signUpUser(UserDto userDto) {
        //A partir de ton userdto tu crée ton userentity
        UserEntity userEntity = new UserEntity(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPassword(),
                UserRole.USER

        );
        //Verif si le user est présnet bdd ou non
        boolean userExists = userRepository.findByEmail(userEntity.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("Email already taken");
        }
        String encodePassword = passwordEncoder.encode(userEntity.getPassword());

        userEntity.setPassword(encodePassword);

        String token = UUID.randomUUID()
                .toString();

       //Pour créer l'objet token
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                //La date et l'heure de maintenant
                LocalDateTime.now(),
                //Ici le timmer de 15 min
                LocalDateTime.now()
                        .plusMinutes(15),
                userEntity
        );

        userRepository.save(userEntity);
        //Que tu insert en bdd ici
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // le lien de confirmation
        String link = "http://localhost:8080/registration/confirm?token=" + token;
        emailSender.send(
                userDto.getEmail(),
                //C'est le texte en bas du mail
                buildEmail(userDto.getFirstName(), link));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                //Si jamais on a pas reçu le token
                .orElseThrow(() ->
                        new IllegalStateException("token not found")
                );
//Si c'est déjà fait il te le dit
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
//ça te donne l'heure d'expiration
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
//Si c'est expiré ben mort
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(("token expired"));
        }
//Sinon ok on sauvegarde et en met à jour la date
        confirmationTokenService.setConfirmedAt(token);
        //et on active le user
        userRepository.enableUser(confirmationToken.getUserEntity()
                .getEmail());
        return "confirmed";
    }

    private String buildEmail(
            String name,
            String link
    ) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;" +
                "color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;" +
                "min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" " +
                "border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" " +
                "style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" " +
                "cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" " +
                "border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;" +
                "Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;" +
                "font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;" +
                "display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" " +
                "align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" " +
                "style=\"border-collapse:collapse;max-width:580px;width:100%!important\" " +
                "width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" " +
                "cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" " +
                "align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" " +
                "style=\"border-collapse:collapse;max-width:580px;width:100%!important\" " +
                "width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;" +
                "line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;" +
                "color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;" +
                "line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the " +
                "below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;" +
                "border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;" +
                "line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;" +
                "color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link " +
                "will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

//C'est un findAll d'users que pour l'administrateur
    //Afin de montrer qu'on peut restreindre la consultation de profils qu'à l'administrateur
    public ResponseEntity<List<UserEntity>> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getAuthorities().contains(UserRole.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (userRepository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<UserEntity> findById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!((UserEntity) auth.getPrincipal()).getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (userRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException((HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> updateUser(Long id, UserDto userDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Condition pour que le bon user puisse faire le update
        if(!((UserEntity) auth.getPrincipal()).getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //On cherche par id et si il n'y est pas ben erreur
        UserEntity user =
                userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("user with id: " + id + "cannot be found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        // Response permet de changer le statut de la requête
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
//Void ne renvoie rien fait juste un truc
    public void deleteUserById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Condition pour que le bon user puisse faire le update
        if(!((UserEntity) auth.getPrincipal()).getId().equals(id)) {
            throw new NotAllowedException();
        }
        if ((userRepository.findById(id).isEmpty())) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        userRepository.deleteById(id);
    }

//Permet d'obtenir l'historique de vente et d'achat de ticket
    public AllTicketDto getAllUserRelatedTicket() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserEntity) auth.getPrincipal()).getId();
        //On recupère la liste des tickets en vente par l'utilisateur lui même
        List<Ticket> ticketOnSale = ticketRepository.findTicketOnSale(userId);
        //On récupère la liste d'achat des tickets par l'user lui même
        List<Ticket> ticketPurchased = ticketRepository.findTicketPurchased(userId);
        //Créer un objet avec la liste de tous les tickets qui on un lien avec le user
        return new AllTicketDto(
                //Objet qui contient une liste de ticket mis en vente et vendu
                ticketOnSale.stream().map(ticket -> new TicketDto(ticket.getEventName(), ticket.getEventDate(), ticket.getPrice(), ticket.getDetails(), ticket.getState())).toList(),
                //Objet qui contient une liste de ticket acheter
                ticketPurchased.stream().map(ticket -> new TicketDto(ticket.getEventName(), ticket.getEventDate(), ticket.getPrice(), ticket.getDetails(), ticket.getState())).toList()
                 //Stream est une biblio ou y a map
                //Map permet de retouner une modification pour l'ensemble des tickets dans la liste (et de le renvoyer)
                //ToList c'est tout ce qu'il a stocké en mémoire il en fait un eliste et me le renvoi
                //Pour le mettre dans ticketOnSale ou ticketPurshed AllTicketDto
        );

    }
}


