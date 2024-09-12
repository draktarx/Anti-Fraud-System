package antifraud.validator.endpoint;

import antifraud.user.endpoint.request.NewStolenCardRequest;
import antifraud.validator.model.Card;
import antifraud.validator.service.cards.stolen.StolenCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud")
public class StolenCardController {

    private final StolenCardService stolenCardService;

    public StolenCardController(StolenCardService stolenCardService) {
        this.stolenCardService = stolenCardService;
    }

    @PostMapping("/stolencard")
    public ResponseEntity<Object> addStolenCard(@RequestBody NewStolenCardRequest request) {
        var savedCard = stolenCardService.save(request.number());
        return ResponseEntity.ok().body(savedCard);
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<Card>> getStolenCardList() {
        var cardList = stolenCardService.list();
        return ResponseEntity.ok().body(cardList);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<Object> deleteStolenCard(@PathVariable String number) {
        stolenCardService.deleteByCardNumber(number);
        return ResponseEntity
                .ok()
                .body(Map.of("status", "Card " + number + " successfully removed!"));
    }

}
